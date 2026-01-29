package com.example.dsatest.generated.queue

import org.junit.Test
import java.util.LinkedList
import java.util.Queue
import java.util.ArrayDeque

/**
 * ==========================================
 * QUEUES: PATTERNS & QUESTIONS
 * ==========================================
 * 
 * Queues (FIFO) are the engine of BFS and Sliding Window.
 *
 * INTERVIEW CHEATSHEET:
 * 1. BFS (Graph/Matrix): Shortest path in unweighted graph.
 *    - Logic: Level-by-level traversal.
 * 2. Monotonic Queue: Sliding Window Maximum/Minimum.
 *    - Logic: Deque stores indices. Remove "useless" elements (smaller than current for Max). Remove "outdated" (out of window).
 * 3. Topological Sort (Kahn's): Dependencies.
 *    - Logic: Queue stores nodes with Indegree 0.
 *
 * MASTER THESE PATTERNS:
 * 1. BFS (Breadth First Search)
 * 2. Monotonic Queue
 * 3. Topological Sort (Kahn's Algo)
 * 
 * ==========================================
 * THE QUESTION BUCKET LIST
 * ==========================================
 * 
 * === EASY (20) ===
 * 1. Implement Queue using Stacks [Implemented Below]
 * 2. Implement Stack using Queues
 * 3. Number of Recent Calls (Ping Counter)
 * 4. First Unique Character in a String
 * 5. Time Needed to Buy Tickets
 * 6. Number of Students Unable to Eat Lunch
 * 7. Moving Average from Data Stream
 * 8. Design Circular Queue
 * 9. Design Front Middle Back Queue
 * 10. Reveal Cards In Increasing Order
 * 11. Stamping The Sequence
 * 12. Dota2 Senate
 * 13. Longest Continuous Subarray With Absolute Diff <= Limit
 * 14. Product of the Last K Numbers
 * 15. Finding the Users Active Minutes
 * 16. Operations on Tree
 * 17. Snake Game
 * 18. Logger Rate Limiter
 * 19. Hit Counter
 * 20. Shortest Distance to a Character
 * 
 * === MEDIUM (20) ===
 * 1. Design Circular Deque
 * 2. Max value of equation
 * 3. Jump Game VI (Monotonic Queue)
 * 4. Shortest Subarray with Sum at Least K (Monotonic Queue)
 * 5. Constrained Subsequence Sum
 * 6. Steps to Make Array Non-decreasing
 * 7. Task Scheduler
 * 8. Design Snake Game
 * 9. Web Crawler
 * 10. Kill Process
 * 11. Maximum Width Ramp
 * 12. Maximum Sum Circular Subarray
 * 13. Delivering Boxes from Storage to Ports
 * 14. Process Tasks Using Servers
 * 15. Single-Threaded CPU
 * 16. Find the Winner of the Circular Game
 * 17. Minimum Number of K Consecutive Bit Flips
 * 18. Longest Subarray of 1's After Deleting One Element
 * 19. Max Consecutive Ones III
 * 20. Fruits into Baskets
 * 
 * === HARD (20) ===
 * 1. Sliding Window Maximum [Monotonic Queue]
 * 2. Trapping Rain Water II (PriorityQueue)
 * 3. Word Ladder (BFS)
 * 4. Word Ladder II
 * 5. Course Schedule III (PriorityQueue)
 * 6. Minimum Cost to Hire K Workers
 * 7. IPO
 * 8. Find Median from Data Stream
 * 9. Swim in Rising Water
 * 10. Cut Off Trees for Golf Event
 * 11. Shortest Path in a Grid with Obstacles Elimination
 * 12. Minimum Moves to Move a Box to Their Target Location
 * 13. Bus Routes
 * 14. Race Car
 * 15. Sliding Puzzle
 * 16. Open the Lock
 * 17. Minimum Jumps to Reach Home
 * 18. Minimum Number of Flips to Convert Binary Matrix to Zero Matrix
 * 19. Closest Room
 * 20. Maximum Number of Tasks You Can Assign
 */
class QueuePatternsAndProblems {

    /**
     * ==========================================
     * PATTERN 1: BFS (Breadth First Search)
     * ==========================================
     * Use when: Find shortest path in unweighted graph.
     */
    @Test
    fun patternBFS() {
        println("=== PATTERN: BFS ===")
        val graph = mapOf(0 to listOf(1, 2), 1 to listOf(3), 2 to listOf(3), 3 to emptyList<Int>())
        val queue: Queue<Int> = LinkedList()
        val visited = HashSet<Int>()
        
        queue.add(0)
        visited.add(0)
        
        println("Traversal Order:")
        while(queue.isNotEmpty()) {
            val node = queue.poll()
            print("$node -> ")
            graph[node]?.forEach { neighbor ->
                if(visited.add(neighbor)) queue.add(neighbor)
            }
        }
        println("End")
    }

    /**
     * ==========================================
     * PATTERN 2: MONOTONIC QUEUE
     * ==========================================
     * Use when: Finding Max/Min in a Sliding Window O(n).
     * Deque stores INDICES. Values corresponding to indices are monotonic.
     */
    @Test
    fun patternMonotonicQueue() {
        println("\n=== PATTERN: MONOTONIC QUEUE (Sliding Max) ===")
        val nums = intArrayOf(1, 3, -1, -3, 5, 3, 6, 7); val k = 3
        val deque = ArrayDeque<Int>() // Indicies
        val res = ArrayList<Int>()
        
        for (i in nums.indices) {
            // 1. Remove out of window indices (front)
            while (deque.isNotEmpty() && deque.peekFirst() < i - k + 1) deque.pollFirst()
            
            // 2. Maintain Monotonic Decreasing (remove smaller from back)
            while (deque.isNotEmpty() && nums[deque.peekLast()] < nums[i]) deque.pollLast()
            
            // 3. Add current
            deque.addLast(i)
            
            // 4. Record result
            if (i >= k - 1) res.add(nums[deque.peekFirst()])
        }
        println("Window Max: $res")
    }
}
