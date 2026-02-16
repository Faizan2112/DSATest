package com.example.dsatest.generated.basic

import org.junit.Test

/**
 * ==========================================
 * KOTLIN BASICS: INTERVIEW CHEAT SHEET
 * ==========================================
 * 
 * This file covers the core syntax needed for DSA.
 * 
 * 1. VARIABLES & NULL SAFETY
 * 2. LOOPS & RANGES
 * 3. CONTROL FLOW (WHEN/IF)
 * 4. SCOPE FUNCTIONS (apply, let, run, also)
 */
class KotlinBasicsTest {

    @Test
    fun variablesAndNullSafety() {
        println("=== VARIABLES ===")
        val immutableName = "Faizan" // 'val' is like final
        var age = 25              // 'var' is mutable
        println("Name: $immutableName, Age: $age")

        println("\n=== NULL SAFETY ===")
        var nullableText: String? = "Hello"
        nullableText = null

        // Safe call operator (?.)
        println("Length: ${nullableText?.length}") 

        // Elvis operator (?:)
        val length = nullableText?.length ?: -1
        println("Default length: $length")
    }

    @Test
    fun loopsPractice() {
        println("=== FOR LOOPS ===")
        print("1..5: ")
        for (i in 1..5) print("$i ")
        println()
        
        print("Until 3 (exclusive): ")
        for (i in 0 until 3) print("$i ")
        println()

        println("\n=== WHILE LOOPS ===")
        var x = 0
        while (x < 3) {
            print("x=$x ")
            x++
        }
        println()
    }

    @Test
    fun conditionsPractice() {
        val number = 10
        println("=== IF EXPRESSION ===")
        // If is an expression (returns a value)
        val message = if (number % 2 == 0) "Even" else "Odd"
        println("$number is $message")

        println("\n=== WHEN EXPRESSION ===")
        // Replacement for switch. Exhaustive and powerful.
        when (number) {
            1 -> println("It's one")
            2, 3 -> println("It's 2 or 3")
            in 4..10 -> println("It's between 4 and 10")
            is Int -> println("It's an integer")
            else -> println("Unknown")
        }
    }

    /**
     * 4. SCOPE FUNCTIONS
     *
     * DESIGN:
     * - `let`: Null checks or local scope. Returns last expression.
     * - `apply`: Configuration (Object Initialization). Returns the object itself.
     * - `also`: Side actions (e.g. logging). Returns the object.
     * - `run`: Execution block. Returns last expression.
     */
    @Test
    fun scopeFunctions() {
        println("\n=== SCOPE FUNCTIONS ===")
        
        // .let for null safety
        val s: String? = "Hello"
        s?.let { println("String length: ${it.length}") }
        
        // .apply for object setup
        val list = ArrayList<Int>().apply {
            add(1)
            add(2)
        }
        println("ArrayList: $list")
    }

    @Test
    fun functionsPractice() {
        println("=== FUNCTIONS ===")
        val sum = add(5, 10)
        println("Sum: $sum")
    }

    fun add(a: Int, b: Int): Int = a + b
}
