package com.example.dsatest

import org.junit.Test

/**
 * ==========================================
 * SEARCHING & BIT MANIPULATION
 * ==========================================
 * 
 * Two "Secret Weapons" in DSA.
 * 
 * 1. BINARY SEARCH (O(log n)):
 *    - Not just for arrays! Use it on "Answer Spaces".
 *    - Key variants: Lower Bound, Upper Bound.
 * 
 * 2. BIT MANIPULATION (O(1)):
 *    - The fastest operations possible.
 *    - Key tricks: XOR, Check Power of 2, Set/Unset bits.
 */
class BinarySearchAndBitMasking {

    /**
     * ==========================================
     * 1. BINARY SEARCH (Standard)
     * ==========================================
     * REQUIREMENT: Input must be SORTED.
     */
    @Test
    fun binarySearchStandard() {
        println("=== BINARY SEARCH (Standard) ===")
        val arr = intArrayOf(1, 3, 5, 9, 12, 15)
        val target = 9
        
        var left = 0
        var right = arr.size - 1
        var ans = -1
        
        while (left <= right) {
            val mid = left + (right - left) / 2 // Prevent overflow
            if (arr[mid] == target) {
                ans = mid
                break
            } else if (arr[mid] < target) {
                left = mid + 1
            } else {
                right = mid - 1
            }
        }
        println("Found $target at index: $ans")
    }

    /**
     * ==========================================
     * 2. LOWER BOUND (First Occurrence / Insertion Point)
     * ==========================================
     * Returns index of first element >= target.
     * Crucial for: "Insert position", "Count occurrences".
     */
    @Test
    fun lowerBoundDemo() {
        println("\n=== LOWER BOUND ===")
        val arr = intArrayOf(1, 2, 4, 4, 4, 6, 7)
        val target = 4
        
        var left = 0
        var right = arr.size 
        
        while (left < right) {
            val mid = left + (right - left) / 2
            if (arr[mid] >= target) {
                right = mid // Valid, but try to find someone to the left
            } else {
                left = mid + 1
            }
        }
        println("First index >= $target is: $left")
    }

    /**
     * ==========================================
     * 3. SEARCH ON ANSWER SPACE (Concept)
     * ==========================================
     * Sometimes, we don't search an array. We search the ANSWER.
     * Example: "Koko Eating Bananas", "Min Capacity of Ship".
     * 
     * PATTERN:
     * 1. Define Range: [MinPossibleAns, MaxPossibleAns].
     * 2. Check Function: `canDo(val)` returns boolean.
     * 3. If `canDo(mid)` is True: Record ans, Try tighter constraint (Left/Right).
     */
    @Test
    fun answerSpaceConcept() {
        println("\n=== BINARY SEARCH ON ANSWER SPACE ===")
        println("Logic: define min/max, check feasibility, shrink range.")
    }

    /**
     * ==========================================
     * BITWISE OPERATORS CHEATSHEET
     * ==========================================
     * & (AND) : Both 1 -> 1
     * | (OR)  : Any 1  -> 1
     * ^ (XOR) : Different -> 1, Same -> 0
     * ~ (NOT) : Invert bits
     * << (Left Shift)  : Multiply by 2
     * >> (Right Shift) : Divide by 2
     */
    @Test
    fun bitwiseBasics() {
        println("\n=== BITWISE BASICS ===")
        val a = 5  // 0101
        val b = 3  // 0011
        
        println("5 & 3 = ${a and b}") // 0001 = 1
        println("5 | 3 = ${a or b}")  // 0111 = 7
        println("5 ^ 3 = ${a xor b}") // 0110 = 6
        println("5 << 1 = ${a shl 1}") // 1010 = 10
    }

    /**
     * ==========================================
     * TRICK 1: Check Odd/Even
     * ==========================================
     * (n & 1) == 0 -> Even
     * (n & 1) == 1 -> Odd
     */
    @Test
    fun checkOddEven() {
        println("\n=== BITWISE: Odd/Even ===")
        val n = 5
        if (n and 1 == 1) println("$n is Odd") else println("$n is Even")
    }

    /**
     * ==========================================
     * TRICK 2: Swap Variables (No Temp) - XOR
     * ==========================================
     * a = a ^ b
     * b = a ^ b
     * a = a ^ b
     */
    @Test
    fun swapXor() {
        println("\n=== BITWISE: Swap XOR ===")
        var x = 10
        var y = 20
        x = x xor y
        y = x xor y
        x = x xor y
        println("Swapped: x=$x, y=$y")
    }

    /**
     * ==========================================
     * TRICK 3: Check Power of 2
     * ==========================================
     * Powers of 2 have only one '1' bit.
     * n & (n-1) removes the last set bit.
     * So if n & (n-1) == 0, it was a power of 2.
     */
    @Test
    fun checkPowerOfTwo() {
        println("\n=== BITWISE: Power of 2 ===")
        val n = 16 // 10000
        val nMinus1 = 15 // 01111
        val isPower = (n > 0) && (n and (n - 1) == 0)
        println("Is 16 Power of 2? $isPower")
    }

    /**
     * ==========================================
     * TRICK 4: Find Single Number
     * ==========================================
     * Given array where every number appears TWICE except one. Find it.
     * Logic: n ^ n = 0. So pairs cancel out.
     */
    @Test
    fun findSingleNumber() {
        println("\n=== BITWISE: Find Unique Element (XOR) ===")
        val arr = intArrayOf(4, 1, 2, 1, 2)
        var result = 0
        for (num in arr) {
            result = result xor num
        }
        println("Single Number: $result") // 4
    }
}
