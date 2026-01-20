package com.example.dsatest

import org.junit.Test
import java.util.Arrays

/**
 * ==========================================
 * ARRAYS: TOP INTERVIEW QUESTIONS & PATTERNS
 * ==========================================
 * 
 * Arrays are the bread and butter of DSA.
 * Mastering them requires mastering specific PATTERNS.
 * 
 * ==========================================
 * THE QUESTION BUCKET LIST
 * ==========================================
 * 
 * === EASY ===
 * 1. Two Sum (Map) [Implemented Below]
 * 2. Best Time to Buy and Sell Stock (One Pass) [Implemented Below]
 * 3. Contains Duplicate (Set)
 * 4. Majority Element (Moore Voting)
 * 5. Move Zeroes (Two Pointers)
 * 6. Remove Duplicates from Sorted Array (Two Pointers)
 * 7. Squares of a Sorted Array (Two Pointers)
 * 8. Pascal's Triangle
 * 9. Merge Sorted Array
 * 10. Intersection of Two Arrays
 * 
 * === MEDIUM ===
 * 1. 3Sum (Sort + Two Pointers) [Implemented Below]
 * 2. Container With Most Water (Two Pointers)
 * 3. Product of Array Except Self (Prefix/Suffix) [Implemented Below]
 * 4. Maximum Subarray (Kadane's Algo) [Implemented Below]
 * 5. Sort Colors (Dutch National Flag) [Implemented Below]
 * 6. Merge Intervals (Sorting)
 * 7. Insert Interval
 * 8. Subarray Sum Equals K (Prefix Sum Map)
 * 9. Spiral Matrix (Simulation)
 * 10. Rotate Image (Matrix)
 * 11. Next Permutation
 * 12. Search in Rotated Sorted Array
 * 13. Find First and Last Position of Element
 * 14. Combination Sum
 * 15. House Robber (DP)
 * 
 * === HARD ===
 * 1. Trapping Rain Water (Two Pointers/Prefix Max) [Implemented Below]
 * 2. First Missing Positive (Cyclic Sort)
 * 3. Largest Rectangle in Histogram (Monotonic Stack)
 * 4. Sliding Window Maximum (Deque)
 * 5. Median of Two Sorted Arrays (Binary Search)
 * 6. Merge k Sorted Lists (Heap)
 * 7. Reverse Pairs (Merge Sort)
 * 8. Count of Smaller Numbers After Self
 * 9. Split Array Largest Sum (Binary Search on Answer)
 * 10. Longest Consecutive Sequence (Set)
 */
class ArrayPatternsAndProblems {

    /**
     * ==========================================
     * PATTERN 1: TWO POINTERS (Opposite Ends)
     * ==========================================
     * Use when array is SORTED or finding pairs.
     * 
     * Problem: 3Sum
     * Find triplets that sum to 0.
     * Logic: Sort. Fix 'i', use 'left' and 'right' to find -arr[i].
     */
    @Test
    fun patternTwoPointers_3Sum() {
        println("=== PATTERN: TWO POINTERS (3Sum) ===")
        val nums = intArrayOf(-1, 0, 1, 2, -1, -4)
        nums.sort() // [-4, -1, -1, 0, 1, 2]
        val res = mutableListOf<List<Int>>()
        
        for (i in 0 until nums.size - 2) {
            if (i > 0 && nums[i] == nums[i - 1]) continue // Skip duplicates
            
            var l = i + 1
            var r = nums.size - 1
            while (l < r) {
                val sum = nums[i] + nums[l] + nums[r]
                when {
                    sum == 0 -> {
                        res.add(listOf(nums[i], nums[l], nums[r]))
                        while (l < r && nums[l] == nums[l + 1]) l++ // Skip duplicates
                        while (l < r && nums[r] == nums[r - 1]) r-- // Skip duplicates
                        l++; r--
                    }
                    sum < 0 -> l++
                    else -> r--
                }
            }
        }
        println("Result: $res")
    }

    /**
     * ==========================================
     * PATTERN 2: KADANE'S ALGORITHM (Max Subarray)
     * ==========================================
     * Use when finding Maximum Sum Subarray.
     * 
     * Logic: Carry sub-sum. If sum < 0, reset to 0. Always take max.
     */
    @Test
    fun patternKadane_MaxSubarray() {
        println("\n=== PATTERN: KADANE'S ALGO (Max Subarray) ===")
        val nums = intArrayOf(-2, 1, -3, 4, -1, 2, 1, -5, 4)
        var maxSoFar = nums[0]
        var currentSum = 0 // Or nums[0]
        
        for (num in nums) {
            // Option 1: Start new subarray at 'num'
            // Option 2: Extend existing subarray
            if (currentSum < 0) currentSum = 0 
            currentSum += num
            maxSoFar = Math.max(maxSoFar, currentSum)
        }
        println("Max Sum: $maxSoFar") // 6 (4, -1, 2, 1)
    }

    /**
     * ==========================================
     * PATTERN 3: PREFIX / SUFFIX ARRAYS
     * ==========================================
     * Use when you need info about "everything to the left/right".
     * 
     * Problem: Product of Array Except Self
     * [1, 2, 3, 4] -> [24, 12, 8, 6]
     * Logic: result[i] = (Product of Left items) * (Product of Right items)
     */
    @Test
    fun patternPrefixSuffix_ProductExceptSelf() {
        println("\n=== PATTERN: PREFIX/SUFFIX (Product Except Self) ===")
        val nums = intArrayOf(1, 2, 3, 4)
        val n = nums.size
        val res = IntArray(n)
        
        // 1. Calculate Left Products
        // res[i] contains product of 0..i-1
        res[0] = 1
        for (i in 1 until n) {
            res[i] = res[i - 1] * nums[i - 1]
        }
        
        // 2. Calculate Right Products on the fly & Multiply
        var rightProd = 1
        for (i in n - 1 downTo 0) {
            res[i] *= rightProd
            rightProd *= nums[i]
        }
        println("Result: ${res.joinToString()}")
    }

    /**
     * ==========================================
     * PATTERN 4: ONE PASS (Running Min/Max)
     * ==========================================
     * Problem: Best Time to Buy and Sell Stock
     * Logic: Track 'minPrice' so far, check profit at every step.
     */
    @Test
    fun patternOnePass_StockBuySell() {
        println("\n=== PATTERN: ONE PASS (Stock) ===")
        val prices = intArrayOf(7, 1, 5, 3, 6, 4)
        var minPrice = Int.MAX_VALUE
        var maxProfit = 0
        
        for (price in prices) {
            if (price < minPrice) {
                minPrice = price
            } else {
                maxProfit = Math.max(maxProfit, price - minPrice)
            }
        }
        println("Max Profit: $maxProfit")
    }

    /**
     * ==========================================
     * PATTERN 5: DUTCH NATIONAL FLAG (Three Pointers)
     * ==========================================
     * Problem: Sort Colors. Sort [2, 0, 2, 1, 1, 0] in place.
     * Logic: Pointers for Low (0), Mid (1), High (2).
     */
    @Test
    fun patternDNF_SortColors() {
        println("\n=== PATTERN: DUTCH NATIONAL FLAG (Sort Colors) ===")
        val nums = intArrayOf(2, 0, 2, 1, 1, 0)
        var low = 0
        var mid = 0
        var high = nums.size - 1
        
        while (mid <= high) {
            when (nums[mid]) {
                0 -> {
                    // Swap with low, increment both
                    val temp = nums[low]
                    nums[low] = nums[mid]
                    nums[mid] = temp
                    low++
                    mid++
                }
                1 -> {
                    // It's in middle, just skip
                    mid++
                }
                2 -> {
                    // Swap with high, decrement high. DON'T increment mid (check swapped val)
                    val temp = nums[high]
                    nums[high] = nums[mid]
                    nums[mid] = temp
                    high--
                }
            }
        }
        println("Result: ${nums.joinToString()}")
    }

    /**
     * ==========================================
     * PATTERN 6: PRE-COMPUTATION (Two Pass)
     * ==========================================
     * Problem: Trapping Rain Water (Hard)
     * Logic: Water[i] = Min(MaxLeft[i], MaxRight[i]) - Height[i]
     */
    @Test
    fun patternPreComputation_TrappingRainWater() {
        println("\n=== PATTERN: PRE-COMPUTATION (Trapping Rain Water) ===")
        val height = intArrayOf(0,1,0,2,1,0,1,3,2,1,2,1)
        val n = height.size
        
        // 1. Max Left Array
        val maxLeft = IntArray(n)
        maxLeft[0] = height[0]
        for (i in 1 until n) maxLeft[i] = Math.max(maxLeft[i-1], height[i])
        
        // 2. Max Right Array
        val maxRight = IntArray(n)
        maxRight[n-1] = height[n-1]
        for (i in n-2 downTo 0) maxRight[i] = Math.max(maxRight[i+1], height[i])
        
        // 3. Calc Volume
        var water = 0
        for (i in 0 until n) {
            water += Math.min(maxLeft[i], maxRight[i]) - height[i]
        }
        println("Result: $water")
    }
}
