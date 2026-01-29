package com.example.dsatest

import org.junit.Test
import java.util.HashMap

/**
 * ==========================================
 * RECURSION MASTERY: THE ART OF SELF-CALLING
 * ==========================================
 * 
 * Recursion is simple: A function calls itself.
 * But "How" it calls itself changes everything.
 * 
 * CONTENTS:
 * 1. Standard Recursion (The Stack Heavy approach)
 * 2. Tail Recursion (Kotlin's Superpower - No Stack Overflow)
 * 3. Backtracking (Exploring all possibilities)
 * 4. Memoization (Caching results - The bridge to Dynamic Programming)
 */
class RecursionMastery {

    /**
     * ==========================================
     * 1. STANDARD RECURSION
     * ==========================================
     * Classic Factorial.
     * RISK: Stack Overflow if N is large (~10,000).
     */
    @Test
    fun standardRecursion() {
        println("=== STANDARD RECURSION ===")
        println("Factorial(5): ${factorial(5)}") // 120
    }

    fun factorial(n: Int): Int {
        if (n <= 1) return 1
        return n * factorial(n - 1) // Stack builds up here
    }

    /**
     * ==========================================
     * 2. TAIL RECURSION (`tailrec`)
     * ==========================================
     * CHEATSHEET:
     * - Concept: If the recursive call is the *last* operation, the compiler optimizes it into a loop.
     * - Benefit: No Stack Overflow. Constant Space O(1).
     * - How: Pass the "accumulated result" down as a parameter.
     */
    @Test
    fun tailRecursion() {
        println("\n=== TAIL RECURSION ===")
        // Can handle massive numbers without crashing
        println("FactorialTail(5): ${factorialTail(5)}") 
    }

    // `tailrec` keyword ensures optimization
    tailrec fun factorialTail(n: Int, accumulator: Int = 1): Int {
        if (n <= 1) return accumulator
        // The call is the ONLY thing returned. No multiplication *after* the call.
        return factorialTail(n - 1, n * accumulator)
    }

    /**
     * ==========================================
     * 3. BACKTRACKING (Try, Undo, Retry)
     * ==========================================
     * PROBLEM: Generate all Subsets (Power Set) of [1, 2].
     * 
     * VISUALIZATION (State Tree):
     *                  []
     *               /      \
     *           [1]          []      (Include 1 vs Exclude 1)
     *          /   \        /  \
     *      [1,2]   [1]    [2]   []   (Include 2 vs Exclude 2)
     *
     * TEMPLATE:
     * 1. Check Base Case (reached goal).
     * 2. Iterate Decisions:
     *    a. Choose (Add to path).
     *    b. Explore (Recurse).
     *    c. Un-Choose (Remove from path - Backtrack).
     */
    @Test
    fun backtrackingDemo() {
        println("\n=== BACKTRACKING (Subsets) ===")
        val nums = intArrayOf(1, 2)
        val result = ArrayList<List<Int>>()
        
        fun backtrack(index: Int, currentPath: ArrayList<Int>) {
            // Base Case: We passed the last element
            if (index == nums.size) {
                result.add(ArrayList(currentPath)) // Add copy
                return
            }
            
            // OPTION 1: Include nums[index]
            currentPath.add(nums[index]) // Choose
            backtrack(index + 1, currentPath) // Explore
            currentPath.removeAt(currentPath.size - 1) // Un-Choose (Backtrack)
            
            // OPTION 2: Don't include nums[index]
            backtrack(index + 1, currentPath)
        }
        
        backtrack(0, ArrayList())
        println("Subsets: $result")
    }

    /**
     * ==========================================
     * 4. MEMOIZATION (Top-Down Dynamic Programming)
     * ==========================================
     * PROBLEM: Fibonacci O(2^n) is too slow.
     * SOL: Cache answers. O(n).
     */
    @Test
    fun memoizationDemo() {
        println("\n=== MEMOIZATION (DP) ===")
        // Clear cache
        memo.clear()
        println("Fib(10): ${fibMemo(10)}")
        println("Cache hits occurred!")
    }

    val memo = HashMap<Int, Int>()
    
    fun fibMemo(n: Int): Int {
        if (n <= 1) return n
        
        // 1. Check Cache
        if (memo.containsKey(n)) return memo[n]!!
        
        // 2. Compute & Store
        val result = fibMemo(n - 1) + fibMemo(n - 2)
        memo[n] = result
        return result
    }
}
