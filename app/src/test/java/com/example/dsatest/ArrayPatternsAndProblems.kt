package com.example.dsatest

import org.junit.Test
import java.util.Arrays

/**
 * ==========================================
 * ARRAYS: THE ARCHITECTURE OF SEQUENCES
 * ==========================================
 * Design & Detail Logic.
 * 
 * "Design" = Choosing the right access pattern (Pointers, Window, Pre-computation).
 * "Detail" = Managing indices without Off-By-One errors.
 * 
 * === PATTERN CHEAT SHEET (ASCII DESIGN) ===
 * 
 * 1. TWO POINTERS (Converging)
 *    [ L -> . . . . . <- R ]
 *    Use: Sorted Arrays, Pairs, Reversing.
 * 
 * 2. SLIDING WINDOW (Fixed/Dynamic)
 *    [ . . { L . . . R } . . ] -> Move R, then shrink L.
 *    Use: Subarrays, Substrings.
 * 
 * 3. PRE-COMPUTATION (Prefix Sums)
 *    Pre: [ A, A+B, A+B+C, ... ]
 *    Use: Range Sum Queries O(1).
 */
class ArrayPatternsAndProblems {

    // ==========================================
    // 1. TWO POINTERS (Converging)
    // ==========================================
    // DESIGN: Reduce O(n^2) to O(n) by moving from both ends.
    // LOGIC: If Sum < Target, move Left (increase). If Sum > Target, move Right (decrease).
    @Test
    fun visualizeTwoPointers() {
        println("\n=== VISUALIZATION: TWO POINTERS ===")
        val nums = intArrayOf(1, 2, 4, 7, 11, 15)
        val target = 15
        
        println("Array: ${nums.contentToString()}")
        println("Target: $target")
        
        var l = 0
        var r = nums.size - 1
        
        while(l < r) {
            val sum = nums[l] + nums[r]
            // Visualizing the pointers
            val visual = CharArray(nums.size * 3) { ' ' } // Rough spacing
            visual[l * 3] = 'L'
            visual[r * 3] = 'R'
            print("[${String(visual).trimEnd()}] Sum: $sum")
            
            if(sum == target) {
                println(" -> FOUND! Indices: $l, $r")
                return
            } else if(sum < target) {
                println(" -> Too Small (L++)")
                l++
            } else {
                println(" -> Too Big (R--)")
                r--
            }
        }
    }

    // ==========================================
    // 2. SLIDING WINDOW (Dynamic)
    // ==========================================
    // DESIGN: Expand 'Right' to include valid. Shrink 'Left' to optimize/validate.
    // LOGIC: Maximize window size where Sum <= k.
    @Test
    fun visualizeSlidingWindow() {
        println("\n=== VISUALIZATION: SLIDING WINDOW ===")
        val nums = intArrayOf(2, 1, 5, 1, 3, 2)
        val limit = 7
        println("Array: ${nums.contentToString()} (Max Sum <= $limit)")
        
        var l = 0
        var currentSum = 0
        var maxLen = 0
        
        for(r in nums.indices) {
            currentSum += nums[r]
            
            // Visualization
            print("Win [$l..$r] Sum: $currentSum")
            
            while(currentSum > limit) {
                print(" -> Shrink L ($currentSum > $limit)")
                currentSum -= nums[l]
                l++
            }
            
            val valid = if(currentSum <= limit) "Valid" else "Invalid"
            println(" -> $valid (Len: ${r - l + 1})")
            maxLen = Math.max(maxLen, r - l + 1)
        }
        println("Max Length: $maxLen")
    }

    // ==========================================
    // 3. PREFIX SUM (Pre-computation)
    // ==========================================
    // DESIGN: Convert "Range Query" from O(n) to O(1).
    // LOGIC: Sum[i, j] = Prefix[j] - Prefix[i-1].
    // Visual: Vertical accumulation stack.
    @Test
    fun visualizePrefixSum() {
        println("\n=== VISUALIZATION: PREFIX SUM ===")
        val nums = intArrayOf(3, 1, 4, 2)
        val prefix = IntArray(nums.size)
        
        var running = 0
        println("Index | Num | Prefix Sum (History)")
        println("------+-----+---------------------")
        for(i in nums.indices) {
            running += nums[i]
            prefix[i] = running
            // Fancy alignment
            println(String.format("  %d   |  %d  |  %d", i, nums[i], running))
        }
        
        println("\nQuery Sum(1, 3) -> Indices [1..3] (Values: 1, 4, 2)")
        println("Formula: Prefix[3] - Prefix[0]")
        println("Calc: ${prefix[3]} - ${prefix[0]} = ${prefix[3] - prefix[0]}")
    }

    // ==========================================
    // 4. KADANE'S ALGORITHM (Visualized)
    // ==========================================
    // DESIGN: "Should I start new, or join previous?"
    // LOGIC: Max(Current, Current + PrevSum).
    @Test
    fun visualizeKadane() {
        println("\n=== VISUALIZATION: KADANE'S ALGO ===")
        val nums = intArrayOf(-2, 1, -3, 4, -1, 2, 1, -5, 4)
        println("Array: ${nums.contentToString()}")
        
        var currentSum = 0
        var maxSoFar = Int.MIN_VALUE
        
        println("Step | Num | CurSum | Decison")
        println("-----+-----+--------+--------")
        
        for(n in nums) {
            val prev = currentSum
            // Decision: Start New (n) vs Extend (prev + n)
            if(n > prev + n) {
                currentSum = n
                println(String.format(" %2d  | %2d  |   %2d   | Start New", n, n, currentSum))
            } else {
                currentSum = prev + n
                println(String.format(" %2d  | %2d  |   %2d   | Extend", n, n, currentSum))
            }
            maxSoFar = Math.max(maxSoFar, currentSum)
        }
        println("Result: $maxSoFar")
    }
}
