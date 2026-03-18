# Ktor Client: Advanced Usage & Configuration

As a Senior/Staff Android Developer, using Retrofit is standard, but **Ktor** (Kotlin's official networking library) is increasingly preferred in **Kotlin Multiplatform (KMP)** and modern Android architectures. 

Ktor is built entirely on Coroutines and provides extreme flexibility through its plugin/interceptor architecture.

---

## 1. Ktor Client Engine Types

Ktor separates the API from the underlying HTTP engine. You must choose an engine:

| Engine | Characteristics | Best For |
| :--- | :--- | :--- |
| **CIO** (Coroutine I/O) | Ktor's native engine, completely async. | Pure Kotlin/JVM backends. |
| **OkHttp** | Google's standard. Supports HTTP/2, caching, interceptors. | **Android Native Apps**. |
| **Darwin** | Apple's NSURLSession. | iOS (in KMP projects). |
| **Android** | Uses `HttpURLConnection`. | Legacy fallback, avoid if possible. |
| **Mock** | Simulates HTTP responses. | Unit Testing. |

### Setup with OkHttp Engine
```kotlin
val client = HttpClient(OkHttp) {
    engine {
        // Configure native OkHttp builder
        config {
            followRedirects(true)
            retryOnConnectionFailure(true)
        }
        // Add OkHttp specific interceptors (e.g. Chucker for debugging)
        addInterceptor(ChuckerInterceptor.Builder(context).build())
    }
}
```

---

## 2. Essential Plugins (Features)

Ktor functionality is added via plugins. If you don't install a plugin, Ktor won't do it.

### A. Content Negotiation (JSON Parsing)
Replaces Retrofit's ConverterFactory (Moshi/Gson).
```kotlin
install(ContentNegotiation) {
    json(Json {
        prettyPrint = true
        isLenient = true
        ignoreUnknownKeys = true // Crucial for resilient API parsing
        explicitNulls = false
    })
}
```

### B. Logging
```kotlin
install(Logging) {
    logger = object : Logger {
        override fun log(message: String) {
            Log.v("Ktor-Network", message)
        }
    }
    level = LogLevel.ALL // ALL for debug, NONE for release
}
```

### C. Default Request (Base URL & Headers)
Applies to every request automatically.
```kotlin
install(DefaultRequest) {
    url {
        protocol = URLProtocol.HTTPS
        host = "api.harman.com"
        path("v1/")
    }
    header(HttpHeaders.ContentType, ContentType.Application.Json)
    header("X-App-Version", BuildConfig.VERSION_NAME)
}
```

---

## 3. Making Requests & Error Handling

Unlike Retrofit interfaces, Ktor uses extension methods on the `HttpClient`.

```kotlin
suspend fun fetchVehicleStatus(vehicleId: String): Result<VehicleStatus> {
    return try {
        // get() automatically parses the JSON into VehicleStatus
        val response = client.get("vehicles/$vehicleId/status") {
            // Per-request configuration
            parameter("includeDetails", true)
            header("Authorization", "Bearer $token")
        }
        Result.success(response.body())
    } catch (e: RedirectResponseException) {
        // 3xx Redirection
        Result.failure(e)
    } catch (e: ClientRequestException) {
        // 4xx Client Errors (e.g., 401 Unauthorized, 404 Not Found)
        Result.failure(e)
    } catch (e: ServerResponseException) {
        // 5xx Server Errors
        Result.failure(e)
    } catch (e: IOException) {
        // Network failure (no internet)
        Result.failure(e)
    }
}
```

---

## 4. Advanced: Custom Auth / Token Refresh

Token refresh in Ktor is handled securely via the `Auth` plugin, avoiding messy interceptors.

```kotlin
install(Auth) {
    bearer {
        loadTokens {
            // Load from encrypted storage
            BearerTokens(securePrefs.getAccessToken(), securePrefs.getRefreshToken())
        }
        refreshTokens {
            // This runs atomically if a 401 occurs
            val response = client.post("auth/refresh") {
                markAsRefreshTokenRequest() // Prevent infinite loops
                setBody(RefreshRequest(oldTokens?.refreshToken))
            }
            if (response.status == HttpStatusCode.OK) {
                val newTokens = response.body<TokenResponse>()
                securePrefs.save(newTokens)
                BearerTokens(newTokens.accessToken, newTokens.refreshToken)
            } else {
                // Refresh failed, kick user to login
                null 
            }
        }
    }
}
```

---

## 5. WebSockets (Real-time Vehicle Telemetry)

WebSockets are ideal for continuous bi-directional data (e.g., live GPS tracking, live RPM stream). Retrofit cannot do this natively; Ktor handles it beautifully.

```kotlin
// 1. Install Plugin
install(WebSockets) {
    pingInterval = 20_000 // Keep connection alive
}

// 2. Connect and listen
suspend fun listenToLiveTelemetry() {
    client.webSocket(method = HttpMethod.Get, host = "api.harman.com", path = "/telemetry") {
        
        // Launch a coroutine to send heartbeats or commands
        launch {
            while (true) {
                send(Frame.Text("{\"event\": \"heartbeat\"}"))
                delay(30000)
            }
        }

        // Suspend and read incoming frames endlessly
        for (frame in incoming) {
            when (frame) {
                is Frame.Text -> {
                    val text = frame.readText()
                    // Parse live telemetry JSON and update flow
                    val telemetry = json.decodeFromString<Telemetry>(text)
                    _telemetryState.value = telemetry
                }
                is Frame.Close -> {
                    Log.w("Ktor", "Socket closed: ${frame.readReason()}")
                }
                else -> {}
            }
        }
    }
}
```

---

## 6. Testing Ktor Client 

The `MockEngine` intercepts requests and allows you to return hardcoded responses, removing the need for a tool like MockWebServer.

```kotlin
val mockEngine = MockEngine { request ->
    if (request.url.encodedPath.contains("status")) {
        respond(
            content = """{"speed": 65, "fuel": 40}""",
            status = HttpStatusCode.OK,
            headers = headersOf(HttpHeaders.ContentType, "application/json")
        )
    } else {
        respondError(HttpStatusCode.NotFound)
    }
}

val testClient = HttpClient(mockEngine) {
    install(ContentNegotiation) { json() }
}

@Test
fun `fetch status returns parsed data`() = runTest {
    val repo = VehicleRepo(testClient)
    val status = repo.getStatus()
    assertEquals(65, status.speed)
}
```

---

## 7. Performance & Best Practices

1.  **Singleton Client:** Always inject a single instance of `HttpClient` per domain. DO NOT instantiate `HttpClient` on every request. It creates a massive memory/thread leak.
2.  **Explicit Cleanup:** If building an SDK or a library module, always call `client.close()` when the module destroys to free the engine's connection pool.
3.  **Expect Success (Opt-in):** By default, Ktor throws exceptions for non-2xx codes. You can disable this to handle 4xx responses as standard bodies if your API sends error JSON.
    ```kotlin
    val response = client.get("endpoint") {
        expectSuccess = false // Now 400s don't crash, they return the response
    }
    ```
