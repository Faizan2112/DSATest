# Android Components Lifecycle (Advanced Scenarios)

For a Senior/Staff Android Developer (8+ Years), knowing `onCreate` $\to$ `onDestroy` is not enough. You must understand state restoration, process death, multi-window, and automotive-specific transitions.

---

## 1. Activity Lifecycle: What to Actually Do

Unlike junior developers who put everything in `onCreate()`, Staff engineers strictly delegate tasks based on visibility and CPU priority.

| Callback | State | What you MUST do | What you MUST NOT do |
| :--- | :--- | :--- | :--- |
| **`onCreate()`** | Created (Not Visible) | Set `setContentView()` or `setContent {}`. Initialize `ViewModel`. Restore `SavedStateHandle`. | Do not start animations. Do not request Camera/Location updates (wastes battery). |
| **`onStart()`** | Visible (But no Focus) | Start observing `Flows` that update the UI (`repeatOnLifecycle(STARTED)`). Register BroadcastReceivers specific to UI state. | Do not start playing Media or accepting Text Input. The user physically cannot click the screen yet. |
| **`onResume()`** | Visible & Focused | Start Camera Previews. Resume Video playback. Request immediate GPS Triangulation. Resume animations. | Do not do heavy DB queries. This is the final millisecond before the app is drawn; blocking here causes stuttering. |
| **`onPause()`** | Visible (Lost Focus) | Pause Video. Pause Camera Previews. Pause heavy CPU operations. | Do not save large DB files or make Network API calls. The OS limits execution time here. |
| **`onStop()`** | Invisible | Cancel `Flow` observers. Unregister BroadcastReceivers. The UI is completely gone. | Do not rely on this being called before Process Death if memory is critically low. |
| **`onDestroy()`** | Dead | Nullify ViewBindings (`_binding = null`). Clean up third-party SDK hard-references. | Do not assume this will ever be called. The OS often murders apps from `onStop` or `onPause` directly. |

---

## 2. The Edge Cases (Multi-Window, Process Death, & Dialogs)

### A. The "Dialog" Trap (Extremely Common Interview Trap)
Junior developers falsely assume that if Activity A shows a Dialog, Activity A goes to `onPause()`. **This is FALSE.**

*   **`AlertDialog` / Standard `Dialog`:** These are *View* components attached to the existing Activity window. **NO lifecycle methods are triggered.** The Activity remains fully in `onResume()`.
*   **`DialogFragment` / `BottomSheetDialogFragment`:** 
    *   The *Fragment* inside the dialog goes through its full lifecycle (`onCreateView` $\to$ `onResume`).
    *   The underlying *Activity* (Activity A) **DOES NOT change lifecycle**. It remains in `onResume()`.
*   **Transparent Activity (Theme.Dialog):** If Activity A launches Activity B (which has a transparent background, making it look like a dialog), Activity A goes to `onPause()` (because it lost focus but is still visible). Activity A does **NOT** go to `onStop()`.

### B. Configuration Change (e.g., Rotation, Screen Fold)
When a device is rotated or a folding phone is opened, the Activity is destroyed and recreated to load new resources (e.g., `layout-land`).

**Flow:**
`onPause()` $\to$ `onStop()` $\to$ `onSaveInstanceState()` $\to$ `onDestroy()`
[NEW INSTANCE] $\to$ `onCreate()` $\to$ `onStart()` $\to$ `onRestoreInstanceState()` $\to$ `onResume()`

> **Key insight:** `ViewModel` survives this because it is retained in the `ViewModelStore`, which the `ComponentActivity` holds onto during configuration changes using `onRetainNonConfigurationInstance()`.

### C. OS Kills App (Process Death)
If the app is in the background (`onStop`) and the system needs memory (very common in constrained automotive head units), the process is murdered. **`onDestroy()` is NOT called.**

**Recovery Flow (User opens app again from Recents):** A completely *new process* is created.
[NEW INSTANCE] $\to$ `onCreate(storedBundle)` $\to$ `onStart()` $\to$ `onRestoreInstanceState(storedBundle)` $\to$ `onResume()`

> **How to test Process Death:** Open app $\to$ Hit Home Button $\to$ Use terminal: `adb shell am kill com.your.package` $\to$ Open app from Recents.

---

## 3. Fragment Lifecycle: The Double Lifecycle Trap

Fragments have a massive architectural flaw: The Fragment object outlives its own UI.

**Creation:**
`onAttach()` $\to$ `onCreate()` $\to$ `onCreateView()` $\to$ `onViewCreated()` $\to$ `onStart()` $\to$ `onResume()`

**Destruction (Back stack / Replacement):**
`onPause()` $\to$ `onStop()` $\to$ `onDestroyView()` $\to$ `onDestroy()` $\to$ `onDetach()`

### The "View" Lifecycle vs "Fragment" Lifecycle
*   The Fragment object (memory variables) outlives the Fragment's View.
*   When a Fragment goes onto the back-stack (e.g., `parentFragmentManager.beginTransaction().replace().addToBackStack()`), the GUI is destroyed (`onDestroyView`), but the Fragment instance remains alive in RAM (`onDestroy` is NOT called).
*   **💥 The #1 Memory Leak in Android:** If you hold a reference to View hierarchy (`private var binding: FragmentMainBinding?`) and do not set `binding = null` in `onDestroyView()`, you leak the entire screen's memory while the Fragment sits invisibly on the back-stack.

```kotlin
// The only correct way to use ViewBinding in Fragments
private var _binding: FragmentExampleBinding? = null
private val binding get() = _binding!! // Crash early if accessed at wrong time

override fun onCreateView(...) {
    _binding = FragmentExampleBinding.inflate(inflater, container, false)
    return binding.root
}

override fun onDestroyView() {
    super.onDestroyView()
    // CRITICAL: Prevents Memory Leaks when Fragment goes to Back-Stack
    _binding = null 
}
```

---

## 4. Service Lifecycle

### A. Started Service (`startService`)
`onCreate()` $\to$ `onStartCommand()` $\to$ (Running) $\to$ `onDestroy()`
*   Runs indefinitely until `stopSelf()` or `stopService()`.
*   If killed by the system, behavior depends on the return value of `onStartCommand`:
    *   `START_STICKY`: Recreates service, but passes `null` Intent.
    *   `START_NOT_STICKY`: Does not recreate.
    *   `START_REDELIVER_INTENT`: Recreates and redelivers the exact same Intent.

### B. Bound Service (`bindService`) - Common for AIDL
`onCreate()` $\to$ `onBind()` $\to$ (Clients Connected) $\to$ `onUnbind()` $\to$ `onDestroy()`
*   Lives only as long as clients are bound to it.
*   If `App A` and `App B` bind to it, it is destroyed only when BOTH unbind.
*   If a client crashes, the system automatically cuts the binding. The service gets `onUnbind()` if no other clients remain.

---

## 5. Harman/Automotive Specific Scenarios 🚗

### Garage Mode (AAOS)
Vehicles don't just "turn off". When you turn off the ignition, the vehicle might enter "Garage Mode".
*   The display shuts off (`onPause`, `onStop`), but the system stays awake for minutes/hours to perform OTA updates, upload logs, and run scheduled jobs.
*   Apps shouldn't rely on `onStop` to mean "the car is dead." Use `WorkManager` for deferred Garage Mode tasks.

### Display Power State
In AAOS, the screen can be turned off (e.g., reverse camera comes on, or cluster dims).
*   Activity moves to `onStop()`.
*   Any background polling (e.g., VHAL updates for the dashboard) MUST be paused here to save CPU/battery, using `repeatOnLifecycle(STARTED)` or `WhileSubscribed`.

### Multi-Display (Cluster vs Center Console)
*   An app can have Activities running on two different physical displays simultaneously.
*   Each Activity gets its own lifecycle and `Configuration` (e.g., cluster is non-touch, center console is touch).

---

## 6. State Saving Decision Matrix

Where do you save state when the user rotates the screen or the OS kills the app?

| State Type | Mechanism | Lifetime | Size Limit |
| :--- | :--- | :--- | :--- |
| UI State (e.g., Scroll pos) | `ViewModel` | Until Activity finishes. Survives rotation. | RAM available |
| Navigation/Input | `SavedStateHandle` / `onSaveInstanceState` | Survives process death. | Small (Bundle < 1MB) |
| Persistent Data (Drafts) | `Room` / `DataStore` | Forever (disk). | Disk available |

**Example of correct handling:**
1.  **Rotation:** ViewModel holds the `List<Car>`. UI re-renders instantly.
2.  **Process Death:** `SavedStateHandle` holds the `search_query` string. When app is relaunched, ViewModel reads `search_query` from the handle and performs the database query again.

---

## 7. Domain-Specific Lifecycle Scenarios 🏭

### A. Digital Gold & FinTech 🥇
**Q: A user is on the final "Confirm Purchase" screen for 10 grams of Digital Gold. A phone call comes in, pushing the app to `onStop()`, and then the OS kills the app due to low memory. How do you ensure the user doesn't lose their transaction state when they return?**
**A:** Use **`SavedStateHandle`**. Store the `transactionId` or transaction payload bundle in the `SavedStateHandle` within the ViewModel. When the process is recreated, `onCreate()` will reconstruct the ViewModel with the `SavedStateHandle` intact, allowing you to instantly restore the "Confirm Purchase" UI state and verify the transaction limits again without forcing the user back to the home screen.

### B. Insurance Claims 📄
**Q: An insurance inspector is using the camera to capture a 360-video of a damaged vehicle. If they rotate the device to get a better angle, the Activity is destroyed. How do you handle the camera lifecycle to avoid interrupting the recording or leaking the camera hardware?**
**A:** Standard configuration changes destroy the Activity. To prevent camera interruption:
1. Declare `android:configChanges="orientation|screenSize|screenLayout|keyboardHidden"` in the Manifest for this specific Activity.
2. The Activity will NOT be destroyed; instead, `onConfigurationChanged()` is called.
3. You manually adjust the UI layout/overlay within `onConfigurationChanged()` while the CameraX/Camera2 session remains completely uninterrupted because `onDestroy` and `onCreate` were bypassed.

### C. BLE (Bluetooth Low Energy) Devices ⌚
**Q: Your app acts as a dashboard for a BLE smart ring. The user backgrounds the app (`onStop()`). What is the correct lifecycle component to keep the BLE connection alive and where do you start it?**
**A:** You must transition the BLE management out of the Activity/ViewModel and into a **Foreground Service** (`connectedDevice` type). 
- Start the Foreground Service or bind to it during `onStart()` or `onCreate()`.
- When the Activity reaches `onStop()`, the UI unbinds or stops listening, but the Service remains alive independently, maintaining the GATT connection and caching data in a Room database until the Activity returns to `onStart()`.

### D. Telemedicine & Healthcare 🩺
**Q: A doctor is in a WebRTC video consultation. They receive a critical text message and swipe down the notification shade or briefly open a reply bubble. Does the WebRTC video pause? What lifecycle callbacks fire?**
**A:** When a system overlay or notification shade obscures the screen, or the app goes into multi-window/split-screen (but is still visible):
- **`onPause()` fires.** 
- **`onStop()` DOES NOT fire** (the Activity is still partially visible).
- **Rule:** Do NOT pause the WebRTC video or camera preview in `onPause()`. In multi-window mode, users expect the video to continue playing. Only release the camera hardware and pause the video stream when the app hits **`onStop()`** (when it becomes entirely invisible).
