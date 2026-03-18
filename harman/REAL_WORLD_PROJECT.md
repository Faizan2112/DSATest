# Real-World Project: "AutoDash" – Connected Car Dashboard 🚗

> Stop reading about `flowOf("Hello")`. Let's build a **Real-Time Vehicle Dashboard** using AAOS patterns.

This guide demonstrates how **Coroutines, Flows, AIDL, and Compose** work together in a production-grade automotive Android app — the type of system Harman builds.

---

## 🏗️ 1. The Architecture (Clean MVVM + Multi-Process)

We are building **"AutoDash"**:
1.  **Displays** real-time vehicle speed, fuel, engine temp via VHAL.
2.  **Controls** HVAC temperature.
3.  **Searches** for Points of Interest (POI) with live typing.
4.  **Handles** service disconnection gracefully.

### Data Pipeline
```text
[VHAL/CAN Bus] <--(AIDL)--> [VehicleService] <--(callbackFlow)--> [Repository]
    <--(StateFlow)--> [ViewModel] <--(Compose)--> [Dashboard UI]
```

---

## 🔌 2. The Vehicle Service Layer (AIDL)

### AIDL Interface
```aidl
// IVehicleService.aidl
interface IVehicleService {
    float getSpeed();
    float getFuelLevel();
    float getEngineTemperature();
    void setHvacTemp(int zone, float tempC);
    oneway void registerListener(IVehicleListener listener);
    oneway void unregisterListener(IVehicleListener listener);
}

// IVehicleListener.aidl
interface IVehicleListener {
    oneway void onSpeedChanged(float speedKmh);
    oneway void onFuelChanged(float fuelPercent);
    oneway void onEngineTempChanged(float tempC);
}
```

---

## 🏭 3. The Repository (Bridge AIDL → Flow)

```kotlin
class VehicleRepository(private val context: Context) {
    private var service: IVehicleService? = null
    private val _speed = MutableStateFlow(0f)
    private val _fuel = MutableStateFlow(0f)
    private val _engineTemp = MutableStateFlow(0f)

    private val listener = object : IVehicleListener.Stub() {
        override fun onSpeedChanged(speed: Float) { _speed.tryEmit(speed) }
        override fun onFuelChanged(fuel: Float) { _fuel.tryEmit(fuel) }
        override fun onEngineTempChanged(temp: Float) { _engineTemp.tryEmit(temp) }
    }

    private val connection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName, binder: IBinder) {
            service = IVehicleService.Stub.asInterface(binder)
            service?.registerListener(listener)
        }
        override fun onServiceDisconnected(name: ComponentName) {
            service = null  // Will auto-reconnect
        }
    }

    fun connect() {
        context.bindService(
            Intent("com.harman.vehicle.SERVICE"),
            connection, Context.BIND_AUTO_CREATE
        )
    }

    fun disconnect() {
        service?.unregisterListener(listener)
        context.unbindService(connection)
    }

    fun getSpeedFlow(): StateFlow<Float> = _speed.asStateFlow()
    fun getFuelFlow(): StateFlow<Float> = _fuel.asStateFlow()
    fun getEngineTempFlow(): StateFlow<Float> = _engineTemp.asStateFlow()

    suspend fun setHvacTemp(zone: Int, tempC: Float) = withContext(Dispatchers.IO) {
        service?.setHvacTemp(zone, tempC)
            ?: throw ServiceDisconnectedException("Vehicle service unavailable")
    }
}
```

---

## 🧠 4. The ViewModel (The Brain)

### State
```kotlin
data class DashboardUiState(
    val speed: Float = 0f,
    val fuel: Float = 0f,
    val engineTemp: Float = 0f,
    val hvacTemp: Float = 22f,
    val isConnected: Boolean = false,
    val warning: String? = null
)
```

### Logic
```kotlin
class DashboardViewModel(
    private val vehicleRepo: VehicleRepository,
    private val poiRepo: PoiRepository
) : ViewModel() {

    // 1. Combined vehicle state
    val dashboardState: StateFlow<DashboardUiState> = combine(
        vehicleRepo.getSpeedFlow(),
        vehicleRepo.getFuelFlow(),
        vehicleRepo.getEngineTempFlow()
    ) { speed, fuel, temp ->
        DashboardUiState(
            speed = speed,
            fuel = fuel,
            engineTemp = temp,
            isConnected = true,
            warning = when {
                temp > 110f -> "⚠️ Engine Overheating!"
                fuel < 10f -> "⚠️ Low Fuel!"
                else -> null
            }
        )
    }
    .catch { emit(DashboardUiState(isConnected = false, warning = "Service disconnected")) }
    .stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = DashboardUiState()
    )

    // 2. POI Search with debounce
    private val _searchQuery = MutableStateFlow("")

    val searchResults: StateFlow<List<Poi>> = _searchQuery
        .debounce(300)
        .distinctUntilChanged()
        .flatMapLatest { query ->
            if (query.isBlank()) flowOf(emptyList())
            else poiRepo.searchPoi(query)
        }
        .catch { emit(emptyList()) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // 3. Actions
    fun updateSearch(query: String) { _searchQuery.value = query }

    fun setHvacTemp(temp: Float) {
        viewModelScope.launch {
            try { vehicleRepo.setHvacTemp(zone = 0, tempC = temp) }
            catch (e: Exception) { /* Show error */ }
        }
    }
}
```

### Why This Code?
*   **`combine`:** Merges 3 vehicle data flows into one UI state.
*   **`catch`:** Handles AIDL disconnection without crashing.
*   **`WhileSubscribed(5000)`:** Stops listening 5s after screen dismissed (saves resources).
*   **`debounce(300)` + `flatMapLatest`:** Efficient search — cancels old queries.

---

## 📱 5. The UI (Jetpack Compose)

```kotlin
@Composable
fun DashboardScreen(viewModel: DashboardViewModel = viewModel()) {
    val state by viewModel.dashboardState.collectAsStateWithLifecycle()

    Column(modifier = Modifier.fillMaxSize().padding(24.dp)) {
        // Warning Banner
        state.warning?.let { warning ->
            Text(warning, color = Color.Red, style = MaterialTheme.typography.headlineSmall)
            Spacer(Modifier.height(16.dp))
        }

        // Gauges Row
        Row(horizontalArrangement = Arrangement.SpaceEvenly, modifier = Modifier.fillMaxWidth()) {
            GaugeWidget(label = "Speed", value = "${state.speed.toInt()} km/h")
            GaugeWidget(label = "Fuel", value = "${state.fuel.toInt()}%")
            GaugeWidget(label = "Engine", value = "${state.engineTemp.toInt()}°C")
        }

        Spacer(Modifier.height(32.dp))

        // HVAC Control
        HvacControl(
            currentTemp = state.hvacTemp,
            onTempChange = { viewModel.setHvacTemp(it) }
        )

        Spacer(Modifier.height(32.dp))

        // POI Search
        var query by remember { mutableStateOf("") }
        OutlinedTextField(
            value = query,
            onValueChange = { query = it; viewModel.updateSearch(it) },
            label = { Text("Search POI") },
            modifier = Modifier.fillMaxWidth()
        )

        val results by viewModel.searchResults.collectAsStateWithLifecycle()
        LazyColumn {
            items(results) { poi -> PoiItem(poi) }
        }
    }
}

@Composable
fun GaugeWidget(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(value, style = MaterialTheme.typography.displaySmall)
        Text(label, style = MaterialTheme.typography.labelMedium)
    }
}
```

### Why `collectAsStateWithLifecycle()`?
*   Automatically stops collection when UI is not visible.
*   Prevents background processing when user switches to another app.
*   Equivalent to `repeatOnLifecycle(STARTED)` but Compose-native.

---

## 🧪 6. Testing

### ViewModel Test
```kotlin
@Test
fun `dashboard shows warning when engine overheats`() = runTest {
    val fakeRepo = FakeVehicleRepository()
    val viewModel = DashboardViewModel(fakeRepo, FakePoiRepository())

    viewModel.dashboardState.test {
        assertEquals(DashboardUiState(), awaitItem()) // Initial

        fakeRepo.emitEngineTemp(115f)
        val state = awaitItem()
        assertEquals("⚠️ Engine Overheating!", state.warning)

        cancelAndIgnoreRemainingEvents()
    }
}
```

### AIDL Service Test
```kotlin
@Test
fun `repository emits speed from AIDL callback`() = runTest {
    val repo = VehicleRepository(mockContext)
    // Simulate AIDL callback
    repo.simulateSpeedCallback(85f)

    assertEquals(85f, repo.getSpeedFlow().first())
}
```

---

## 📝 7. Summary Checklist (For Interviews)

If asked "How do you build a real automotive dashboard?", say:

1.  **Vehicle Data:** "I read data from VHAL via **AIDL bound service**."
2.  **Repository:** "I bridge AIDL callbacks to **StateFlow** using `tryEmit()`."
3.  **ViewModel:** "I **combine** multiple vehicle flows into one `DashboardUiState` StateFlow with `WhileSubscribed(5000)`."
4.  **UI:** "I collect using **`collectAsStateWithLifecycle()`** in Compose."
5.  **Crash Isolation:** "Critical services run in **separate processes**, communicating via AIDL."
6.  **Error Handling:** "I use **`catch`** in the Flow pipeline and show fallback UI on service disconnection."
