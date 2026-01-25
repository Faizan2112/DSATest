package com.example.dsatest

import org.junit.Test
import kotlin.system.measureNanoTime

/**
 * ==========================================
 * TIME COMPLEXITY: THE ARCHITECTURE OF SPEED
 * ==========================================
 * Design & Detail Explanation.
 */
class TimeComplexityDemo {

    @Test
    fun demoConstantTime() {
        println("\n=== O(1) CONSTANT TIME ===")
        val hugeList = ArrayList<Int>(1000)
        for(i in 0 until 1000) hugeList.add(i)

        runAndTime("Get First (Index 0)") { hugeList[0] }
        runAndTime("Get Last (Index 999)") { hugeList[999] }
    }

    @Test
    fun demoLogarithmicTime() {
        println("\n=== O(log n) LOGARITHMIC TIME ===")
        val size = 1000
        val sortedList = IntArray(size) { it }
        val target = 999

        runAndTime("Binary Search 1K items") {
            var l = 0; var r = size - 1
            while(l <= r) {
                val mid = l + (r - l) / 2
                if(sortedList[mid] == target) break
                if(sortedList[mid] < target) l = mid + 1 else r = mid - 1
            }
        }
    }

    @Test
    fun demoLinearTime() {
        println("\n=== O(n) LINEAR TIME ===")
        val listStart = (0..100).toList()
        runAndTime("Sum 100 items") { listStart.sum() }
    }

    @Test
    fun demoLinearithmicTime() {
        println("\n=== O(n log n) SORTING ===")
        val unsorted = (0..1000).shuffled()
        runAndTime("Sort 1k items") { unsorted.sorted() }
    }

    @Test
    fun demoQuadraticTime() {
        println("\n=== O(n^2) QUADRATIC TIME ===")
        val smallList = (0..100).toList()
        runAndTime("Nested Loop (100 items)") {
            var count = 0L
            for (i in smallList) {
                for (j in smallList) {
                    count++
                }
            }
        }
    }

    @Test
    fun demoExponentialTime() {
        println("\n=== O(2^n) EXPONENTIAL TIME ===")
        runAndTime("Fib(10)") { fib(10) }
    }

    fun fib(n: Int): Int {
        if (n <= 1) return n
        return fib(n-1) + fib(n-2)
    }

    // ==========================================
    // 7. VISUALIZATION (ASCII GRAPHS)
    // ==========================================
    private fun printGraph(label: String, func: (Double) -> Double) {
        println("\n[ GRAPH: " + label + " ]")
        val height = 10
        val width = 20
        val grid = Array(height) { CharArray(width) { ' ' } }

        for (col in 0 until width) {
            val x = col.toDouble() / width
            val y = func(x)
            
            val row = ((1.0 - y) * (height - 1)).toInt()
            if (row in 0 until height) {
                grid[row][col] = '*'
            }
        }

        for (i in 0 until height) {
            print("|")
            println(String(grid[i]))
        }
        println("+" + "-".repeat(width))
    }

    @Test
    fun visualizeAllGraphs() {
        println("=== VISUALIZING GROWTH RATES ===")
        
        printGraph("O(1) Constant (Flat)") { 0.1 }
        printGraph("O(n) Linear (Straight)") { x -> x }
        printGraph("O(n^2) Quadratic (Curve)") { x -> x * x }
        printGraph("O(sqrt n) Square Root") { x -> Math.sqrt(x) } 
        printGraph("O(2^n) Exponential (Rocket)") { x -> Math.pow(2.0, x * 10) / 1024.0 }
    }

    private fun runAndTime(label: String, block: () -> Unit) {
        val time = measureNanoTime { block() }
        val timeDisplay = if(time > 1000000) (time/1000000).toString() + " ms" else (time/1000).toString() + " us"
        println(String.format("%-25s : %s", label, timeDisplay))
    }
}
