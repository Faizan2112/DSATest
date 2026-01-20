package com.example.dsatest

import org.junit.Test

/**
 * ==========================================
 * KOTLIN COLLECTIONS MASTERY
 * ==========================================
 * 
 * CONTENTS:
 * 1. Filtering
 * 2. Transforming
 * 3. Aggregating
 * 4. Grouping
 * 5. Sorting
 * 6. Conversions
 * 7. SEQUENCES (Lazy Evaluation)
 */
class CollectionsMastery {

    val numbers = listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
    val names = listOf("Alice", "Bob", "Charlie", "David", "Eve")

    /**
     * ==========================================
     * 1. FILTERING
     * ==========================================
     */
    @Test
    fun filteringDemo() {
        println("=== FILTERING ===")
        val evens = numbers.filter { it % 2 == 0 }
        println("Evens: $evens")
        val (longNames, shortNames) = names.partition { it.length > 4 }
        println("partition: $longNames vs $shortNames")
    }

    /**
     * ==========================================
     * 2. TRANSFORMING
     * ==========================================
     */
    @Test
    fun transformingDemo() {
        println("\n=== TRANSFORMING ===")
        val lengths = names.map { it.length }
        println("map (lengths): $lengths")
        
        val nested = listOf(listOf(1, 2), listOf(3, 4))
        println("flatMap: ${nested.flatMap { it }}")
        
        println("zip: ${names.zip(listOf(1, 2, 3, 4, 5))}")
        println("associate: ${names.associate { it to it.length }}")
    }

    /**
     * ==========================================
     * 3. AGGREGATING
     * ==========================================
     */
    @Test
    fun aggregatingDemo() {
        println("\n=== AGGREGATING ===")
        val sum = numbers.fold(0) { acc, num -> acc + num }
        println("fold (sum): $sum")
    }

    /**
     * ==========================================
     * 4. GROUPING
     * ==========================================
     */
    @Test
    fun groupingDemo() {
        println("\n=== GROUPING ===")
        val freq = listOf("a", "b", "a").groupingBy { it }.eachCount()
        println("Frequency Map: $freq") 
    }

    /**
     * ==========================================
     * 5. SORTING
     * ==========================================
     */
    @Test
    fun sortingDemo() {
        println("\n=== SORTING ===")
        val sorted = names.sortedBy { it.length }
        println("sortedBy: $sorted")
    }
    
    /**
     * ==========================================
     * 6. CONVERSIONS
     * ==========================================
     */
    @Test
    fun conversionsDemo() {
        println("\n=== CONVERSIONS ===")
        val list = names.toMutableList()
        val set = names.toHashSet()
        println("List/Set conversions standard.")
    }

    /**
     * ==========================================
     * 7. SEQUENCES (Lazy Evaluation)
     * ==========================================
     * Standard Collections (List, Set) are EAGER. They create a new list for EVERY step.
     * Sequences are LAZY. They process item-by-item perfectly (like Java Streams).
     * 
     * WHEN TO USE:
     * - Large data sets (10k+ items).
     * - Long chains of operations (map.filter.map.filter...).
     */
    @Test
    fun sequencesDemo() {
        println("\n=== SEQUENCES (Lazy) ===")
        
        // EAGER (Inefficient for huge lists)
        // Creates a list for 'map', then another list for 'filter'. 2 intermediate lists.
        val eagerResult = numbers
            .map { 
                print("Map($it) ")
                it * 2 
            }
            .filter { 
                print("Filter($it) ")
                it > 10 
            }
        println("\nEager Result: $eagerResult") 
        
        println("-" .repeat(20))

        // LAZY (Efficient)
        // No intermediate lists. Processes 1, maps 1, filters 1. Then 2...
        val lazyResult = numbers.asSequence()
            .map { 
                print("Map($it) ")
                it * 2 
            }
            .filter { 
                print("Filter($it) ")
                it > 10 
            }
            .toList() // Terminal operation triggers the chain
        println("\nLazy Result: $lazyResult")
    }
}
