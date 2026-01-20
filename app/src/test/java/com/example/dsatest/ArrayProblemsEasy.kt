package com.example.dsatest

import org.junit.Test
import java.util.HashMap
import java.util.HashSet
import java.util.Arrays

/**
 * ==========================================
 * ARRAY PROBLEMS: EASY (1-20)
 * ==========================================
 * 
 * Solutions to 20 Easy Array interview questions.
 */
class ArrayProblemsEasy {

    /**
     * 1. Two Sum
     * Logic: Map<Value, Index>. Check if (target - num) exists.
     * Time: O(n), Space: O(n)
     */
    @Test
    fun q1_twoSum() {
        println("=== Q1: Two Sum ===")
        val nums = intArrayOf(2, 7, 11, 15); val target = 9
        val map = HashMap<Int, Int>()
        var res = intArrayOf()
        for ((i, num) in nums.withIndex()) {
            val diff = target - num
            if (map.containsKey(diff)) {
                res = intArrayOf(map[diff]!!, i)
                break
            }
            map[num] = i
        }
        println("Result: ${res.contentToString()}")
    }

    /**
     * 2. Best Time to Buy and Sell Stock
     * Logic: Track minPrice and maxProfit.
     * Time: O(n), Space: O(1)
     */
    @Test
    fun q2_bestTimeStock() {
        println("=== Q2: Best Time to Buy/Sell Stock ===")
        val prices = intArrayOf(7, 1, 5, 3, 6, 4)
        var min = Int.MAX_VALUE; var maxP = 0
        for (p in prices) {
            if (p < min) min = p
            else maxP = Math.max(maxP, p - min)
        }
        println("Result: $maxP")
    }

    /**
     * 3. Contains Duplicate
     * Logic: HashSet.
     * Time: O(n), Space: O(n)
     */
    @Test
    fun q3_containsDuplicate() {
        println("=== Q3: Contains Duplicate ===")
        val nums = intArrayOf(1, 2, 3, 1)
        val set = HashSet<Int>()
        var res = false
        for (n in nums) {
            if (!set.add(n)) { res = true; break }
        }
        println("Result: $res")
    }

    /**
     * 4. Majority Element (> n/2)
     * Logic: Moore Voting Algorithm.
     * Time: O(n), Space: O(1)
     */
    @Test
    fun q4_majorityElement() {
        println("=== Q4: Majority Element ===")
        val nums = intArrayOf(2, 2, 1, 1, 1, 2, 2)
        var count = 0; var candidate = 0
        for (num in nums) {
            if (count == 0) candidate = num
            count += if (num == candidate) 1 else -1
        }
        println("Result: $candidate")
    }

    /**
     * 5. Move Zeroes
     * Logic: Two pointers. Shift non-zeros to front. Fill rest with 0.
     * Time: O(n), Space: O(1)
     */
    @Test
    fun q5_moveZeroes() {
        println("=== Q5: Move Zeroes ===")
        val nums = intArrayOf(0, 1, 0, 3, 12)
        var insertPos = 0
        for (num in nums) {
            if (num != 0) nums[insertPos++] = num
        }
        while (insertPos < nums.size) nums[insertPos++] = 0
        println("Result: ${nums.contentToString()}")
    }

    /**
     * 6. Remove Duplicates from Sorted Array
     * Logic: Two pointers. Overwrite unique elements at front.
     */
    @Test
    fun q6_removeDuplicates() {
        println("=== Q6: Remove Duplicates (Sorted) ===")
        val nums = intArrayOf(1, 1, 2)
        if (nums.isEmpty()) return
        var i = 0
        for (j in 1 until nums.size) {
            if (nums[j] != nums[i]) {
                i++
                nums[i] = nums[j]
            }
        }
        println("Length: ${i + 1}, Arr: ${nums.contentToString()}")
    }

    /**
     * 7. Squares of a Sorted Array
     * Logic: Two pointers from ends (largest abs values). Fill result from back.
     */
    @Test
    fun q7_sortedSquares() {
        println("=== Q7: Squares of Sorted Array ===")
        val nums = intArrayOf(-4, -1, 0, 3, 10)
        val n = nums.size
        val res = IntArray(n)
        var l = 0; var r = n - 1; var k = n - 1
        while (l <= r) {
            if (Math.abs(nums[l]) > Math.abs(nums[r])) {
                res[k--] = nums[l] * nums[l]
                l++
            } else {
                res[k--] = nums[r] * nums[r]
                r--
            }
        }
        println("Result: ${res.contentToString()}")
    }

    /**
     * 8. Pascal's Triangle
     * Logic: Row[i][j] = Row[i-1][j-1] + Row[i-1][j].
     */
    @Test
    fun q8_pascalsTriangle() {
        println("=== Q8: Pascal's Triangle ===")
        val numRows = 5
        val res = ArrayList<List<Int>>()
        for (i in 0 until numRows) {
            val row = ArrayList<Int>()
            for (j in 0..i) {
                if (j == 0 || j == i) row.add(1)
                else row.add(res[i - 1][j - 1] + res[i - 1][j])
            }
            res.add(row)
        }
        println("Result: $res")
    }

    /**
     * 9. Merge Sorted Array
     * Logic: Fill from BACK to avoid overwriting.
     */
    @Test
    fun q9_mergeSortedArray() {
        println("=== Q9: Merge Sorted Array ===")
        val nums1 = intArrayOf(1, 2, 3, 0, 0, 0); val m = 3
        val nums2 = intArrayOf(2, 5, 6); val n = 3
        var i = m - 1; var j = n - 1; var k = m + n - 1
        while (j >= 0) {
            if (i >= 0 && nums1[i] > nums2[j]) nums1[k--] = nums1[i--]
            else nums1[k--] = nums2[j--]
        }
        println("Result: ${nums1.contentToString()}")
    }

    /**
     * 10. Intersection of Two Arrays II
     * Logic: Frequency Map or Sorting.
     */
    @Test
    fun q10_intersection() {
        println("=== Q10: Intersection II ===")
        val nums1 = intArrayOf(1, 2, 2, 1); val nums2 = intArrayOf(2, 2)
        val map = HashMap<Int, Int>()
        for (n in nums1) map[n] = map.getOrDefault(n, 0) + 1
        val res = ArrayList<Int>()
        for (n in nums2) {
            if (map.getOrDefault(n, 0) > 0) {
                res.add(n)
                map[n] = map[n]!! - 1
            }
        }
        println("Result: $res")
    }

    /**
     * 11. Missing Number
     * Logic: Sum(0..n) - Sum(arr) OR XOR.
     */
    @Test
    fun q11_missingNumber() {
        println("=== Q11: Missing Number ===")
        val nums = intArrayOf(3, 0, 1)
        var xor = 0
        for (i in 0..nums.size) xor = xor xor i // XOR all expected
        for (n in nums) xor = xor xor n // Xor actual
        println("Result: $xor")
    }

    /**
     * 12. Single Number
     * Logic: XOR. a^a=0.
     */
    @Test
    fun q12_singleNumber() {
        println("=== Q12: Single Number ===")
        val nums = intArrayOf(2, 2, 1)
        var res = 0
        for (n in nums) res = res xor n
        println("Result: $res")
    }

    /**
     * 13. Plus One
     * Logic: Handle carry. [9, 9] -> [1, 0, 0].
     */
    @Test
    fun q13_plusOne() {
        println("=== Q13: Plus One ===")
        val digits = intArrayOf(1, 2, 9)
        for (i in digits.indices.reversed()) {
            if (digits[i] < 9) {
                digits[i]++
                println("Result: ${digits.contentToString()}")
                return
            }
            digits[i] = 0
        }
        // If all 9s, create new array
        val res = IntArray(digits.size + 1).apply { this[0] = 1 }
        println("Result: ${res.contentToString()}")
    }

    /**
     * 14. Find Pivot Index (LeftSum == RightSum)
     * Logic: TotalSum. Iterate and check sum so far.
     */
    @Test
    fun q14_pivotIndex() {
        println("=== Q14: Pivot Index ===")
        val nums = intArrayOf(1, 7, 3, 6, 5, 6)
        val total = nums.sum()
        var left = 0
        var pivot = -1
        for ((i, num) in nums.withIndex()) {
            if (left == (total - left - num)) {
                pivot = i; break
            }
            left += num
        }
        println("Result: $pivot")
    }

    /**
     * 15. Running Sum of 1d Array
     * Logic: nums[i] += nums[i-1].
     */
    @Test
    fun q15_runningSum() {
        println("=== Q15: Running Sum ===")
        val nums = intArrayOf(1, 2, 3, 4)
        for (i in 1 until nums.size) nums[i] += nums[i - 1]
        println("Result: ${nums.contentToString()}")
    }

    /**
     * 16. Remove Element
     * Logic: Two pointers. Skip val.
     */
    @Test
    fun q16_removeElement() {
        println("=== Q16: Remove Element ===")
        val nums = intArrayOf(3, 2, 2, 3); val `val` = 3
        var i = 0
        for (n in nums) {
            if (n != `val`) nums[i++] = n
        }
        println("Length: $i")
    }

    /**
     * 17. Search Insert Position
     * Logic: Binary Search (Lower Bound).
     */
    @Test
    fun q17_searchInsert() {
        println("=== Q17: Search Insert Position ===")
        val nums = intArrayOf(1, 3, 5, 6); val target = 5
        var l = 0; var r = nums.size - 1
        while (l <= r) {
            val mid = l + (r - l) / 2
            if (nums[mid] == target) { l = mid; break } // Actually just return mid
            else if (nums[mid] < target) l = mid + 1
            else r = mid - 1
        }
        println("Result Index: $l")
    }

    /**
     * 18. Third Maximum Number
     * Logic: Track 3 max variables.
     */
    @Test
    fun q18_thirdMax() {
        println("=== Q18: Third Max ===")
        val nums = intArrayOf(3, 2, 1)
        var max1: Long = Long.MIN_VALUE
        var max2: Long = Long.MIN_VALUE
        var max3: Long = Long.MIN_VALUE
        for (n in nums) {
            val num = n.toLong()
            if (num == max1 || num == max2 || num == max3) continue
            if (num > max1) { max3=max2; max2=max1; max1=num }
            else if (num > max2) { max3=max2; max2=num }
            else if (num > max3) { max3=num }
        }
        val res = if (max3 == Long.MIN_VALUE) max1 else max3
        println("Result: $res")
    }

    /**
     * 19. Fibonacci Number
     * Logic: Iterative O(n).
     */
    @Test
    fun q19_fibonacci() {
        println("=== Q19: Fibonacci ===")
        val n = 4
        if (n <= 1) { println(n); return }
        var a = 0; var b = 1
        for (i in 2..n) {
            val sum = a + b; a = b; b = sum
        }
        println("Result: $b")
    }

    /**
     * 20. Monotonic Array
     * Logic: Check if increasing or decreasing.
     */
    @Test
    fun q20_monotonicArray() {
        println("=== Q20: Monotonic Array ===")
        val nums = intArrayOf(1, 2, 2, 3)
        var inc = true; var dec = true
        for (i in 0 until nums.size - 1) {
            if (nums[i] > nums[i + 1]) inc = false
            if (nums[i] < nums[i + 1]) dec = false
        }
        println("Result: ${inc || dec}")
    }
}
