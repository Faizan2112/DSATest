package com.example.dsatest

import org.junit.Test
import kotlin.system.measureNanoTime

/**
 * ==========================================
 * TIME & SPACE COMPLEXITY: DEEP DIVE
 * ==========================================
 *
 * EXPANDED CONCEPTS:
 * 1. O(log n)   : "Cut it in half". The gold standard for search.
 * 2. O(n log n) : "Do linear work, log n times". The speed of good sorting.
 * 3. O(2^n)     : "Double the work with every step". The danger zone (Recursion).
 * 4. O(n!)      : "Factorial". Try every permutation. The SLOWEST common complexity.
 * 5. AMORTIZED  : "Occasionally slow, mostly fast". (e.g. ArrayList resizing).
 * 6. MEMORY     : Heap (Variables) vs Stack (Recursion Depth).
 */
class TimeComplexityDemo {

    @Test
    fun visualizeDeepDive() {
        // Sorted list for Binary Search
        val sortedList = (0..10_000).toList()

        println("=== O(log n) vs O(n) ===")
        // Finding 9999
        runAndTime("O(n) Linear Search") { linearSearch(sortedList, 9999) }
        runAndTime("O(log n) Binary Search") { binarySearch(sortedList, 9999) }
        // Result: Binary search is almost instant, even for millions of items.

        println("\n=== O(n log n) Sorting ===")
        val unsorted = (10_000 downTo 0).toList()
        runAndTime("O(n log n) Sorting") { unsorted.sorted() }
        // Fast! Sorting 10k items takes milliseconds.
        
        println("\n=== O(2^n) Exponential (Warning!) ===")
        // Try calculating small Fibonacci numbers
        runAndTime("Fib(10)") { recursiveFibonacci(10) }
        runAndTime("Fib(30)") { recursiveFibonacci(30) } 
        // Notice the massive jump from 10 to 30! 
        
        println("\n=== O(n!) Factorial (Danger!) ===")
        // Permutations grow insanely fast.
        // 5! = 120. 10! = 3.6 million. 13! = 6 Billion (Overflows Int).
        println("Permutations of 10 items: ${factorial(10)}")
    }
    
    /**
     * AMORTIZED ANALYSIS: ArrayList Resizing
     * 1. Adding to a list is O(1) USUALLY.
     * 2. But when it gets full, it creates a new double-sized array and copies EVERYTHING O(n).
     * 3. Amortized Analysis says: "On average, it's still O(1) because resizing is rare."
     */
    @Test
    fun amortizedDemo() {
        println("=== Amortized Analysis (ArrayList) ===")
        val list = ArrayList<Int>(2) // Start small capacity: 2
        
        for (i in 1..10) {
            // When i=3, it resizes (copies 2 items).
            // When i=5, it resizes (copies 4 items).
            // When i=9, it resizes (copies 8 items).
            list.add(i)
            println("Added $i. Size: ${list.size}")
        }
        println("Most adds were instant. A few caused a full copy.")
    }

    /**
     * O(log n) - Logarithmic Time
     * BINARY SEARCH style.
     */
    fun binarySearch(list: List<Int>, target: Int): Int {
        var left = 0
        var right = list.size - 1
        
        while (left <= right) {
            val mid = left + (right - left) / 2
            if (list[mid] == target) return mid
            if (list[mid] < target) left = mid + 1
            else right = mid - 1
        }
        return -1
    }

    /**
     * O(n) - For comparison
     */
    fun linearSearch(list: List<Int>, target: Int): Int {
        for ((index, value) in list.withIndex()) {
            if (value == target) return index
        }
        return -1
    }

    /**
     * O(2^n) - Exponential Time
     * RECURSIVE FIBONACCI
     */
    fun recursiveFibonacci(n: Int): Int {
        if (n <= 1) return n
        return recursiveFibonacci(n - 1) + recursiveFibonacci(n - 2)
    }

    /**
     * O(n!) - Factorial Time
     * Used for permutations (Traveling Salesman Brute Force).
     */
    fun factorial(n: Int): Long {
        if (n <= 1) return 1
        return n * factorial(n - 1)
    }

    /**
     * ==========================================
     * SPACE COMPLEXITY: STACK VS HEAP
     * ==========================================
     */
    @Test
    fun spaceComplexityDeepDive() {
        // 1. HEAP SPACE (O(n))
        val list = MutableList(1000) { it } 

        // 2. STACK SPACE (O(n) Recursion)
        try {
           // recursiveFunction(100000) // This would cause StackOverflowError
        } catch (e: StackOverflowError) {
            println("Ran out of stack memory!")
        }
    }
    
    fun recursiveFunction(n: Int) {
        if (n <= 0) return
        recursiveFunction(n - 1) 
    }

    private fun runAndTime(label: String, block: () -> Unit) {
        val time = measureNanoTime { block() }
        println(String.format("%-25s : %d micros", label, time / 1000))
    }
}
