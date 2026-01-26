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
     *
     * PROBLEM:
     * Implement Deque with fixed size `k`.
     * `insertFront`, `insertLast`, `deleteFront`, `deleteLast`.
     *
     * DESIGN:
     * Why Array + Modulo?
     * - `head` points to front element. `tail` points to next available slot (or last added).
     * - Circular interaction using `% k`.
     * - insertFront: `head = (head - 1 + k) % k`.
     * - insertLast: `tail = (tail + 1) % k`.
     *
     * COMPLEXITY:
     * Time: O(1)
     * Space: O(K)
     */
    @Test
    fun q1_circularDeque() {
        println("=== Q1: Circular Deque ===")
        println("Logic: Similar to Circular Queue (Easy Q8), but with insertFront/deleteLast.")
    }

    /**
     * 2. Max Value of Equation
     *
     * PROBLEM:
     * Given `points` sorted by x-value. Find max `yi + yj + |xi - xj|` such that `|xi - xj| <= k`.
     * Since sorted, `xj > xi`, so `yi + yj + xj - xi` = `(yi - xi) + (yj + xj)`.
     *
     * DESIGN:
     * Why Monotonic Deque?
     * - Iterate `j` (current point). We want to maximize `(yi - xi)` for `i < j` and `xj - xi <= k`.
     * - Maintain `(yi - xi)` in decreasing deque.
     * - Remove `i` from front if `xj - xi > k` (expired).
     * - The best `i` is at `q.peekFirst()`.
     * - Calculate res: `(yj + xj) + deque.peek().val`.
     * - Insert current `(yj - xj)` into deque maintaining order.
     *
     * DETAIL:
     * 1. Q stores pair `(diff, x_val)` or just index.
     * 2. Loop `j`:
     *    - Poll front if `x[j] - x[front] > k`.
     *    - If Q not empty, max = `val[j] + val[front]`.
     *    - Poll back if `curr_diff >= back_diff`. Push current.
     *
     * COMPLEXITY:
     * Time: O(N)
     * Space: O(N)
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
     *
     * PROBLEM:
     * Start at 0. Jump at most `k` steps. Maximize score (sum of visited indices).
     * `DP[i] = nums[i] + max(DP[i-k]...DP[i-1])`.
     *
     * DESIGN:
     * Why Monotonic Queue?
     * - Finding `max` in sliding window of size `k` is O(N) with Deque.
     * - Queue stores indices `j` such that `DP[j]` is decreasing.
     * - `DP[i] = nums[i] + DP[q.peekFirst()]`.
     * - Push `i` into queue: Remove indices from back with `DP` < current.
     *
     * COMPLEXITY:
     * Time: O(N)
     * Space: O(N)
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
     *
     * PROBLEM:
     * Find smallest length subarray with sum >= K.
     * Negative numbers exist (Sliding Window not directly applicable).
     *
     * DESIGN:
     * Why Prefix Sum + Monotonic Queue?
     * - `sum(i, j) = P[j] - P[i] >= K`.
     * - Calculate Prefix Sum array `P`.
     * - Iterate `j`. We want largest `i < j` such that `P[j] - P[i] >= K`.
     * - `maxQ` (Increasing):
     *   - Before pushing `j`:
     *     - Check `P[j] - P[first] >= K`. If true, `first` is a valid `i`. Since we want shortest, we take it and update `minLen`. Poll first (because for any future `j' > j`, `j-i` is better than `j'-i`).
     *   - Maintain increasing order: Pop back if `P[back] >= P[j]` (because `j` is later and smaller, so `P[back]` is redundant).
     *
     * COMPLEXITY:
     * Time: O(N)
     * Space: O(N)
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
     *
     * PROBLEM:
     * Pick subsequence such that `j - i <= k` (adjacent indices diff limited). Maximize sum.
     *
     * DESIGN:
     * Why DP + Queue?
     * - Identical format to Jump Game VI.
     * - `DP[i] = nums[i] + max(0, max(DP[i-k...i-1]))`.
     * - We can choose NOT to include previous ones if they are all negative? No, subsequence must be non-empty. But within step `k`.
     * - Logic: The `max` term corresponds to picking the best previous end-point within range.
     *
     * COMPLEXITY:
     * Time: O(N)
     * Space: O(N)
     */
    @Test
    fun q5_constrainedSubsetSum() {
        println("=== Q5: Constrained Subsequence ===")
        println("Logic: DP[i] = max(0, max(DP[i-k..i-1])) + nums[i]. Mono Queue for max.")
    }

    /**
     * 6. Steps to Make Array Non-decreasing
     *
     * PROBLEM:
     * In one step, remove all `nums[i]` where `nums[i-1] > nums[i]`.
     * How many steps until non-decreasing?
     *
     * DESIGN:
     * Why Monotonic Stack?
     * - Each element `x` can eat smaller elements to its right.
     * - `steps[i]`: max steps `nums[i]` was eaten by its left neighbor.
     * - Stack stores index `j`.
     * - If `nums[i] >= stack.peek`: pop. We are bigger, we stop the eating chain coming from left.
     * - `dp[i] = max(dp[i], dp[popped] + 1)`.
     *
     * COMPLEXITY:
     * Time: O(N)
     * Space: O(N)
     */
    @Test
    fun q6_stepsMakeNonDecreasing() {
        println("=== Q6: Steps Non-Decreasing ===")
        println("Logic: Stack tracking (val, steps) chain.")
    }

    /**
     * 7. Task Scheduler
     *
     * PROBLEM:
     * `A, A, B`, n=2. Intervals must be `A -> idle -> idle -> A`.
     * Minimize time.
     *
     * DESIGN:
     * Why Frequency Math?
     * - Most frequent task defines the structure.
     * - Frame: `(max_freq - 1) * (n + 1) + Count(max_freq)`.
     * - Result is max of `tasks.length` or `frame`.
     *
     * COMPLEXITY:
     * Time: O(N) or O(1) (26 letters).
     * Space: O(1)
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
     *
     * PROBLEM:
     * Snake game logic (See Easy Q17).
     *
     * NOTE:
     * Often categorized as Medium due to implementation details (Deque + Set).
     */
    @Test
    fun q8_snakeGame() { println("See Easy Q17") }

    /**
     * 9. Web Crawler
     *
     * PROBLEM:
     * Given `startUrl` and `htmlParser`, return all URLs with same hostname found.
     *
     * DESIGN:
     * Why BFS?
     * - Standard Graph traversal.
     * - `visited` Set to avoid loops.
     * - `extractHost(url)`: Filter links.
     * - Parallel BFS possible for multi-threaded crawler.
     *
     * COMPLEXITY:
     * Time: O(V+E)
     * Space: O(V)
     */
    @Test
    fun q9_webCrawler() {
        println("=== Q9: Web Crawler ===")
        println("Logic: BFS from startUrl. Hostname check.")
    }

    /**
     * 10. Kill Process
     *
     * PROBLEM:
     * `pid` list, `ppid` list. Kill process `k`, kill all children recursively. return list.
     *
     * DESIGN:
     * Why Map + BFS?
     * - Build Adjacency List: `Map<PPID, List<PID>>`.
     * - BFS starting from `kill` node. Collect all descendants.
     *
     * COMPLEXITY:
     * Time: O(N)
     * Space: O(N)
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
     *
     * PROBLEM:
     * Max `j - i` where `A[i] <= A[j]`.
     *
     * DESIGN:
     * Why Monotonic Stack?
     * - We need smallest valid `i` for each `j`.
     * - Store decreasing sequence of indices `i` in stack.
     * - Iterate `j` backwards. If `A[j] >= A[stack.peek]`, we have a valid ramp.
     * - Maximize width `j - stack.pop()`.
     * - Since `j` decreases, if `stack.peek` satisfies `A[j]`, we can't find a better `j` for this `i` later.
     *
     * COMPLEXITY:
     * Time: O(N)
     * Space: O(N)
     */
    @Test
    fun q11_maxWidthRamp() {
        println("=== Q11: Max Width Ramp ===")
        println("See Stack Problems Q19 (Implemented there)")
    }

    /**
     * 12. Maximum Sum Circular Subarray
     *
     * PROBLEM:
     * Find max subarray sum. Array connects end-to-start.
     *
     * DESIGN:
     * Why Kadane x2?
     * - Case 1: Max subarray is in the middle (normal Kadane).
     * - Case 2: Max subarray wraps around.
     *   - Total Sum - Min Subarray Sum = Wraparound Max Sum.
     * - Result: `max(max_normal, total - min_normal)`.
     * - Edge Case: If all numbers are negative, `total - min` would be 0 (empty subarray), but we need at least 1 element. Return `max_normal` in that case.
     *
     * COMPLEXITY:
     * Time: O(N)
     * Space: O(1)
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
     * 13. Delivering Boxes from Storage to Ports
     *
     * PROBLEM:
     * Truck delivers boxes. Limitations: max boxes, max weight.
     * Minimize trips (storage -> ports -> storage).
     *
     * DESIGN:
     * Why Sliding Window DP?
     * - `DP[i]`: min trips to deliver first `i` boxes.
     * - `DP[i] = DP[j] + trips(j+1..i) + 2` (base trip).
     * - The `trips` count function is monotonic.
     * - Sliding window maintains valid `j` range (weight/count limits).
     * - Monotonic queue optimizes finding min `DP[j]`.
     *
     * COMPLEXITY:
     * Time: O(N)
     * Space: O(N)
     */
    @Test
    fun q13_deliveringBoxes() {
        println("=== Q13: Delivering Boxes ===")
        println("Logic: Hard DP with Sliding Window optimization (Deque).")
    }

    /**
     * 14. Process Tasks Using Servers
     *
     * PROBLEM:
     * `servers[weight]`, `tasks[time]`.
     * Assign task `j` to server with smallest weight (then index).
     * If all busy, wait for earliest free.
     *
     * DESIGN:
     * Why 2 Priority Queues?
     * - `freeServers`: Min-heap sorted by (weight, index).
     * - `busyServers`: Min-heap sorted by (endTime, weight, index).
     * - At time `t` (max(t, task_avail)):
     *   - Move finished from `busy` to `free`.
     *   - If `free` empty, forward `t` to `busy.peek().endTime`.
     *   - Pop `free`, assign task. Push to `busy`.
     *
     * COMPLEXITY:
     * Time: O(M log N)
     * Space: O(N)
     */
    @Test
    fun q14_processTasks() {
        println("=== Q14: Process Tasks Servers ===")
        println("Logic: PQ<Server> free, PQ<Server> busy (ordered by time).")
    }

    /**
     * 15. Single-Threaded CPU
     *
     * PROBLEM:
     * Tasks `[start, len]`. CPU processes 1 at a time.
     * Pick available task with shortest len (then min index).
     *
     * DESIGN:
     * Why Min Heap + Sorting?
     * - Sort tasks by `startTime`.
     * - `available`: Min-heap sorted by (len, index).
     * - While `tasks` available or `heap` not empty:
     *   - Add all `tasks` where `start <= currTime` to heap.
     *   - Pop heap, process (currTime += len).
     *   - If heap empty but tasks remain, jump `currTime` to next task start.
     *
     * COMPLEXITY:
     * Time: O(N log N)
     * Space: O(N)
     */
    @Test
    fun q15_singleThreadedCPU() {
        println("=== Q15: Single Threaded CPU ===")
        println("Logic: Time simulation + Min Heap.")
    }

    /**
     * 16. Find the Winner of the Circular Game (Josephus)
     *
     * PROBLEM:
     * `n` friends. Count `k`. Eliminate.
     *
     * DESIGN:
     * Why Simulation vs Math?
     * - Queue Simulation: O(N*K).
     * - Math (Recursive): `f(n, k) = (f(n-1, k) + k) % n`. O(N).
     * - Simulation:
     *   - Enqueue 1..n.
     *   - Poll and re-add `k-1` times.
     *   - Poll (eliminate) 1 time.
     *
     * COMPLEXITY:
     * Time: O(N*K) for Queue.
     * Space: O(N)
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
     * 17. Minimum Number of K-Consecutive Bit Flips
     *
     * PROBLEM:
     * Array of 0s and 1s. Bit flip subarray of size `K`. Make all 1s.
     *
     * DESIGN:
     * Why Greedy Queue?
     * - If we meet a 0, we MUST flip `[i, i+K-1]`.
     * - Tracking flips: `flipCount` tracks active flips covering current `i`.
     * - Queue (or `isFlipped` array) stores *end indices* of flips.
     * - Determine if `i` is effectively 0 or 1: `val = A[i] + activeFlips` % 2.
     * - If 0, start new flip. Push `i + K - 1`. `activeFlips++`.
     * - Remove expired flips from Queue front (`< i`).
     *
     * COMPLEXITY:
     * Time: O(N)
     * Space: O(K) or O(1) with modification.
     */
    @Test
    fun q17_minBitFlips() {
        println("=== Q17: Min K Bit Flips ===")
        println("Logic: Track current flip state. Greedy.")
    }

    /**
     * 18. Longest Subarray of 1s After Deleting One Element
     *
     * PROBLEM:
     * Longest subarray of 1s if you MUST delete exactly one element.
     *
     * DESIGN:
     * Why Sliding Window?
     * - Allow at most 1 zero in window.
     * - Window `[l, r]`. Count zeros.
     * - If zeros > 1: increment `l`, update count.
     * - Max length is `r - l` (since we delete 1).
     *
     * DETAIL:
     * 1. Expand `r`. `if(nums[r]==0) zeros++`.
     * 2. While `zeros > 1`: remove `l`.
     * 3. `max = max(max, r - l)`.
     *
     * COMPLEXITY:
     * Time: O(N)
     * Space: O(1)
     */
    @Test
    fun q18_longestOnes() {
        println("=== Q18: Longest 1s Delete One ===")
        val nums = intArrayOf(1,1,0,1)
        var l = 0; var zeros = 0; var max = 0
        for(r in nums.indices) {
            if(nums[r] == 0) zeros++
            while(zeros > 1) { if(nums[l] == 0) zeros--; l++ }
            max = Math.max(max, r - l) // -1 effective length usually, but here 'delete' means 'skip'
        }
        println("Result: $max")
    }

    /**
     * 19. Max Consecutive Ones III
     *
     * PROBLEM:
     * Flip at most `K` zeros. Max consecutive 1s.
     *
     * DESIGN:
     * Why Sliding Window?
     * - Standard pattern: Find longest subarray with `sum(zeros) <= K`.
     * - Expand `r`.
     * - Shrink `l` if zeros > K.
     * - `len = r - l + 1`.
     *
     * COMPLEXITY:
     * Time: O(N)
     * Space: O(1)
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
     * 20. Fruit Into Baskets
     *
     * PROBLEM:
     * Longest subarray with at most 2 distinct types of fruits (numbers).
     *
     * DESIGN:
     * Why Hash Map Sliding Window?
     * - `Map<Fruit, Count>`.
     * - Expand `r`: `map[fruit]++`.
     * - While `map.size > 2`: `map[l]--`. Remove if 0. `l++`.
     * - `max = r - l + 1`.
     *
     * COMPLEXITY:
     * Time: O(N)
     * Space: O(1) (limit 2 keys? actually max N if generic k).
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
