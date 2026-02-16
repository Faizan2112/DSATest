package com.example.dsatest.generated.sorting

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
     * 1. STANDARD RECURSION
     *
     * PROBLEM:
     * Calculate Factorial of N.
     *
     * DESIGN:
     * Why Recursion?
     * - Factorial(N) = N * Factorial(N-1).
     * - This natural mathematical definition maps perfectly to recursive calls.
     *
     * DETAIL:
     * 1. Base Case: If n <= 1, return 1.
     * 2. Recursive Step: Return n * factorial(n - 1).
     *
     * COMPLEXITY:
     * Time: O(N) - N calls.
     * Space: O(N) - Stack depth N.
     *
     * RISK: Stack Overflow if N is large (~10,000).
     */
    @Test
    fun standardRecursion() {
        println("=== STANDARD RECURSION ===")
        println("Factorial(5): ${factorial(5)}") // 120
    }

    fun factorial(n: Int): Int {
        if (n <= 1) return 1
        return n * factorial(n - 1)
    }

    /**
     * 2. TAIL RECURSION (`tailrec`)
     *
     * DESIGN:
     * Why Tail Call Optimization?
     * - In standard recursion, the computer must "remember" to multiply by `n` *after* the recursive call returns. This uses stack space.
     * - In tail recursion, the recursive call is the *very last* thing. The computer can reuse the same stack frame.
     *
     * DETAIL:
     * 1. Use an `accumulator` to store the intermediate result.
     * 2. Base Case: Return `accumulator`.
     * 3. Recursive Step: `factorialTail(n - 1, n * accumulator)`.
     *
     * COMPLEXITY:
     * Time: O(N)
     * Space: O(1) - Kotlin compiler turns this into a loop!
     */
    @Test
    fun tailRecursion() {
        println("\n=== TAIL RECURSION ===")
        println("FactorialTail(5): ${factorialTail(5)}") 
    }

    tailrec fun factorialTail(n: Int, accumulator: Int = 1): Int {
        if (n <= 1) return accumulator
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
     * 4. MEMOIZATION (Top-Down Dynamic Programming)
     *
     * PROBLEM:
     * Fibonacci O(2^n) is too slow because it re-calculates the same branches.
     *
     * VISUALIZATION (Redundant Work):
     *           fib(5)
     *          /      \
     *      fib(4)      fib(3)
     *      /    \      /    \
     *   fib(3) fib(2) fib(2) fib(1)
     *     ^           ^
     *     Both branches calculate fib(2)!
     *
     * DESIGN:
     * Why Cache?
     * - Store results in a Map so we only calculate each `n` once.
     *
     * COMPLEXITY:
     * Time: O(N) - Each state is visited once.
     * Space: O(N) - Cache + Stack.
     */
    @Test
    fun memoizationDemo() {
        println("\n=== MEMOIZATION (DP) ===")
        memo.clear()
        println("Fib(10): ${fibMemo(10)}")
    }

    val memo = HashMap<Int, Int>()
    
    fun fibMemo(n: Int): Int {
        if (n <= 1) return n
        if (memo.containsKey(n)) return memo[n]!!
        val result = fibMemo(n - 1) + fibMemo(n - 2)
        memo[n] = result
        return result
    }
}
