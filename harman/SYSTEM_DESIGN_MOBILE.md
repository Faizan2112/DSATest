# System Design: Connected Car Infotainment Platform 🚗

> **Scenario:** "Design an Android Automotive Infotainment System Dashboard that shows vehicle data (speed, fuel, HVAC), supports navigation, media, and phone — with multi-display support."
> **Constraint:** Performance, automotive safety compliance, and multi-process isolation are paramount (Harman/AAOS context).

---

## 1. Requirements Analysis (The "What")

### A. Functional Requirements
1.  **Vehicle Dashboard:** Real-time speed, RPM, fuel level, engine temperature.
2.  **HVAC Control:** Set temperature, fan speed, seat heating.
3.  **Navigation:** Turn-by-turn with map rendering.
4.  **Media Player:** Audio (Bluetooth, USB, Streaming), controls.
5.  **Phone Integration:** Calls, contacts, messages (via Bluetooth HFP).
6.  **Multi-Display:** Center console + Instrument cluster + Rear-seat entertainment.
7.  **OTA Updates:** Background download and update scheduling.

### B. Non-Functional Requirements
1.  **Performance:** Boot to interactive < 5 seconds. 60fps UI rendering.
2.  **Reliability:** Crash isolation — media crash shouldn't kill navigation.
3.  **Safety:** Driver distraction compliance (limited interactions while driving).
4.  **Security:** Secure boot, encrypted storage, SELinux policies.
5.  **Memory:** Operate within 1-4GB RAM (shared with entire OS).

---

## 2. High-Level Architecture

### A. AAOS Architecture Overview
```text
┌──────────────────────────────────────────────────────────────┐
│                    APPLICATION LAYER                         │
│  ┌───────────┐ ┌───────────┐ ┌───────────┐ ┌─────────────┐ │
│  │ Dashboard │ │ Navigation│ │   Media   │ │    Phone    │ │
│  └─────┬─────┘ └─────┬─────┘ └─────┬─────┘ └──────┬──────┘ │
│        │              │              │              │        │
├────────┼──────────────┼──────────────┼──────────────┼────────┤
│        ▼              ▼              ▼              ▼        │
│                   FRAMEWORK LAYER                            │
│  ┌──────────────────────────────────────────────────────┐   │
│  │                   CarService                         │   │
│  │  ┌─────────────┐ ┌────────────┐ ┌────────────────┐  │   │
│  │  │CarHvacManager│ │CarAudioMgr │ │CarPropertyMgr  │  │   │
│  │  └──────┬──────┘ └─────┬──────┘ └───────┬────────┘  │   │
│  └─────────┼──────────────┼────────────────┼────────────┘   │
│            │              │                │                 │
├────────────┼──────────────┼────────────────┼─────────────────┤
│            ▼              ▼                ▼                 │
│                      HAL LAYER                               │
│  ┌──────────────────────────────────────────────────────┐   │
│  │              Vehicle HAL (VHAL)                       │   │
│  │         (AIDL/HIDL Interface)                        │   │
│  └──────────────────────┬───────────────────────────────┘   │
│                         │                                    │
├─────────────────────────┼────────────────────────────────────┤
│                         ▼                                    │
│                   HARDWARE LAYER                             │
│  ┌──────────┐ ┌──────────┐ ┌──────────┐ ┌────────────────┐ │
│  │ CAN Bus  │ │ GPS/GNSS │ │ Bluetooth│ │ Display/Audio  │ │
│  └──────────┘ └──────────┘ └──────────┘ └────────────────┘ │
└──────────────────────────────────────────────────────────────┘
```

### B. Modularization Strategy
```text
[:app] (DI Root, System UI Shell)
  │
  ├── [:feature:dashboard]     (Speed, RPM, Fuel, Temp widgets)
  ├── [:feature:hvac]          (Climate control UI)
  ├── [:feature:navigation]    (Map, Turn-by-turn)
  ├── [:feature:media]         (Audio player, source selection)
  ├── [:feature:phone]         (Dialer, contacts, call UI)
  │
  ├── [:core:vehicle-api]      (AIDL interfaces, CarService wrappers)
  ├── [:core:network]          (OTA, cloud services)
  ├── [:core:database]         (Room, offline POI cache)
  ├── [:core:designsystem]     (OEM-branded theme, components)
  └── [:core:common]           (Utilities, extensions)
```

### C. Data Flow: Vehicle Speed → UI
```text
[CAN Bus] → [ECU] → [VHAL (HAL)] → [CarPropertyManager] → [Repository (Flow)] → [ViewModel (StateFlow)] → [Compose UI]
```

---

## 3. Multi-Process Architecture (Crash Isolation) 🛡️

**Why?** If the media player crashes, navigation MUST keep running (safety-critical).

### Process Isolation Strategy
```text
Process 1: System UI Shell (Launcher, Status Bar)
Process 2: Navigation (android:process=":nav")
Process 3: Media (android:process=":media")
Process 4: Phone (android:process=":phone")
```

### Inter-Process Communication via AIDL
```aidl
// IMediaService.aidl — exposed by Media process
interface IMediaService {
    MediaInfo getCurrentTrack();
    void play();
    void pause();
    oneway void registerCallback(IMediaCallback callback);
}
```

```text
[Dashboard (Process 1)] ──(AIDL/Binder)──> [Media (Process 3)]
         │                                          │
         │ "What song is playing?"                 │
         │◀── MediaInfo { title, artist, art } ────│
```

---

## 4. Vehicle HAL Integration (Harman Core) 🔧

### Reading Vehicle Properties
```kotlin
class VehicleRepository(private val carPropertyManager: CarPropertyManager) {

    fun getSpeedFlow(): Flow<Float> = callbackFlow {
        val callback = object : CarPropertyManager.CarPropertyEventCallback {
            override fun onChangeEvent(event: CarPropertyValue<*>) {
                trySend(event.value as Float)
            }
            override fun onErrorEvent(propId: Int, zone: Int) {
                close(VhalException("Error: prop=$propId zone=$zone"))
            }
        }
        carPropertyManager.registerCallback(
            callback,
            VehiclePropertyIds.PERF_VEHICLE_SPEED,
            CarPropertyManager.SENSOR_RATE_NORMAL
        )
        awaitClose { carPropertyManager.unregisterCallback(callback) }
    }

    fun setHvacTemperature(zone: Int, tempC: Float) {
        carPropertyManager.setFloatProperty(
            VehiclePropertyIds.HVAC_TEMPERATURE_SET,
            zone,
            tempC
        )
    }
}
```

### VHAL Property Examples
| Property | ID | Type | Description |
| :--- | :--- | :--- | :--- |
| `PERF_VEHICLE_SPEED` | 0x11600207 | Float | Vehicle speed (m/s) |
| `ENGINE_RPM` | 0x11600305 | Float | Engine RPM |
| `FUEL_LEVEL` | 0x11600307 | Float | Fuel level (mL) |
| `HVAC_TEMPERATURE_SET` | 0x11500503 | Float | HVAC target temp (°C) |
| `GEAR_SELECTION` | 0x11400400 | Int | Current gear |

---

## 5. Performance Architecture ⚡

### A. Boot Time Optimization
**Target:** < 5 seconds to interactive dashboard.

| Technique | Description |
| :--- | :--- |
| **Lazy Module Loading** | Only load dashboard module at boot. Media/Phone load on demand. |
| **Preloaded Resources** | Precompile Compose layouts, cache vehicle data from last session. |
| **zygote Optimization** | Pre-fork app processes in Android init. |
| **Dex Optimization** | AOT compilation (profiles-guided) for critical paths. |
| **Deferred Init** | Analytics, crash reporting, OTA checks start after dashboard renders. |

### B. Rendering Performance (60fps)
```text
Frame Budget: 16.67ms (60 Hz display)
             └── Layout: < 4ms
             └── Draw:   < 4ms
             └── Compose: < 4ms
             └── Buffer:  ~4ms
```

*   **Compose:** Use `@Stable` / `@Immutable` to prevent unnecessary recomposition.
*   **`derivedStateOf`:** Avoid recomputing derived values on every frame.
*   **`remember` + keys:** Memoize expensive calculations.
*   **LazyColumn/LazyRow:** Virtualized lists for media libraries.

### C. Memory Management
*   Automotive head units: **1-4GB total** (shared with system).
*   Large bitmaps (album art, map tiles): Use Coil with downsampling.
*   Avoid `Bitmap.createBitmap()` in loops (GC pressure).
*   Monitor with `adb shell dumpsys meminfo <package>`.

---

## 6. Security Architecture 🛡️

### A. Automotive Threat Model
| Threat | Mitigation |
| :--- | :--- |
| **Malicious app via USB** | App signing enforcement, SELinux MAC |
| **CAN bus injection** | VHAL validates; gateway ECU filters |
| **Code tampering** | Verified Boot, dm-verity |
| **Data exfiltration** | Encrypted storage, permission model |
| **OTA man-in-the-middle** | TLS pinning, signed update packages |

### B. SELinux in AAOS
*   Every process runs in a **confined domain**.
*   VHAL access restricted to privileged system apps only.
*   Third-party apps: **sandboxed** with limited car API access.

### C. Secure OTA Updates
```text
[Cloud Server] ──(TLS 1.3)──> [OTA Manager Service]
                                     │
                                     ▼
                            [Verify Package Signature]
                                     │
                                     ▼
                            [A/B Partition Update]
                                     │
                                     ▼
                            [Reboot to New Partition]
```

---

## 7. Multi-Display Support 📺

### Display Architecture
```text
┌─────────────────────────────────────────────┐
│           INSTRUMENT CLUSTER (IC)           │
│   Speed │ RPM │ Navigation Turn │ Warnings  │
└─────────────────────────────────────────────┘

┌─────────────────────────────────────────────┐
│         CENTER CONSOLE (IVI)                │
│   Full Dashboard │ Media │ HVAC │ Settings  │
└─────────────────────────────────────────────┘

┌────────────────┐ ┌────────────────┐
│  REAR LEFT     │ │  REAR RIGHT    │
│  Entertainment │ │  Entertainment │
└────────────────┘ └────────────────┘
```

### Android Multi-Display API
```kotlin
// Launch activity on specific display
val options = ActivityOptions.makeBasic()
options.setLaunchDisplayId(clusterDisplayId)
startActivity(intent, options.toBundle())
```

---

## 8. Driver Distraction Compliance 🚦

### Driving State Restrictions
| State | Allowed UI | Restricted |
| :--- | :--- | :--- |
| **Parked** | Full UI, keyboard, video | None |
| **Moving** | Max 6 list items, large buttons, voice | Keyboard, video, long text, complex settings |
| **Restricted** | Essential info only (speed, navigation) | All non-essential |

### Implementation
```kotlin
val drivingState = carUxRestrictionsManager.getCurrentRestrictions()

if (drivingState.isRequiresDistractionOptimization) {
    // Simplify UI: fewer items, larger text, no animations
    showOptimizedUI()
} else {
    showFullUI()
}
```

---

## 9. Offline-First Data Layer 📡

### POI Cache Strategy
```text
[Navigation UI] <--(StateFlow)--> [ViewModel] <--(UseCase)--> [Repository]
                                                                    │
                                          ┌─────────────────────────┴──────────────┐
                                          ▼                                        ▼
                                   [Room DB (Cache)]                        [Map API (Cloud)]
                                   (Offline POIs)                           (Online search)
```

*   **SSOT:** UI reads from Room. Cloud data saved to Room first.
*   **Sync:** WorkManager periodic sync when connectivity available.

---

## 10. Interview Checklist ✅

If asked "Design an infotainment system", hit these:

1.  **"How do you ensure crash isolation?"** → Multi-process + AIDL.
2.  **"How do you read vehicle speed?"** → VHAL → CarPropertyManager → callbackFlow → StateFlow.
3.  **"How fast does it boot?"** → Lazy module loading, deferred init, AOT compilation.
4.  **"Does it support multiple screens?"** → Multi-display API, display-specific Activities.
5.  **"How do you comply with driver safety?"** → CarUxRestrictions, optimized UI modes.
6.  **"How do you handle OTA updates?"** → A/B partitions, signed packages, WorkManager download.

---

## 11. Deep Dive Q&A 🔥

### Q1: "Your navigation app renders at 30fps on the head unit. How do you debug?"
1.  **Profile:** Use GPU rendering profile (`adb shell setprop debug.hwui.profile true`).
2.  **Identify:** Look for overdraws, heavy recompositions (Compose compiler metrics).
3.  **Fix:** Reduce draw calls, use `@Stable` annotations, offload map tile processing to background thread.
4.  **Validate:** GPU profiling shows consistent < 16ms frames.

### Q2: "Media process crashes. What happens to the dashboard?"
*   Dashboard runs in a **separate process** → no crash impact.
*   Dashboard's AIDL connection to media gets `onServiceDisconnected()`.
*   Dashboard shows "Media Unavailable" and auto-reconnects via `bindService()`.

### Q3: "How do you test vehicle integration without a real car?"
1.  **VHAL Emulator:** AndroidX Car Testing library provides mock VHAL.
2.  **FakeCarPropertyManager:** Inject fake properties in unit tests.
3.  **Automotive OS Emulator:** Android Studio AVD with Automotive system image.
4.  **Hardware-in-the-Loop (HIL):** Harman's test bench with simulated CAN bus.

### Q4: "Design the data flow for displaying speed with unit conversion."
```text
[VHAL: 27.78 m/s] 
    → [Repository: callbackFlow] 
    → [UseCase: convert m/s → km/h → 100 km/h] 
    → [ViewModel: StateFlow<SpeedUiModel>] 
    → [Compose: Gauge(value=100, unit="km/h")]
```

### Q5: "How do you handle first-boot vs warm-boot of the infotainment?"
*   **Cold Boot (Power On):** Full system init. Target: < 5s to dashboard.
*   **Warm Boot (Wake from sleep/ACC On):** Resume from saved state. Target: < 1s.
*   **Strategy:** Save last-known vehicle state to disk. Show cached dashboard immediately. Refresh from VHAL in background.
