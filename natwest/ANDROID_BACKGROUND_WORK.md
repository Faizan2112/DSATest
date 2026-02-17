# Android Background Work: The Ultimate Decision Matrix

> "Do I use a Service? A Thread? WorkManager?"
> This guide stops the guessing.

---

## 1. The Decision Tree (Start Here) 🌳

Before writing code, ask **3 Questions**:

1.  **Is it Instant?** (User is waiting for it).
2.  **Is it Exact?** (Must happen at 8:00 AM sharp).
3.  **Is it Long-Running?** (Checking server every 15 mins).

| Scenario | Solution | Why? |
| :--- | :--- | :--- |
| **User clicks "Save"** | **Coroutines (`lifecycleScope`)** | Instant, tied to UI lifecycle. |
| **Playing Music / GPS** | **Foreground Service** | User needs to *know* it's running (Notification). |
| **Sync Data (Gmail)** | **WorkManager** | Deferred, Guaranteed, Battery Optimized. |
| **Alarm Clock** | **AlarmManager** | Needs EXACT timing (wakes up phone). |
| **Downloading File** | **WorkManager / DownloadManager** | Resilient to network loss. |

---

## 2. WorkManager (The Hero) 🦸‍♂️

**Google's Recommended Solution** for deferrable, guaranteed work.
*   **Guaranteed:** Runs even if app is killed or device restarts.
*   **Battery Friendly:** Batches jobs together (Doze Mode complaint).

### A. Simple One-Time Request
```kotlin
val uploadWork = OneTimeWorkRequestBuilder<UploadWorker>()
    .setConstraints(
        Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED) // Only when online
            .setRequiresCharging(true)                     // Only when charging
            .build()
    )
    .build()

WorkManager.getInstance(context).enqueue(uploadWork)
```

### B. The Worker Class
```kotlin
class UploadWorker(context: Context, params: WorkerParameters) : 
    CoroutineWorker(context, params) { // Use CoroutineWorker for async!

    override suspend fun doWork(): Result {
        return try {
            val response = api.uploadFile() // Suspend function
            if (response.isSuccessful) Result.success() else Result.retry()
        } catch (e: Exception) {
            Result.failure()
        }
    }
}
```

### C. Periodic Work (Sync)
**Note:** Minimum interval is **15 minutes**. You cannot run WorkManager every 10 seconds.
```kotlin
val syncWork = PeriodicWorkRequestBuilder<SyncWorker>(15, TimeUnit.MINUTES)
    .build()
```

---

## 3. Foreground Services (The Specialist) 🏋️‍♂️

**Use Only If:** The user **MUST** be aware the app is doing something (Music, Navigation, Screen Recording).
**Requirement:** Must show a lingering **Notification**.

```kotlin
class MusicService : Service() {
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        // 1. Create Notification
        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Playing Music")
            .setSmallIcon(R.drawable.ic_music)
            .build()

        // 2. PROMOTE to Foreground (Crucial!)
        startForeground(1, notification)

        // 3. Do work...
        return START_NOT_STICKY
    }
}
```

---

## 4. The Obsolete & The Dangerous ☠️

### A. IntentService (Deprecated)
*   **Status:** **DEAD**. Do not use.
*   **Why?** API 30+ kills background services instantly.
*   **Replacement:** WorkManager.

### B. Background Service (Hidden)
*   **Status:** **Restricted**.
*   **Behavior:** If your app is not visible, the OS will kill this service within minutes.
*   **Use Case:** Almost none. Use WorkManager.

---

## 5. AlarmManager (The Precision Tool) ⏰

**Use Only If:** You need to wake up the device at an **EXACT** time (e.g., Calendar Event, Alarm Clock).
**Cost:** High battery usage.

```kotlin
val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
val intent = Intent(context, AlarmReceiver::class.java)
val pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0)

// Wakes up device at specific time
alarmManager.setExactAndAllowWhileIdle(
    AlarmManager.RTC_WAKEUP, 
    triggerTime, 
    pendingIntent
)
```

---

## 6. Interview Cheat Sheet

*   **"How do I run a task every hour?"** -> `WorkManager` (Periodic).
*   **"How do I verify a file upload continues if I quit the app?"** -> `WorkManager` (OneTime).
*   **"How do I build a Music Player?"** -> `Foreground Service`.
*   **"How do I run a task at exactly 8:00 AM?"** -> `AlarmManager`.

---

## 7. Modern Android Restrictions (Must Know) 🚨

If you target **Android 12+**, these rules apply.

### A. Android 12 (API 31)
1.  **Exact Alarms:** Require `SCHEDULE_EXACT_ALARM` permission. If you abuse it, Google Play rejects your app.
2.  **Foreground Service Launch:** You **cannot** start a Foreground Service from the background anymore (unless whitelisted). Use WorkManager for deferred work.

### B. Android 13 (API 33)
1.  **Notifications:** Runtime permission `POST_NOTIFICATIONS` is required to show the Foreground Service notification.

### C. Android 14 (API 34)
1.  **Mandatory Service Types:** You differ specify a type in Manifest.
    *   `<service android:foregroundServiceType="mediaPlayback" />`
    *   `<service android:foregroundServiceType="dataSync" />`
    *   If you don't use a valid type, the app crashes.
