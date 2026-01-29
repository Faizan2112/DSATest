package com.example.dsatest.generated.arraylist

import org.junit.Test
import java.util.HashMap
import java.util.HashSet

/**
 * ==========================================
 * ARRAY PROBLEMS: EASY (1-20)
 * ==========================================
 * 
 * ULTIMATE STUDY GUIDE
 * --------------------
 * Solutions to 20 Easy Array interview questions.
 * 
 * STRUCTURE:
 * 1. PROBLEM: Full text description.
 * 2. INPUT/OUTPUT: Concrete examples.
 * 3. DESIGN: The "Why" behind the approach (e.g., Space-Time Tradeoff).
 * 4. DETAIL: Step-by-step logic walkthrough (Bulleted).
 * 5. COMPLEXITY: Time O(T) and Space O(S).
 */
class ArrayProblemsEasy {

    /**
     * 1. Two Sum
     * 
     * PROBLEM: Given an array of integers nums and an integer target, return indices of the two numbers such that they add up to target.
     * You may assume that each input would have exactly one solution, and you may not use the same element twice.
     * 
     * INPUT: nums = [2,7,11,15], target = 9
     * OUTPUT: [0,1]
     * 
     * DESIGN: Hash Map (Trade Space for Time).
     * - Brute force checks every pair (O(n^2)).
     * - We use a Map to "remember" numbers we've already seen, allowing O(1) lookups.
     * 
     * DETAIL:
     * 1. Initialize an empty HashMap {Value -> Index}.
     * 2. Iterate through the array with index 'i'.
     * 3. For each 'num', calculate 'diff = target - num'.
     * 4. Check if 'diff' is already in the map.
     *    - YES: We found the pair! Return [map[diff], i].
     *    - NO: Add 'num' to the map (map[num] = i) and continue.
     * 
     * COMPLEXITY: Time O(n) | Space O(n)
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
     * 
     * PROBLEM: You are given an array prices where prices[i] is the price of a given stock on the ith day.
     * You want to maximize your profit by choosing a single day to buy one stock and choosing a different day in the future to sell that stock.
     * 
     * INPUT: prices = [7,1,5,3,6,4]
     * OUTPUT: 5 (Buy at 1, Sell at 6)
     * 
     * DESIGN: One Pass (Greedy).
     * - Visualizing the graph, we just need the deepest valley (buy) before a peak (sell).
     * - We don't need to know the future, just the minimum price "so far".
     * 
     * DETAIL:
     * 1. Initialize 'minPrice' to Integer.MAX_VALUE.
     * 2. Initialize 'maxProfit' to 0.
     * 3. Iterate through prices:
     *    - If current price < minPrice: Update minPrice (Found a cheaper buy).
     *    - Else if (price - minPrice) > maxProfit: Update maxProfit (Found a better sell).
     * 4. Return maxProfit.
     * 
     * COMPLEXITY: Time O(n) | Space O(1)
     */
    @Test
    fun q2_bestTimeStock() {
        println("=== Q2: Best Time to Buy/Sell Stock ===")
        val prices = intArrayOf(7, 1, 5, 3, 6, 4)
        var minPrice = Int.MAX_VALUE
        var maxProfit = 0
        
        for (price in prices) {
            if (price < minPrice) minPrice = price
            else maxProfit = Math.max(maxProfit, price - minPrice)
        }
        println("Result: $maxProfit")
    }

    /**
     * 3. Contains Duplicate
     * 
     * PROBLEM: Given an integer array nums, return true if any value appears at least twice in the array, and return false if every element is distinct.
     * 
     * INPUT: nums = [1,2,3,1]
     * OUTPUT: true
     * 
     * DESIGN: HashSet.
     * - Sets guarantees uniqueness.
     * - If we try to add an item that exists, it fails. That's our duplicate detector.
     * 
     * DETAIL:
     * 1. Create a HashSet.
     * 2. Iterate through each number in 'nums'.
     * 3. Try `set.add(num)`.
     *    - If it returns false, the number is already there. Return true immediately.
     * 4. If loop finishes, return false.
     * 
     * COMPLEXITY: Time O(n) | Space O(n)
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
     * 4. Majority Element
     * 
     * PROBLEM: Given an array nums of size n, return the majority element (> n / 2 times).
     * 
     * INPUT: nums = [2,2,1,1,1,2,2]
     * OUTPUT: 2
     * 
     * DESIGN: Boyer-Moore Voting Algorithm.
     * - Intuition: If we cancel out every non-majority element with a majority element, the majority element will still remain.
     * 
     * DETAIL:
     * 1. Maintain `count = 0` and `candidate = 0`.
     * 2. Iterate through `num` in `nums`:
     *    - If `count == 0`: Set `candidate = num`.
     *    - If `num == candidate`: Increment `count`.
     *    - Else: Decrement `count`.
     * 3. The remaining `candidate` is the answer.
     * 
     * COMPLEXITY: Time O(n) | Space O(1)
     */
    @Test
    fun q4_majorityElement() {
        println("=== Q4: Majority Element ===")
        val nums = intArrayOf(2, 2, 1, 1, 1, 2, 2)
        var count = 0
        var candidate = 0
        
        for (num in nums) {
            if (count == 0) candidate = num
            count += if (num == candidate) 1 else -1
        }
        println("Result: $candidate")
    }

    /**
     * 5. Move Zeroes
     * 
     * PROBLEM: Given an integer array nums, move all 0's to the end of it while maintaining the relative order of the non-zero elements. Do it in-place.
     * 
     * INPUT: nums = [0,1,0,3,12]
     * OUTPUT: [1,3,12,0,0]
     * 
     * DESIGN: Two Pointers (Writer Index).
     * - We don't need to "swap" 0s explicitly. We just need to compress all non-zeros to the front, and fill the rest with 0.
     * 
     * DETAIL:
     * 1. `insertPos = 0`. This tracks where the NEXT non-zero number goes.
     * 2. Iterate `num` in `nums`.
     *    - If `num != 0`: Write `nums[insertPos] = num`, then `insertPos++`.
     * 3. After the loop, fill from `insertPos` to `nums.size` with `0`.
     * 
     * COMPLEXITY: Time O(n) | Space O(1)
     */
    @Test
    fun q5_moveZeroes() {
        println("=== Q5: Move Zeroes ===")
        val nums = intArrayOf(0, 1, 0, 3, 12)
        var insertPos = 0
        
        for (num in nums) if (num != 0) nums[insertPos++] = num
        while (insertPos < nums.size) nums[insertPos++] = 0
        
        println("Result: ${nums.contentToString()}")
    }

    /**
     * 6. Remove Duplicates from Sorted Array
     * 
     * PROBLEM: Remove duplicates in-place from sorted array such that each unique element appears only once. Relative order kept same. Return k (count of unique).
     * 
     * INPUT: nums = [1,1,2]
     * OUTPUT: k=2, nums=[1,2,_]
     * 
     * DESIGN: Two Pointers (Slow & Fast).
     * - Since array is sorted, duplicates are always adjacent.
     * - `i` (Slow) builds the unique array. `j` (Fast) scans.
     * 
     * DETAIL:
     * 1. `i = 0` (Points to last unique element found).
     * 2. Loop `j` from 1 to end:
     *    - If `nums[j] != nums[i]` (New unique number found):
     *      - Increment `i`.
     *      - Update `nums[i] = nums[j]`.
     * 3. Return `i + 1` (Length).
     * 
     * COMPLEXITY: Time O(n) | Space O(1)
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
        println("Length: ${i + 1}, Prefix: ${nums.take(i+1)}")
    }

    /**
     * 7. Squares of a Sorted Array
     * 
     * PROBLEM: Given an integer array nums sorted in non-decreasing order, return an array of the squares of each number sorted in non-decreasing order.
     * 
     * INPUT: nums = [-4,-1,0,3,10]
     * OUTPUT: [0,1,9,16,100]
     * 
     * DESIGN: Two Pointers (Converging).
     * - The largest squares are at the extremes (large negative or large positive).
     * - The smallest square (0) is somewhere in the middle.
     * - It's easier to build the result from Back (largest) to Front.
     * 
     * DETAIL:
     * 1. `l = 0`, `r = n-1`.
     * 2. `k = n-1` (Points to end of result array).
     * 3. While `l <= r`:
     *    - Compare `abs(nums[l])` vs `abs(nums[r])`.
     *    - If Left is larger: Square left, put at `res[k]`, `l++`.
     *    - Else: Square right, put at `res[k]`, `r--`.
     *    - `k--`.
     * 
     * COMPLEXITY: Time O(n) | Space O(n)
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
     * 
     * PROBLEM: Return the first numRows of Pascal's triangle. Each number is the sum of the two numbers directly above it.
     * 
     * INPUT: numRows = 5
     * OUTPUT: [[1],[1,1],[1,2,1],[1,3,3,1],[1,4,6,4,1]]
     * 
     * DESIGN: Simulation/DP.
     * - Row `i` depends on Row `i-1`.
     * 
     * DETAIL:
     * 1. Outer loop `i` from 0 to numRows-1.
     * 2. Inner loop `j` from 0 to `i`.
     *    - If `j == 0` or `j == i` (Edges): Value is 1.
     *    - Else: `Value = PrevRow[j-1] + PrevRow[j]`.
     * 3. Add row to result list.
     * 
     * COMPLEXITY: Time O(n^2) | Space O(n^2)
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
     * 
     * PROBLEM: Merge nums2 into nums1 as one sorted array. nums1 has size m+n (buffer at end).
     * 
     * INPUT: nums1 = [1,2,3,0,0,0], m=3, nums2 = [2,5,6], n=3
     * OUTPUT: [1,2,2,3,5,6]
     * 
     * DESIGN: Two Pointers (Backwards).
     * - If we start from front, we overwrite needed data in nums1.
     * - By filling from the back (largest elements first), we use the empty buffer safely.
     * 
     * DETAIL:
     * 1. `i = m-1` (End of valid nums1), `j = n-1` (End of nums2).
     * 2. `k = m+n-1` (End of buffer).
     * 3. Loop while `j >= 0` (While nums2 has elements):
     *    - If `nums1[i] > nums2[j]`: Put `nums1[i]` at `k`.
     *    - Else: Put `nums2[j]` at `k`.
     *    - Decrement `k` and the chosen pointer.
     * 
     * COMPLEXITY: Time O(m+n) | Space O(1)
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
     * 
     * PROBLEM: Return intersection of nums1 and nums2. Each element in result must appear as many times as it shows in both arrays.
     * 
     * INPUT: nums1 = [1,2,2,1], nums2 = [2,2]
     * OUTPUT: [2,2]
     * 
     * DESIGN: Frequency Map.
     * - We need to match counts.
     * 
     * DETAIL:
     * 1. Count frequencies of `nums1` into a HashMap.
     * 2. Iterate `nums2`.
     *    - If `map[num] > 0`:
     *      - Add `num` to result.
     *      - Decrement `map[num]`.
     * 
     * COMPLEXITY: Time O(m+n) | Space O(min(m,n))
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
     * 
     * PROBLEM: Given array containing n distinct numbers in range [0, n], return the missing number.
     * 
     * INPUT: nums = [3,0,1]
     * OUTPUT: 2
     * 
     * DESIGN: XOR Trick.
     * - Property: `A ^ A = 0` and `A ^ 0 = A`.
     * - If we XOR all indices [0..n] and all values [nums], the matching pairs cancel out. The one remaining is the missing index.
     * 
     * DETAIL:
     * 1. `xor = 0`.
     * 2. XOR `xor` with all `i` from 0 to n.
     * 3. XOR `xor` with all `num` in `nums`.
     * 4. Result is `xor`.
     * 
     * COMPLEXITY: Time O(n) | Space O(1)
     */
/*
    this code, the XOR (xor) operator is used to find the missing number by exploiting three mathematical properties of bitwise XOR:
    1.
    $A \oplus A = 0$: Any number XORed with itself results in 0.
    2.
    $A \oplus 0 = A$: Any number XORed with 0 remains unchanged.
    3.
    Commutative & Associative: The order in which you XOR numbers doesn't matter ($A \oplus B \oplus C$ is the same as $C \oplus A \oplus B$).
    How it works in the "Missing Number" problem:
    If you have an array of size $n$ containing numbers from $0$ to $n$ with one missing, you essentially have two sets of numbers:
    1.
    The Complete Set: Every number from $0$ to $n$.
    2.
    The Actual Set: Every number in your nums array.
    If you XOR everything together, every number that appears in both sets will pair up and cancel itself out to $0$. The only number left standing is the one that didn't have a pair—the missing number.
    Step-by-Step Dry Run
    Using your input: nums = [3, 0, 1] (Size is 3, so expected numbers are 0, 1, 2, 3).
    1. The Setup
    var xor = 0
    2. First Loop: XOR all numbers that should be there (0 to $n$)
    Kotlin
    for (i in 0..nums.size) xor = xor xor i
    •
    0 ^ 0 = 0
    •
    0 ^ 1 = 1
    •
    1 ^ 2 = 3 (binary 01 ^ 10 = 11)
    •
    3 ^ 3 = 0 (binary 11 ^ 11 = 00)
    •
    Result after loop: 0 (This is just $0 \oplus 1 \oplus 2 \oplus 3$)
    3. Second Loop: XOR all numbers that are actually in the array
    Kotlin
    for (n in nums) xor = xor xor n
    Current xor is $0 \oplus 1 \oplus 2 \oplus 3$. Now we XOR it with 3, 0, 1:
    •
    xor = (0 ^ 1 ^ 2 ^ 3) ^ 3 ^ 0 ^ 1
    4. The Magic (Reordering)
    Because XOR is commutative, we can rearrange the operation:
    •
    xor = (0 ^ 0) ^ (1 ^ 1) ^ (3 ^ 3) ^ 2
    •
    xor = 0 ^ 0 ^ 0 ^ 2
    •
    xor = 2
    The result is 2, which is the missing number.

    Why use this instead of Summation?
While you could also find the missing number using the sum formula ($Sum_{expected} - Sum_{actual}$), XOR is often preferred because:
1.
No Overflow: Adding very large numbers can exceed the maximum value of an Integer. XOR never overflows; it only flips bits.
2.
Performance: Bitwise operations are extremely fast at the hardware level.

    */
    @Test
    fun q11_missingNumber() {
        println("=== Q11: Missing Number ===")
        val nums = intArrayOf(3, 0, 1)
        var xor = 0
        for (i in 0..nums.size) xor = xor xor i
        for (n in nums) xor = xor xor n
        println("Result: $xor")
    }

    /**
     * 12. Single Number
     * 
     * PROBLEM: Every element appears twice except for one. Find that single one.
     * 
     * INPUT: nums = [4,1,2,1,2]
     * OUTPUT: 4
     * 
     * DESIGN: XOR.
     * - Same logic as Missing Number. `(4^1^2^1^2)` -> `4^(1^1)^(2^2)` -> `4^0^0` -> `4`.
     * 
     * DETAIL:
     * 1. Initialize `res = 0`.
     * 2. Loop through `nums`, `res = res ^ num`.
     * 3. Return `res`.
     * 
     * COMPLEXITY: Time O(n) | Space O(1)
     */
    @Test
    fun q12_singleNumber() {
        println("=== Q12: Single Number ===")
        val nums = intArrayOf(4, 1, 2, 1, 2)
        var res = 0
        for (n in nums) res = res xor n
        println("Result: $res")
    }

    /**
     * 13. Plus One
     * 
     * PROBLEM: Increment large integer represented as array of digits.
     * 
     * INPUT: digits = [1,2,9]
     * OUTPUT: [1,3,0]
     * 
     * DESIGN: Schoolbook Addition with Carry.
     * 
     * DETAIL:
     * 1. Iterate from end (right) to start (left).
     * 2. If `digit < 9`: Increment it and RETURN immediately (no further carry).
     * 3. If `digit == 9`: Set it to `0` representing a carry over, and continue loop.
     * 4. If loop finishes (e.g. [9,9,9] -> [0,0,0]), it implies we need an extra digit.
     * 5. Create new array of size `n+1`, sets first digit to `1`.
     * 
     * COMPLEXITY: Time O(n) | Space O(1)
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
        val res = IntArray(digits.size + 1).apply { this[0] = 1 }
        println("Result: ${res.contentToString()}")
    }

    /**
     * 14. Find Pivot Index
     * 
     * PROBLEM: Find index where Sum(Left) == Sum(Right).
     * 
     * INPUT: nums = [1,7,3,6,5,6]
     * OUTPUT: 3
     * 
     * DESIGN: Total Sum vs Running Sum.
     * - We know `TotalSum = LeftSum + PivotVal + RightSum`.
     * - So `RightSum = TotalSum - LeftSum - PivotVal`.
     * - Condition becomes: `LeftSum == TotalSum - LeftSum - PivotVal`.
     * 
     * DETAIL:
     * 1. Calculate `total`.
     * 2. Iterate `i`, maintaining `leftSum`.
     * 3. Check condition. If true, return `i`.
     * 4. Add `nums[i]` to `leftSum`.
     * 
     * COMPLEXITY: Time O(n) | Space O(1)
     */
    @Test
    fun q14_pivotIndex() {
        println("=== Q14: Pivot Index ===")
        val nums = intArrayOf(1, 7, 3, 6, 5, 6)
        val total = nums.sum()
        var leftSum = 0
        var pivot = -1
        
        for ((i, num) in nums.withIndex()) {
            if (leftSum == (total - leftSum - num)) {
                pivot = i; break
            }
            leftSum += num
        }
        println("Result: $pivot")
    }

    /**
     * 15. Running Sum of 1d Array
     * 
     * PROBLEM: runningSum[i] = sum(nums[0]…nums[i]).
     * 
     * INPUT: nums = [1,2,3,4]
     * OUTPUT: [1,3,6,10]
     * 
     * DESIGN: In-place Prefix Sum.
     * 
     * DETAIL:
     * 1. Iterate from index 1.
     * 2. `nums[i] += nums[i-1]`.
     * 
     * COMPLEXITY: Time O(n) | Space O(1)
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
     * 
     * PROBLEM: Remove all occurrences of val in-place. Return new length.
     * 
     * INPUT: nums = [3,2,2,3], val = 3
     * OUTPUT: 2
     * 
     * DESIGN: Two Pointers (Filter).
     * - Similar to Move Zeroes.
     * 
     * DETAIL:
     * 1. `i = 0` (Writer).
     * 2. Loop `n` in `nums`:
     *    - If `n != val`: Write `nums[i] = n`, increment `i`.
     * 3. Return `i`.
     * 
     * COMPLEXITY: Time O(n) | Space O(1)
     */
    @Test
    fun q16_removeElement() {
        println("=== Q16: Remove Element ===")
        val nums = intArrayOf(3, 2, 2, 3); val `val` = 3
        var i = 0
        for (n in nums) {
            if (n != `val`) nums[i++] = n
        }
        println("New Length: $i")
    }

    /**
     * 17. Search Insert Position
     * 
     * PROBLEM: Return index if target is found. If not, return index where it would be.
     * 
     * INPUT: nums = [1,3,5,6], target = 5
     * OUTPUT: 2
     * 
     * DESIGN: Binary Search.
     * - We need O(log n).
     * 
     * DETAIL:
     * 1. Standard Binary Search.
     * 2. If `nums[mid] == target`, return `mid`.
     * 3. If `nums[mid] < target`, `l = mid + 1`.
     * 4. Else `r = mid - 1`.
     * 5. If not found, `l` (low) will be pointing to the correct insertion index.
     * 
     * COMPLEXITY: Time O(log n) | Space O(1)
     */
    @Test
    fun q17_searchInsert() {
        println("=== Q17: Search Insert Position ===")
        val nums = intArrayOf(1, 3, 5, 6); val target = 5
        var l = 0; var r = nums.size - 1
        
        while (l <= r) {
            val mid = l + (r - l) / 2
            if (nums[mid] == target) { l = mid; break }
            else if (nums[mid] < target) l = mid + 1
            else r = mid - 1
        }
        println("Result Index: $l")
    }

    /**
     * 18. Third Maximum Number
     * 
     * PROBLEM: Return the third distinct maximum number. If non-existent, return max.
     * 
     * INPUT: [3, 2, 1]
     * OUTPUT: 1
     * 
     * DESIGN: Three Variables.
     * - Maintain `max1 > max2 > max3`.
     * 
     * DETAIL:
     * 1. Use `Long.MIN_VALUE` to handle Integer edge cases.
     * 2. Iterate nums. Skip duplicates.
     * 3. Cascade logic:
     *    - If > max1: Shift all down (max3=max2, max2=max1, max1=num).
     *    - Else if > max2: Shift max2 down.
     *    - Else if > max3: Update max3.
     * 
     * COMPLEXITY: Time O(n) | Space O(1)
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
     * 
     * PROBLEM: Calculate F(n) = F(n-1) + F(n-2).
     * 
     * INPUT: n = 4
     * OUTPUT: 3
     * 
     * DESIGN: Iterative DP (Space Optimized).
     * - We don't need the whole array, just previous 2 numbers.
     * 
     * DETAIL:
     * 1. `a=0, b=1`.
     * 2. Iterate 2 to n.
     * 3. `sum = a + b`, shift `a=b`, `b=sum`.
     * 
     * COMPLEXITY: Time O(n) | Space O(1)
     */
    @Test
    fun q19_fibonacci() {
        println("=== Q19: Fibonacci ===")
        val n = 4
        if (n <= 1) { println(n); return }
        var a = 0; var b = 1
        
        for (i in 2..n) {
            val sum = a + b
            a = b
            b = sum
        }
        println("Result: $b")
    }

    /**
     * 20. Monotonic Array
     * 
     * PROBLEM: Return true if array is monotone increasing or decreasing.
     * 
     * INPUT: [1,2,2,3]
     * OUTPUT: true
     * 
     * DESIGN: Flags.
     * 
     * DETAIL:
     * 1. Assume `increasing = true` and `decreasing = true`.
     * 2. Iterate pairs.
     *    - If `nums[i] > nums[i+1]`: It's NOT increasing. Set `inc = false`.
     *    - If `nums[i] < nums[i+1]`: It's NOT decreasing. Set `dec = false`.
     * 3. Result is `inc || dec`.
     * 
     * COMPLEXITY: Time O(n) | Space O(1)
     */
    @Test
    fun q20_monotonicArray() {
        println("=== Q20: Monotonic Array ===")
        val nums = intArrayOf(1, 2, 2, 3)
        var inc = true
        var dec = true
        
        for (i in 0 until nums.size - 1) {
            if (nums[i] > nums[i + 1]) inc = false
            if (nums[i] < nums[i + 1]) dec = false
        }
        println("Result: ${inc || dec}")
    }
}
