package com.example.dsatest.generated.sorting

import org.junit.Test

/**
 * ==========================================
 * SORTING ALGORITHMS: DEEP DIVE (COMPLETE)
 * ==========================================
 * 
 * CONTENTS:
 * 1. O(n^2)     : Bubble, Selection, Insertion
 * 2. O(n log n) : Merge, Quick
 * 3. O(n)       : Counting, Radix (Non-Comparative)
 * 4. PATTERNS   : Cyclic Sort (Range 1..N)
 * 5. OBJECTS    : Custom Comparators
 */
class SortingAlgorithmsDeepDive {

     /**
     * ==========================================
     * 1. BUBBLE SORT (O(n^2))
     * ==========================================
     * - Complexity: Time O(n^2) | Space O(1).
     * - Stability: STABLE (Preserves order of equal elements).
     * - Usage: Teaching only. "Sinks" largest element to the end.
     */
    @Test
    fun bubbleSort() {
        println("=== BUBBLE SORT ===")
        val arr = intArrayOf(5, 1, 4, 2, 8)
        val n = arr.size
        for (i in 0 until n - 1) {
            for (j in 0 until n - i - 1) {
                if (arr[j] > arr[j + 1]) {
                    val temp = arr[j]
                    arr[j] = arr[j + 1]
                    arr[j + 1] = temp
                }
            }
        }
        println("Result: ${arr.joinToString()}")
    }

    /**
     * ==========================================
     * 2. SELECTION SORT (O(n^2))
     * ==========================================
     * - Complexity: Time O(n^2) | Space O(1).
     * - Stability: UNSTABLE (Depending on swap).
     * - Usage: Very small arrays. Minimizes swaps (O(n) swaps).
     */
    @Test
    fun selectionSort() {
        println("\n=== SELECTION SORT ===")
        val arr = intArrayOf(5, 1, 4, 2, 8)
        for (i in 0 until arr.size - 1) {
            var minIdx = i
            for (j in i + 1 until arr.size) {
                if (arr[j] < arr[minIdx]) minIdx = j
            }
            val temp = arr[minIdx]
            arr[minIdx] = arr[i]
            arr[i] = temp
        }
        println("Result: ${arr.joinToString()}")
    }

    /**
     * ==========================================
     * 3. INSERTION SORT (O(n^2))
     * ==========================================
     * - Complexity: Time O(n^2) (O(n) if nearly sorted) | Space O(1).
     * - Stability: STABLE.
     * - Usage: Small arrays (N < 50), Streaming data.
     */
    @Test
    fun insertionSort() {
        println("\n=== INSERTION SORT ===")
        val arr = intArrayOf(5, 1, 4, 2, 8)
        for (i in 1 until arr.size) {
            val key = arr[i]
            var j = i - 1
            while (j >= 0 && arr[j] > key) {
                arr[j + 1] = arr[j]
                j--
            }
            arr[j + 1] = key
        }
        println("Result: ${arr.joinToString()}")
    }

    /**
     * ==========================================
     * 4. MERGE SORT (O(n log n))
     * ==========================================
     * - Complexity: Time O(n log n) | Space O(n).
     * - Stability: STABLE.
     * - Usage: Linked Lists, External Sorting, Stable requirement.
     */
    @Test
    fun mergeSortDemo() {
        println("\n=== MERGE SORT ===")
        val arr = intArrayOf(38, 27, 43, 3)
        sort(arr, 0, arr.size - 1)
        println("Result: ${arr.joinToString()}")
    }

    private fun sort(arr: IntArray, l: Int, r: Int) {
        if (l < r) {
            val m = l + (r - l) / 2
            sort(arr, l, m)
            sort(arr, m + 1, r)
            merge(arr, l, m, r)
        }
    }

    private fun merge(arr: IntArray, l: Int, m: Int, r: Int) {
        val n1 = m - l + 1
        val n2 = r - m
        val L = IntArray(n1)
        val R = IntArray(n2)
        for (i in 0 until n1) L[i] = arr[l + i]
        for (j in 0 until n2) R[j] = arr[m + 1 + j]

        var i = 0; var j = 0; var k = l
        while (i < n1 && j < n2) {
            if (L[i] <= R[j]) arr[k++] = L[i++] else arr[k++] = R[j++]
        }
        while (i < n1) arr[k++] = L[i++]
        while (j < n2) arr[k++] = R[j++]
    }

    /**
     * ==========================================
     * 5. QUICK SORT (O(n log n))
     * ==========================================
     * - Complexity: Time O(n log n) avg, O(n^2) worst | Space O(log n).
     * - Stability: UNSTABLE (Partition logic).
     * - Usage: General purpose (Arrays.sort primitive). Fastest in practice (Cache friendly).
     */
    @Test
    fun quickSortDemo() {
        println("\n=== QUICK SORT ===")
        val arr = intArrayOf(10, 7, 8, 9, 1, 5)
        quickSort(arr, 0, arr.size - 1)
        println("Result: ${arr.joinToString()}")
    }

    private fun quickSort(arr: IntArray, low: Int, high: Int) {
        if (low < high) {
            val pi = partition(arr, low, high)
            quickSort(arr, low, pi - 1)
            quickSort(arr, pi + 1, high)
        }
    }

    private fun partition(arr: IntArray, low: Int, high: Int): Int {
        val pivot = arr[high]
        var i = (low - 1)
        for (j in low until high) {
            if (arr[j] < pivot) {
                i++
                val temp = arr[i]
                arr[i] = arr[j]
                arr[j] = temp
            }
        }
        val temp = arr[i + 1]
        arr[i + 1] = arr[high]
        arr[high] = temp
        return i + 1
    }

    /**
     * ==========================================
     * 6. COUNTING SORT (O(n + k))
     * ==========================================
     * - Complexity: Time O(N + K) | Space O(K).
     * - Stability: STABLE (if iterating backwards).
     * - Usage: Small integer range (e.g., Ages 0-100).
     */
    @Test
    fun countingSort() {
        println("\n=== COUNTING SORT ===")
        val arr = intArrayOf(4, 2, 2, 8, 3, 3, 1)
        val max = 8
        val count = IntArray(max + 1)
        val output = IntArray(arr.size)

        // 1. Count frequencies
        for (num in arr) count[num]++

        // 2. Accumulate counts
        for (i in 1..max) count[i] += count[i - 1]

        // 3. Build output array
        for (i in arr.indices.reversed()) {
            output[count[arr[i]] - 1] = arr[i]
            count[arr[i]]--
        }
        println("Result: ${output.joinToString()}")
    }

    /**
     * ==========================================
     * 7. CYCLIC SORT (O(n))
     * ==========================================
     * - Complexity: Time O(N) | Space O(1).
     * - Usage: "Find Missing/Duplicate Number" in range 1..N.
     */
    @Test
    fun cyclicSort() {
        println("\n=== CYCLIC SORT ===")
        val arr = intArrayOf(3, 5, 2, 1, 4) // Range 1..5
        var i = 0
        while (i < arr.size) {
            val correctIndex = arr[i] - 1
            if (arr[i] != arr[correctIndex]) {
                // Swap current number to its correct index
                val temp = arr[i]
                arr[i] = arr[correctIndex]
                arr[correctIndex] = temp
            } else {
                i++
            }
        }
        println("Result: ${arr.joinToString()}")
    }

    /**
     * ==========================================
     * 8. CUSTOM COMPARATORS
     * ==========================================
     * Sorting complex objects.
     */
    @Test
    fun customComparator() {
        println("\n=== CUSTOM COMPARATOR ===")
        data class Student(val name: String, val score: Int)
        
        val students = listOf(
            Student("Alice", 85),
            Student("Bob", 92),
            Student("Charlie", 85)
        )

        // Sort by Score Descending, then Name Ascending
        val sorted = students.sortedWith(
            compareByDescending<Student> { it.score }
                .thenBy { it.name }
        )
        println("Ranked: $sorted")
    }
}
