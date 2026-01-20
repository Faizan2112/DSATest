package com.example.dsatest

import org.junit.Test

/**
 * ==========================================
 * FOUNDATION: MATH & COLLECTIONS
 * ==========================================
 *
 * To solve DSA problems, you needed:
 * 1. Logic (Loops/Conditions) -> Covered.
 * 2. Complexity (Big O)       -> Covered.
 * 3. MATH & DATA CONTAINERS   -> Covered Here.
 *
 * THIS FILE COVERS:
 * 1. Basic Math (GCD, Primes, Modulo).
 * 2. Arrays & Lists (The bread and butter).
 * 3. Strings (Manipulation).
 * 4. Maps (Key-Value storage).
 */
class FoundationMathTest {

    /**
     * ==========================================
     * TOPIC 1: BASIC MATH PATTERNS
     * ==========================================
     */
    @Test
    fun mathBasics() {
        println("=== MATH BASICS ===")
        
        // 1. Modulo Operator (%)
        // "The Remainder". Crucial for cycles, even/odd, hashing.
        println("10 % 3 = ${10 % 3}") // 1
        println("Is 7 odd? ${7 % 2 != 0}")

        // 2. GCD (Greatest Common Divisor) - Euclidean Algorithm
        // Essential for number theory problems.
        println("GCD(48, 18) = ${gcd(48, 18)}") // 6

        // 3. Prime Checking
        // A number is prime if it has no divisors other than 1 and itself.
        println("Is 17 prime? ${isPrime(17)}")
        println("Is 18 prime? ${isPrime(18)}")
    }

    // Recursive GCD (Euclidean)
    // O(log(min(a,b)))
    fun gcd(a: Int, b: Int): Int {
        if (b == 0) return a
        return gcd(b, a % b)
    }

    // Check Prime efficiently
    // O(sqrt(n))
    fun isPrime(n: Int): Boolean {
        if (n <= 1) return false
        // We only need to check up to square root of n
        for (i in 2..Math.sqrt(n.toDouble()).toInt()) {
            if (n % i == 0) return false
        }
        return true
    }

    /**
     * ==========================================
     * TOPIC 2: COLLECTIONS (Arrays, Lists, Maps)
     * ==========================================
     */
    @Test
    fun collectionsBasics() {
        println("\n=== ARRAYS & LISTS ===")
        
        // 1. Array (Fixed Size)
        // Like int[] in Java. Fast, but hard to resize.
        val nums = intArrayOf(1, 2, 3, 4, 5)
        nums[0] = 99
        println("Array: ${nums.joinToString()}")

        // 2. List (Immutable & Mutable)
        // Kotlin lists are immutable by default!
        val readOnlyList = listOf("A", "B", "C")
        // readOnlyList.add("D") // ERROR
        
        val mutableList = mutableListOf("A", "B")
        mutableList.add("C")
        mutableList.removeAt(0)
        println("Mutable List: $mutableList")

        println("\n=== MAPS (HashMap) ===")
        // Key -> Value storage. O(1) mostly.
        val map = HashMap<String, Int>()
        map["Alice"] = 90
        map["Bob"] = 85
        
        // Check existence
        if (map.containsKey("Alice")) {
            println("Alice's score: ${map["Alice"]}")
        }
        
        // Loop through map
        for ((name, score) in map) {
            println("$name -> $score")
        }
    }
    
    /**
     * ==========================================
     * TOPIC 3: STRINGS
     * ==========================================
     */
    @Test
    fun stringBasics() {
        println("\n=== STRINGS ===")
        val str = "Hello World"
        
        // Accessing characters
        println("First char: ${str[0]}")
        
        // Substring
        println("Substring(0, 5): ${str.substring(0, 5)}") // "Hello"
        
        // String to Char Array (Mutable)
        val charArray = str.toCharArray()
        charArray[0] = 'h' // lowercase
        println("Modified: ${String(charArray)}")
        
        // Sorting a string (Common interview trick)
        // "cba" -> "abc"
        val sorted = str.toCharArray().sorted().joinToString("")
        println("Sorted String: $sorted")
    }
}
