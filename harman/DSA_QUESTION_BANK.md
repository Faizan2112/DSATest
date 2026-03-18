# Harman Senior/Staff Android Developer – DSA Question Bank (8+ Years)

This document serves as a comprehensive DSA question bank for the Senior/Staff Android Developer role at Harman International (8+ years experience).

At this experience level, **brute-force is a fail**. You must lead with optimized approaches, discuss trade-offs, and handle follow-up questions on complexity and edge cases.

---

## 1. Arrays & Hashing
**Focus:** Efficiency, HashMaps, Prefix Sums, Frequency Counting.

| Problem | Difficulty | Description | Key Technique |
| :--- | :--- | :--- | :--- |
| **Two Sum** | Easy | Find indices of two numbers that add up to a target. | HashMap |
| **Group Anagrams** | Medium | Group strings that are anagrams of each other. | Sorted Key HashMap |
| **Top K Frequent Elements** | Medium | Return the `k` most frequent elements. | Heap / Bucket Sort |
| **Product of Array Except Self** | Medium | Product of all elements except `nums[i]`, no division. | Prefix/Suffix Products |
| **Longest Consecutive Sequence** | Medium | Longest consecutive sequence in O(n). | HashSet |
| **Subarray Sum Equals K** | Medium | Count subarrays with sum equal to `k`. | Prefix Sum + HashMap |
| **First Missing Positive** | Hard | Find smallest missing positive integer in O(n). | Index Mapping (Cyclic Sort) |

---

## 2. Two Pointers
**Focus:** Linear structures, optimization, sorted arrays.

| Problem | Difficulty | Description | Key Technique |
| :--- | :--- | :--- | :--- |
| **Valid Palindrome** | Easy | Check if string is palindrome (alphanumeric only). | Two Pointers |
| **3Sum** | Medium | Find all unique triplets summing to zero. | Sort + Two Pointers |
| **Container With Most Water** | Medium | Find two lines forming container with most water. | Greedy Two Pointers |
| **Trapping Rain Water** | Hard | Calculate trapped water given elevation map. | Two Pointers / Stack |

---

## 3. Sliding Window
**Focus:** Substrings, Subarrays, Optimization.

| Problem | Difficulty | Description | Key Technique |
| :--- | :--- | :--- | :--- |
| **Best Time to Buy and Sell Stock** | Easy | Maximize profit with one buy-sell. | Kadane's Variant |
| **Longest Substring Without Repeating Chars** | Medium | Length of longest unique substring. | HashSet + Window |
| **Longest Repeating Character Replacement** | Medium | Longest same-letter substring after `k` changes. | Window + Frequency |
| **Minimum Window Substring** | Hard | Min window in `s` containing all chars of `t`. | HashMap + Two Pointers |
| **Sliding Window Maximum** | Hard | Max in every window of size `k`. | Monotonic Deque |

---

## 4. Stack
**Focus:** LIFO, Monotonic Stacks, Validating structure.

| Problem | Difficulty | Description | Key Technique |
| :--- | :--- | :--- | :--- |
| **Valid Parentheses** | Easy | Validate bracket sequences. | Stack |
| **Min Stack** | Medium | Stack with O(1) `getMin`. | Auxiliary Stack |
| **Daily Temperatures** | Medium | Days to wait for warmer temperature. | Monotonic Stack |
| **Largest Rectangle in Histogram** | Hard | Max rectangle area in histogram. | Monotonic Stack |
| **Basic Calculator II** | Medium | Evaluate `"3+2*2"`. | Stack + Precedence |

---

## 5. Binary Search
**Focus:** O(log n), Sorted/Rotated Arrays, Search Space Reduction.

| Problem | Difficulty | Description | Key Technique |
| :--- | :--- | :--- | :--- |
| **Binary Search** | Easy | Search target in sorted array. | Binary Search |
| **Search in Rotated Sorted Array** | Medium | Find target in rotated array. | Modified Binary Search |
| **Find Minimum in Rotated Sorted Array** | Medium | Min element in rotated array. | Binary Search |
| **Median of Two Sorted Arrays** | Hard | Median in O(log(min(m,n))). | Binary Search on Partition |
| **Search a 2D Matrix** | Medium | Search in row-sorted, col-sorted matrix. | Binary Search |

---

## 6. Linked List
**Focus:** Pointer manipulation, cycle detection, merge operations.

| Problem | Difficulty | Description | Key Technique |
| :--- | :--- | :--- | :--- |
| **Reverse Linked List** | Easy | Reverse singly linked list. | Iterative / Recursive |
| **Merge Two Sorted Lists** | Easy | Merge into one sorted list. | Two Pointers |
| **Remove Nth Node From End** | Medium | Remove Nth node from end. | Two-Pointer Gap |
| **Linked List Cycle (Detect + Entry)** | Medium | Detect cycle and find entry. | Floyd's Algorithm |
| **Reorder List** | Medium | L0→Ln→L1→Ln-1→... | Split + Reverse + Merge |
| **LRU Cache** | Medium | Design LRU cache. | **HashMap + Doubly Linked List** |
| **Merge K Sorted Lists** | Hard | Merge K lists into one sorted. | Min-Heap |

---

## 7. Trees & Graphs
**Focus:** Recursion, BFS/DFS, Topological Sort.

| Problem | Difficulty | Description | Key Technique |
| :--- | :--- | :--- | :--- |
| **Invert Binary Tree** | Easy | Swap left/right children. | Recursion |
| **Maximum Depth of Binary Tree** | Easy | Find height. | DFS |
| **Same Tree** | Easy | Structurally identical with same values. | Recursion |
| **Lowest Common Ancestor (BST)** | Medium | LCA in BST. | BST Property |
| **Lowest Common Ancestor (Binary Tree)** | Medium | LCA in general BT. | Post-order DFS |
| **Binary Tree Level Order Traversal** | Medium | BFS level by level. | Queue |
| **Validate Binary Search Tree** | Medium | Verify valid BST. | In-order / Range Check |
| **Kth Smallest Element in BST** | Medium | In-order traversal. | Iterative In-order |
| **Serialize/Deserialize Binary Tree** | Hard | Encode/decode tree to string. | BFS/DFS + Markers |
| **Number of Islands** | Medium | Count islands in 2D grid. | DFS/BFS |
| **Course Schedule (I & II)** | Medium | Topological sort / cycle detection. | Kahn's BFS / DFS |
| **Clone Graph** | Medium | Deep copy undirected graph. | DFS + HashMap |
| **Word Ladder** | Hard | Shortest transformation sequence. | BFS |

---

## 8. Heap / Priority Queue
**Focus:** Top-K, Streaming data, Merge operations.

| Problem | Difficulty | Description | Key Technique |
| :--- | :--- | :--- | :--- |
| **Kth Largest Element in Array** | Medium | Find Kth largest. | Min-Heap / QuickSelect |
| **K Closest Points to Origin** | Medium | K closest to (0,0). | Max-Heap |
| **Find Median from Data Stream** | Hard | Running median. | Two Heaps (Max + Min) |
| **Top K Frequent Elements** | Medium | K most frequent. | Heap / Bucket Sort |
| **Task Scheduler** | Medium | Min intervals to execute tasks with cooldown. | Greedy + Heap |

---

## 9. Dynamic Programming
**Focus:** Memoization, Tabulation, State Transitions.

| Problem | Difficulty | Description | Key Technique |
| :--- | :--- | :--- | :--- |
| **Climbing Stairs** | Easy | Distinct ways to climb `n` stairs (1 or 2 steps). | 1D DP |
| **House Robber** | Medium | Max money without robbing adjacent houses. | 1D DP |
| **Coin Change** | Medium | Fewest coins to make amount. | BFS / DP |
| **Longest Increasing Subsequence** | Medium | LIS length in O(n log n). | Patience Sort |
| **Longest Common Subsequence** | Medium | LCS of two strings. | 2D DP |
| **Edit Distance** | Hard | Min operations to convert word1 → word2. | 2D DP |
| **Word Break** | Medium | Can string be segmented into dictionary words? | DP + HashSet |
| **Decode Ways** | Medium | Number of ways to decode digit string. | 1D DP |

---

## 10. Design & Miscellaneous
**Focus:** OOP design, system-level data structures.

| Problem | Difficulty | Description | Key Technique |
| :--- | :--- | :--- | :--- |
| **LRU Cache** | Medium | O(1) get/put with eviction. | HashMap + DLL |
| **LFU Cache** | Hard | Least-frequently-used eviction. | HashMap + DLL + Freq Map |
| **Implement Trie** | Medium | Prefix tree with insert/search. | Trie Node + HashMap |
| **Design Twitter** | Medium | Post, follow, feed generation. | OOP + Heap |
| **Min Stack** | Medium | Stack with O(1) min retrieval. | Two Stacks |

---

## 11. Additional Resources in Codebase

The following directories contain generated practice problems and patterns:

- **Arrays:** [`d:\practice\DSATest\app\src\test\java\com\example\dsatest\generated\arraylist`](file:///d:/practice/DSATest/app/src/test/java/com/example/dsatest/generated/arraylist)
- **Strings:** [`d:\practice\DSATest\app\src\test\java\com\example\dsatest\generated\strings`](file:///d:/practice/DSATest/app/src/test/java/com/example/dsatest/generated/strings)
- **Linked List:** [`d:\practice\DSATest\app\src\test\java\com\example\dsatest\generated\linkedlist`](file:///d:/practice/DSATest/app/src/test/java/com/example/dsatest/generated/linkedlist)
- **Stack:** [`d:\practice\DSATest\app\src\test\java\com\example\dsatest\generated\stack`](file:///d:/practice/DSATest/app/src/test/java/com/example/dsatest/generated/stack)
- **Queue:** [`d:\practice\DSATest\app\src\test\java\com\example\dsatest\generated\queue`](file:///d:/practice/DSATest/app/src/test/java/com/example/dsatest/generated/queue)
- **Sorting & Searching:** [`d:\practice\DSATest\app\src\test\java\com\example\dsatest\generated\sorting`](file:///d:/practice/DSATest/app/src/test/java/com/example/dsatest/generated/sorting)

> **Pro Tip:** At 8+ years, always start by stating the optimal approach, then briefly mention the brute-force for comparison. Discuss Time and Space complexity for both. Handle edge cases proactively.
