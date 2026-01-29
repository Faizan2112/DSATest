package com.example.dsatest.generated.basic

import org.junit.Test

class KotlinBasicsTest {

    @Test
    fun variablesAndNullSafety() {
        println("=== VARIABLES ===")
        val immutableName = "Faizan"
        var age = 25
        age = 26
        println("Name: " + immutableName + ", Age: " + age)

        println("\n=== NULL SAFETY ===")
        var nullableText: String? = "Hello"
        nullableText = null

        println("Length of null string: " + nullableText?.length)

        val length = nullableText?.length ?: -1
        println("Elvis operator result: " + length)

        // REMOVED TRY-CATCH to see if it fixes IR Lowering error
        // val l = nullableText!!.length
    }

    @Test
    fun loopsPractice() {
        println("=== FOR LOOPS ===")
        print("1..5: ")
        for (i in 1..5) print(i.toString() + " ")
        println()
        
        print("Repeat 3 times: ")
        for (i in 0 until 3) print("Hello ")
        println()

        println("\n=== WHILE LOOPS ===")
        var x = 0
        while (x < 3) {
            print("x=" + x + " ")
            x++
        }
        println()
    }

    @Test
    fun conditionsPractice() {
        val number = 10
        println("=== IF EXPRESSION ===")
        val message = if (number % 2 == 0) "Even" else "Odd"
        println(number.toString() + " is " + message)

        println("\n=== WHEN EXPRESSION ===")
        when (number) {
            1 -> println("It's one")
            2, 3 -> println("It's 2 or 3")
            in 4..10 -> println("It's between 4 and 10")
            else -> println("Unknown number")
        }
    }

    @Test
    fun functionsPractice() {
        println("=== FUNCTIONS ===")
        val sum = add(5, 10)
        println("Sum of 5 + 10 = " + sum)
        
        greet("Faizan")
    }

    fun add(a: Int, b: Int): Int = a + b

    fun greet(name: String) {
        println("Hello, " + name + "!")
    }
}
