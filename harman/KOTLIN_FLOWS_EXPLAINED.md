# Kotlin Flows: Advanced Guide for Staff-Level Android (8+ Years)

This guide covers **Kotlin Flow** at the depth expected by Harman International for a Senior/Staff Android Developer, including automotive and embedded system patterns.

---

## 1. The Basics (Quick Recap)

**Concept:** A Flow is a sequence of values emitted over time.

### How to Create One?
1.  **`flow { emit("value") }`**: Manual emission.
2.  **`listOf(1, 2, 3).asFlow()`**: Convert a collection.
3.  **`flowOf("A", "B", "C")`**: Quick fixed values.
4.  **`callbackFlow { ... }`**: Bridge callback APIs (AIDL, sensors, BLE).

### A Flow Does NOTHING Until Collected
```kotlin
val sensorFlow = flow {
    emit(vehicleService.getSpeed())
    delay(100) // 10 Hz
    emit(vehicleService.getSpeed())
}

// Nothing happens until:
lifecycleScope.launch {
    sensorFlow.collect { speed -> updateSpeedometer(speed) }
}
```

---

## 2. The Flow Pipeline (Three Roles)

```text
   [ Producer ]  ----->  [ Transformer ]  ----->  [ Collector ]
   ( emit(raw) )         ( map/filter )          ( collect/UI )
       ⬆                       ⬆                       ⬆
    "I make"               "I change"              "I consume"
```

*   **Producer:** `flow { }`, `callbackFlow { }`, Room DAO
*   **Transformer:** `.map()`, `.filter()`, `.debounce()`, `.flatMapLatest()`
*   **Collector:** `.collect()`, `.launchIn()`, `.first()`, `.toList()`
*   **Pipeline starts ONLY when Collector starts!** (Terminal operator)

---

## 3. Cold vs Hot Streams

### ❄️ Cold Stream (`Flow`)
**Analogy:** Netflix (On-Demand).
*   Each collector gets a **fresh** stream from the beginning.
*   Stops when collector stops.

```text
  [ Movie File ] (Inactive until Play)
        │
   ┌────┴────┐
   ▼         ▼
[User A]   [User B]
(00:00)    (00:00)   <- Both start fresh
```

### 🔥 Hot Stream (`StateFlow` / `SharedFlow`)
**Analogy:** Live Radio Broadcast.
*   Broadcasting even with 0 listeners.
*   Late subscribers miss past emissions.

```text
  [ Radio Tower ] 📡 ⚡ (Broadcasting 24/7)
        │
   ┌────┼────┐
   ▼    ▼    ▼
[You] [Car] [Friend]  <- All hear same thing NOW
```

---

## 4. StateFlow vs SharedFlow

| Feature | `StateFlow` | `SharedFlow` |
| :--- | :--- | :--- |
| **Analogy** | **Dashboard Gauge** (always shows current) | **Alert Notification** (rings once) |
| **Initial Value** | **Required** | **None** |
| **Replay** | **1** (always current value) | **0** (configurable) |
| **Equality Check** | **Skips duplicate values** | **Emits all values** |
| **Use Case** | UI State (Speed, Temperature display) | One-shot Events (Toast, Navigation) |

### Visual: StateFlow (The Gauge)
```text
[StateFlow: Speed] holds: "60 km/h"
     │
     ├──> New Fragment subscribes?
     │    Sees "60 km/h" immediately.
     │
     └──> Speed changes to "80 km/h"?
           All subscribers see "80 km/h".
```

### Code
```kotlin
// StateFlow: Vehicle state (always has a value)
private val _speed = MutableStateFlow(0f)
val speed = _speed.asStateFlow()

// SharedFlow: One-shot alerts (collision warning, lane departure)
private val _alerts = MutableSharedFlow<DriverAlert>()
val alerts = _alerts.asSharedFlow()
```

---

## 5. Backpressure (Fast Producer, Slow Consumer)

### A. `buffer()` (The Queue)
Producer keeps emitting. Items queue up. Consumer processes one by one.
```text
[VHAL: 100Hz] --(Fast)--> [🚗🚗🚗🚗🚗] --(Slow)--> [UI: 16ms/frame]
```
```kotlin
speedFlow.buffer(64).collect { updateUI(it) }
```

### B. `conflate()` (Keep Latest Only)
Drop intermediate values. Consumer always gets the **latest**.
**Use Case:** Vehicle speed display (you only care about current speed).
```text
[Speed] --> 60 --> 62 --> 65 (drops 60, 62) --> [UI gets 65]
```
```kotlin
vehicleSpeedFlow.conflate().collect { updateSpeedometer(it) }
```

### C. `collectLatest()` (Cancel Previous Work)
New value arrives → cancel ongoing consumer work → start fresh.
**Use Case:** Search, Map rendering.
```text
User types "Ber"... starts searching...
User types "Berl"... 🛑 CANCEL "Ber" search... starts "Berl"…
```
```kotlin
searchQuery.collectLatest { query ->
    val results = searchPOI(query) // Cancelled if new query comes
    showResults(results)
}
```

---

## 6. Operators (The Toolkit)

### `map` / `filter` (Transform / Filter)
```kotlin
// Convert raw VHAL property to UI model
vehiclePropertyFlow
    .filter { it.propertyId == VehicleProperty.PERF_VEHICLE_SPEED }
    .map { SpeedDisplay(it.value, it.unit) }
    .collect { updateSpeedWidget(it) }
```

### `zip` (Pair Two Streams)
Waits for BOTH streams to have a value, then combines.
```text
Speed:    [60] ---------> [80]
Fuel:     [Full] --> [Half]
          ⬇ zip
Result:   [60, Full] --> [80, Half]
```

### `combine` (React to ANY Change)
Updates whenever **either** stream emits.
```text
Speed:    [60] --------------------> [80]
Fuel:     [Full] ---------> [Half]
          ⬇ combine
Result:   [60, Full] --> [60, Half] --> [80, Half]
```

**Use Case at Harman:** Dashboard combining speed + fuel + engine temp.
```kotlin
combine(speedFlow, fuelFlow, tempFlow) { speed, fuel, temp ->
    DashboardState(speed = speed, fuel = fuel, engineTemp = temp)
}
```

### `debounce` (Wait for Pause)
```kotlin
// Don't process every keystroke, wait 300ms for user to stop
searchInput.debounce(300).distinctUntilChanged().collect { search(it) }
```

---

## 7. Exception Handling in Flows

**Rule:** Use `catch` operator. It catches **upstream** errors.

```kotlin
vehicleDataFlow
    .map { processData(it) }      // Error could happen here
    .catch { e ->
        emit(FallbackData)         // 🛡️ Emit fallback
        logError(e)
    }
    .collect { updateUI(it) }
```

### Visual: Safety Net
```text
[VHAL Source] --(Data)--> [map] --(Error!)--> 💥
                                               │
                                               ▼
                                        [catch] 🛡️ → [Collector: FallbackData]
```

---

## 8. `stateIn` & `SharingStarted` (Staff-Level Essential)

**Concept:** Convert cold Flows into hot StateFlows with lifecycle awareness.

| Strategy | Behavior | Use Case |
| :--- | :--- | :--- |
| **`Eagerly`** | Starts immediately, never stops. | Vehicle state monitoring |
| **`Lazily`** | Starts on first subscriber, never stops. | Heavy cache |
| **`WhileSubscribed(5000)`** | Starts on sub, **stops** 5s after last unsub. | **UI State (Best Practice)** |

### Why `WhileSubscribed(5000)`?
```text
[Dashboard UI] (Active) ------> [VHAL Polling Runs]
      │
      ▼
(Switch to Settings) ----------> [Polling Pauses: 5..4..3..]
      │
      ▼
(Switch back to Dashboard) ----> [Polling Resumes (No restart!)] ✅
```

If the user doesn't return within 5s → upstream stops → saves battery/CPU.

```kotlin
val dashboardState = repository.getVehicleDataFlow()
    .map { DashboardUiState.Success(it) }
    .stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = DashboardUiState.Loading
    )
```

---

## 9. `callbackFlow` (Bridge Legacy APIs)

**Critical for Harman:** Most vehicle APIs use callback-based patterns. `callbackFlow` bridges them to Flow.

```kotlin
fun getVehicleSpeedFlow(carPropertyManager: CarPropertyManager): Flow<Float> = callbackFlow {
    val callback = object : CarPropertyManager.CarPropertyEventCallback {
        override fun onChangeEvent(event: CarPropertyValue<*>) {
            trySend(event.value as Float) // ✅ Send to Flow
        }
        override fun onErrorEvent(propId: Int, zone: Int) {
            close(VehiclePropertyException("Error reading speed"))
        }
    }

    carPropertyManager.registerCallback(
        callback,
        VehiclePropertyIds.PERF_VEHICLE_SPEED,
        CarPropertyManager.SENSOR_RATE_NORMAL
    )

    awaitClose {
        carPropertyManager.unregisterCallback(callback) // 🧹 Cleanup!
    }
}
```

---

## 10. `flowOn` & Context Preservation

**Rule:** `flowOn` affects everything **UPSTREAM** (above it).

```text
flow {
    emit(vehicleService.getSpeed()) // Executed on IO ⚡
}
.flowOn(Dispatchers.IO)             // ⬆ Affects everything above
.map { formatSpeed(it) }            // Executed on Main (Collector's thread)
.collect { showSpeed(it) }          // Executed on Main (UI)
```

---

## 11. Concurrency Operators

### `flatMapConcat` (Sequential)
Process one emission at a time. Safe, ordered, slow.

### `flatMapMerge` (Parallel)
Process multiple emissions concurrently. Fast, unordered.

### `flatMapLatest` (Cancel Previous)
New emission → cancel in-flight work → start fresh. **Best for search/UI**.

```kotlin
// The search pattern at Harman
searchQuery
    .debounce(300)
    .distinctUntilChanged()
    .flatMapLatest { query -> // Cancel old search
        repository.searchPOI(query)
    }
    .flowOn(Dispatchers.IO)
    .catch { emit(emptyList()) }
    .collect { showResults(it) }
```

---

## 12. Lifecycle-Safe Collection

### ❌ BAD: Keeps collecting in background
```kotlin
lifecycleScope.launch {
    viewModel.speedFlow.collect { ... } // Runs even when app is backgrounded!
}
```

### ✅ GOOD: Stops when UI is invisible
```kotlin
lifecycleScope.launch {
    repeatOnLifecycle(Lifecycle.State.STARTED) {
        viewModel.speedFlow.collect { speed ->
            updateSpeedometer(speed)
        }
    }
}
```

---

## 13. Testing Flows (Turbine)

```kotlin
@Test
fun `speed flow emits correct values`() = runTest {
    val repo = FakeVehicleRepository()

    repo.getSpeedFlow().test {
        assertEquals(0f, awaitItem())    // Initial
        repo.emitSpeed(60f)
        assertEquals(60f, awaitItem())   // Updated
        cancelAndIgnoreRemainingEvents()
    }
}
```

---

## 14. The Flow Cookbook (Automotive Recipes)

### A. Real-Time Vehicle Dashboard
```kotlin
val dashboardState = combine(
    vehicleRepo.getSpeedFlow(),
    vehicleRepo.getFuelFlow(),
    vehicleRepo.getEngineTemp()
) { speed, fuel, temp ->
    DashboardUiState(speed, fuel, temp)
}
.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), DashboardUiState.Loading)
```

### B. POI Search (Points of Interest)
```kotlin
searchQuery
    .debounce(300)
    .distinctUntilChanged()
    .flatMapLatest { query ->
        if (query.isBlank()) flowOf(emptyList())
        else repository.searchPOI(query)
    }
    .flowOn(Dispatchers.IO)
    .catch { emit(emptyList()) }
```

### C. Multi-Sensor Monitoring
```kotlin
// Monitor multiple vehicle sensors, react to thresholds
combine(
    speedFlow, rpmFlow, coolantTempFlow
) { speed, rpm, coolant ->
    if (coolant > 110f) DriverAlert.OverheatWarning
    else if (speed > 120f && rpm > 6000) DriverAlert.HighPerformanceMode
    else DriverAlert.Normal
}
.distinctUntilChanged()
.collect { alert -> showDriverAlert(alert) }
```

### D. Periodic OTA Check
```kotlin
flow {
    while (currentCoroutineContext().isActive) {
        emit(otaService.checkForUpdates())
        delay(3600_000) // Check every hour
    }
}
.flowOn(Dispatchers.IO)
.catch { emit(OtaStatus.CheckFailed) }
```
