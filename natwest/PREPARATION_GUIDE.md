# NatWest Senior Android Developer (6-9 Years) Preparation Guide

This guide is tailored for a Senior/Lead Android Developer role at NatWest. At this level (6-9 years experience), the interview will focus less on "how to code" and more on **"how to design, lead, and optimize"**, alongside a solid test of your problem-solving skills (DSA).

## 1. Data Structures & Algorithms (DSA) - The "Filter"
NatWest asks DSA questions to test problem-solving and optimization skills. While they don't always ask Hard problems, you must be very comfortable with **Medium** level questions and explaining your thought process.

### **Priority 1: Must-Know Patterns**
These appear most frequently in senior Android interviews.

#### **Arrays & Hashing**
1.  **Two Sum:** Given an array of integers, return indices of the two numbers such that they add up to a specific target.
2.  **Group Anagrams:** Given an array of strings, group anagrams together.
3.  **Top K Frequent Elements:** Given an integer array `nums` and an integer `k`, return the `k` most frequent elements.
4.  **Product of Array Except Self:** Given an integer array `nums`, return an array such that `answer[i]` is equal to the product of all the elements of `nums` except `nums[i]`.
5.  **Longest Consecutive Sequence:** Given an unsorted array of integers, find the length of the longest consecutive elements sequence.

#### **Two Pointers**
1.  **Valid Palindrome:** Given a string, determine if it is a palindrome, considering only alphanumeric characters and ignoring cases.
2.  **3Sum:** Given an array `nums` of n integers, are there elements a, b, c in `nums` such that a + b + c = 0? Find all unique triplets.
3.  **Container With Most Water:** Find two lines that together with the x-axis form a container, such that the container contains the most water.

#### **Sliding Window**
1.  **Best Time to Buy and Sell Stock:** You want to maximize your profit by choosing a single day to buy one stock and choosing a different day in the future to sell that stock.
2.  **Longest Substring Without Repeating Characters:** Given a string, find the length of the longest substring without repeating characters.
3.  **Longest Repeating Character Replacement:** You are given a string s and an integer k. You can choose any character of the string and change it to any other uppercase English character. You can perform this operation at most k times.
4.  **Minimum Window Substring:** Given two strings s and t of lengths m and n respectively, return the minimum window substring of s such that every character in t (including duplicates) is included in the window.

### **Priority 2: Core Data Structures**

#### **Stack**
1.  **Valid Parentheses:** Given a string containing just the characters '(', ')', '{', '}', '[' and ']', determine if the input string is valid.
2.  **Min Stack:** Design a stack that supports push, pop, top, and retrieving the minimum element in constant time.
3.  **Daily Temperatures:** Given a list of daily temperatures `T`, return a list such that, for each day in the input, tells you how many days you would have to wait until a warmer temperature.

#### **Binary Search**
1.  **Binary Search:** Given an array of integers `nums` which is sorted in ascending order, and an integer `target`, write a function to search `target` in `nums`.
2.  **Search in Rotated Sorted Array:** There is an integer array `nums` sorted in ascending order (with distinct values). Prior to being passed to your function, `nums` is possibly rotated at an unknown pivot index.
3.  **Find Minimum in Rotated Sorted Array:** Suppose an array of length n sorted in ascending order is rotated between 1 and n times. Find the minimum element.

#### **Linked List**
1.  **Reverse Linked List:** Reverse a singly linked list.
2.  **Merge Two Sorted Lists:** Merge two sorted linked lists and return it as a sorted list.
3.  **Reorder List:** You are given the head of a singly linked-list. The list can be represented as: L0 → L1 → … → Ln - 1 → Ln. Reorder the list to be on the following form: L0 → Ln → L1 → Ln - 1 → L2 → Ln - 2 → …
4.  **Remove Nth Node From End of List:** Given the head of a linked list, remove the nth node from the end of the list and return its head.
5.  **Linked List Cycle:** Given `head`, the head of a linked list, determine if the linked list has a cycle in it.

#### **Trees**
1.  **Invert Binary Tree:** Given the root of a binary tree, invert the tree, and return its root.
2.  **Maximum Depth of Binary Tree:** Given the root of a binary tree, return its maximum depth.
3.  **Same Tree:** Given the roots of two binary trees p and q, write a function to check if they are the same or not.
4.  **Subtree of Another Tree:** Given the roots of two binary trees root and subRoot, return true if there is a subtree of root with the same structure and node values of subRoot and false otherwise.
5.  **Lowest Common Ancestor of a Binary Search Tree:** Given a binary search tree (BST), find the lowest common ancestor (LCA) of two given nodes in the BST.
6.  **Binary Tree Level Order Traversal:** Given the root of a binary tree, return the level order traversal of its nodes' values. (i.e., from left to right, level by level).
7.  **Validate Binary Search Tree:** Given the root of a binary tree, determine if it is a valid binary search tree (BST).
8.  **Kth Smallest Element in a BST:** Given the root of a binary search tree, and an integer k, return the kth smallest value (1-indexed) of all the values of the nodes in the tree.

### **Priority 3: Advanced Topics**

#### **Heap / Priority Queue**
1.  **Kth Largest Element in a Stream:** Design a class to find the kth largest element in a stream. Note that it is the kth largest element in the sorted order, not the kth distinct element.
2.  **Last Stone Weight:** You are given an array of integers `stones` where `stones[i]` is the weight of the `ith` stone.
3.  **K Closest Points to Origin:** Given an array of points where `points[i] = [xi, yi]` represents a point on the X-Y plane and an integer `k`, return the `k` closest points to the origin `(0, 0)`.

#### **Graphs (BFS/DFS focus)**
1.  **Number of Islands:** Given an `m x n` 2D binary grid `grid` which represents a map of '1's (land) and '0's (water), return the number of islands.
2.  **Max Area of Island:** You are given an `m x n` binary matrix `grid`. An island is a group of `1`s (representing land) connected 4-directionally (horizontal or vertical). You may assume all four edges of the grid are surrounded by water.
3.  **Clone Graph:** Given a reference of a node in a connected undirected graph. Return a deep copy (clone) of the graph.
4.  **Rotting Oranges:** You are given an `m x n` grid where each cell can have one of three values: 0 representing an empty cell, 1 representing a fresh orange, or 2 representing a rotten orange. Every minute, any fresh orange that is 4-directionally adjacent to a rotten orange becomes rotten. Return the minimum number of minutes that must elapse until no cell has a fresh orange. If this is impossible, return -1.

#### **Dynamic Programming (1D)**
1.  **Climbing Stairs:** You are climbing a staircase. It takes `n` steps to reach the top. Each time you can either climb 1 or 2 steps. In how many distinct ways can you climb to the top?
2.  **House Robber:** You are a professional robber planning to rob houses along a street. Each house has a certain amount of money stashed, the only constraint stopping you from robbing each of them is that adjacent houses have security systems connected and it will automatically contact the police if two adjacent houses were broken into on the same night.
3.  **Coin Change:** You are given an integer array `coins` representing coins of different denominations and an integer `amount` representing a total amount of money. Return the fewest number of coins that you need to make up that amount.

> **Strategy:** Always discuss Time (Big O) and Space complexity. For a senior role, purely brute-force solutions are often considered a "fail" or "weak pass".

## 2. Advanced Kotlin & Coroutines (The "Deep Dive")
You will be grilled on internals.

### Coroutines
*   **Structured Concurrency:** Explain it like you are teaching a junior. Application in `viewModelScope` vs `GlobalScope`.
*   **Exception Handling:** What happens if a child coroutine fails? (Difference between `launch` vs `async`, and `SupervisorJob` vs `Job`).
*   **Context switching:** usage of `withContext(Dispatchers.IO)` vs `launch(Dispatchers.IO)`.
*   **Under the hood:** How `suspend` functions work (State Machine compilation).

### Flows
*   **Cold vs Hot:** Difference between `Flow` (Cold) and `StateFlow`/`SharedFlow` (Hot).
*   **State Management:**
    *   `StateFlow` (Sticky, requires initial value, replaces LiveData).
    *   `SharedFlow` (One-time events, configurable buffer/replay).
*   **Backpressure:** How Flow handles faster emitters and slower collectors (`buffer`, `conflate`, `collectLatest`).

## 3. Core Android & Architecture
*   **Architecture:** Clean Architecture + MVVM/MVI.
    *   *Question:* "How would you handle a long-running background task that needs to survive process death?" (WorkManager).
    *   *Question:* "Design a caching layer." (Repository pattern with single source of truth logic).
*   **Memory Management:**
    *   Detecting Leaks (LeakCanary, Android Profiler).
    *   Handling large Bitmaps.
    *   WeakReference use cases.
*   **Dependency Injection:** Hilt/Dagger annotations (`@Singleton`, `@Scoped`, `@Provides` vs `@Binds`).

## 4. Jetpack Compose (Modern UI)
*   **State Management:** Hoisting state, CompositionLocal, SideEffects (`LaunchedEffect`, `DisposableEffect`, `derivedStateOf`).
*   **Performance:** Recomposition scoping, `remember`, `stable` vs `unstable` types.
*   **Interoperability:** Using Compose in legacy Views and vice-versa (AndroidView).
*   **Navigation:** Navigation Compose vs Fragment Navigation.

## 5. Testing & Quality
*   **Pyramid:** Unit (JUnit, Mockk), Integration, UI (Espresso/Compose Test Rules).
*   **Structure:** Testing ViewModels, Repositories (fake sources), and UseCases.
*   **CI/CD:** Jenkins, GitHub Actions basics. Static Analysis (Detekt, Ktlint, SonarQube).

## 6. Android Core & Lifecycle
*   **Process Death:** Saving and restoring state (`SavedStateHandle`). System-initiated process death vs user killing the app.
*   **Background Work:**
    *   **WorkManager:** Constraints, chaining requests, backoff criteria.
    *   **Foreground Services:** When to use them (e.g., navigation, music).
*   **Context:** Application vs Activity context. Preventing leaks.

## 7. System Design (Mobile)
*   **Scenario:** "Design a Banking App Dashboard" or "Design a Secure Payment Flow".
*   **Key Considerations:**
    *   **Offline First:** Room DB + Sync logic.
    *   **Security (Mandatory for Banking):**
        *   **SSL Pinning:** Prevent Man-in-the-Middle attacks.
        *   **Root Detection:** Risks and mitigation.
        *   **Data Storage:** EncryptedSharedPreferences (never plain text).
        *   **Biometrics:** Implementation details.

## 8. Banking Specifics (Bonus Points)
*   **Accessibility (a11y):** TalkBack, dynamic text sizing, color contrast. Crucial for banking regulations.
*   **PSD2 / Open Banking:** High-level understanding of APIs in banking.
*   **Compliance:** GDPR, handling PII (Personally Identifiable Information).

## 9. Deep Dive: Kotlin Internals (Senior/Lead Level)
*   **`suspend` under the hood:** Explain **Continuation Passing Style (CPS)**. How the compiler generates a `Continuation` interface and a State Machine (switch-case) to manage suspension points and local variables on the heap (not stack).
*   **`inline` functions:** Why do we use them with High-Order functions? (To prevent object creation for lambdas). What is `noinline` and `crossinline`?
*   **Delegation:** How `by` works. `by lazy` (synchronized by default) vs `lateinit`.
*   **Generics:** `reified` type parameters (preserving type at runtime in inline functions). `in` (Consumer/Contravariance) and `out` (Producer/Covariance) - PECS rule.
*   **Memory Model:**
    *   **Stack vs Heap:** Primitives on stack, Objects on heap.
    *   **Closure:** How lambdas capture variables (creating an internal class to hold the reference).

## 10. Deep Dive: Coroutines & Flow Masterclass
*   **Job Hierarchy:**
    *   **SupervisorJob vs Job:** Crucial for error handling. If a child fails in a `Job`, parent dies. In `SupervisorJob`, parent ignores child failure.
    *   **CoroutineScope:** Difference between creating a scope `CoroutineScope(Job() + Dispatchers.IO)` and using `MainScope()`.
*   **Dispatchers:**
    *   `Default`: CPU intensive (threads = number of cores).
    *   `IO`: I/O operations (64 threads or more).
    *   `Unconfined`: Starts in current thread, resumes in whatever thread the suspending function finished.
*   **Flow Internals:**
    *   **Cold Stream:** Code inside `flow { ... }` runs *only* when gathered (`collect`).
    *   **Backpressure:**
        *   `buffer()`: Runs collector in a separate coroutine.
        *   `conflate()`: Drops intermediate values, keeps latest.
        *   `collectLatest()`: Cancels previous collection block if new value arrives.
    *   **Exception Transparency:** Why `catch` must be downstream. 

## 11. Deep Dive: Background Work Strategy
*   **The Decision Tree:**
    *   **Immediate & User Perceptible:** **Foreground Service** (Media, Calls, Navigation). Must show Notification. *Android 14 restricted heavily.*
    *   **Deferrable & Guaranteed:** **WorkManager** (Sync, Uploads). Survives app kill & device restart.
    *   **Exact Timing:** **AlarmManager** (Calendar events). Expensive on battery.
*   **WorkManager Internals:**
    *   Uses `JobScheduler` (API 23+) or `AlarmManager` + `BroadcastReceiver` (older) automatically.
    *   **Constraints:** Charging, Wi-Fi, Storage not low.
    *   **Chaining:** `.beginWith(A).then(B).enqueue()`. passing output data from A to B (`Data.Builder`).
    *   **UniqueWork:** `KEEP`, `REPLACE`, `APPEND`.

## 12. Behavioral & Leadership (STAR Method)
NatWest values: **Inclusive, Curious, Robust, Sustainable, Ambitious**.

*   *Leadership:* "Tell me about a time you mentored a junior developer who was struggling."
*   *Conflict:* "Describe a time you disagreed with a Product Owner or Designer. How did you resolve it?"
*   *Failure:* "Tell me about a time you broke production or made a bad architectural decision. What did you learn?"
*   *Innovation:* "Describe a time you introduced a new technology (e.g., Compose, KMP) to your team. How did you get buy-in?"

## 13. Comprehensive Question Bank

### Technical
1.  Difference between `launch` and `async`. Returns `Job` vs `Deferred`.
2.  What is the look-up time complexity of a HashMap? (O(1) avg, O(n) worst). How does collision resolution work?
3.  Explain `derivedStateOf` in Jetpack Compose. When should you use it?
4.  How does `Recomposition` work? How do you optimize it? (Skipping with `@Stable`, `remember`).
5.  Difference between `val` and `const val`.
6.  How does Garbage Collection work in Android (Generational GC)?

### Scenario Based
1.  App is crashing on production only for specific devices. How do you debug? (Crashlytics, breadcrumbs, device-specific logs).
2.  API is slow. How do you handle it in UI? (Shimmer, Pagination, Caching).
3.  You have a flaky test. What do you do?

### Suggested Mock Interview Prep
1.  **Architecture:** Draw a diagram of a feature (e.g., "Transfer Money") showing UI -> ViewModel -> UseCase -> Repository -> Remote/Local Data Source.
2.  **Security:** Explain how you would store a user's session token securely. (Answer: EncryptedSharedPreferences/Keystore, not plain prefs).
3.  **Concurrency:** Explain a race condition you faced and how you fixed it with Mutex or Actors.
