# System Design: Secure Mobile Banking App 🏦

> **Scenario:** "Design a Mobile Banking Dashboard that shows account balances, recent transactions, and works offline."
> **Constraint:** Security is paramount (NatWest/Fintech context).

---

## 1. Requirements Analysis (The "What")

Before drawing boxes, clarify the scope.

### A. Functional Requirements (What the user does)
1.  **View Balance:** Real-time updates.
2.  **Transaction History:** Infinite scroll list.
3.  **Money Transfer:** Secure form execution.
4.  **Offline Support:** View last known balance/transactions if internet fails.

### B. Non-Functional Requirements (How it performs)
1.  **Security:** **Zero Trust**. Data at rest and in transit must be encrypted.
2.  **Consistency:** Local DB must match Server DB eventually.
3.  **Scalability:** Modular architecture to add "Loans" or "Investments" later.

---

## 2. High-Level Architecture (The "How")

We follow **Clean Architecture** + **MVVM** + **Modularization**.

### A. Modularization Strategy
Don't put everything in `app`. Split by **Feature** and **Layer**.

```text
[:app] (DI Root)
  │
  ├── [:feature:dashboard] (UI + ViewModel)
  ├── [:feature:transfer]
  │
  ├── [:core:network] (Retrofit, OkHttp, SSL Pinning)
  ├── [:core:database] (Room, EncryptedSharedPreferences)
  ├── [:core:security] (Biometrics, Root Detection)
  └── [:core:designsystem] (Theme, Typography)
```

### B. Data Flow Diagram
```text
[UI (Compose)] <--(State)--> [ViewModel] <--(UseCase)--> [Repository]
                                                           │
                                         ┌─────────────────┴─────────────────┐
                                         ▼                                   ▼
                                  [Local Source]                      [Remote Source]
                                  (Room DB + Encrypted)               (Retrofit + SSL)
```

---

## 3. The Security Layer (NatWest Special) 🛡️


This is where you pass or fail the interview.

### Why Security Matters? (The 3 Pillars)
1.  **Trust:** If a user loses £10, they delete the app. If they lose £10,000, they sue the bank.
2.  **Compliance:** Laws like **PSD2** and **GDPR** require strict protection. Fines are millions of dollars.
3.  **Fraud:** Hackers actively target banking apps to steal sessions, money, and identity.

### A. SSL Pinning (The Secret Handshake)

#### 1. What is it? (The Analogy)
Imagine you walk into a bank branch.
*   **Without Pinning:** You trust anyone wearing a "Bank Manager" badge (even if it's a fake badge printed at home).
*   **With Pinning:** You only trust the specific Manager whose face matches the photo in your wallet. Even if an imposter has a badge, you say "No".

In technical terms: The app tells the Android OS: **"I don't trust the global Certificate Authorities. I ONLY trust this specific SHA-256 hash of my server's certificate."**

#### 2. Why do we need it? (The Attack)
**Man-in-the-Middle (MitM) Attack:**
A hacker sits in a coffee shop with "Free WiFi".
1.  user connects to WiFi.
2.  Hacker intercepts the request to `api.natwest.com`.
3.  Hacker sends back a **fake certificate**.
4.  Without pinning, the app accepts it because the hacker tricked the OS.
5.  Hacker now sees your username/password in plain text.

#### 3. How is it implemented? (The Code)
Use `network_security_config.xml` (Easiest & Safest).

```xml
<!-- res/xml/network_security_config.xml -->
<network-security-config>
    <domain-config>
        <domain includeSubdomains="true">api.natwest.com</domain>
        <pin-set>
            <pin digest="SHA-256">7HIpactkIAq2Y49orFOOQKurWxmmSFZhBCoQYcRhJ3Y=</pin>
            <pin digest="SHA-256">fwzaO7r9S302B+vK3925jJb74j0Yj112ujjo435345=</pin> <!-- Backup -->
        </pin-set>
    </domain-config>
</network-security-config>
```


### B. Secure Storage (Data at Rest)
Never use `SharedPreferences` for tokens or PII. Use `EncryptedSharedPreferences`.

```kotlin
val masterKey = MasterKey.Builder(context)
    .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
    .build()

val securePrefs = EncryptedSharedPreferences.create(
    context,
    "secret_prefs",
    masterKey,
    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
)
```

### C. Root Detection
If the device is rooted, the secure enclave might be compromised.
**Solution:** Use **Play Integrity API** (formerly SafetyNet). if `integrityVerdict != MEETS_STRONG_INTEGRITY`, block the app.

---

## 4. Offline-First Data Layer (Repository Pattern) 📡

We need a **Single Source of Truth** (SSOT). The UI **always** observes the Database, never the Network directly.

### The Algorithm:
1.  **UI** asks for data.
2.  **Repo** returns `Flow` from **Room DB** (fast, offline-ready).
3.  **Repo** triggers a **Network Fetch** in the background.
4.  If fetch succeeds, **Repo** saves to **Room**.
5.  **Room** emits valid new data to **UI**.

```kotlin
fun getTransactions(): Flow<List<Transaction>> {
    return object : NetworkBoundResource<List<Transaction>, List<NetworkTransaction>>() {
        override fun query() = dao.getAllTransactions() // Read DB
        
        override suspend fun fetch() = api.getTransactions() // Fetch Network
        
        override suspend fun saveFetchResult(items: List<NetworkTransaction>) {
            dao.insertAll(items.map { it.toEntity() }) // Write DB
        }
        
        override fun shouldFetch(data: List<Transaction>?) = isDataStale(data)
    }.asFlow()
}
```

---

## 5. Scalability & Performance 🚀

### A. Image Loading (Profile Pics, Merchant Logos)
Use **Coil** (Coroutines Image Loader).
*   **Memory Cache:** LruCache (RAM).
*   **Disk Cache:** OkHttp Cache.
*   **Downsampling:** Don't load a 4K image into a 100x100 avatar view.

### B. Network Optimization
*   **GZIP Compression:** Enable on OkHttp (default, but verify).
*   **Pagination:** Use Paging 3 library for infinite scrolling lists (Transactions). Don't fetch 10,000 records at once.

---

## 6. Interview Checklist ✅

If asked: **"How do you design X?"**, hit these points:

1.  **"Does it need to work offline?"** -> Yes? Room DB + Sync.
2.  **"How do you secure the API key?"** -> Gradle Secrets Plugin / NDK (Harder to reverse engineer).
3.  **"How do you handle Token Expiry?"** -> OkHttp Authenticator (Auto-refresh token 401).

---

## 7. Deep Dive Q&A (The Grilling) 🔥

This is where the interview gets real. They will try to find the gaps in your knowledge.

### Q1: "What happens if our SSL Certificate expires or changes? Does the app crash?"
**The Beginner Explanation:**
Imagine SSL Pinning like a **Key to a Door**. You gave the app a key (the Pin) to open the server door.
*   **The Problem:** If the server changes the lock (Certificate Rotation) and you didn't give the app the new key, the app **cannot connect**. It's locked out.
*   **The Solution:**
    1.  **Backup Keys:** Always pin at least **2 keys** (Current + Backup). If the main one expires, the server switches to the backup, and the app still works.
    2.  **Force Update:** If you messed up and didn't pin a backup, you must force every user to update the app immediately.

### Q2: "How do you handle a Token Expiry (401) in the middle of a file upload?"
**The Beginner Explanation:**
You are uploading a 1GB video. At 99%, your "Session ID" expires. The server says "Who are you? (401)".
*   **The Wrong Way:** The upload fails. The user screams. You log them out.
*   **The Right Way (OkHttp Authenticator):**
    1.  The Network Layer creates a secret side-channel.
    2.  It pauses the upload.
    3.  It calls a special "Refresh Token" API to get a new ID.
    4.  It automatically retries the failed request with the new ID.
    5.  **The user never knows anything happened.**

### Q3: "How do you sync data if the user is offline for 3 days?"
**The Beginner Explanation:**
The user makes 50 transfers while on a plane (Offline).
*   **The Buffer:** You assume the `Room DB` is the single source of truth. You save all 50 transfers locally with a flag `is_synced = false`.
*   **The Sync (WorkManager):** When the plane lands (Internet is back), WorkManager wakes up.
    1.  It reads all rows where `is_synced = false`.
    2.  It sends them to the server one by one (or in a batch).
    3.  If one fails (e.g., "Insufficient Funds"), you mark it as `failed` locally and show a red error to the user.

### Q4: "What if the user has a Rooted device? Can they steal the keys?"
**The Beginner Explanation:**
Yes. If a device is rooted, the user is "God Mode". They can read your app's memory.
*   **The Reality:** You cannot 100% stop a skilled hacker on a rooted device.
*   **The Defense:**
    1.  **Play Integrity API:** Ask Google "Is this device trusted?". If Google says "No", you disable the Banking feature entirely and only show the Balance.
    2.  **Obfuscation (R8):** Make your code unreadable (rename `getUserPassword` to `a`).

### Q5: "How does 'Login with Fingerprint' work? Do we save the password?"
**The Beginner Explanation:**
**NO.** We never save the password.
1.  **The Vault (Keystore):** When you first login, we create a secret **Encryption Key** inside the phone's secure hardware (The Vault).
2.  **The Lock:** The Vault says: *"I will only release this key if the user touches the fingerprint sensor."*
3.  **The Token:** We encrypt your "Session Token" with this key and save the gibberish to disk.
4.  **The Unlock:** Next time, you touch the sensor -> Vault unlocks the Key -> We decrypt the Token -> You are logged in.
