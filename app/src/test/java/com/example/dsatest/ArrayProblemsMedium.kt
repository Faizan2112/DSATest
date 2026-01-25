package com.example.dsatest

import org.junit.Test
import java.util.HashMap
import java.util.HashSet
import java.util.Arrays

/**
 * ==========================================
 * ARRAY PROBLEMS: MEDIUM (1-20)
 * ==========================================
 * 
 * ULTIMATE STUDY GUIDE
 * --------------------
 * Solutions to 20 Medium Array interview questions.
 * 
 * STRUCTURE:
 * 1. PROBLEM: Full text description.
 * 2. INPUT/OUTPUT: Concrete examples.
 * 3. DESIGN: The "Why" behind the approach (e.g., Space-Time Tradeoff).
 * 4. DETAIL: Step-by-step logic walkthrough (Bulleted).
 * 5. COMPLEXITY: Time O(T) and Space O(S).
 */
class ArrayProblemsMedium {

    /**
     * 1. 3Sum
     * 
     * PROBLEM: Given an integer array nums, return all the triplets [nums[i], nums[j], nums[k]] such that i != j, i != k, and j != k, and nums[i] + nums[j] + nums[k] == 0.
     * The solution set must not contain duplicate triplets.
     * 
     * INPUT: nums = [-1,0,1,2,-1,-4]
     * OUTPUT: [[-1,-1,2],[-1,0,1]]
     * 
     * DESIGN: Sorting + Two Pointers.
     * - Sorting helps in two ways: 
     *   1. We can easily skip duplicates.
     *   2. We can use the Two Pointer "converging" technique to find pairs that sum to a target.
     * 
     * DETAIL:
     * 1. Sort the array.
     * 2. Iterate `i` from 0 to n-2.
     *    - If `i > 0` and `nums[i] == nums[i-1]`, skip to avoid duplicate triplets.
     * 3. Set `l = i + 1`, `r = n - 1`.
     * 4. While `l < r`:
     *    - `sum = nums[i] + nums[l] + nums[r]`.
     *    - If `sum == 0`: 
     *      - Add to result.
     *      - Skip duplicates for `l` (while `nums[l] == nums[l+1]`) and `r` (while `nums[r] == nums[r-1]`).
     *      - `l++`, `r--`.
     *    - If `sum < 0`: Need larger number -> `l++`.
     *    - If `sum > 0`: Need smaller number -> `r--`.
     * 
     * COMPLEXITY: Time O(n^2) | Space O(1)
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
     * 
     * PROBLEM: Given n vertical lines, find two lines that form a container with x-axis holding the most water. Return max water area.
     * 
     * INPUT: height = [1,8,6,2,5,4,8,3,7]
     * OUTPUT: 49
     * 
     * DESIGN: Two Pointers (Greedy).
     * - `Area = (r - l) * min(h[l], h[r])`.
     * - Maximize width by starting at both ends.
     * - To maximize height, we should move the POINTER pointing to the SHORTER line (hoping for a taller one). Moving the taller one is useless (width decreases, and height is bottlenecked by the short one anyway).
     * 
     * DETAIL:
     * 1. `l = 0`, `r = n-1`.
     * 2. While `l < r`:
     *    - `h = min(height[l], height[r])`.
     *    - `maxArea = max(maxArea, (r-l) * h)`.
     *    - If `height[l] < height[r]`: `l++`.
     *    - Else: `r--`.
     * 
     * COMPLEXITY: Time O(n) | Space O(1)
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
     * 3. Product of Array Except Self
     * 
     * PROBLEM: Return array where answer[i] = product of all nums except nums[i]. O(n) time, no division.
     * 
     * INPUT: nums = [1,2,3,4]
     * OUTPUT: [24,12,8,6]
     * 
     * DESIGN: Prefix & Suffix Products.
     * - `answer[i] = PrefixProduct(0..i-1) * SuffixProduct(i+1..n-1)`.
     * 
     * DETAIL:
     * 1. Create `res` array.
     * 2. Pass 1 (Left): `res[i] = res[i-1] * nums[i-1]`.
     * 3. Hold a variable `right = 1`.
     * 4. Pass 2 (Right): `res[i] = res[i] * right`. Update `right = right * nums[i]`.
     * 
     * COMPLEXITY: Time O(n) | Space O(1)
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
     * 4. Maximum Subarray
     * 
     * PROBLEM: Find continuous subarray with largest sum.
     * 
     * INPUT: [-2,1,-3,4,-1,2,1,-5,4]
     * OUTPUT: 6 ([4,-1,2,1])
     * 
     * DESIGN: Kadane's Algorithm (DP).
     * - Decision at each index: Start a new subarray here (dump the past baggage)? Or extend the current subarray?
     * 
     * DETAIL:
     * 1. `curr = 0`, `max = -Infinity`.
     * 2. Loop `n` in `nums`:
     *    - `curr = max(n, curr + n)`. (Choice: Start fresh at n vs Carry on).
     *    - `max = max(max, curr)`.
     * 
     * COMPLEXITY: Time O(n) | Space O(1)
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
     * 5. Sort Colors
     * 
     * PROBLEM: Sort array of 0s, 1s, and 2s in-place.
     * 
     * INPUT: [2,0,2,1,1,0]
     * OUTPUT: [0,0,1,1,2,2]
     * 
     * DESIGN: Dutch National Flag (3 Pointers).
     * - `low` region: [0...low-1] are 0s.
     * - `high` region: [high+1...n-1] are 2s.
     * - `mid`: Current scanner.
     * 
     * DETAIL:
     * 1. While `mid <= high`:
     *    - Case `nums[mid] == 0`: Swap `nums[low]` and `nums[mid]`. Increment `low`, `mid`.
     *    - Case `nums[mid] == 1`: Correctly placed. Increment `mid`.
     *    - Case `nums[mid] == 2`: Swap `nums[mid]` and `nums[high]`. Decrement `high`. (Do NOT increment mid, we need to check what we just swapped in).
     * 
     * COMPLEXITY: Time O(n) | Space O(1)
     */
    @Test
    fun q5_sortColors() {
        println("=== Q5: Sort Colors ===")
        val nums = intArrayOf(2, 0, 2, 1, 1, 0)
        var low = 0; var mid = 0; var high = nums.size - 1
        while (mid <= high) {
            when (nums[mid]) {
                0 -> { val t = nums[low]; nums[low] = nums[mid]; nums[mid] = t; low++; mid++ }
                1 -> mid++
                2 -> { val t = nums[high]; nums[high] = nums[mid]; nums[mid] = t; high-- }
            }
        }
        println("Result: ${nums.contentToString()}")
    }

    /**
     * 6. Merge Intervals
     * 
     * PROBLEM: Merge overlapping intervals.
     * 
     * INPUT: [[1,3],[2,6],[8,10]]
     * OUTPUT: [[1,6],[8,10]]
     * 
     * DESIGN: Sorting.
     * - If we sort by start time, overlapping intervals will be adjacent.
     * 
     * DETAIL:
     * 1. Sort intervals by start time.
     * 2. `curr = intervals[0]`. Add to result.
     * 3. Loop rest:
     *    - `next = intervals[i]`.
     *    - If `curr.end >= next.start`: Overlap! Update `curr.end = max(curr.end, next.end)`.
     *    - Else: No overlap. `curr = next`. Add `curr` to result.
     * 
     * COMPLEXITY: Time O(n log n) | Space O(n)
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
            if (curr[1] >= next[0]) curr[1] = Math.max(curr[1], next[1])
            else { curr = next; res.add(curr) }
        }
        println("Result: ${res.map { it.contentToString() }}")
    }

    /**
     * 7. Insert Interval
     * 
     * PROBLEM: Insert newInterval into sorted non-overlapping intervals. Merge if necessary.
     * 
     * INPUT: intervals = [[1,3],[6,9]], new = [2,5]
     * OUTPUT: [[1,5],[6,9]]
     * 
     * DESIGN: One-Pass Linear Scan.
     * 
     * DETAIL:
     * 1. Add all intervals ending *before* newInterval starts.
     * 2. Process overlapping intervals: While `interval.start <= new.end`, merge them: `min(start), max(end)`.
     * 3. Add the merged `newInterval`.
     * 4. Add all intervals starting *after* newInterval ends.
     * 
     * COMPLEXITY: Time O(n) | Space O(n)
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
     * 
     * PROBLEM: Rotate n x n matrix 90 degrees clockwise in-place.
     * 
     * INPUT: [[1,2],[3,4]]
     * OUTPUT: [[3,1],[4,2]]
     * 
     * DESIGN: Linear Algebra.
     * - Rotate = Transpose (Swap Rows/Cols) + Reverse Rows.
     * 
     * DETAIL:
     * 1. Transpose: Swap `matrix[i][j]` with `matrix[j][i]` for `j > i`.
     * 2. Reverse each row: Swap `row[start]` with `row[end]`.
     * 
     * COMPLEXITY: Time O(n^2) | Space O(1)
     */
    @Test
    fun q8_rotateImage() {
        println("=== Q8: Rotate Image 90 deg ===")
        val matrix = arrayOf(intArrayOf(1, 2), intArrayOf(3, 4))
        val n = matrix.size
        for (i in 0 until n) {
            for (j in i until n) { val t = matrix[i][j]; matrix[i][j] = matrix[j][i]; matrix[j][i] = t }
        }
        for (i in 0 until n) matrix[i].reverse()
        println("Result: ${matrix.map { it.contentToString() }}")
    }

    /**
     * 9. Spiral Matrix
     * 
     * PROBLEM: Traverse matrix strings in spiral order.
     * 
     * INPUT: [[1,2,3],[4,5,6],[7,8,9]]
     * OUTPUT: [1,2,3,6,9,8,7,4,5]
     * 
     * DESIGN: Boundary Simulation.
     * 
     * DETAIL:
     * 1. Maintain `top`, `bottom`, `left`, `right`.
     * 2. Loop while `top<=bot && left<=right`.
     *    - Traverse right (top row). `top++`.
     *    - Traverse down (right col). `right--`.
     *    - Check if valid still.
     *    - Traverse left (bottom row). `bot--`.
     *    - Traverse up (left col). `left++`.
     * 
     * COMPLEXITY: Time O(m*n) | Space O(1)
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
     * 
     * PROBLEM: Count subarrays where sum equals k.
     * 
     * INPUT: nums = [1,1,1], k = 2
     * OUTPUT: 2
     * 
     * DESIGN: Prefix Sum Hash Map.
     * - `Sum(i, j) = PrefixSum[j] - PrefixSum[i-1]`.
     * - If `PrefixSum[j] - PrefixSum[i-1] == k`, then `PrefixSum[i-1] = PrefixSum[j] - k`.
     * - We check how many times we've seen `CurrentSum - k` before.
     * 
     * DETAIL:
     * 1. Map `map = {0 -> 1}` (Base case).
     * 2. Loop `nums`. `sum += num`.
     * 3. `count += map[sum - k]`.
     * 4. `map[sum]++`.
     * 
     * COMPLEXITY: Time O(n) | Space O(n)
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
     * 
     * PROBLEM: Rearrange to lexicographically next greater permutation.
     * 
     * INPUT: [1,2,3] -> [1,3,2]
     * 
     * DESIGN: Algorithmic Pattern.
     * 
     * DETAIL:
     * 1. Find pivot `i` from right where `nums[i] < nums[i+1]`.
     * 2. If no pivot (i=-1), whole array is decreasing. Reverse it.
     * 3. Else, find rightmost `j` where `nums[j] > nums[i]`.
     * 4. Swap `nums[i]` and `nums[j]`.
     * 5. Reverse array from `i+1` to end.
     * 
     * COMPLEXITY: Time O(n) | Space O(1)
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
        var l = i + 1; var r = nums.size - 1
        while (l < r) { val t = nums[l]; nums[l++] = nums[r]; nums[r--] = t }
        println("Result: ${nums.contentToString()}")
    }

    /**
     * 12. Search in Rotated Sorted Array
     * 
     * PROBLEM: Search target in rotated sorted array in O(log n).
     * 
     * INPUT: [4,5,6,7,0,1,2], target 0
     * OUTPUT: 4
     * 
     * DESIGN: Binary Search.
     * - The property of rotated arrays is that at least ONE half is always sorted.
     * 
     * DETAIL:
     * 1. Calculate `mid`.
     * 2. If `nums[l] <= nums[mid]` (Left Sorted):
     *    - If target in `[l, mid]`, go Left. Else go Right.
     * 3. Else (Right Sorted):
     *    - If target in `[mid, r]`, go Right. Else go Left.
     * 
     * COMPLEXITY: Time O(log n) | Space O(1)
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
     * 13. Find First and Last Position of Element
     * 
     * PROBLEM: Find start and end indices of target in sorted array in O(log n).
     * 
     * INPUT: [5,7,7,8,8,10], target 8
     * OUTPUT: [3,4]
     * 
     * DESIGN: Double Binary Search.
     * 
     * DETAIL:
     * 1. Helper function `search(findFirst)`.
     * 2. `if nums[mid] == target`:
     *    - Record `mid`.
     *    - If finding first, go Left (`high = mid-1`) to see if there's an earlier one.
     *    - If finding last, go Right (`low = mid+1`).
     * 
     * COMPLEXITY: Time O(log n) | Space O(1)
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
     * 
     * PROBLEM: Find all unique combinations of candidates that sum to target. Reuse allowed.
     * 
     * INPUT: [2,3,6,7], target 7
     * OUTPUT: [[2,2,3],[7]]
     * 
     * DESIGN: Backtracking.
     * 
     * DETAIL:
     * 1. `dfs(index, currentTarget, path)`.
     * 2. Base case: `target == 0` -> Add path to result. `target < 0` -> Return.
     * 3. Choice 1 (Take): Add `nums[index]`. Recurse with `dfs(index, target - nums[index])` (Stay on index). Backtrack.
     * 4. Choice 2 (Skip): Recurse `dfs(index+1, target)`.
     * 
     * COMPLEXITY: Time Exponential | Space Recursion Depth.
     */
    @Test
    fun q14_combinationSum() {
        println("=== Q14: Combination Sum ===")
        val nums = intArrayOf(2, 3, 6, 7); val target = 7
        val res = mutableListOf<List<Int>>()
        fun dfs(idx: Int, t: Int, path: MutableList<Int>) {
            if (t == 0) { res.add(ArrayList(path)); return }
            if (t < 0 || idx == nums.size) return
            path.add(nums[idx])
            dfs(idx, t - nums[idx], path) // Use same index again
            path.removeAt(path.size - 1)
            dfs(idx + 1, t, path) // Skip index
        }
        dfs(0, target, mutableListOf())
        println("Result: $res")
    }

    /**
     * 15. House Robber
     * 
     * PROBLEM: Rob houses without robbing adjacent ones. Maximize amount.
     * 
     * INPUT: [1,2,3,1]
     * OUTPUT: 4
     * 
     * DESIGN: Dynamic Programming.
     * - `dp[i] = max(rob[i] + dp[i-2], skip[i] + dp[i-1])`.
     * 
     * DETAIL:
     * 1. `prev1` (dp[i-1]) and `prev2` (dp[i-2]) initialized to 0.
     * 2. Loop nums:
     *    - `curr = max(prev1, prev2 + num)`.
     *    - Shift prevs.
     * 3. Return `prev1`.
     * 
     * COMPLEXITY: Time O(n) | Space O(1)
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
     * 
     * PROBLEM: Can you reach the last index?
     * 
     * INPUT: [2,3,1,1,4]
     * OUTPUT: true
     * 
     * DESIGN: Greedy.
     * 
     * DETAIL:
     * 1. `maxReach = 0`.
     * 2. Loop `i` from 0 to n:
     *    - If `i > maxReach`: Unreachable! Return false.
     *    - `maxReach = max(maxReach, i + nums[i])`.
     * 3. Return true.
     * 
     * COMPLEXITY: Time O(n) | Space O(1)
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
     * 
     * PROBLEM: Robot moves Down or Right. Count paths to bottom-right.
     * 
     * INPUT: 3x7 grid
     * OUTPUT: 28
     * 
     * DESIGN: DP.
     * - `Paths(i,j) = Paths(i-1, j) + Paths(i, j-1)`.
     * 
     * DETAIL:
     * 1. Init `row` of size `n` with 1s (First row has 1 way).
     * 2. Loop rows `1..m`:
     *    - Loop cols `1..n`:
     *      - `row[j] += row[j-1]` (Current = Top + Left).
     * 3. Result in `row[n-1]`.
     * 
     * COMPLEXITY: Time O(m*n) | Space O(n)
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
     * 
     * PROBLEM: Longest sequence length in O(n).
     * 
     * INPUT: [100,4,200,1,3,2]
     * OUTPUT: 4
     * 
     * DESIGN: HashSet.
     * 
     * DETAIL:
     * 1. Add all to Set.
     * 2. Loop `num` in Set:
     *    - Check if `num-1` exists.
     *    - If NO, then `num` is a START of a sequence.
     *      - While `set.contains(num+1)`, increment count.
     *      - Update max.
     * 
     * COMPLEXITY: Time O(n) | Space O(n)
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
     * 
     * PROBLEM: If element is 0, set row/col to 0 in-place.
     * 
     * INPUT: [[1,1,1],[1,0,1],[1,1,1]]
     * OUTPUT: [[1,0,1],[0,0,0],[1,0,1]]
     * 
     * DESIGN: Markers.
     * - Use row 0 and col 0 as storage bits.
     * 
     * DETAIL:
     * 1. Check if col 0 has any zeros, set `col0` flag.
     * 2. Loop rest of matrix. If `M[i][j] == 0`, mark `M[i][0]` and `M[0][j]`.
     * 3. Loop BACKWARDS from bottom-right.
     *    - If markers are 0, set cell to 0.
     *    - Finally use `col0` flag for first column.
     * 
     * COMPLEXITY: Time O(m*n) | Space O(1)
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
     * 20. K-diff Pairs
     * 
     * PROBLEM: Return count of pairs with difference k.
     * 
     * INPUT: [3,1,4,1,5], k=2
     * OUTPUT: 2
     * 
     * DESIGN: Frequency Map.
     * 
     * DETAIL:
     * 1. Count frequencies.
     * 2. Loop Map:
     *    - If `k > 0`: check if `num+k` exists.
     *    - If `k == 0`: check if `freq > 1`.
     * 
     * COMPLEXITY: Time O(n) | Space O(n)
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
