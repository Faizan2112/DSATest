# Harman International – Senior/Staff Android Developer (8+ Years) Preparation Guide

This guide is tailored for a **Senior/Staff Android Developer** role at **Harman International** (a Samsung subsidiary). At 8+ years, the interview will focus on **architecture ownership, performance at scale, inter-process communication (AIDL/HIDL), Android Automotive OS**, and deep system-level Android knowledge — far beyond basic app development.

> **About Harman:** Harman builds connected car infotainment systems (AAOS), audio solutions, and IoT platforms. Their Android work spans **embedded Android, Android Automotive OS (AAOS), custom System UI, HAL integration**, and consumer-facing apps.

---

## 1. Data Structures & Algorithms (DSA) – The "Filter"
Harman interviews include DSA rounds focused on **Medium-to-Hard** problems. At 8+ years, you must optimize and explain trade-offs fluently.

### **Priority 1: Must-Know Patterns**

#### **Arrays & Hashing**
1.  **Two Sum:** Find indices of two numbers summing to a target. (HashMap approach)
2.  **Group Anagrams:** Group strings that are anagrams. (Sorted key HashMap)
3.  **Top K Frequent Elements:** Return `k` most frequent elements. (Heap / Bucket Sort)
4.  **Product of Array Except Self:** Product of all except `nums[i]`, no division.
5.  **Longest Consecutive Sequence:** Length of longest consecutive sequence in O(n).
6.  **Subarray Sum Equals K:** Count subarrays with sum equal to `k`. (Prefix Sum + HashMap)

#### **Two Pointers**
1.  **Valid Palindrome:** Check palindrome ignoring non-alphanumeric.
2.  **3Sum:** Find all unique triplets summing to zero.
3.  **Container With Most Water:** Maximize water between two lines.
4.  **Trapping Rain Water:** Calculate trapped water given elevation map. *(Hard)*

#### **Sliding Window**
1.  **Best Time to Buy and Sell Stock:** Maximize profit with one buy-sell.
2.  **Longest Substring Without Repeating Characters:** Longest unique substring.
3.  **Minimum Window Substring:** Minimum window in `s` containing all chars of `t`. *(Hard)*
4.  **Sliding Window Maximum:** Max element in every window of size `k`. *(Hard, Deque)*

### **Priority 2: Core Data Structures**

#### **Stack**
1.  **Valid Parentheses:** Validate bracket sequences.
2.  **Min Stack:** Stack with O(1) `getMin`.
3.  **Daily Temperatures:** Days until warmer temperature. (Monotonic Stack)
4.  **Largest Rectangle in Histogram:** Max rectangle area. *(Hard)*

#### **Binary Search**
1.  **Binary Search:** Classic sorted array search.
2.  **Search in Rotated Sorted Array:** Find target in rotated array.
3.  **Find Minimum in Rotated Sorted Array:** Min element in rotated array.
4.  **Median of Two Sorted Arrays:** Find median in O(log(min(m,n))). *(Hard)*

#### **Linked List**
1.  **Reverse Linked List:** Iterative and recursive.
2.  **Merge Two Sorted Lists:** Merge into one sorted list.
3.  **Remove Nth Node From End:** Two-pointer technique.
4.  **Linked List Cycle (Detect + Entry):** Floyd's algorithm.
5.  **LRU Cache:** Design LRU cache (HashMap + Doubly Linked List). *(Very Important)*

#### **Trees**
1.  **Invert Binary Tree / Maximum Depth / Same Tree:** Basics.
2.  **Lowest Common Ancestor (BST & BT):** LCA for both BST and general BT.
3.  **Binary Tree Level Order Traversal:** BFS.
4.  **Validate Binary Search Tree:** In-order or range check.
5.  **Serialize and Deserialize Binary Tree:** Encode/decode tree to/from string. *(Hard)*
6.  **Kth Smallest Element in a BST:** In-order traversal.

### **Priority 3: Advanced Topics**

#### **Heap / Priority Queue**
1.  **Kth Largest Element in an Array:** QuickSelect or Min-Heap.
2.  **Merge K Sorted Lists:** Min-Heap approach. *(Hard)*
3.  **Find Median from Data Stream:** Two heaps (Max + Min). *(Hard)*

#### **Graphs (BFS/DFS)**
1.  **Number of Islands:** Grid DFS/BFS.
2.  **Course Schedule (I & II):** Topological Sort (cycle detection).
3.  **Clone Graph:** Deep copy undirected graph.
4.  **Word Ladder:** BFS shortest transformation. *(Hard)*

#### **Dynamic Programming**
1.  **Climbing Stairs / House Robber / Coin Change:** Classic 1D DP.
2.  **Longest Increasing Subsequence:** O(n log n) with patience sort.
3.  **Longest Common Subsequence:** 2D DP.
4.  **Edit Distance:** 2D DP (Levenshtein distance). *(Hard)*

> **Strategy:** At 8+ years, brute-force is a fail. Always lead with optimized approach, discuss Time & Space complexity, and suggest follow-ups.

---

## 2. Advanced Kotlin & Coroutines (The "Deep Dive")
At Harman's level, expect internals-focused questions.

### Coroutines
*   **Structured Concurrency:** Parent-child hierarchy, `viewModelScope` vs `GlobalScope` vs custom `CoroutineScope`.
*   **Exception Handling:** `SupervisorJob` vs `Job`, `CoroutineExceptionHandler`, `try/catch` inside `launch` vs wrapping `.await()`.
*   **Context & Dispatchers:** `withContext(Dispatchers.IO)` vs `launch(Dispatchers.IO)`. Custom dispatchers for limited parallelism.
*   **Under the Hood:** CPS (Continuation Passing Style), state machine compilation, `COROUTINE_SUSPENDED` marker.
*   **Thread Safety:** `Mutex`, `Channel`, `AtomicReference`, `Dispatchers.Default.limitedParallelism(1)`.

### Flows
*   **Cold vs Hot:** `Flow` (cold) vs `StateFlow`/`SharedFlow` (hot).
*   **Backpressure:** `buffer()`, `conflate()`, `collectLatest()`.
*   **`stateIn` & `SharingStarted`:** `WhileSubscribed(5000)` — why 5000ms?
*   **`callbackFlow`:** Bridging legacy callback APIs (sensors, location, Bluetooth).
*   **`flowOn` & Context Preservation:** Thread switching for upstream only.

---

## 3. Core Android & Architecture (Staff-Level)
*   **Architecture:** Clean Architecture + MVVM/MVI.
    *   *Question:* "Design a module that communicates with vehicle hardware (HVAC, speed sensors) via AIDL."
    *   *Question:* "How do you design a caching layer for offline-first infotainment data?"
*   **Inter-Process Communication (IPC):**
    *   **AIDL (Android Interface Definition Language):** Defining interfaces, `oneway` modifier, `Parcelable` types, thread model (Binder thread pool).
    *   **HIDL (Hardware Interface Definition Language):** HAL communication in AAOS.
    *   **Bound Services:** `bindService()` lifecycle, `ServiceConnection`, death recipient.
    *   **ContentProvider:** Cross-process data sharing, URI-based access.
    *   **Messenger vs AIDL:** Single-threaded (Handler-based) vs multi-threaded IPC.
*   **Memory Management:**
    *   Detecting leaks (LeakCanary, Android Studio Profiler).
    *   Handling large Bitmaps (downsampling, `inSampleSize`).
    *   `WeakReference` use cases. Native memory in embedded systems.
*   **Dependency Injection:** Hilt/Dagger (`@Singleton`, `@Scoped`, `@Provides` vs `@Binds`). Component hierarchies for multi-module projects.

---

## 4. Jetpack Compose (Modern UI)
*   **State Management:** State hoisting, `CompositionLocal`, Side Effects (`LaunchedEffect`, `DisposableEffect`, `derivedStateOf`, `snapshotFlow`).
*   **Performance:** Recomposition scoping, `remember`, `@Stable` vs `@Immutable`, Layout Inspector compose metrics.
*   **Custom Layouts & Drawing:** `Canvas`, custom `Layout` composable, `Modifier.drawBehind`.
*   **Interoperability:** Compose ↔ legacy Views (`AndroidView`, `ComposeView`). Critical for migrating large automotive UI codebases.
*   **Navigation:** Navigation Compose, deep links, multi-module navigation.
*   **Automotive UI:** Building for larger displays (10"+), landscape orientation, driver-distraction compliance.

---

## 5. Testing & Quality
*   **Pyramid:** Unit (JUnit5, MockK, Turbine), Integration, UI (Compose Test Rules, Espresso).
*   **Structure:** Testing ViewModels (fake repos), Repositories (fake data sources), UseCases.
*   **Coroutine Testing:** `runTest`, `StandardTestDispatcher`, `Turbine` for Flow testing.
*   **CI/CD:** Jenkins, GitHub Actions, Gerrit code review (common in Harman/automotive).
*   **Static Analysis:** Detekt, Ktlint, SonarQube, Lint rules for automotive safety.

---

## 6. Android Core & Lifecycle (Deep)
*   **Process Death:** `SavedStateHandle`, process recreation, `onSaveInstanceState`.
*   **Background Work:**
    *   **WorkManager:** Constraints, chaining, backoff, `ExistingPeriodicWorkPolicy`.
    *   **Foreground Services:** Types (Android 14+), `ServiceInfo.FOREGROUND_SERVICE_TYPE_*`.
*   **Context:** Application vs Activity context. Preventing leaks in long-lived components.
*   **Multi-Process Apps:** Apps running in different processes (`android:process`), IPC between them.

---

## 7. Android Automotive OS (AAOS) – Harman Specific 🚗

### What is AAOS?
Android Automotive OS runs **natively on vehicle hardware** (not a phone projection like Android Auto). Harman is one of the leading AAOS integrators.

### Key Concepts
*   **AAOS vs Android Auto:**
    *   AAOS = Full Android OS running on car's head unit.
    *   Android Auto = Phone screen projected to car display.
*   **Vehicle HAL (VHAL):** Abstraction layer between Android framework and vehicle hardware.
    *   Properties: `VehicleProperty.PERF_VEHICLE_SPEED`, `HVAC_TEMPERATURE_SET`, etc.
    *   Accessed via `CarPropertyManager`.
*   **CarService:** System service providing car-specific APIs.
    *   `CarHvacManager`, `CarSensorManager`, `CarAudioManager`.
    *   Permission model for car data access.
*   **Car App Library:** Templates for driving-safe apps (NavigationTemplate, ListTemplate, PaneTemplate).
*   **System UI Customization:** OEMs (like Harman's clients) customize Status Bar, Notifications, Launcher for their brand.
*   **AIDL/HIDL in AAOS:** HAL services expose AIDL interfaces; apps communicate via Binder IPC.
*   **Multi-Display Support:** Instrument cluster + center console + rear-seat displays.
*   **Driver Distraction Guidelines:** UI must comply with driver attention limits (e.g., max 6 list items, no video while driving).

---

## 8. System Design (Mobile + Automotive)
*   **Scenario:** "Design an Infotainment Dashboard" or "Design a Connected Car App with OTA updates."
*   **Key Considerations:**
    *   **Offline First:** Room/SQLite + sync when connectivity returns.
    *   **IPC Architecture:** AIDL services for hardware communication.
    *   **Multi-Module:** Feature modules for Navigation, Media, HVAC, Phone.
    *   **Security:** SELinux policies, secure boot, encrypted storage on head unit.
    *   **Performance:** Fast boot time (<5s to interactive), low memory footprint (~512MB-2GB available).

---

## 9. Deep Dive: Kotlin Internals (Staff Level)
*   **`suspend` under the hood:** CPS, `Continuation` interface, state machine with labels.
*   **`inline` functions:** Preventing lambda object allocation. `noinline`, `crossinline`, `reified`.
*   **Delegation:** `by lazy` (synchronized default), `by map`, custom delegates.
*   **Generics:** Type erasure, `reified`. `in` (Contravariance) / `out` (Covariance) — PECS.
*   **Memory Model:** Stack vs Heap. Closure capture. `@JvmField`, `@JvmStatic` implications.
*   **Sealed classes/interfaces:** Exhaustive `when`, sealed hierarchies for state machines.
*   **Value classes (`@JvmInline`):** Zero-overhead type wrappers.

---

## 10. Deep Dive: IPC & Binder (Harman Critical) 🔧

### Binder Framework
*   **What:** Android's core IPC mechanism. All system services use Binder.
*   **Why it matters at Harman:** AAOS apps communicate with vehicle hardware (HVAC, sensors) via AIDL over Binder.
*   **Thread Model:** Binder calls arrive on a **Binder thread pool** (not Main thread). You must handle synchronization.
*   **Death Notification:** `IBinder.DeathRecipient` — know when a remote service dies.
*   **Parcelable vs Serializable:** Parcelable is mandatory for IPC (efficient binary marshalling).

### AIDL Deep Dive
```aidl
// IVehicleService.aidl
interface IVehicleService {
    float getSpeed();
    void setHvacTemperature(float temp);
    oneway void registerCallback(IVehicleCallback callback);
}
```
*   **`oneway`:** Async call, doesn't block the caller.
*   **Callback pattern:** Register callbacks for real-time data (speed, RPM).
*   **Versioned AIDL:** Backward-compatible interfaces across OS versions.

---

## 11. Performance & Optimization (Embedded Focus) ⚡
*   **Boot Time Optimization:** Lazy init, deferred module loading, preloading critical paths.
*   **Memory Constraints:** Automotive head units have limited RAM (1-4GB shared with entire OS).
*   **Rendering:** 60fps on automotive displays, avoid jank in animations.
*   **APK/Bundle Size:** R8/ProGuard, resource shrinking, on-demand feature modules.
*   **Battery/Power:** Automotive doesn't have "battery" per se, but power management for accessories mode.

---

## 12. Behavioral & Leadership (STAR Method)
Harman values: **Innovation, Collaboration, Quality, Customer Focus**.

*   *Architecture:* "Tell me about a time you redesigned a legacy system to improve performance or scalability."
*   *Leadership:* "Describe a time you mentored engineers or drove adoption of a new technology (e.g., Compose, KMP, AAOS)."
*   *Conflict:* "How did you handle disagreements with cross-functional teams (Product, QA, Hardware teams)?"
*   *Failure:* "Tell me about a production incident you owned and how you resolved it."
*   *Innovation:* "Describe a technical initiative you drove that significantly improved the product."

---

## 13. Comprehensive Question Bank

### Technical
1.  Explain AIDL. How does Binder IPC work under the hood? What thread does a Binder call execute on?
2.  Difference between `launch` and `async`. What happens if `async` throws but `.await()` is never called?
3.  How does `Recomposition` work in Jetpack Compose? What makes a type `@Stable`?
4.  Design an LRU Cache. What data structures do you use? Time complexity?
5.  Explain `stateIn` with `SharingStarted.WhileSubscribed(5000)`. Why 5000ms?
6.  What is the Vehicle HAL? How does an AAOS app read vehicle speed?
7.  Difference between `val` and `const val`. Where is each stored at bytecode level?
8.  How does Garbage Collection work in Android (Generational GC, ART)?
9.  Explain sealed classes. How do they differ from enum classes? When would you use each?
10. What is `SavedStateHandle` and when should you use it over `onSaveInstanceState()`?

### Scenario Based
1.  "Your infotainment app takes 8 seconds to load. How do you optimize boot time?"
2.  "A background service crash is killing the infotainment UI. How do you isolate it?" (Separate process + AIDL)
3.  "Your AAOS app needs to read vehicle speed and display it. Walk through the full data flow from VHAL to UI."
4.  "You need to migrate a 500K LOC legacy View-based app to Compose. What is your strategy?"
5.  "Two modules need to share data but can't depend on each other. How do you solve this?"

### Suggested Mock Interview Prep
1.  **Architecture:** Draw the data flow for an AAOS infotainment app: UI → ViewModel → UseCase → Repository → AIDL Service → VHAL.
2.  **IPC:** Explain how you would design a service that exposes vehicle data to multiple client apps via AIDL.
3.  **Performance:** Profile and optimize a Compose UI rendering at 60fps on an automotive display.
4.  **System Design:** Design a multi-display infotainment system (center console + instrument cluster + rear-seat entertainment).
