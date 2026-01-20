package com.example.dsatest

import org.junit.Test
import java.util.Arrays

/**
 * ==========================================
 * ARRAY PROBLEMS: MEDIUM (1-20)
 * ==========================================
 * 
 * Solutions to 20 Medium Array interview questions.
 * Patterns: Two Pointers, Sliding Window, Intervals, Matrix.
 */
class ArrayProblemsMedium {

    /**
     * 1. 3Sum
     * Logic: Sort, then 2 Pointers.
     */
    @Test
    fun q1_threeSum() {
        println("=== Q1: 3Sum ===")
        val nums = intArrayOf(-1, 0, 1, 2, -1, -4)
        nums.sort()
        val res = mutableListOf<List<Int>>()
        for (i in 0 until nums.size - 2) {
            if (i > 0 && nums[i] == nums[i - 1]) continue
            var l = i + 1; var r = nums.size - 1
            while (l < r) {
                val sum = nums[i] + nums[l] + nums[r]
                if (sum == 0) {
                    res.add(listOf(nums[i], nums[l], nums[r]))
                    while (l < r && nums[l] == nums[l + 1]) l++
                    while (l < r && nums[r] == nums[r - 1]) r--
                    l++; r--
                } else if (sum < 0) l++ else r--
            }
        }
        println("Result: $res")
    }

    /**
     * 2. Container With Most Water
     * Logic: Two pointers from outside. Move smaller height.
     */
    @Test
    fun q2_containerMostWater() {
        println("=== Q2: Container With Most Water ===")
        val height = intArrayOf(1, 8, 6, 2, 5, 4, 8, 3, 7)
        var l = 0; var r = height.size - 1; var maxArea = 0
        while (l < r) {
            val h = Math.min(height[l], height[r])
            maxArea = Math.max(maxArea, h * (r - l))
            if (height[l] < height[r]) l++ else r--
        }
        println("Result: $maxArea")
    }

    /**
     * 3. Product Except Self
     * Logic: Left Prefix * Right Suffix.
     */
    @Test
    fun q3_productExceptSelf() {
        println("=== Q3: Product Except Self ===")
        val nums = intArrayOf(1, 2, 3, 4)
        val res = IntArray(nums.size)
        res[0] = 1
        for (i in 1 until nums.size) res[i] = res[i - 1] * nums[i - 1]
        var right = 1
        for (i in nums.size - 1 downTo 0) {
            res[i] *= right
            right *= nums[i]
        }
        println("Result: ${res.contentToString()}")
    }

    /**
     * 4. Maximum Subarray (Kadane)
     * Logic: max(num, currentSum + num).
     */
    @Test
    fun q4_maxSubarray() {
        println("=== Q4: Max Subarray ===")
        val nums = intArrayOf(-2, 1, -3, 4, -1, 2, 1, -5, 4)
        var max = nums[0]; var curr = nums[0]
        for (i in 1 until nums.size) {
            curr = Math.max(nums[i], curr + nums[i])
            max = Math.max(max, curr)
        }
        println("Result: $max")
    }

    /**
     * 5. Sort Colors (DNF)
     * Logic: 3 Pointers (low, mid, high).
     */
    @Test
    fun q5_sortColors() {
        println("=== Q5: Sort Colors ===")
        val nums = intArrayOf(2, 0, 2, 1, 1, 0)
        var low = 0; var mid = 0; var high = nums.size - 1
        while (mid <= high) {
            when (nums[mid]) {
                0 -> {
                    val t = nums[low]; nums[low] = nums[mid]; nums[mid] = t
                    low++; mid++
                }
                1 -> mid++
                2 -> {
                    val t = nums[high]; nums[high] = nums[mid]; nums[mid] = t
                    high--
                }
            }
        }
        println("Result: ${nums.contentToString()}")
    }

    /**
     * 6. Merge Intervals
     * Logic: Sort by start. Merge overlap.
     */
    @Test
    fun q6_mergeIntervals() {
        println("=== Q6: Merge Intervals ===")
        val intervals = arrayOf(intArrayOf(1, 3), intArrayOf(2, 6), intArrayOf(8, 10))
        intervals.sortBy { it[0] }
        val res = mutableListOf<IntArray>()
        var curr = intervals[0]
        res.add(curr)
        for (i in 1 until intervals.size) {
            val next = intervals[i]
            if (curr[1] >= next[0]) {
                curr[1] = Math.max(curr[1], next[1])
            } else {
                curr = next
                res.add(curr)
            }
        }
        println("Result: ${res.map { it.contentToString() }}")
    }

    /**
     * 7. Insert Interval
     * Logic: Add before, merge new, add after.
     */
    @Test
    fun q7_insertInterval() {
        println("=== Q7: Insert Interval ===")
        val intervals = arrayOf(intArrayOf(1, 3), intArrayOf(6, 9))
        var newInterval = intArrayOf(2, 5)
        val res = mutableListOf<IntArray>()
        var i = 0; val n = intervals.size
        
        while (i < n && intervals[i][1] < newInterval[0]) res.add(intervals[i++])
        while (i < n && intervals[i][0] <= newInterval[1]) {
            newInterval[0] = Math.min(newInterval[0], intervals[i][0])
            newInterval[1] = Math.max(newInterval[1], intervals[i][1])
            i++
        }
        res.add(newInterval)
        while (i < n) res.add(intervals[i++])
        println("Result: ${res.map { it.contentToString() }}")
    }

    /**
     * 8. Rotate Image
     * Logic: Transpose (Swap i,j) then Reverse Rows.
     */
    @Test
    fun q8_rotateImage() {
        println("=== Q8: Rotate Image 90 deg ===")
        val matrix = arrayOf(intArrayOf(1, 2), intArrayOf(3, 4))
        val n = matrix.size
        // 1. Transpose
        for (i in 0 until n) {
            for (j in i until n) {
                val t = matrix[i][j]; matrix[i][j] = matrix[j][i]; matrix[j][i] = t
            }
        }
        // 2. Reverse Rows
        for (i in 0 until n) matrix[i].reverse()
        println("Result: ${matrix.map { it.contentToString() }}")
    }

    /**
     * 9. Spiral Matrix
     * Logic: 4 boundaries (top, bottom, left, right). Loop while correct.
     */
    @Test
    fun q9_spiralMatrix() {
        println("=== Q9: Spiral Matrix ===")
        val matrix = arrayOf(intArrayOf(1, 2, 3), intArrayOf(4, 5, 6), intArrayOf(7, 8, 9))
        val res = mutableListOf<Int>()
        var top = 0; var bot = matrix.size - 1
        var left = 0; var right = matrix[0].size - 1
        while (top <= bot && left <= right) {
            for (i in left..right) res.add(matrix[top][i]); top++
            for (i in top..bot) res.add(matrix[i][right]); right--
            if (top <= bot) { for (i in right downTo left) res.add(matrix[bot][i]); bot-- }
            if (left <= right) { for (i in bot downTo top) res.add(matrix[i][left]); left++ }
        }
        println("Result: $res")
    }

    /**
     * 10. Subarray Sum Equals K
     * Logic: Prefix Sum Map<Sum, Count>.
     */
    @Test
    fun q10_subarraySumK() {
        println("=== Q10: Subarray Sum Equals K ===")
        val nums = intArrayOf(1, 1, 1); val k = 2
        val map = HashMap<Int, Int>()
        map[0] = 1
        var sum = 0; var count = 0
        for (n in nums) {
            sum += n
            if (map.containsKey(sum - k)) count += map[sum - k]!!
            map[sum] = map.getOrDefault(sum, 0) + 1
        }
        println("Result: $count")
    }

    /**
     * 11. Next Permutation
     * Logic: Find first decreasing from right. Swap with next larger. Reverse rest.
     */
    @Test
    fun q11_nextPermutation() {
        println("=== Q11: Next Permutation ===")
        val nums = intArrayOf(1, 2, 3)
        var i = nums.size - 2
        while (i >= 0 && nums[i] >= nums[i + 1]) i--
        if (i >= 0) {
            var j = nums.size - 1
            while (nums[j] <= nums[i]) j--
            val t = nums[i]; nums[i] = nums[j]; nums[j] = t
        }
        // Reverse i+1 to end
        var l = i + 1; var r = nums.size - 1
        while (l < r) { val t = nums[l]; nums[l++] = nums[r]; nums[r--] = t }
        println("Result: ${nums.contentToString()}")
    }

    /**
     * 12. Search Rotated Sorted Array
     * Logic: Binary Search. Determine sorted half.
     */
    @Test
    fun q12_searchRotated() {
        println("=== Q12: Search Rotated Array ===")
        val nums = intArrayOf(4, 5, 6, 7, 0, 1, 2); val target = 0
        var l = 0; var r = nums.size - 1
        var res = -1
        while (l <= r) {
            val mid = l + (r - l) / 2
            if (nums[mid] == target) { res = mid; break }
            if (nums[l] <= nums[mid]) { // Left sorted
                if (target >= nums[l] && target < nums[mid]) r = mid - 1 else l = mid + 1
            } else { // Right sorted
                if (target > nums[mid] && target <= nums[r]) l = mid + 1 else r = mid - 1
            }
        }
        println("Result: $res")
    }

    /**
     * 13. Find First and Last Position
     * Logic: Binary Search twice (Lower Bound, Upper Bound).
     */
    @Test
    fun q13_findFirstLast() {
        println("=== Q13: Find First and Last ===")
        val nums = intArrayOf(5, 7, 7, 8, 8, 10); val t = 8
        fun search(findFirst: Boolean): Int {
            var l = 0; var r = nums.size - 1; var idx = -1
            while (l <= r) {
                val mid = l + (r - l) / 2
                if (nums[mid] == t) {
                    idx = mid
                    if (findFirst) r = mid - 1 else l = mid + 1
                } else if (nums[mid] < t) l = mid + 1 else r = mid - 1
            }
            return idx
        }
        println("Result: [${search(true)}, ${search(false)}]")
    }

    /**
     * 14. Combination Sum
     * Logic: Backtracking. Reuse elements.
     */
    @Test
    fun q14_combinationSum() {
        println("=== Q14: Combination Sum ===")
        val nums = intArrayOf(2, 3, 6, 7); val target = 7
        val res = mutableListOf<List<Int>>()
        fun dfs(idx: Int, t: Int, path: MutableList<Int>) {
            if (t == 0) { res.add(ArrayList(path)); return }
            if (t < 0 || idx == nums.size) return
            // Choice 1: Include
            path.add(nums[idx])
            dfs(idx, t - nums[idx], path)
            path.removeAt(path.size - 1)
            // Choice 2: Skip
            dfs(idx + 1, t, path)
        }
        dfs(0, target, mutableListOf())
        println("Result: $res")
    }

    /**
     * 15. House Robber
     * Logic: DP. dp[i] = max(dp[i-1], dp[i-2] + nums[i]).
     */
    @Test
    fun q15_houseRobber() {
        println("=== Q15: House Robber ===")
        val nums = intArrayOf(1, 2, 3, 1)
        var prev1 = 0; var prev2 = 0
        for (n in nums) {
            val curr = Math.max(prev1, prev2 + n)
            prev2 = prev1
            prev1 = curr
        }
        println("Result: $prev1")
    }

    /**
     * 16. Jump Game
     * Logic: Greedy. Track maxReachable.
     */
    @Test
    fun q16_jumpGame() {
        println("=== Q16: Jump Game ===")
        val nums = intArrayOf(2, 3, 1, 1, 4)
        var reach = 0
        var canReach = true
        for (i in nums.indices) {
            if (i > reach) { canReach = false; break }
            reach = Math.max(reach, i + nums[i])
        }
        println("Result: $canReach")
    }

    /**
     * 17. Unique Paths
     * Logic: DP or Combinatorics (m+n-2 C m-1).
     */
    @Test
    fun q17_uniquePaths() {
        println("=== Q17: Unique Paths ===")
        val m = 3; val n = 7
        val dp = IntArray(n) { 1 }
        for (i in 1 until m) {
            for (j in 1 until n) dp[j] += dp[j - 1]
        }
        println("Result: ${dp[n - 1]}")
    }

    /**
     * 18. Longest Consecutive Sequence
     * Logic: HashSet. Check if (num-1) exists.
     */
    @Test
    fun q18_longestConsecutive() {
        println("=== Q18: Longest Consecutive Sequence ===")
        val nums = intArrayOf(100, 4, 200, 1, 3, 2)
        val set = HashSet<Int>()
        for (n in nums) set.add(n)
        var maxLen = 0
        for (n in set) {
            if (!set.contains(n - 1)) {
                var curr = n
                var len = 1
                while (set.contains(curr + 1)) { curr++; len++ }
                maxLen = Math.max(maxLen, len)
            }
        }
        println("Result: $maxLen")
    }

    /**
     * 19. Set Matrix Zeroes
     * Logic: Use first row/col as markers.
     */
    @Test
    fun q19_setZeroes() {
        println("=== Q19: Set Matrix Zeroes ===")
        val matrix = arrayOf(intArrayOf(1, 1, 1), intArrayOf(1, 0, 1), intArrayOf(1, 1, 1))
        val m = matrix.size; val n = matrix[0].size
        var col0 = 1
        for (i in 0 until m) {
            if (matrix[i][0] == 0) col0 = 0
            for (j in 1 until n) if (matrix[i][j] == 0) { matrix[i][0]=0; matrix[0][j]=0 }
        }
        for (i in m - 1 downTo 0) {
            for (j in n - 1 downTo 1) if (matrix[i][0] == 0 || matrix[0][j] == 0) matrix[i][j] = 0
            if (col0 == 0) matrix[i][0] = 0
        }
        println("Result: ${matrix.map { it.contentToString() }}")
    }

    /**
     * 20. Count Subarrays with sum K 
     * (Same as Q10 in different problem set/name, using K-diff pairs logic here for variety)
     * Problem: K-diff Pairs in an Array (Medium)
     */
    @Test
    fun q20_kDiffPairs() {
        println("=== Q20: K-diff Pairs ===")
        val nums = intArrayOf(3, 1, 4, 1, 5); val k = 2
        val map = HashMap<Int, Int>()
        for (n in nums) map[n] = map.getOrDefault(n, 0) + 1
        var count = 0
        for ((num, freq) in map) {
            if (k > 0 && map.containsKey(num + k)) count++
            else if (k == 0 && freq > 1) count++
        }
        println("Result: $count")
    }
}
