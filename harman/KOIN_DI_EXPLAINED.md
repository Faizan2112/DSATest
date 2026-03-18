# Koin Dependency Injection: Advanced Guide (Staff Level)

While Hilt/Dagger use code generation (kapt/ksp) at compile-time to resolve dependencies, **Koin** is a lightweight, pure-Kotlin Service Locator that resolves dependencies at **runtime**.

Koin is exceptionally popular in **Kotlin Multiplatform (KMP)** and isolated architectures (like Automotive SDKs) due to its lack of annotation processing.

---

## 1. The Core DSL (Domain Specific Language)

### Modules & Definitions
```kotlin
val networkModule = module {
    // 1. single: Creates a Singleton (lives for app lifetime)
    single { HttpClient(OkHttp) }

    // 2. factory: Creates a NEW instance every time it is injected
    factory { DateFormatter() }

    // 3. viewModel: Special binding tied to Android architecture lifecycle
    viewModel { DashboardViewModel(repo = get()) }
}
```

### The `get()` function
`get()` is the magic resolver. Koin will look at the type required by the parameter and find the corresponding definition.

```kotlin
val dataModule = module {
    // Needs HttpClient, Koin will resolve it by calling get()
    single { VehicleRepository(client = get()) } 
}
```

---

## 2. Scopes: Managing Memory (Crucial for Harman)

If an infotainment app stays open 24/7, using `single` for everything causes catastrophic memory leaks. We must use **Scopes** to tie dependencies to specific lifecycles (e.g., an active User Session, or a specific feature flow).

```kotlin
val sessionModule = module {
    // Define a scope named "UserSession"
    scope<UserSession> {
        // These objects ONLY exist when the UserSession scope is active
        scoped { UserProfileRepository(api = get()) }
        scoped { SecureKeysManager() }
    }
}
```

**Using the Scope in Code:**
```kotlin
class LoginViewModel(private val koin: Koin) : ViewModel() {
    fun onLoginSuccess(userId: String) {
        // 1. CREATE the scope
        val sessionScope = koin.createScope("session_$userId", named<UserSession>())
        
        // 2. Resolve scoped dependency
        val profileRepo = sessionScope.get<UserProfileRepository>()
    }

    fun onLogout() {
        // 3. DESTROY the scope (Frees all objects from memory instantly)
        koin.getScope("session_$userId").close()
    }
}
```

---

## 3. Qualifiers (Named Parameters)

What if you have multiple objects of the same Type? (e.g., An authenticated Retrofit instance and a public Retrofit instance).

```kotlin
val networkModule = module {
    single(named("public")) { HttpClient() }
    
    single(named("auth")) { 
        HttpClient { install(Auth) { ... } } 
    }

    // Injecting specific ones
    single { PublicRepository(api = get(named("public"))) }
    single { SecureRepository(api = get(named("auth"))) }
}
```

---

## 4. Injected Parameters (Dynamic Arguments)

Sometimes you can't satisfy a dependency at module load time. You need to pass an argument from the Activity/Fragment dynamically.

```kotlin
val vehicleModule = module {
    // Expects a string 'vehicleId' at injection time
    factory { (vehicleId: String) -> VehicleTelemetryManager(vehicleId, api = get()) }
}
```

**Injecting with parameters:**
```kotlin
class VehicleDetailsActivity : ComponentActivity() {
    // Pass the vehicleId from the Intent into the Koin resolution
    private val telemetryManager: VehicleTelemetryManager by inject { 
        parametersOf(intent.getStringExtra("VIN")) 
    }
}
```

---

## 5. Isolated Koin Contexts (SDK Development)

**Staff-Level Scenario:** Harman builds a `VehicleSDK.aar` that car manufacturers include in their apps. 
*   **Problem:** If the manufacturer uses Koin, and the SDK uses Koin, their dependencies will clash in the global `GlobalContext`.
*   **Solution:** Isolated Koin instances (`KoinApplication`).

```kotlin
// Inside the SDK
object VehicleSdk {
    // Create an isolated instance, DO NOT launch into GlobalContext
    val koinApp = koinApplication {
        modules(sdkInternalModule, networkModule)
    }

    // Expose a custom inject delegate for internal SDK classes
    inline fun <reified T> inject() = lazy { koinApp.koin.get<T>() }
}

// Internal SDK class using the isolated instance
internal class TelemetryService {
    private val api: VehicleApi by VehicleSdk.inject()
}
```

---

## 6. Koin in Jetpack Compose

Koin has first-class integration with Compose.

```kotlin
@Composable
fun DashboardScreen(
    // 1. Inject ViewModel safely integrated with Compose Navigation/BackStack
    viewModel: DashboardViewModel = koinViewModel(),
    
    // 2. Inject standard Koin components
    formatter: DateFormatter = koinInject()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    Text("Last sync: ${formatter.format(state.timestamp)}")
}
```

---

## 7. Testing with Koin

Koin lets you hot-swap modules, which is significantly easier than Dagger/Hilt's test setups.

```kotlin
class DashboardViewModelTest : KoinTest { // Implements KoinTest

    // Hot-swap the network module with a Mock module
    private val mockModule = module {
        single<VehicleApi> { FakeVehicleApi() }
    }

    @Before
    fun setup() {
        startKoin { modules(appModule, mockModule) }
    }

    @After
    fun tearDown() {
        stopKoin() // Must clean up!
    }

    @Test
    fun `test loading data`() {
        // Will inject the App ViewModel, but with the Mock API!
        val viewModel: DashboardViewModel = get()
        // ... assert
    }
}
```

---

## 8. Hilt vs Koin (Interview Question)

**"Why would we choose Koin over Hilt/Dagger?"**

| Metric | Hilt / Dagger | Koin |
| :--- | :--- | :--- |
| **Error Checking** | **Compile-time** (App won't build if missing deps). | **Run-time** (App crashes at start if missing deps). |
| **Build Time** | Slow (KAPT/KSP generates thousands of lines). | **Fast** (No code generation). |
| **Multiplatform** | Android/JVM only. | **KMP Native** (iOS, Desktop, Web). |
| **Learning Curve**| Steep (`@Component`, `@Subcomponent`, `@Binds`). | Easy (DSL is just Kotlin lambdas). |

**The Developer Decision:**
*   Large legacy Android team (50+ devs): Use **Hilt** (compile-time safety prevents stupid merges).
*   Fast-moving modern team / KMP project / Automotive SDK: Use **Koin** (flexibility, fast builds, isolated contexts).
