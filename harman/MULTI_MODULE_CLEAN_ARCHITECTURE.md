# Feature Development: Login in Multi-Module Clean Architecture

This guide breaks down how to build a `Login` feature in an enterprise-grade Android application (like those at Harman) using **Multi-Module Clean Architecture** and **SOLID principles**.

When interviewing for an 8+ YOE Staff/Principal role, you must articulate *why* code goes where it goes, focusing on dependency inversion and isolated testing.

---

## 1. The Module Structure

A single feature should be split across exactly three Gradle modules to enforce architectural boundaries. 

```text
[:feature:login:domain]       <-- PURE KOTLIN. ZERO Android Dependencies.
[:feature:login:data]         <-- Depends on :domain + Retrofit/Room.
[:feature:login:presentation] <-- Depends on :domain + Compose/ViewModel.
```

### Why 3 Modules per Feature?
1.  **Strict Boundary Enforcements:** Gradle physically prevents the `presentation` layer from importing `Retrofit` or `Room` classes. 
2.  **Build Speed:** If you edit a ViewModel in `:presentation`, Gradle does NOT need to recompile `:data` or `:domain`.
3.  **KMP Ready:** Pure Kotlin `:domain` modules can be instantly shared with iOS.

> **SOLID Principle at play:** **Single Responsibility Principle (SRP).** Each module has one reason to change. `:data` changes if the API changes. `:presentation` changes if the UI changes.

---

## 2. Layer 1: The Domain Layer (`:feature:login:domain`)

This is the core of the feature. It contains the business rules and is completely agnostic to UI, Databases, and APIs.

### A. The Entities (Models)
Pure data classes representing business objects.
```kotlin
// feature/login/domain/src/main/java/.../domain/model/UserAccount.kt
data class UserAccount(
    val id: String,
    val email: String,
    val sessionToken: String
)
```

### B. The Repository Interface (The Contract)
```kotlin
// feature/login/domain/src/main/java/.../domain/repository/LoginRepository.kt
interface LoginRepository {
    suspend fun login(email: String, pin: String): Result<UserAccount>
}
```
> **SOLID Principle at play:** **Dependency Inversion Principle (DIP).** High-level modules (`:domain`) should not depend on low-level modules (`:data`). They both depend on abstractions (`LoginRepository` interface). The domain module *defines* the interface, but does *not* implement it.

### C. The Use Case (Interactor)
A class that executes a specific business action.

```kotlin
// feature/login/domain/src/main/java/.../domain/usecase/LoginUseCase.kt
class LoginUseCase(
    private val repository: LoginRepository
) {
    suspend operator fun invoke(email: String, pin: String): Result<UserAccount> {
        // Business Rule 1: Email format validation
        if (!email.contains("@")) {
            return Result.failure(InvalidEmailException("Invalid format"))
        }
        
        // Business Rule 2: PIN length validation (Automotive safety constraint)
        if (pin.length < 4) {
            return Result.failure(WeakPinException("PIN must be 4 digits"))
        }

        return repository.login(email, pin)
    }
}
```
> **SOLID Principle at play:** **Open-Closed Principle (OCP).** If we add a new requirement (e.g., locking the account after 3 failed attempts), we create a *new* UseCase or extend the Repository, rather than modifying the UI or Network code.

---

## 3. Layer 2: The Data Layer (`:feature:login:data`)

This module implements the interfaces defined in the Domain module. It speaks to external systems (APIs, Databases, DataStore).

**Dependencies:** `:feature:login:domain`, `Retrofit`, `Room`, `Ktor`.

### A. The DTOs (Data Transfer Objects)
How the external API models the data. MUST be mapped to Domain Models so the Domain doesn't break if the API changes.
```kotlin
// feature/login/data/src/main/java/.../data/remote/dto/LoginResponseDto.kt
@Serializable
data class LoginResponseDto(
    @SerialName("user_id") val userId: String,
    @SerialName("user_email") val userEmail: String,
    @SerialName("auth_token") val authToken: String
) {
    // Mapper function
    fun toDomain() = UserAccount(id = userId, email = userEmail, sessionToken = authToken)
}
```

### B. The Data Source Interfaces
```kotlin
// feature/login/data/src/main/java/.../data/remote/AuthApi.kt
interface AuthApi {
    @POST("v1/auth/login")
    suspend fun authenticate(@Body request: LoginRequestDto): LoginResponseDto
}
```

### C. The Repository Implementation
```kotlin
// feature/login/data/src/main/java/.../data/repository/LoginRepositoryImpl.kt
class LoginRepositoryImpl(
    private val api: AuthApi,
    private val secureStorage: SecureStorage // e.g., EncryptedSharedPreferences
) : LoginRepository { // Implements the DOMAIN interface

    override suspend fun login(email: String, pin: String): Result<UserAccount> {
        return try {
            // 1. Fetch from network (DTO)
            val responseDto = api.authenticate(LoginRequestDto(email, pin))
            
            // 2. Map to Domain Model
            val userAccount = responseDto.toDomain()
            
            // 3. Save session token securely
            secureStorage.saveToken(userAccount.sessionToken)
            
            // 4. Return pure domain model
            Result.success(userAccount)
            
        } catch (e: HttpException) {
            Result.failure(NetworkException("Authentication failed"))
        } catch (e: IOException) {
            Result.failure(NoInternetException("No connection"))
        }
    }
}
```
> **SOLID Principle at play:** **Liskov Substitution Principle (LSP).** We could easily swap `LoginRepositoryImpl` with a `MockLoginRepositoryImpl` in our tests, and the `LoginUseCase` would never know the difference, because they both fulfill the contract of `LoginRepository`.

---

## 4. Layer 3: The Presentation Layer (`:feature:login:presentation`)

This module holds the UI and State management.
**Dependencies:** `:feature:login:domain`, `Compose`, `ViewModel`. (Notice it does NOT depend on `:data`).

### A. The UI State
```kotlin
// feature/login/presentation/src/main/java/.../presentation/LoginUiState.kt
data class LoginUiState(
    val emailInput: String = "",
    val pinInput: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isLoginSuccessful: Boolean = false
)
```

### B. The ViewModel
```kotlin
// feature/login/presentation/src/main/java/.../presentation/LoginViewModel.kt
@HiltViewModel // Or Koin
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase // Injected from Domain
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    // Event handlers from Compose
    fun onEmailChanged(email: String) { _uiState.update { it.copy(emailInput = email) } }
    fun onPinChanged(pin: String) { _uiState.update { it.copy(pinInput = pin) } }

    fun onLoginClicked() {
        _uiState.update { it.copy(isLoading = true, errorMessage = null) }
        
        viewModelScope.launch {
            val currentState = _uiState.value
            
            // Call the Domain UseCase
            val result = loginUseCase(currentState.emailInput, currentState.pinInput)
            
            result.onSuccess { userAccount ->
                _uiState.update { it.copy(isLoading = false, isLoginSuccessful = true) }
                // Analytics: e.g., mixpanel.track("Login Success")
            }.onFailure { error ->
                _uiState.update { it.copy(isLoading = false, errorMessage = error.message) }
            }
        }
    }
}
```

### C. The UI (Jetpack Compose)
```kotlin
// feature/login/presentation/src/main/java/.../presentation/LoginScreen.kt
@Composable
fun LoginScreen(
    viewModel: LoginViewModel = hiltViewModel(),
    onNavigateToDashboard: () -> Unit
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    // Side-effect to handle navigation safely out of recompositions
    LaunchedEffect(state.isLoginSuccessful) {
        if (state.isLoginSuccessful) {
            onNavigateToDashboard()
        }
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        if (state.isLoading) CircularProgressIndicator()
        
        state.errorMessage?.let { Text(text = it, color = Color.Red) }

        OutlinedTextField(
            value = state.emailInput,
            onValueChange = { viewModel.onEmailChanged(it) },
            label = { Text("Email") }
        )
        
        OutlinedTextField(
            value = state.pinInput,
            onValueChange = { viewModel.onPinChanged(it) },
            label = { Text("PIN") },
            visualTransformation = PasswordVisualTransformation()
        )
        
        Button(onClick = { viewModel.onLoginClicked() }, enabled = !state.isLoading) {
            Text("Login")
        }
    }
}
```

---

## 5. Dependency Injection (The Glue)

Where do we wire it all together? In the `:app` module (The Composition Root). The `:app` module depends on ALL other modules to build the final APK.

```kotlin
// app/src/main/java/.../di/LoginModule.kt
@Module
@InstallIn(SingletonComponent::class)
object LoginModule {

    @Provides
    @Singleton
    fun provideAuthApi(retrofit: Retrofit): AuthApi {
        return retrofit.create(AuthApi::class.java)
    }

    @Provides
    @Singleton
    // Hilt allows us to return the Interface, but provide the Implementation
    fun provideLoginRepository(
        api: AuthApi, 
        secureStorage: SecureStorage
    ): LoginRepository {
        return LoginRepositoryImpl(api, secureStorage)
    }

    @Provides
    @Singleton
    fun provideLoginUseCase(repository: LoginRepository): LoginUseCase {
        return LoginUseCase(repository)
    }
}
```

---

## 6. Interview Checklist: "Why did you design it this way?"

If asked by a Harman architect, answer with:

1.  **"Why Use Cases?"** $\to$ They encapsulate single, reusable business rules. If another team needs to implement "Biometric Login", they use `BiometricLoginUseCase`, keeping logic DRY.
2.  **"Why Dependency Inversion on the Repository?"** $\to$ It makes the Domain module testable instantly. I don't need a real Rest API to test my `LoginUseCase`. I pass in a FakeRepository.
3.  **"Why separate Domain and Data modules?"** $\to$ To protect business logic. If the backend team switches from REST to GraphQL, or we swap Room for Realm, only the `:data` module changes. `:domain` and `:presentation` do not even recompile.
4.  **"Where does Interface Segregation Principle (ISP) apply?"** $\to$ In the repository. Instead of one massive `UserRepository` with 50 methods, we have smaller, focused interfaces like `LoginRepository`, `ProfileUpdateRepository`, so clients only depend on methods they actually use.
