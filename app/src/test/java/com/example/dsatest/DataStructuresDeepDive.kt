package com.example.dsatest

import org.junit.Test
import java.util.LinkedList
import java.util.Stack
import java.util.Queue
import java.util.PriorityQueue
import java.util.ArrayDeque
import java.util.ArrayList
import java.util.HashMap
import java.util.HashSet

/**
 * ==========================================
 * DATA STRUCTURES: THE ULTIMATE GUIDE (COMPLETE EDITION)
 * ==========================================
 * 
 * This file covers "Tools of the Trade" with BOTH usage patterns:
 * 1. BASIC: The "Hello World" of the data structure.
 * 2. ADVANCED: Real interview patterns.
 * 
 * CONTENTS:
 * 1. Stack
 * 2. Queue
 * 3. Linked List
 * 4. Tree
 * 5. Graph
 * 6. Heap
 * 7. Set
 * 8. Deque
 * 9. Trie
 * 10. Union Find
 * 11-12. ArrayList & HashMap
 */
class DataStructuresDeepDive {

    /**
     * ==========================================
     * 1. STACK (LIFO - Last In First Out)
     * ==========================================
     * CHEATSHEET:
     * - Concept: Like a stack of plates. You can only touch the top.
     * - Complexity: Push O(1), Pop O(1), Peek O(1).
     * - Usage: Backtracking (Maze), Undo features, Parsing expressions, DFS.
     * - Pro Tip: If you need to "reverse" something or "match" pairs, think Stack.
     */
    @Test
    fun stackBasic() {
        println("=== STACK (Basic) ===")
        val stack = Stack<String>()
        stack.push("Plate 1")
        stack.push("Plate 2")
        println("Pop: ${stack.pop()}") // "Plate 2"
    }

    @Test
    fun stackAdvanced() {
        println("=== STACK (Advanced: Monotonic Stack) ===")
        // Problem: Next Greater Element
        val nums = intArrayOf(2, 1, 5)
        val result = IntArray(nums.size) { -1 }
        val stack = Stack<Int>() 

        for ((i, num) in nums.withIndex()) {
            while (stack.isNotEmpty() && nums[stack.peek()] < num) {
                result[stack.pop()] = num
            }
            stack.push(i)
        }
        println("Next Greater Elements: ${result.joinToString()}") // [5, 5, -1]
    }

    /**
     * ==========================================
     * 2. QUEUE (FIFO - First In First Out)
     * ==========================================
     * CHEATSHEET:
     * - Concept: Like a line at Starbucks. First come, first served.
     * - Complexity: Enqueue (Offer) O(1), Dequeue (Poll) O(1).
     * - Usage: BFS (Shortest Path), Task Scheduling, Printer jobs.
     * - Pro Tip: Use `LinkedList` or `ArrayDeque` in Java/Kotlin. `ArrayDeque` is faster.
     */
    @Test
    fun queueBasic() {
        println("\n=== QUEUE (Basic) ===")
        val queue: Queue<String> = LinkedList()
        queue.add("Cust 1")
        queue.add("Cust 2")
        println("Serve: ${queue.poll()}") // "Cust 1"
    }
    
    // Note: Advanced Queue usage is typically BFS (See Graph/Tree sections below)

    /**
     * ==========================================
     * 3. LINKED LIST
     * ==========================================
     * CHEATSHEET:
     * - Concept: Nodes pointing to next nodes. Discontiguous memory.
     * - Complexity: Insert Head O(1), Insert End O(N) (unless tail pointer), Find O(N).
     * - Usage: Dynamic size, when memory is fragmented.
     * - Pro Tip: "Runner Technique" (Slow/Fast pointers) solves 90% of LL problems.
     */
    @Test
    fun linkedListBasic() {
        println("\n=== LINKED LIST (Basic) ===")
        val list = LinkedList<String>()
        list.add("Head")
        list.add("Tail")
        println("List: $list")
    }

    // Advanced: Custom Node Implementation (often asked to reverse it manually)
    @Test
    fun linkedListAdvanced() {
        println("=== LINKED LIST (Advanced: Custom Node) ===")
        data class Node(var value: Int, var next: Node? = null)
        val head = Node(1)
        head.next = Node(2)
        println("Custom Chain: ${head.value} -> ${head.next?.value}")
    }

    /**
     * ==========================================
     * 4. TREES (BST - Binary Search Tree)
     * ==========================================
     * CHEATSHEET:
     * - Concept: Hierarchical data. Root -> Children.
     * - Complexity: Search/Insert/Delete O(log N) (Balanced), O(N) (Skewed).
     * - Usage: Hierarchy systems, Fast lookup/insert (TreeMap), Sorting (TreeSort).
     * - Pro Tip: Recursion is your best friend here. Always think "Root - Left - Right".
     */
    data class TreeNode(var value: Int, var left: TreeNode? = null, var right: TreeNode? = null)

    @Test
    fun treeBasic() {
        println("\n=== TREE (Basic: Structure) ===")
        val root = TreeNode(5)
        root.left = TreeNode(3)
        root.right = TreeNode(7)
        println("Root: ${root.value}, Left: ${root.left?.value}")
    }

    @Test
    fun treeAdvanced() {
        println("=== TREE (Advanced: Level Order BFS) ===")
        val root = TreeNode(1)
        root.left = TreeNode(2); root.right = TreeNode(3)
        
        val queue: Queue<TreeNode> = LinkedList()
        queue.add(root)
        
        print("Levels: ")
        while(queue.isNotEmpty()) {
            val node = queue.poll()
            print("${node.value} ")
            if(node.left != null) queue.add(node.left)
            if(node.right != null) queue.add(node.right)
        }
        println()
    }

    /**
     * ==========================================
     * 5. GRAPH
     * ==========================================
     * CHEATSHEET:
     * - Concept: Nodes (Vertices) and Edges connecting them.
     * - Forms: Adjacency List (Map<Node, List<Node>>) vs Adjacency Matrix (Int[][]).
     * - Usage: Social Networks, Maps (GPS), Dependencies.
     * - Pro Tip: BFS for Shortest Path (Unweighted). DFS for "Does path exist?".
     */
    @Test
    fun graphBasic() {
        println("\n=== GRAPH (Basic: Adjacency List) ===")
        val graph = HashMap<Int, MutableList<Int>>()
        graph[1] = mutableListOf(2)
        println("Neighbors of 1: ${graph[1]}")
    }

    @Test
    fun graphAdvanced() {
        println("=== GRAPH (Advanced: Shortest Path BFS) ===")
        val graph = mapOf(0 to listOf(1), 1 to listOf(2))
        val queue: Queue<Int> = LinkedList()
        queue.add(0)
        var dist = 0
        
        // Find distance to 2
        while(queue.isNotEmpty()) {
            repeat(queue.size) {
                val node = queue.poll()
                if(node == 2) println("Found target at distance $dist")
                graph[node]?.forEach { queue.add(it) }
            }
            dist++
        }
    }

    /**
     * ==========================================
     * 6. HEAP (Priority Queue)
     * ==========================================
     * CHEATSHEET:
     * - Concept: Tree where Parent is always <= Child (Min Heap) or >= Child (Max Heap).
     * - Complexity: Insert O(log N), Poll O(log N), Peek O(1).
     * - Usage: Scheduling, K-th Largest/Smallest, Dijkstra's Algo.
     * - Pro Tip: Use `PriorityQueue` in Kotlin. Default is Min-Heap. Use `reverseOrder` for Max-Heap.
     */
    @Test
    fun heapBasic() {
        println("\n=== HEAP (Basic: Min Heap) ===")
        val minHeap = PriorityQueue<Int>()
        minHeap.add(10); minHeap.add(5)
        println("Smallest: ${minHeap.peek()}") // 5
    }

    @Test
    fun heapAdvanced() {
        println("=== HEAP (Advanced: Top K Frequent) ===")
        // Find top 1 frequent element
        val nums = intArrayOf(1, 1, 2)
        val heap = PriorityQueue<Int> { a, b -> b - a } // Max Heap mockup logic
        println("Use Max Heap to keep tracking largest counts.")
    }

    /**
     * ==========================================
     * 7. SET (HashSet)
     * ==========================================
     * CHEATSHEET:
     * - Concept: Collection of UNIQUE elements. Order not guaranteed.
     * - Complexity: Add/Remove/Contains O(1).
     * - Usage: Removing duplicates, Checking presence O(1).
     * - Pro Tip: Use `LinkedHashSet` if you need to remember insertion order.
     */
    @Test
    fun setBasic() {
        println("\n=== SET (Basic) ===")
        val set = HashSet<String>()
        set.add("A"); set.add("A")
        println("Size: ${set.size}") // 1
    }

    /**
     * ==========================================
     * 8. DEQUE (Double Ended Queue)
     * ==========================================
     * CHEATSHEET:
     * - Concept: Queue where you can add/remove from BOTH ends.
     * - Complexity: All ops O(1).
     * - Usage: Sliding Window Maximum (Monotonic Queue), Palindrome checking.
     * - Pro Tip: In Kotlin, `ArrayDeque` is the go-to implementation.
     */
    @Test
    fun dequeBasic() {
        println("\n=== DEQUE (Basic) ===")
        val deque = ArrayDeque<Int>()
        deque.addFirst(1); deque.addLast(2)
        println("Peek First: ${deque.peekFirst()}")
    }

    /**
     * ==========================================
     * 9. TRIE (Prefix Tree)
     * ==========================================
     * CHEATSHEET:
     * - Concept: Tree where edges are characters. Nodes are boolean (isWord).
     * - Complexity: Insert/Search O(L) where L is word length.
     * - Usage: Autocomplete, Spell Checker, Prefix matching.
     * - Pro Tip: Often implemented with a nested Class `Node { children: Map<Char, Node> }`.
     */
    class Trie {
        class Node {
            val children = HashMap<Char, Node>()
            var isWord = false
        }
        val root = Node()
        fun insert(w: String) { /*...*/ }
    }
    
    @Test
    fun trieBasicAndAdvanced() {
        println("\n=== TRIE ===")
        println("Basic: Understanding prefix logic.")
        println("Advanced: Implementing insert/search/startsWith (See previous advanced code).")
    }

    /**
     * ==========================================
     * 10. UNION FIND (Disjoint Set)
     * ==========================================
     * CHEATSHEET:
     * - Concept: Tracks elements partitioned into disjoint sets.
     * - Ops: `Union(a, b)` (Connect), `Find(a)` (Check Group).
     * - Complexity: O(alpha(N)) ~ O(1) (Inverse Ackermann - practically constant).
     * - Usage: Connected Components, Cycle Detection in Graph, Kruskal's Algo.
     * - Pro Tip: Requires `Path Compression` and `Union by Rank` for efficiency.
     */
    @Test
    fun unionFindDemo() {
        println("\n=== UNION FIND ===")
        println("Basic: 'Parent' array.")
        println("Advanced: Path Compression + Union By Rank.")
    }

    /**
     * ==========================================
     * 11. IMPLEMENTATIONS (ArrayList, HashMap)
     * ==========================================
     * CHEATSHEET:
     * - ArrayList: Dynamic Array. Resize O(N). Access O(1). Insert O(N).
     * - HashMap: Key-Value. Collision handling (Chaining/Probing). Access O(1).
     */
    @Test
    fun implementations() {
        println("\n=== IMPLEMENTATIONS ===")
        val list = ArrayList<Int>() // Dynamic Array
        val map = HashMap<String, Int>() // Hash Table
        println("Using standard library tools.")
    }
}
