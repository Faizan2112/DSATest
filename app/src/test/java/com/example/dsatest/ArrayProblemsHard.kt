package com.example.dsatest

import org.junit.Test
import java.util.*

/**
 * ==========================================
 * ARRAY PROBLEMS: HARD (1-20)
 * ==========================================
 * 
 * ULTIMATE STUDY GUIDE
 * --------------------
 * Solutions to 20 Hard Array interview questions.
 * 
 * STRUCTURE:
 * 1. PROBLEM: Full text description.
 * 2. INPUT/OUTPUT: Concrete examples.
 * 3. DESIGN: The "Why" behind the approach (e.g., Space-Time Tradeoff).
 * 4. DETAIL: Step-by-step logic walkthrough (Bulleted).
 * 5. COMPLEXITY: Time O(T) and Space O(S).
 */
class ArrayProblemsHard {

    /**
     * 1. Trapping Rain Water
     * 
     * PROBLEM: Compute how much water can be trapped after raining.
     * 
     * INPUT: [0,1,0,2,1,0,1,3,2,1,2,1]
     * OUTPUT: 6
     * 
     * DESIGN: Two Pointers (Space Opt).
     * - Water is limited by the shorter wall.
     * 
     * DETAIL:
     * 1. `l = 0`, `r = n-1`.
     * 2. `leftMax = 0`, `rightMax = 0`.
     * 3. Loop `l <= r`:
     *    - If `height[l] <= height[r]`:
     *      - If `height[l] >= leftMax`: Update `leftMax`.
     *      - Else: `res += leftMax - height[l]`.
     *      - `l++`.
     *    - Else (Right side smaller):
     *      - If `height[r] >= rightMax`: Update `rightMax`.
     *      - Else: `res += rightMax - height[r]`.
     *      - `r--`.
     * 
     * COMPLEXITY: Time O(n) | Space O(1)
     */
    @Test
    fun q1_trapWater() {
        println("=== Q1: Trapping Rain Water ===")
        val h = intArrayOf(0,1,0,2,1,0,1,3,2,1,2,1)
        val n = h.size
        var left = 0; var right = n - 1
        var leftMax = 0; var rightMax = 0
        var res = 0
        while (left <= right) {
            if (h[left] <= h[right]) {
                if (h[left] >= leftMax) leftMax = h[left] else res += leftMax - h[left]
                left++
            } else {
                if (h[right] >= rightMax) rightMax = h[right] else res += rightMax - h[right]
                right--
            }
        }
        println("Result: $res")
    }

    /**
     * 2. First Missing Positive
     * 
     * PROBLEM: Smallest missing positive integer in O(n) and O(1) space.
     * 
     * INPUT: [3,4,-1,1]
     * OUTPUT: 2
     * 
     * DESIGN: Cyclic Sort.
     * - We want `nums[i] == i + 1`.
     * 
     * DETAIL:
     * 1. Iterate `i`.
     * 2. While `nums[i]` is in range [1, n] AND `nums[i]` is not at correct index (`nums[num-1]`):
     *    - Swap `nums[i]` with `nums[num-1]`.
     * 3. Pass 2: Find first `i` where `nums[i] != i+1`.
     * 4. Return `i+1`.
     * 
     * COMPLEXITY: Time O(n) | Space O(1)
     */
    @Test
    fun q2_firstMissingPositive() {
        println("=== Q2: First Missing Positive ===")
        val nums = intArrayOf(3, 4, -1, 1)
        val n = nums.size
        var i = 0
        while (i < n) {
            val correct = nums[i] - 1
            if (nums[i] in 1..n && nums[i] != nums[correct]) {
                val t = nums[i]; nums[i] = nums[correct]; nums[correct] = t
            } else i++
        }
        var res = n + 1
        for (j in 0 until n) if (nums[j] != j + 1) { res = j + 1; break }
        println("Result: $res")
    }

    /**
     * 3. Largest Rectangle in Histogram
     * 
     * PROBLEM: Largest rectangle area in histogram.
     * 
     * INPUT: [2,1,5,6,2,3]
     * OUTPUT: 10
     * 
     * DESIGN: Monotonic Stack.
     * 
     * DETAIL:
     * 1. Stack stores indices of increasing heights.
     * 2. Loop `i` to n. (Append 0 height as sentinel).
     * 3. While `currentHeight < stack.topHeight`:
     *    - `h = stack.pop()`.
     *    - `w = i - stack.peek() - 1`.
     *    - `maxArea = max(maxArea, h * w)`.
     * 4. Push `i`.
     * 
     * COMPLEXITY: Time O(n) | Space O(n)
     */
    @Test
    fun q3_largestRectangle() {
        println("=== Q3: Largest Rectangle in Histogram ===")
        val h = intArrayOf(2, 1, 5, 6, 2, 3)
        val stack = Stack<Int>()
        var maxArea = 0
        val n = h.size
        for (i in 0..n) {
            val currH = if (i == n) 0 else h[i]
            while (stack.isNotEmpty() && currH < h[stack.peek()]) {
                val height = h[stack.pop()]
                val width = if (stack.isEmpty()) i else i - stack.peek() - 1
                maxArea = Math.max(maxArea, height * width)
            }
            stack.push(i)
        }
        println("Result: $maxArea")
    }

    /**
     * 4. Sliding Window Maximum
     * 
     * PROBLEM: Max of each sliding window of size k.
     * 
     * INPUT: [1,3,-1,-3,5,3,6,7], k=3
     * OUTPUT: [3,3,5,5,6,7]
     * 
     * DESIGN: Monotonic Deque.
     * - Deque holds indices of potential max elements.
     * 
     * DETAIL:
     * 1. Remove indices out of window range from front.
     * 2. Remove indices with values `< current` from back (useless).
     * 3. Add current.
     * 4. If`i >= k-1`, add `Deque.front` value to result.
     * 
     * COMPLEXITY: Time O(n) | Space O(k)
     */
    @Test
    fun q4_slidingWindowMax() {
        println("=== Q4: Sliding Window Maximum ===")
        val nums = intArrayOf(1, 3, -1, -3, 5, 3, 6, 7); val k = 3
        val res = IntArray(nums.size - k + 1)
        val q = ArrayDeque<Int>()
        var ri = 0
        for (i in nums.indices) {
            if (q.isNotEmpty() && q.peekFirst() == i - k) q.pollFirst()
            while (q.isNotEmpty() && nums[q.peekLast()] < nums[i]) q.pollLast()
            q.addLast(i)
            if (i >= k - 1) res[ri++] = nums[q.peekFirst()]
        }
        println("Result: ${res.contentToString()}")
    }

    /**
     * 5. Median of Two Sorted Arrays
     * 
     * PROBLEM: Median of two sorted arrays in O(log(m+n)).
     * 
     * T: O(log min(m,n)) | S: O(1)
     * DESIGN: Binary Search on Partition.
     * 
     * DETAIL:
     * 1. Binary Search on smaller array (size `m`).
     * 2. `cut1 = mid`, `cut2 = (m+n+1)/2 - cut1`.
     * 3. Check boundary conditions:
     *    - `L1 <= R2` and `L2 <= R1` -> FOUND.
     *    - `L1 > R2` -> Move left.
     *    - Else -> Move right.
     */
    @Test
    fun q5_medianSortedArrays() {
        println("=== Q5: Median Sorted Arrays ===")
        val nums1 = intArrayOf(1, 3); val nums2 = intArrayOf(2)
        val a = if (nums1.size > nums2.size) nums2 else nums1
        val b = if (nums1.size > nums2.size) nums1 else nums2
        val n1 = a.size; val n2 = b.size
        var low = 0; var high = n1
        val total = n1 + n2
        var res = 0.0
        while (low <= high) {
            val cut1 = (low + high) / 2
            val cut2 = (total + 1) / 2 - cut1
            val l1 = if (cut1 == 0) Int.MIN_VALUE else a[cut1 - 1]
            val l2 = if (cut2 == 0) Int.MIN_VALUE else b[cut2 - 1]
            val r1 = if (cut1 == n1) Int.MAX_VALUE else a[cut1]
            val r2 = if (cut2 == n2) Int.MAX_VALUE else b[cut2]
            if (l1 <= r2 && l2 <= r1) {
                res = if (total % 2 == 0) (Math.max(l1, l2) + Math.min(r1, r2)) / 2.0 else Math.max(l1, l2).toDouble()
                break
            } else if (l1 > r2) high = cut1 - 1 else low = cut1 + 1
        }
        println("Result: $res")
    }

    /**
     * 6. Merge k Sorted Lists
     * 
     * PROBLEM: Merge k sorted linked lists.
     * 
     * DESIGN: Min Heap.
     * 
     * DETAIL:
     * 1. Add all K heads to PriorityQueue.
     * 2. While PQ not empty:
     *    - Pop min node. Add to result.
     *    - If node has next, add next to PQ.
     * 
     * COMPLEXITY: Time O(N log k) | Space O(k)
     */
    @Test
    fun q6_mergeKLists() {
        println("=== Q6: Merge k Sorted Lists ===")
        data class ListNode(var `val`: Int, var next: ListNode? = null)
        val lists = arrayOf(ListNode(1, ListNode(4, ListNode(5))), ListNode(1, ListNode(3, ListNode(4))), ListNode(2, ListNode(6)))
        val pq = PriorityQueue<ListNode> { a, b -> a.`val` - b.`val` }
        for (node in lists) pq.add(node)
        val dummy = ListNode(0); var curr = dummy
        while (pq.isNotEmpty()) {
            val node = pq.poll()
            curr.next = node; curr = curr.next!!
            if (node.next != null) pq.add(node.next)
        }
        val res = ArrayList<Int>(); var p = dummy.next
        while (p != null) { res.add(p.`val`); p = p.next }
        println("Result: $res")
    }

    /**
     * 7. Reverse Pairs
     * 
     * PROBLEM: Count i < j where nums[i] > 2*nums[j].
     * 
     * DESIGN: Merge Sort.
     * 
     * DETAIL:
     * 1. Recursively sort left and right.
     * 2. Before merging: Loop through Left. For each, increment pointer in Right while condition holds. Add count.
     * 3. Merge sorted arrays.
     * 
     * COMPLEXITY: Time O(n log n) | Space O(n)
     */
    @Test
    fun q7_reversePairs() {
        println("=== Q7: Reverse Pairs ===")
        val nums = intArrayOf(1, 3, 2, 3, 1)
        fun merge(l: Int, m: Int, r: Int): Int {
            var count = 0; var j = m + 1
            for (i in l..m) {
                while (j <= r && nums[i].toLong() > 2 * nums[j].toLong()) j++
                count += (j - (m + 1))
            }
            val left = nums.copyOfRange(l, m + 1); val right = nums.copyOfRange(m + 1, r + 1)
            var p1 = 0; var p2 = 0; var k = l
            while (p1 < left.size && p2 < right.size) nums[k++] = if (left[p1] <= right[p2]) left[p1++] else right[p2++]
            while (p1 < left.size) nums[k++] = left[p1++]
            while (p2 < right.size) nums[k++] = right[p2++]
            return count
        }
        fun sort(l: Int, r: Int): Int {
            if (l >= r) return 0
            val mid = l + (r - l) / 2
            return sort(l, mid) + sort(mid + 1, r) + merge(l, mid, r)
        }
        println("Result: ${sort(0, nums.size - 1)}")
    }

    /**
     * 8. Count Smaller Numbers After Self
     * 
     * PROBLEM: Return counts of smaller elements to the right.
     * 
     * DESIGN: Merge Sort (Index Tracking).
     * 
     * DETAIL:
     * 1. Sort INDICES, not values.
     * 2. During merge: If taking from Right (smaller), increment `rightCounter`.
     * 3. If taking from Left, add `rightCounter` to `counts[leftIndex]`.
     * 
     * COMPLEXITY: Time O(n log n) | Space O(n)
     */
    @Test
    fun q8_countSmaller() {
        println("=== Q8: Count Smaller After Self ===")
        val nums = intArrayOf(5, 2, 6, 1); val n = nums.size
        val indices = IntArray(n) { it }; val counts = IntArray(n)
        fun merge(l: Int, m: Int, r: Int) {
            val left = indices.copyOfRange(l, m + 1); val right = indices.copyOfRange(m + 1, r + 1)
            var i = 0; var j = 0; var k = l; var rightCounter = 0
            while (i < left.size && j < right.size) {
                if (nums[left[i]] <= nums[right[j]]) { counts[left[i]] += rightCounter; indices[k++] = left[i++] }
                else { rightCounter++; indices[k++] = right[j++] }
            }
            while (i < left.size) { counts[left[i]] += rightCounter; indices[k++] = left[i++] }
            while (j < right.size) indices[k++] = right[j++]
        }
        fun sort(l: Int, r: Int) {
            if (l < r) { val m = l + (r - l) / 2; sort(l, m); sort(m + 1, r); merge(l, m, r) }
        }
        sort(0, n - 1)
        println("Result: ${counts.toList()}")
    }

    /**
     * 9. Split Array Largest Sum
     * 
     * PROBLEM: Split nums into k subarrays to minimize largest sum.
     * 
     * DESIGN: Binary Search on Answer.
     * 
     * DETAIL:
     * 1. BS range [Max(nums), Sum(nums)].
     * 2. `check(mid)`: Iterate greedy. If sum > mid, new split.
     * 3. If pieces > k, `low = mid + 1`. Else `high = mid`.
     * 
     * COMPLEXITY: Time O(n log S) | Space O(1)
     */
    @Test
    fun q9_splitArray() {
        println("=== Q9: Split Array Largest Sum ===")
        val nums = intArrayOf(7, 2, 5, 10, 8); val k = 2
        var l = nums.maxOrNull()!!; var r = nums.sum()
        while (l < r) {
            val mid = l + (r - l) / 2
            var sum = 0; var pieces = 1
            for (n in nums) {
                if (sum + n > mid) { sum = n; pieces++ } else sum += n
            }
            if (pieces > k) l = mid + 1 else r = mid
        }
        println("Result: $l")
    }

    /**
     * 10. Longest Consecutive Sequence
     * 
     * PROBLEM: Longest consecutive seq in O(n).
     * 
     * DESIGN: Hash Set.
     * 
     * DETAIL:
     * 1. Add all to Set.
     * 2. Check if start of sequence (!set.contains(n-1)).
     * 3. Count upwards.
     * 
     * COMPLEXITY: Time O(n) | Space O(n)
     */
    @Test
    fun q10_longestConsecutive() {
        println("=== Q10: Longest Consecutive Sequence ===")
        val nums = intArrayOf(100, 4, 200, 1, 3, 2)
        val set = HashSet<Int>(); for (n in nums) set.add(n)
        var maxLen = 0
        for (n in nums) {
            if (!set.contains(n - 1)) {
                var curr = n; var len = 1
                while (set.contains(curr + 1)) { curr++; len++ }
                maxLen = Math.max(maxLen, len)
            }
        }
        println("Result: $maxLen")
    }

    /**
     * 11. Candy
     * 
     * PROBLEM: Minimize candies. Neighbor constraints.
     * 
     * DESIGN: Two Pass.
     * 
     * DETAIL:
     * 1. Pass Left-Right: If `rate[i]>rate[i-1]`, increase candy.
     * 2. Pass Right-Left: If `rate[i]>rate[i+1]`, set to `max(curr, right+1)`.
     * 
     * COMPLEXITY: Time O(n) | Space O(n)
     */
    @Test
    fun q11_candy() {
        println("=== Q11: Candy ===")
        val ratings = intArrayOf(1, 0, 2)
        val res = IntArray(ratings.size) { 1 }
        for (i in 1 until ratings.size) if (ratings[i] > ratings[i-1]) res[i] = res[i-1] + 1
        for (i in ratings.size - 2 downTo 0) if (ratings[i] > ratings[i+1]) res[i] = Math.max(res[i], res[i+1] + 1)
        println("Result: ${res.sum()}")
    }

    /**
     * 12. Maximal Rectangle
     * 
     * PROBLEM: Max rectangle of 1s in binary matrix.
     * 
     * DESIGN: Histogram + Stack.
     * 
     * DETAIL:
     * 1. Convert each row to a Histogram accumulator.
     * 2. Solve Largest Rect in Histogram for each row.
     * 
     * COMPLEXITY: Time O(m*n) | Space O(n)
     */
    @Test
    fun q12_maximalRectangle() {
        println("=== Q12: Maximal Rectangle ===")
        val matrix = arrayOf(charArrayOf('1','0','1','0','0'), charArrayOf('1','0','1','1','1'), charArrayOf('1','1','1','1','1'), charArrayOf('1','0','0','1','0'))
        if (matrix.isEmpty()) return
        val heights = IntArray(matrix[0].size)
        var maxArea = 0
        fun largestHist(h: IntArray): Int {
            val stack = Stack<Int>(); var maxA = 0
            for (i in 0..h.size) {
                val currH = if (i == h.size) 0 else h[i]
                while (stack.isNotEmpty() && currH < h[stack.peek()]) {
                    val prevH = h[stack.pop()]
                    val w = if (stack.isEmpty()) i else i - stack.peek() - 1
                    maxA = Math.max(maxA, prevH * w)
                }
                stack.push(i)
            }
            return maxA
        }
        for (row in matrix) {
            for (j in row.indices) heights[j] = if (row[j] == '1') heights[j] + 1 else 0
            maxArea = Math.max(maxArea, largestHist(heights))
        }
        println("Result: $maxArea")
    }

    /**
     * 13. N-Queens
     * 
     * PROBLEM: Place N queens safely.
     * 
     * DESIGN: Backtracking.
     * 
     * DETAIL:
     * 1. Sets for Cols, Diag1, Diag2.
     * 2. Recurse row by row.
     * 
     * COMPLEXITY: Time O(N!) | Space O(N)
     */
    @Test
    fun q13_nQueens() {
        println("=== Q13: N-Queens ===")
        val n = 4
        val res = mutableListOf<List<String>>()
        val cols = BooleanArray(n); val diag1 = BooleanArray(2 * n); val diag2 = BooleanArray(2 * n)
        val board = Array(n) { CharArray(n) { '.' } }
        fun backtrack(r: Int) {
            if (r == n) { res.add(board.map { String(it) }); return }
            for (c in 0 until n) {
                if (cols[c] || diag1[r - c + n] || diag2[r + c]) continue
                board[r][c] = 'Q'; cols[c] = true; diag1[r - c + n] = true; diag2[r + c] = true
                backtrack(r + 1)
                board[r][c] = '.'; cols[c] = false; diag1[r - c + n] = false; diag2[r + c] = false
            }
        }
        backtrack(0)
        println("Result Count: ${res.size}")
    }

    /**
     * 14. Sudoku Solver
     * 
     * PROBLEM: Solve Sudoku.
     * 
     * DESIGN: Backtracking.
     * 
     * DETAIL:
     * 1. Find empty cell.
     * 2. Try '1'-'9'.
     * 3. Check valid in Row, Col, Subgrid.
     */
    @Test
    fun q14_sudokuSolver() {
        println("=== Q14: Sudoku Solver ===")
        val board = arrayOf(
            charArrayOf('5', '3', '.', '.', '7', '.', '.', '.', '.'),
            charArrayOf('6', '.', '.', '1', '9', '5', '.', '.', '.'),
            charArrayOf('.', '9', '8', '.', '.', '.', '.', '6', '.'),
            charArrayOf('8', '.', '.', '.', '6', '.', '.', '.', '3'),
            charArrayOf('4', '.', '.', '8', '.', '3', '.', '.', '1'),
            charArrayOf('7', '.', '.', '.', '2', '.', '.', '.', '6'),
            charArrayOf('.', '6', '.', '.', '.', '.', '2', '8', '.'),
            charArrayOf('.', '.', '.', '4', '1', '9', '.', '.', '5'),
            charArrayOf('.', '.', '.', '.', '8', '.', '.', '7', '9')
        )
        fun isValid(r: Int, c: Int, k: Char): Boolean {
            for (i in 0..8) {
                if (board[r][i] == k) return false
                if (board[i][c] == k) return false
                if (board[3 * (r / 3) + i / 3][3 * (c / 3) + i % 3] == k) return false
            }
            return true
        }
        fun solve(): Boolean {
            for (i in 0..8) {
                for (j in 0..8) {
                    if (board[i][j] == '.') {
                        for (k in '1'..'9') {
                            if (isValid(i, j, k)) {
                                board[i][j] = k
                                if (solve()) return true
                                board[i][j] = '.'
                            }
                        }
                        return false
                    }
                }
            }
            return true
        }
        solve()
        println("Solved Top Left: ${board[0][0]}")
    }

    /**
     * 15. The Skyline Problem
     * 
     * PROBLEM: Silhouette of city.
     * 
     * DESIGN: Sweep Line + Max Heap.
     * 
     * DETAIL:
     * 1. Events: Start (add h), End (remove h).
     * 2. Sort by x.
     * 3. Iterate. If max height changes, record point.
     * 
     * COMPLEXITY: Time O(n log n) | Space O(n)
     */
    @Test
    fun q15_skyline() {
        println("=== Q15: Skyline Problem ===")
        val buildings = arrayOf(intArrayOf(2, 9, 10), intArrayOf(3, 7, 15), intArrayOf(5, 12, 12))
        val events = ArrayList<IntArray>()
        for (b in buildings) { events.add(intArrayOf(b[0], -b[2])); events.add(intArrayOf(b[1], b[2])) }
        events.sortWith { a, b -> if (a[0] != b[0]) a[0] - b[0] else a[1] - b[1] }
        val res = ArrayList<List<Int>>()
        val heights = TreeMap<Int, Int> { a, b -> b - a }; heights[0] = 1
        var prevMax = 0
        for (e in events) {
            if (e[1] < 0) heights[-e[1]] = heights.getOrDefault(-e[1], 0) + 1
            else { val c = heights[e[1]]!!; if (c == 1) heights.remove(e[1]) else heights[e[1]] = c - 1 }
            val currMax = heights.firstKey()
            if (currMax != prevMax) { res.add(listOf(e[0], currMax)); prevMax = currMax }
        }
        println("Result: $res")
    }

    /**
     * 16. Maximum Gap
     * 
     * PROBLEM: Max gap in sorted version O(n).
     * 
     * DESIGN: Buckets (Pigeonhole).
     * 
     * DETAIL:
     * 1. Bucket size = (Max-Min)/(n-1).
     * 2. Track min/max per bucket.
     * 3. Result = Max(NextMin - CurrMax).
     * 
     * COMPLEXITY: Time O(n) | Space O(n)
     */
    @Test
    fun q16_maxGap() {
        println("=== Q16: Maximum Gap ===")
        val nums = intArrayOf(3, 6, 9, 1)
        if (nums.size < 2) return
        val min = nums.minOrNull()!!; val max = nums.maxOrNull()!!
        val gap = Math.max(1, (max - min) / (nums.size - 1))
        val buckets = (max - min) / gap + 1
        val minB = IntArray(buckets) { Int.MAX_VALUE }; val maxB = IntArray(buckets) { Int.MIN_VALUE }
        for (n in nums) {
            val idx = (n - min) / gap
            minB[idx] = Math.min(minB[idx], n)
            maxB[idx] = Math.max(maxB[idx], n)
        }
        var maxGap = 0; var prev = min
        for (i in 0 until buckets) {
            if (minB[i] == Int.MAX_VALUE) continue
            maxGap = Math.max(maxGap, minB[i] - prev)
            prev = maxB[i]
        }
        println("Result: $maxGap")
    }

    /**
     * 17. Wildcard Matching
     * 
     * PROBLEM: '?' matches char. '*' matches sequence.
     * 
     * DESIGN: DP.
     * 
     * DETAIL: match[i][j] = match[i-1][j-1] if equal.
     * 
     * COMPLEXITY: Time O(mn) | Space O(mn)
     */
    @Test
    fun q17_wildcard() {
        println("=== Q17: Wildcard Matching ===")
        val s = "adceb"; val p = "*a*b"
        val m = s.length; val n = p.length
        val dp = Array(m + 1) { BooleanArray(n + 1) }
        dp[0][0] = true
        for (j in 1..n) if (p[j-1] == '*') dp[0][j] = dp[0][j-1]
        for (i in 1..m) {
            for (j in 1..n) {
                if (p[j-1] == '?' || p[j-1] == s[i-1]) dp[i][j] = dp[i-1][j-1]
                else if (p[j-1] == '*') dp[i][j] = dp[i][j-1] || dp[i-1][j]
            }
        }
        println("Result: ${dp[m][n]}")
    }

    /**
     * 18. Word Ladder II
     * 
     * PROBLEM: Shortest transform sequences.
     * 
     * DESIGN: BFS + DFS.
     * - BFS build level graph. DFS trace paths.
     */
    @Test
    fun q18_wordLadderII() {
        println("=== Q18: Word Ladder II ===")
        println("Logic: 1. BFS to generate Level Map. 2. DFS to reconstruct paths.")
    }

    /**
     * 19. Palindrome Partitioning II
     * 
     * PROBLEM: Min cuts for palindrome partition.
     * 
     * DESIGN: DP.
     * 
     * DETAIL:
     * 1. `cut[i]` = min cuts for s[0..i].
     * 2. Inner loop j: If s[j..i] is palindrome, `cut[i] = min(cut[j-1]+1)`.
     * 
     * COMPLEXITY: Time O(n^2) | Space O(n^2)
     */
    @Test
    fun q19_minCut() {
        println("=== Q19: Palindrome Partitioning II ===")
        val s = "aab"; val n = s.length
        val cut = IntArray(n); val isPal = Array(n) { BooleanArray(n) }
        for (i in 0 until n) {
            var min = i
            for (j in 0..i) {
                if (s[j] == s[i] && (i - j < 2 || isPal[j + 1][i - 1])) {
                    isPal[j][i] = true
                    min = if (j == 0) 0 else Math.min(min, cut[j - 1] + 1)
                }
            }
            cut[i] = min
        }
        println("Result: ${cut[n - 1]}")
    }

    /**
     * 20. Alien Dictionary
     * 
     * PROBLEM: Sort characters based on word order.
     * 
     * DESIGN: Topological Sort.
     * 
     * DETAIL:
     * 1. Build Adj List from prefixes.
     * 2. Run Kahn's Algo (Degrees).
     */
    @Test
    fun q20_alienDictionary() {
        println("=== Q20: Alien Dictionary ===")
        val words = arrayOf("wrt", "wrf", "er", "ett", "rftt")
        val adj = HashMap<Char, MutableSet<Char>>()
        val degree = HashMap<Char, Int>()
        for (w in words) for (c in w) degree[c] = 0
        for (i in 0 until words.size - 1) {
            val w1 = words[i]; val w2 = words[i + 1]
            if (w1.length > w2.length && w1.startsWith(w2)) return 
            for (j in 0 until Math.min(w1.length, w2.length)) {
                if (w1[j] != w2[j]) {
                    adj.computeIfAbsent(w1[j]) { HashSet() }.let { if (it.add(w2[j])) degree[w2[j]] = degree[w2[j]]!! + 1 }
                    break
                }
            }
        }
        val q: Queue<Char> = LinkedList()
        for ((k, v) in degree) if (v == 0) q.add(k)
        val sb = StringBuilder()
        while (q.isNotEmpty()) {
            val c = q.poll(); sb.append(c)
            adj[c]?.forEach { neighbor -> degree[neighbor] = degree[neighbor]!! - 1; if (degree[neighbor] == 0) q.add(neighbor) }
        }
        if (sb.length != degree.size) println("Invalid") else println("Result: $sb")
    }
}
