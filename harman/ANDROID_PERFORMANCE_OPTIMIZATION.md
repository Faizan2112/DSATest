# Android Performance: Memory & Battery Optimization (Staff Level)

At the 8+ YOE Staff level (especially for embedded systems like Harman's Android Automotive OS), "making it work" is assumed. You must prove you can "make it work without burning the user's battery or crashing the system." Automotive head units often share 2-4GB of RAM between Navigation, HVAC, Spotify, Voice Assistants, and the Android OS itself.

This guide focuses heavily on **Trade-offs**—because performance optimization is always about trading CPU for Memory, or Memory for Battery.

---

## 1. Memory Leak Prevention (The Silent Killer)

A memory leak occurs when the Garbage Collector (GC) cannot destroy a heavy object (like an `Activity` or `Bitmap`) because a long-lived object (like a `Singleton` or a running `Thread`) still holds a strong reference to it.

### A. The Fragment ViewBinding Trap
**How it happens:** In Fragments, the View lives a shorter life than the Fragment instance itself (e.g., when added to the backstack, `onDestroyView` is called but `onDestroy` is not). If you store the `Binding` object as a class property and don't null it out, the entire View hierarchy stays in RAM.
**The Fix:** Nullify the binding in `onDestroyView()`.

| ViewBinding Trade-off |
| :--- |
| **🟢 PRO:** Guaranteed nil-saftey for UI elements.
| **🔴 CON (Complexity):** Requires manual lifecycle boilerplate (setting to null).
| **🛠️ Staff Advice:** Abstract this away using a custom Kotlin Property Delegate (e.g., `by viewBinding()`) that automatically observes the Fragment's `viewLifecycleOwner` and sets itself to null on `ON_DESTROY`.

### B. Untamed Coroutines & Callbacks
**How it happens:** Launching a massive data sync operation using `GlobalScope.launch` or holding onto a `HardwareCallback` listener without unregistering. Even after the user leaves the screen, the operation continues, holding references to UI components.
**The Fix:** Use `lifecycleScope`, `viewModelScope`, and structure your VHAL/Hardware callbacks using `callbackFlow { ... awaitClose { unregister() } }`.

### C. Static Contexts (The Classic)
**How it happens:** Passing an `Activity` context into a Singleton manager: `BluetoothManager.init(activityContext)`. The Singleton lives forever, so the Activity lives forever.
**The Fix:** Always pass `context.applicationContext` into Singletons.

---

## 2. Memory Footprint Optimization (Doing More with Less)

Even if you have zero leaks, your app might just consume too much RAM during normal operation. 

### A. Bitmap Management (The Heaviest Objects)
A single 4K image loaded into an `ImageView` uncompressed takes 100+ MB of RAM. The app will immediately `OutOfMemoryError` (OOM).

*   **Downsampling (`inSampleSize`):** Never load a 2000x2000 image into a 200x200 box. Image loaders (Glide/Coil) downsample automatically, but if doing it manually using `BitmapFactory.Options`, you calculate the ratio and downscale before decoding into RAM.
*   **Color Configurations (ARGB_8888 vs RGB_565):** 
    *   `ARGB_8888`: 4 bytes per pixel (Red, Green, Blue, Alpha).
    *   `RGB_565`: 2 bytes per pixel (No transparency). Great for opaque album art.

| Bitmap Strategy Trade-off |
| :--- |
| **🟢 PRO (RGB_565):** Cuts RAM consumption by exactly 50%.
| **🔴 CON (RGB_565):** You lose the alpha channel (transparency). Color banding becomes visible on smooth gradients (like the sky in a map app).
| **🛠️ Staff Advice (Coil Integration):** For Automotive Media Players, configure Coil to load Album Art as `Bitmap.Config.RGB_565` because album covers are guaranteed to be opaque squares.

### B. Allocations in Tight Loops (`onDraw`)
**The Problem:** If you build a custom Speedometer Compose Canvas or classic `View.onDraw()`, these methods run 60 times a second. If you instantiate `Paint()` or `Rect()` objects *inside* the `onDraw` loop, you create 60 objects per second. The GC has to run constantly to clean them up, which steals CPU time and causes "Jank" (dropped frames).
**The Fix:** Instantiate `Paint` objects at the class level and reuse them. 

### C. Data Structures: `ArrayMap` vs `HashMap`
Standard Java `HashMap` creates a generic `Map.Entry` object for every single key-value pair you insert. This is incredibly heavy on memory overhead.
Android introduced `ArrayMap` and `SparseArray` (for Int keys).

| ArrayMap Trade-off |
| :--- |
| **🟢 PRO:** Drastically lowers memory footprint (keeps two tiny arrays under the hood, no wrapper objects).
| **🔴 CON (CPU Time):** Lookups are `O(log N)` (via binary search) instead of a HashMap's `O(1)`.
| **🛠️ Staff Advice:** If you have < 1000 items (e.g., a list of HVAC zones or Vehicle Doors), use `ArrayMap`. The memory savings are huge, and the CPU cost of binary searching 1000 items is literally imperceptible. If you have 100,000 items (a giant contacts list), stick to `HashMap`.

---

## 3. Battery Power Optimization (Radio & WakeLocks)

The two biggest battery drains are the **Cellular/WiFi Radio** and the **Screen/GPU**.

### A. Network Batching
When you make an HTTP request, the Cellular Radio wakes up, stays at high power for ~5 seconds, then drops to a standby tail for another ~10 seconds.
**The Problem:** Making a 1KB request every 15 seconds keeps the radio powered on at 100% permanently.
**The Fix:** Batch your Analytics/Telemetry. Save telemetry locally to Room, then upload a single 50KB payload every 15 minutes using WorkManager. 

### B. Location Services (FusedLocationProvider)
Never request `PRIORITY_HIGH_ACCURACY` (GPS Chip) unless you are an actively running Turn-By-Turn navigation app.

| Location Strategy Trade-off |
| :--- |
| **🟢 PRO (High Accuracy):** Pinpoint exactness (under an overpass).
| **🔴 CON (High Accuracy):** Destroys battery. Heats up the device dramatically. 
| **🛠️ Staff Advice:** If you just need to know if the car is "in the home city" to update local weather, use `PRIORITY_BALANCED_POWER_ACCURACY` (Cell tower / Wi-Fi triangulation) and `setInterval(1 hour)`.

### C. WakeLocks (The Danger Zone)
A WakeLock prevents the CPU from going into deep sleep.
**The Fix:** Almost never use a raw `PowerManager.WakeLock`. If your process dies without calling `.release()`, the battery will drain to 0% in hours. Use **WorkManager**; it manages the WakeLock for you automatically during the `doWork()` function and releases it safely.

---

## 4. AAOS & Harman Specifics (Automotive Constraints)

Vehicles aren't smartphones. They operate in extreme temperatures and have bizarre power cycles.

### A. Thermal Throttling
A car parked in the Dubai sun can hit 140°F (60°C) inside the cabin. When the user turns on the head unit, the CPU starts at a massive thermal disadvantage.
If your app pushes 60fps animations or heavy AI processing, the Android OS will initiate **Thermal Throttling**:
1. OS drops screen refresh rates from 60fps to 30fps.
2. CPU cores are turned off.
3. Your app will feel incredibly laggy.
**Optimization:** Listen to the `PowerManager.onThermalStatusChanged` listener. If the status hits `THERMAL_STATUS_SEVERE`, gracefully turn off background blurs, drop animation frame rates, and disable heavy background syncing.

### B. Garage Mode
When the driver turns off the ignition, the display shuts down to give the illusion the car is "off". However, modern cars enter **Garage Mode**. The CPU stays awake for a limited window (e.g., 15 minutes) to download OTA updates, upload crash logs, and sync user profiles.
**Optimization:** Never run heavy data syncs while the user is actively driving (competing for CPU with Navigation/Music). Schedule your `WorkManager` jobs with restrictions. When the car enters Garage Mode, Android executes all deferred WorkManager jobs in a batched, power-efficient burst before physically cutting battery power to the ECU.

---

## 5. Staff-Level Interview Summary 📝

If the interviewer asks: **"How do you profile and optimize an app's performance?"**

1.  **Memory Leaks:** "I integrate **LeakCanary** in debug builds to catch lingering Fragment ViewBindings and forgotten BroadcastReceivers. I enforce strict unidirectional data flow using `viewModelScope` to ensure coroutines die with the UI."
2.  **Memory Footprint:** "I trade CPU for RAM where appropriate. If I have a dictionary of 500 vehicle sensor mappings, I use `ArrayMap` instead of `HashMap`. For heavy media (album art), I configure Coil to decode using `RGB_565` to instantly cut RAM consumption by 50% because transparency isn't needed."
3.  **Battery & Network:** "I avoid keeping the cellular radio active by batching telemetry logs into Room, and syncing them via heavily constrained **WorkManager** jobs (e.g., waiting for unmetered Wi-Fi or when the vehicle enters Garage Mode)."
4.  **UI Jank (Dropped Frames):** "I use the **Android Studio Profiler (Systrace)** to identify garbage collection pauses. I strictly avoid instantiating heavy objects, like `Paint` or `Path`, inside tight loops like `onDraw` or Compose Canvas phases, moving those allocations to pre-computed class properties."

---

## 6. Domain-Specific Performance Scenarios 🏭

### A. Digital Gold & FinTech 🥇
**Q: A live-trading dashboard displays updating gold prices and the user's portfolio graph (canvas). It stutters heavily when scrolling. How do you optimize it?**
**A:** Live graphs are often redrawn 60 times a second. 
1. **Never allocate objects in `onDraw()`** (or Compose `Canvas { }` phases). Pre-allocate `Paint`, `Path`, and color objects at the class/state level. 
2. Use a **`StateFlow` with `conflate()`** for the live price ticker to drop intermediate websocket ticks that arrive faster than the UI can draw them.
3. Use **Recycler View / LazyColumn** efficiently by implementing `DiffUtil` or stable Compose keys to prevent rebinding entire lists when only one asset price changes.

### B. Insurance Claims 📄
**Q: An inspector takes 30 high-resolution photos of a crash. When they click "Submit", the app OOMs (Out Of Memory) instantly.**
**A:** A single 12MP camera image takes ~48MB of raw RGB RAM. Putting 30 in memory simultaneously guarantees an OOM.
1. **Never load full images into memory simultaneously.** Process pictures sequentially using WorkManager or Coroutines.
2. Calculate `inSampleSize` to aggressively downsample the raw byte array into a smaller Bitmap *before* decoding. 
3. Instantly write the compressed JPEG/WebP to a local `File` on the disk, and let the GC clear the Bitmap before loading the next photo.

### C. BLE (Bluetooth Low Energy) Devices ⌚
**Q: A health app maintains a continuous BLE connection to a smart ring, but the user complains the app drains 20% of their phone battery overnight. Why, and how do you fix it?**
**A:** The BLE radio is likely being kept active unnecessarily or waking the CPU constantly.
1. Avoid continuous 100Hz polling if it's not needed. Switch the BLE characteristics to **Notifications/Indications** instead of constant Reads.
2. If raw data must be logged overnight, batch the data on the BLE device itself (if hardware supports it) and sync once an hour.
3. Avoid CPU WakeLocks. If the ring streams data, process it, dump it to `Room`, and let the CPU sleep. 

### D. Telemedicine & Healthcare 🩺
**Q: A telemedicine app experiences severe audio/video latency and thermal throttling heavily during a 45-minute WebRTC call on a mid-range device.**
**A:** Video encoding/decoding is incredibly CPU intensive.
1. Use **Hardware Acceleration**. Ensure WebRTC is strictly configured to use Hardware Video Codecs (like `HardwareVideoEncoderFactory` in Android) rather than falling back to software codecs (which burn CPU and battery).
2. Monitor `PowerManager.onThermalStatusChanged`. If the device hits `THERMAL_STATUS_SEVERE`, automatically gracefully degrade the target video bitrate, drop the framerate from 30fps to 15fps, or turn off the user's camera to cool the device and save the call.
