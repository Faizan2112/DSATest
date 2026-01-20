package com.example.dsatest

import org.junit.Test

/**
 * ==========================================
 * KOTLIN BASICS: THE ESSENTIALS
 * ==========================================
 * This file is a "Playground" to understand the core building blocks of Kotlin.
 *
 * HOW TO USE:
 * 1. Read the comments above each section to understand the "What", "Why", and "When".
 * 2. Run the test to see the output.
 * 3. Uncomment/Edit code to experiment.
 */
class KotlinBasicsTest {

    /**
     * ==========================================
     * TOPIC 1: VARIABLES & NULL SAFETY
     * ==========================================
     *
     * 1. WHAT: Storing data. `val` (read-only) vs `var` (mutable). Null Safety prevents crashes.
     * 2. BENEFITS: 
     *      - `val` makes code predictable (immutability).
     *      - Null Safety eliminates the Billion Dollar Mistake (NullPointerException).
     */
    @Test
    fun variablesAndNullSafety() {
        println("=== VARIABLES ===")
        val immutableName = "Faizan" // Cannot change this
        var age = 25                 // Can change this
        // immutableName = "Khan" // ERROR: Val cannot be reassigned
        age = 26
        println("Name: $immutableName, Age: $age")

        println("\n=== NULL SAFETY (The ??? Operators) ===")
        // Kotlin types are non-null by default.
        var text: String = "Hello"
        // text = null // ERROR: Null can not be a value of a non-null type String

        var nullableText: String? = "Hello"
        nullableText = null // This is OK

        // Safe Call Operator (?.)
        // "If nullableText is not null, get length. Else return null."
        println("Length of null string: ${nullableText?.length}") 

        // Elvis Operator (?:)
        // "If left side is null, use the right side."
        val length = nullableText?.length ?: -1 
        println("Elvis operator result: $length")

        // Not-null Assertion (!!)
        // "I swear this is not null. If I'm wrong, CRASH the app."
        // Use sparingly!
        try {
            val l = nullableText!!.length
        } catch (e: NullPointerException) {
            println("Crashed due to !! on null reference")
        }
    }

    /**
     * ==========================================
     * TOPIC 2: LOOPS (For, While, Do-While)
     * ==========================================
     *
     * 1. WHAT: constructs to repeat code multiple times.
     * 2. BENEFITS: Automates repetitive tasks without code duplication.
     * 3. WHEN TO USE:
     *    - Use `for` when you know the range or are iterating over a collection.
     *    - Use `while` when you don't know how many times to loop, but you have a condition.
     */
    @Test
    fun loopsPractice() {
        println("=== FOR LOOPS ===")
        // 1. Simple Range (Inclusive)
        print("1..5: ")
        for (i in 1..5) {
            print("$i ")
        }
        println()

        // 2. Until Range (Exclusive)
        // Commonly used for array indices (0 until length)
        print("1 until 5: ")
        for (i in 1 until 5) {
            print("$i ")
        }
        println()

        // 3. DownTo (Reverse)
        print("5 downTo 1: ")
        for (i in 5 downTo 1) {
            print("$i ")
        }
        println()

        // 4. Step (Skip items)
        print("1..10 step 2: ")
        for (i in 1..10 step 2) {
            print("$i ")
        }
        println()
        
        // 5. Repeat
        print("Repeat 3 times: ")
        repeat(3) {
            print("Hello ")
        }
        println()

        println("\n=== WHILE LOOPS ===")
        var x = 0
        while (x < 3) {
            print("x=$x ")
            x++
        }
        println()
    }

    /**
     * ==========================================
     * TOPIC 3: CONDITIONS (If, When)
     * ==========================================
     *
     * 1. WHAT: Decision making logic.
     * 2. BENEFITS: Allows your code to handle different inputs differently.
     * 3. WHEN TO USE:
     *    - Use `if` for simple true/false or binary checks.
     *    - Use `when` (Switch on steroids) for multiple branches or complex type checking.
     */
    @Test
    fun conditionsPractice() {
        val number = 10

        println("=== IF EXPRESSION ===")
        // 'if' returns a value.
        val message = if (number % 2 == 0) "Even" else "Odd"
        println("$number is $message") 

        println("\n=== WHEN EXPRESSION ===")
        when (number) {
            1 -> println("It's one")
            2, 3 -> println("It's 2 or 3")
            in 4..10 -> println("It's between 4 and 10")
            else -> println("Unknown number")
        }
    }

    /**
     * ==========================================
     * TOPIC 4: FUNCTIONS
     * ==========================================
     *
     * 1. WHAT: Reusable blocks of code.
     * 2. BENEFITS: Modular, readable, and DRY (Don't Repeat Yourself).
     */
    @Test
    fun functionsPractice() {
        println("=== FUNCTIONS ===")
        val sum = add(5, 10)
        println("Sum of 5 + 10 = $sum")

        val product = multiply(5, 10)
        println("Product of 5 * 10 = $product")

        greet("Faizan")
        greet(name = "User", isMorning = false) // Named arguments!
    }

    // Standard Function
    // fun name(param: Type): ReturnType { body }
    fun add(a: Int, b: Int): Int {
        return a + b
    }

    // Single-Expression Function
    // "If the function is just one line, remove braces and 'return'"
    fun multiply(a: Int, b: Int): Int = a * b

    // Default Arguments & Named Parameters
    fun greet(name: String, isMorning: Boolean = true) {
        val greeting = if (isMorning) "Good Morning" else "Good Evening"
        println("$greeting, $name!")
    }
}
