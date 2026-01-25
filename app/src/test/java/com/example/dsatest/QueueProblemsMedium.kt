package com.example.dsatest

import org.junit.Test
import java.util.ArrayDeque
import java.util.PriorityQueue
import java.util.LinkedList
import java.util.Queue

/**
 * ==========================================
 * QUEUE PROBLEMS: MEDIUM (1-20)
 * ==========================================
 * 
 * Solutions to 20 Medium Queue questions.
 * Patterns: Monotonic Queue, Sliding Window, Design.
 */
class QueueProblemsMedium {

    /**
     * 1. Design Circular Deque
     * Logic: Array + head/tail markers.
     */
    @Test
    fun q1_circularDeque() {
        println("=== Q1: Circular Deque ===")
        println("Logic: Similar to Circular Queue (Easy Q8), but with insertFront/deleteLast.")
    }

    /**
     * 2. Max Value of Equation
     * Logic: yi + yj + |xi - xj| = (yi - xi) + (yj + xj).
     * Mono Deque tracking max(yi - xi).
     */
    @Test
    fun q2_maxValueEquation() {
        println("=== Q2: Max Value Equation ===")
        val points = arrayOf(intArrayOf(1,3), intArrayOf(2,0), intArrayOf(5,10), intArrayOf(6,-10))
        val k = 1
        val q = ArrayDeque<Int>() // Index
        var max = Int.MIN_VALUE
        for(i in points.indices) {
            while(q.isNotEmpty() && points[i][0] - points[q.peekFirst()][0] > k) q.pollFirst()
            if(q.isNotEmpty()) {
                val j = q.peekFirst()
                max = Math.max(max, points[i][1] + points[j][1] + Math.abs(points[i][0] - points[j][0]))
            }
            while(q.isNotEmpty() && points[q.peekLast()][1] - points[q.peekLast()][0] <= points[i][1] - points[i][0]) q.pollLast()
            q.addLast(i)
        }
        println("Result: $max")
    }

    /**
     * 3. Jump Game VI
     * Logic: DP[i] = max(DP[i-k..i-1]) + nums[i].
     * Max Sliding Window (Mono Queue) to find max(DP).
     */
    @Test
    fun q3_jumpGameVI() {
        println("=== Q3: Jump Game VI ===")
        val nums = intArrayOf(1,-1,-2,4,-7,3); val k = 2
        val dp = IntArray(nums.size)
        dp[0] = nums[0]
        val q = ArrayDeque<Int>()
        q.add(0)
        for(i in 1 until nums.size) {
            if(q.peekFirst() < i - k) q.pollFirst()
            dp[i] = dp[q.peekFirst()] + nums[i]
            while(q.isNotEmpty() && dp[q.peekLast()] <= dp[i]) q.pollLast()
            q.addLast(i)
        }
        println("Result: ${dp.last()}")
    }

    /**
     * 4. Shortest Subarray with Sum at Least K
     * Logic: Prefix Sums + Monotonic Increasing Deque.
     */
    @Test
    fun q4_shortestSubarraySumK() {
        println("=== Q4: Shortest Subarray Sum K ===")
        val nums = intArrayOf(2, -1, 2); val k = 3
        val P = LongArray(nums.size + 1); for(i in nums.indices) P[i+1] = P[i] + nums[i]
        val q = ArrayDeque<Int>()
        var res = Int.MAX_VALUE
        for(i in P.indices) {
            while(q.isNotEmpty() && P[i] - P[q.peekFirst()] >= k) res = Math.min(res, i - q.pollFirst())
            while(q.isNotEmpty() && P[q.peekLast()] >= P[i]) q.pollLast()
            q.addLast(i)
        }
        println("Result: ${if(res == Int.MAX_VALUE) -1 else res}")
    }

    /**
     * 5. Constrained Subsequence Sum
     * Logic: Similar to Jump Game VI. DP + Mono Queue.
     */
    @Test
    fun q5_constrainedSubsetSum() {
        println("=== Q5: Constrained Subsequence ===")
        println("Logic: DP[i] = max(0, max(DP[i-k..i-1])) + nums[i]. Mono Queue for max.")
    }

    /**
     * 6. Steps to Make Array Non-decreasing
     * Logic: Monotonic Stack/Queue DP.
     */
    @Test
    fun q6_stepsMakeNonDecreasing() {
        println("=== Q6: Steps Non-Decreasing ===")
        println("Logic: Stack tracking (val, steps) chain.")
    }

    /**
     * 7. Task Scheduler
     * Logic: Frequency Map + Idle slots calculation (or PriorityQueue).
     */
    @Test
    fun q7_taskScheduler() {
        println("=== Q7: Task Scheduler ===")
        val tasks = charArrayOf('A','A','A','B','B','B'); val n = 2
        val count = IntArray(26); for(c in tasks) count[c - 'A']++
        count.sort()
        val maxVal = count[25] - 1
        var idle = maxVal * n
        for(i in 24 downTo 0) idle -= Math.min(count[i], maxVal)
        println("Result: ${tasks.size + Math.max(0, idle)}")
    }

    /**
     * 8. Design Snake Game
     * Logic: See Easy Q17 (Same logic, usually Medium).
     */
    @Test
    fun q8_snakeGame() { println("See Easy Q17") }

    /**
     * 9. Web Crawler
     * Logic: BFS with Set<URL>.
     */
    @Test
    fun q9_webCrawler() {
        println("=== Q9: Web Crawler ===")
        println("Logic: BFS from startUrl. Hostname check.")
    }

    /**
     * 10. Kill Process
     * Logic: Map<Parent, Children>. BFS/DFS from kill node.
     */
    @Test
    fun q10_killProcess() {
        println("=== Q10: Kill Process ===")
        val pid = listOf(1, 3, 10, 5); val ppid = listOf(3, 0, 5, 3); val kill = 5
        val map = HashMap<Int, MutableList<Int>>()
        for(i in ppid.indices) if(ppid[i] > 0) map.computeIfAbsent(ppid[i]) { ArrayList() }.add(pid[i])
        val res = ArrayList<Int>()
        val q: Queue<Int> = LinkedList(); q.add(kill)
        while(q.isNotEmpty()) {
            val n = q.poll()
            res.add(n)
            map[n]?.let { q.addAll(it) }
        }
        println("Result: $res")
    }

    /**
     * 11. Maximum Width Ramp
     * Logic: See Stack Medium Q19.
     */
    @Test
    fun q11_maxWidthRamp() { println("See Stack Problems Q19") }

    /**
     * 12. Maximum Sum Circular Subarray
     * Logic: Kadane max & min. Result max(maxSum, total - minSum).
     * (Corner case: all negative).
     */
    @Test
    fun q12_maxSumCircular() {
        println("=== Q12: Max Sum Circular ===")
        val nums = intArrayOf(1,-2,3,-2)
        var total = 0; var maxSum = nums[0]; var curMax = 0
        var minSum = nums[0]; var curMin = 0
        for(n in nums) {
            total += n
            curMax = Math.max(curMax + n, n); maxSum = Math.max(maxSum, curMax)
            curMin = Math.min(curMin + n, n); minSum = Math.min(minSum, curMin)
        }
        println("Result: ${if(maxSum > 0) Math.max(maxSum, total - minSum) else maxSum}")
    }

    /**
     * 13. Delivering Boxes
     * Logic: DP + Monotonic Queue.
     */
    @Test
    fun q13_deliveringBoxes() {
        println("=== Q13: Delivering Boxes ===")
        println("Logic: Hard DP with Sliding Window optim.")
    }

    /**
     * 14. Process Tasks Using Servers
     * Logic: Two PriorityQueues (Free Servers, Busy Servers).
     */
    @Test
    fun q14_processTasks() {
        println("=== Q14: Process Tasks Servers ===")
        println("Logic: PQ<Server> free, PQ<Server> busy (ordered by time).")
    }

    /**
     * 15. Single-Threaded CPU
     * Logic: Sort Tasks by start. PQ for Available tasks (by duration).
     */
    @Test
    fun q15_singleThreadedCPU() {
        println("=== Q15: Single Threaded CPU ===")
        println("Logic: Time simulation + Min Heap.")
    }

    /**
     * 16. Find the Winner of the Circular Game
     * Logic: Simulation (Queue rotation) or Josephus Problem (Math).
     */
    @Test
    fun q16_circularGame() {
        println("=== Q16: Circular Game (Josephus) ===")
        val n = 5; val k = 2
        var q: Queue<Int> = LinkedList((1..n).toList())
        while(q.size > 1) {
            for(i in 1 until k) q.add(q.poll())
            q.poll()
        }
        println("Result: ${q.peek()}")
    }

    /**
     * 17. Min K Bit Flips
     * Logic: Queue/Sliding Window to track flip effect.
     */
    @Test
    fun q17_minBitFlips() {
        println("=== Q17: Min K Bit Flips ===")
        println("Logic: Track current flip state. Greedy.")
    }

    /**
     * 18. Longest Subarray of 1s Deleting One
     * Logic: Sliding Window. (Queue can track zero usage).
     */
    @Test
    fun q18_longestOnes() {
        println("=== Q18: Longest 1s Delete One ===")
        val nums = intArrayOf(1,1,0,1)
        var l = 0; var zeros = 0; var max = 0
        for(r in nums.indices) {
            if(nums[r] == 0) zeros++
            while(zeros > 1) { if(nums[l] == 0) zeros--; l++ }
            max = Math.max(max, r - l) // -1 effective length?
        }
        println("Result: $max")
    }

    /**
     * 19. Max Consecutive Ones III
     * Logic: Sliding Window k zeros.
     */
    @Test
    fun q19_maxConsecutiveOnesIII() {
        println("=== Q19: Max Consecutive Ones III ===")
        val nums = intArrayOf(1,1,1,0,0,0,1,1,1,1,0); val k = 2
        var l = 0; var z = 0; var max = 0
        for(r in nums.indices) {
            if(nums[r] == 0) z++
            while(z > k) { if(nums[l] == 0) z--; l++ }
            max = Math.max(max, r - l + 1)
        }
        println("Result: $max")
    }

    /**
     * 20. Fruits into Baskets
     * Logic: Sliding Window (Map count).
     */
    @Test
    fun q20_fruitsBaskets() {
        println("=== Q20: Fruits Into Baskets ===")
        val fruits = intArrayOf(1,2,1)
        val map = HashMap<Int, Int>()
        var l = 0; var max = 0
        for(r in fruits.indices) {
            map[fruits[r]] = map.getOrDefault(fruits[r], 0) + 1
            while(map.size > 2) {
                map[fruits[l]] = map[fruits[l]]!! - 1
                if(map[fruits[l]] == 0) map.remove(fruits[l])
                l++
            }
            max = Math.max(max, r - l + 1)
        }
        println("Result: $max")
    }
}
