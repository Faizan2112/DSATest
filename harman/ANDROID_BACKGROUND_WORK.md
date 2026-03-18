# Android Background Work & Services: Staff-Level Guide (8+ Years)

> "Foreground Service? WorkManager? AIDL Service? Bound Service?"
> This guide covers the full decision matrix, including automotive-specific patterns for Harman, complete with detailed use cases.

---

## 1. The Decision Tree 🌳

| Scenario | Solution | Why? |
| :--- | :--- | :--- |
| **User clicks "Save"** | **Coroutines** | Instant, tied to UI lifecycle. |
| **Music / Navigation** | **Foreground Service** | User must know it's running. |
| **Sync / Upload** | **WorkManager** | Deferred, Guaranteed. |
| **Alarm / Reminder** | **AlarmManager** | Exact timing. |
| **Vehicle Data Monitoring** | **Bound Service (AIDL)** | Long-lived IPC with VHAL. |
| **OTA Download** | **WorkManager + Foreground** | Resilient, user-visible. |

---

## 2. WorkManager 🦸‍♂️

Google's recommended solution for **deferrable, guaranteed** work. It survives app process death and device reboots.

### A. One-Time Request
**Use Case:** Downloading an Over-The-Air (OTA) update package when the car connects to Wi-Fi.

```kotlin
val otaWork = OneTimeWorkRequestBuilder<OtaWorker>()
    .setConstraints(Constraints.Builder()
        .setRequiredNetworkType(NetworkType.UNMETERED) // Wi-Fi only to save cellular data
        .setRequiresStorageNotLow(true).build())
    .setBackoffCriteria(BackoffPolicy.EXPONENTIAL, 30, TimeUnit.SECONDS) // Retry logic
    .build()

// KEEP ensures if an OTA download is already running, we don't start a duplicate
WorkManager.getInstance(context).enqueueUniqueWork("ota", ExistingWorkPolicy.KEEP, otaWork)
```
**Explanation:** `setConstraints` ensures the OS only runs this job when conditions are met (Wi-Fi, sufficient storage). If the device loses Wi-Fi mid-download, WorkManager pauses the job and resumes it automatically when Wi-Fi returns.

### B. CoroutineWorker
**Use Case:** The actual implementation of the OTA download using Coroutines to avoid blocking threads.

```kotlin
class OtaWorker(ctx: Context, params: WorkerParameters) : CoroutineWorker(ctx, params) {
    override suspend fun doWork(): Result {
        return try {
            // Promotes worker to foreground, showing a notification (required for long-running tasks in Android 12+)
            setForeground(createForegroundInfo("Downloading..."))
            
            // Suspending function
            otaService.download(inputData.getString("url")!!)
            Result.success()
        } catch (e: Exception) {
            // Automatically retry with backoff if attempts < 3
            if (runAttemptCount < 3) Result.retry() else Result.failure()
        }
    }
}
```

### C. Chaining Workers
**Use Case:** A multi-step OTA process: Download $\to$ Verify Integrity $\to$ Install.

```kotlin
WorkManager.getInstance(context)
    .beginWith(downloadWorker) // Runs first
    .then(verifyWorker)        // Only starts if download completes successfully
    .then(installWorker)       // Only starts if verification passes
    .enqueue()
```
**Explanation:** WorkManager passes the output of one worker as the input to the next. If `verifyWorker` fails, `installWorker` is automatically cancelled.

### D. Periodic Work (min 15 minutes)
**Use Case:** Checking a cloud server for new map updates every 6 hours while the vehicle is parked.

```kotlin
val check = PeriodicWorkRequestBuilder<OtaCheckWorker>(6, TimeUnit.HOURS)
    .setConstraints(Constraints.Builder()
        .setRequiredNetworkType(NetworkType.CONNECTED).build())
    .build()
```
**Explanation:** Periodic work cannot be forced to run exactly at the 6-hour mark; Android batches background tasks to save battery, but it guarantees to run it *around* that interval.

---

## 3. Foreground Services 🏋️‍♂️

**Use Case:** Tasks the user actively realizes are happening and would be annoyed if the OS killed them secretly.

### Service Types (Android 14+ Mandatory)
You MUST declare why you need a Foreground Service in the Manifest.

| Type | Use Case in Automotive |
| :--- | :--- |
| `mediaPlayback` | The main Radio/Spotify app playing audio while navigation is on screen. |
| `location` | The Turn-By-Turn Navigation service tracking GPS coordinates. |
| `connectedDevice` | Maintaining a constant Bluetooth Audio connection to the user's phone. |
| `dataSync` | Syncing large offline map tiles to the local database. |

```kotlin
class NavigationService : Service() {
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        // Must show a notification so the user knows this is running
        val notification = NotificationCompat.Builder(this, NAV_CHANNEL)
            .setContentTitle("Navigation Active")
            .setSmallIcon(R.drawable.ic_nav).setOngoing(true).build()
            
        // Android 14 strict requirement: Pass the type explicitly
        ServiceCompat.startForeground(this, 1, notification,
            ServiceInfo.FOREGROUND_SERVICE_TYPE_LOCATION)
            
        return START_STICKY // Crash recovery: OS will restart this service with a null intent
    }
    
    override fun onBind(intent: Intent?): IBinder? = null
}
```

---

## 4. Bound Services & AIDL (Harman Critical) 🔗

This is the backbone of Android Automotive OS (AAOS). Different apps run in isolated sandboxes to ensure a crash in Spotify doesn't crash the Speedometer. They talk via AIDL (Inter-Process Communication).

### A. AIDL Interface Definition
Define the contract. `oneway` means the client doesn't block waiting for a response (crucial for 60fps UIs).

```aidl
interface IVehicleDataService {
    VehicleData getLatestData();
    void setHvacTemperature(int zone, float tempC);
    
    // Register clients to receive real-time updates
    oneway void registerCallback(IVehicleDataCallback callback);
    oneway void unregisterCallback(IVehicleDataCallback callback);
}
```

### B. Service (Server Process)
**Use Case:** A privileged system service that sits directly on top of the Vehicle HAL (Hardware Abstraction Layer) constantly reading the CAN bus.

```kotlin
class VehicleDataService : Service() {
    // Thread-safe list of active client apps (e.g., Dashboard, Navigation)
    private val callbacks = RemoteCallbackList<IVehicleDataCallback>()
    
    // The actual Binder stub exposed to clients
    private val binder = object : IVehicleDataService.Stub() {
        override fun getLatestData() = vhalReader.readCurrentState()
        
        override fun setHvacTemperature(zone: Int, tempC: Float) {
            vhalWriter.setProperty(HVAC_TEMP, zone, tempC)
        }
        
        override fun registerCallback(cb: IVehicleDataCallback) { callbacks.register(cb) }
        override fun unregisterCallback(cb: IVehicleDataCallback) { callbacks.unregister(cb) }
    }

    // Called when the CAN bus reports a new speed
    fun notifySpeedChanged(speed: Float) {
        val n = callbacks.beginBroadcast()
        for (i in 0 until n) {
            try { 
                // Push update to the connected client
                callbacks.getBroadcastItem(i).onSpeedChanged(speed) 
            }
            catch (_: RemoteException) { /* The client app crashed, ignore it */ }
        }
        callbacks.finishBroadcast() // Clean up crashed clients
    }
    
    override fun onBind(intent: Intent): IBinder = binder
}
```

### C. Client Connection
**Use Case:** The Dashboard App needs to show the speed on the screen.

```kotlin
class VehicleRepository(private val context: Context) {
    private var service: IVehicleDataService? = null
    
    private val connection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName, binder: IBinder) {
            // Connect the proxy
            service = IVehicleDataService.Stub.asInterface(binder)
            service?.registerCallback(vehicleCallback) // Start listening!
        }
        override fun onServiceDisconnected(name: ComponentName) { 
            // The service crashed! Android will try to reconnect automatically
            service = null 
        }
    }
    
    private val vehicleCallback = object : IVehicleDataCallback.Stub() {
        // DANGER: This runs on a Binder Thread pole, NOT the Main Thread!
        override fun onSpeedChanged(speed: Float) { _speedFlow.tryEmit(speed) }
        override fun onFuelLevelChanged(fuel: Float) { _fuelFlow.tryEmit(fuel) }
        override fun onEngineAlarm(code: Int, msg: String) { _alertFlow.tryEmit(EngineAlert(code, msg)) }
    }
    
    fun connect() {
        context.bindService(Intent().apply {
            component = ComponentName("com.harman.vehicle", "com.harman.vehicle.VehicleDataService")
        }, connection, Context.BIND_AUTO_CREATE) // BIND_AUTO_CREATE starts the service if it isn't running
    }
}
```

### Visual: AIDL Architecture
```text
[Dashboard App]                    [Vehicle Service]
(Process A)                        (Process B)
     │── bindService() ──────────>│
     │<── onServiceConnected() ──│
     │── registerCallback() ────>│
     │                            │ (CAN bus data arrives)
     │<── onSpeedChanged(85f) ──│  (Binder Thread!)
     │ [switch to Main] → UI    │
```

---

## 5. Obsolete APIs ☠️

DO NOT use these in modern codebases. They show outdated knowledge.

| API | Status | Replacement |
| :--- | :--- | :--- |
| `IntentService` | DEAD (Dep API 30) | WorkManager or Coroutines |
| `AsyncTask` | DEAD (Dep API 30) | Coroutines (`Dispatchers.IO`) |
| Background Service | Restricted | WorkManager / Foreground Service |
| `Loader` | Deprecated | ViewModel + `StateFlow` / `LiveData` |

---

## 6. AlarmManager ⏰

**Use Case:** You absolutely need something to run at exactly 2:00 AM (e.g., forcing a reboot and OTA installation while the car is unused).

```kotlin
alarmManager.setExactAndAllowWhileIdle(
    AlarmManager.RTC_WAKEUP, // Wakes the CPU up from deep sleep
    targetTime2AM, 
    pendingIntent
)
```
> **Safety Warning:** Android 12+ requires the user to explicitly grant the `SCHEDULE_EXACT_ALARM` permission, otherwise your app crashes.

---

## 7. Modern Restrictions & Automotive Context 🚨

Mobile devices aggressively kill background apps, but vehicles work slightly differently.

### Android 12/13/14 Evolution
*   **Android 12 (31):** Cannot launch Foreground Services from the background anymore. Must use WorkManager Expedited Jobs. `PendingIntent` must specify mutability flag.
*   **Android 13 (33):** Must ask for the `POST_NOTIFICATIONS` runtime permission to show your foreground service notification!
*   **Android 14 (34):** If you declare a Foreground service but omit the "type" in the manifest, the OS throws a custom `SecurityException`.

### Automotive Specifics (Harman Context)
*   **Persistent Services:** AAOS allows OEM system apps to declare `android:persistent="true"` in the manifest. The OS will never kill these (used for CAN bus readers).
*   **Garage Mode:** When the user turns off the ignition, the display turns off, but the OS goes into "Garage Mode". This is specifically designed for `WorkManager` to spin up and execute deferred jobs (sync logs, download OTA) for a set period before total shutdown.

---

## 8. Interview Cheat Sheet

| Question | Answer |
| :--- | :--- |
| "Task every hour?" | WorkManager (Periodic) |
| "Upload survives app kill?" | WorkManager (OneTime) |
| "Music player background?" | Foreground Service (mediaPlayback) |
| "Exact 8 AM task?" | AlarmManager |
| "App A reads vehicle data from App B?" | **Bound Service + AIDL** |
| "Multiple apps need vehicle data?" | **AIDL + RemoteCallbackList** |
