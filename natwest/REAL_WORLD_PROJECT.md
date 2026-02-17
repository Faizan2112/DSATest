# Real-World Project: "CryptoLive" 🚀

> Stop reading about `flowOf("Hello", "World")`. Let's build a **Real-Time Bitcoin Price Tracker**.

This guide demonstrates how **Coroutines** and **Flows** work together in a production-grade Android app.

---

## 🏗️ 1. The Architecture (Clean MVVM)

We are building **"CryptoLive"**:
1.  **Polls** the Bitcoin price every 5 seconds.
2.  **Searches** for other coins (e.g., "Ethereum") with live typing.
3.  **Handles Errors** gracefully (no crashes!).

**The Data Pipeline:**
```text
[Network (Retrofit)] <--(Suspend)--> [Repository (Flow)] <--(Transform)--> [ViewModel (StateFlow)] <--(Collect)--> [UI (Activity)]
```

---

## 🌐 2. The Network Layer (Retrofit)

We use `suspend` functions for one-shot API calls.

```kotlin
interface CryptoApi {
    @GET("coins/{id}")
    suspend fun getCoinPrice(@Path("id") id: String): CoinResponse
}
```

---

## 🏭 3. The Repository (The Data Pump)

**Goal:** Fetch data every 5 seconds (Polling).
**Tool:** `flow { while(true) { ... } }`.

```kotlin
class CryptoRepository(private val api: CryptoApi) {

    // 1. Polling Flow: Emits price every 5 seconds
    fun getPriceFlow(coinId: String): Flow<CoinResponse> = flow {
        while (true) {
            emit(api.getCoinPrice(coinId)) // Fetch Data
            delay(5000)                    // Wait 5s
        }
    }.flowOn(Dispatchers.IO) // ⚡ Run on IO Thread
}
```

---

## 🧠 4. The ViewModel (The Brain)

**Goal:** Manage State, Handle Errors, and Transform Data.
**State:** We use a simple Sealed Class.

```kotlin
sealed class CryptoUiState {
    object Loading : CryptoUiState()
    data class Success(val price: Double) : CryptoUiState()
    data class Error(val message: String) : CryptoUiState()
}
```

**The Logic:**
```kotlin
class CryptoViewModel(private val repository: CryptoRepository) : ViewModel() {

    // 1. The Trigger (User types here)
    private val _searchQuery = MutableStateFlow("bitcoin")

    // 2. The Transformation Pipeline
    val uiState: StateFlow<CryptoUiState> = _searchQuery
        .debounce(300)                     // Wait for typing to stop
        .distinctUntilChanged()            // Don't search "btc" twice
        .flatMapLatest { query ->          // Cancel old search, start new one
            repository.getPriceFlow(query)
                .map { response -> 
                    CryptoUiState.Success(response.price) 
                }
                .catch { e -> 
                    emit(CryptoUiState.Error(e.message ?: "Unknown Error")) 
                }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000), // ❤️ Saves Battery!
            initialValue = CryptoUiState.Loading
        )

    // Public method for UI to call
    fun updateSearch(query: String) {
        _searchQuery.value = query
    }
}
```

### Why this code?
*   **`debounce(300)`:** Prevents API spam when user types fast.
*   **`flatMapLatest`:** Crucial for search. If user types "Bit", then "Bitc", we **cancel** the "Bit" request.
*   **`catch`:** Catches network errors *inside* the pipeline so the Flow doesn't crash.
*   **`WhileSubscribed(5000)`:** Stops polling 5 seconds after the user minimizes the app. **This is a Pro-level optimization.**

---

## 📱 5. The UI (The View)

**Goal:** Consume the State safely.
**Tool:** `repeatOnLifecycle`.

```kotlin
class CryptoActivity : AppCompatActivity() {
    private val viewModel: CryptoViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crypto)

        val priceText = findViewById<TextView>(R.id.priceText)
        val progressBar = findViewById<ProgressBar>(R.id.progressBar)
        val search = findViewById<EditText>(R.id.search)

        // 1. Setup Search Listener
        search.addTextChangedListener { text ->
            viewModel.updateSearch(text.toString())
        }

        // 2. Collect State Safely
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) { // 🛑 Stops when backgrounded
                viewModel.uiState.collect { state ->
                    when (state) {
                        is CryptoUiState.Loading -> {
                            progressBar.isVisible = true
                            priceText.isVisible = false
                        }
                        is CryptoUiState.Success -> {
                            progressBar.isVisible = false
                            priceText.isVisible = true
                            priceText.text = "$${state.price}"
                        }
                        is CryptoUiState.Error -> {
                            progressBar.isVisible = false
                            showSnackbar(state.message) // "Network Error"
                        }
                    }
                }
            }
        }
    }
}
```

---

## 📝 6. Summary Checklist (For Interviews)

If asked "How do you fetch data in a real app?", say this:

1.  **Network:** "I use Retrofit with **Suspend Functions**."
2.  **Repository:** "I expose a **Flow** that emits updates (e.g., polling)."
3.  **ViewModel:** "I transform the Flow into a **StateFlow** using `stateIn` with `WhileSubscribed(5000)` to handle rotation and backgrounding efficiently."
4.  **UI:** "I collect the StateFlow using `repeatOnLifecycle` to prevent memory leaks and wasted resources."
