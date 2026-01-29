package com.example.dsatest.generated.linkedlist

import org.junit.Test

/**
 * ==========================================
 * LINKED LISTS: PATTERNS & QUESTIONS
 * ==========================================
 * 
 * Linked Lists are about pointer manipulation.
 * MASTER THESE PATTERNS:
 * 1. Fast & Slow Pointers (Cycle, Middle)
 * 2. In-Place Reversal
 * 3. Dummy Head (Simplifies edge cases)
 * 4. Two Pointers (Kth from end, Intersection)
 * 
 * ==========================================
 * THE QUESTION BUCKET LIST
 * ==========================================
 * 
 * === EASY (20) ===
 * 1. Reverse Linked List [Implemented Below]
 * 2. Middle of the Linked List [Implemented Below]
 * 3. Merge Two Sorted Lists [Implemented Below]
 * 4. Linked List Cycle (Has Cycle?) [Implemented Below]
 * 5. Remove Duplicates from Sorted List
 * 6. Delete Node in a Linked List (Given node access only)
 * 7. Intersection of Two Linked Lists
 * 8. Palindrome Linked List
 * 9. Remove Linked List Elements (Val)
 * 10. Convert Binary Number in List to Integer
 * 11. Design HashSet (Bucket Array of LLs)
 * 12. Design HashMap
 * 13. My HashSet / My HashMap
 * 14. Print Immutable Linked List in Reverse
 * 15. Delete N Nodes After M Nodes
 * 16. Get Decimal Value
 * 17. Insert into Sorted Circular Linked List
 * 18. Remove Duplicates (Unsorted - Easy if Buffer allowed)
 * 19. Find Loop Start (Floyd's - often Medium but basic concept)
 * 20. Flatten a Multilevel Doubly Linked List (Easy version)
 * 
 * === MEDIUM (20) ===
 * 1. Add Two Numbers
 * 2. Remove Nth Node From End of List
 * 3. Swap Nodes in Pairs
 * 4. Copy List with Random Pointer (Deep Copy)
 * 5. Linked List Cycle II (Return Start Node)
 * 6. Reorder List
 * 7. Rotate List
 * 8. Sort List (Merge Sort O(n log n))
 * 9. Partition List
 * 10. Reverse Linked List II (Between m and n)
 * 11. Odd Even Linked List
 * 12. Remove Duplicates from Sorted List II (Distinct)
 * 13. Insertion Sort List
 * 14. Flatten a Multilevel Doubly Linked List
 * 15. Next Greater Node In Linked List
 * 16. Swapping Nodes in a Linked List
 * 17. Split Linked List in Parts
 * 18. Plus One Linked List
 * 19. Delete the Middle Node of a Linked List
 * 20. Design Browser History (Doubly LL)
 * 
 * === HARD (10-15) - (LLs have fewer pure Hard problems) ===
 * 1. Merge k Sorted Lists (Heap/Divide Conquer)
 * 2. Reverse Nodes in k-Group
 * 3. LFU Cache (Doubly LL + HashMaps)
 * 4. LRU Cache (Doubly LL + HashMap)
 * 5. All O`one Data Structure
 * 6. Design Skip List
 * 7. Text Editor (Doubly LL)
 * 8. Max Stack (DLL logic)
 * 9. Dinner Plate Stacks
 */

// Shared Node Logic
class ListNode(var `val`: Int) {
    var next: ListNode? = null
}

class LinkedListPatternsAndProblems {

    /**
     * ==========================================
     * PATTERN 1: REVERSAL (Iterative)
     * ==========================================
     * Problem: Reverse Linked List
     * Pattern: Prev, Curr, Next.
     */
    @Test
    fun patternReverse() {
        println("=== PATTERN: REVERSAL ===")
        val head = makeList(1, 2, 3, 4, 5)
        
        var prev: ListNode? = null
        var curr = head
        while (curr != null) {
            val nextTemp = curr.next
            curr.next = prev
            prev = curr
            curr = nextTemp
        }
        printList(prev) // 5->4->3->2->1
    }

    /**
     * ==========================================
     * PATTERN 2: FAST & SLOW POINTERS
     * ==========================================
     * Problem: Cycle Detection / Middle
     * Pattern: Slow moves 1, Fast moves 2.
     */
    @Test
    fun patternFastSlow() {
        println("\n=== PATTERN: FAST & SLOW (Middle) ===")
        val head = makeList(1, 2, 3, 4, 5)
        
        var slow = head
        var fast = head
        while (fast != null && fast.next != null) {
            slow = slow?.next
            fast = fast.next?.next
        }
        println("Middle: ${slow?.`val`}") // 3
    }

    /**
     * ==========================================
     * PATTERN 3: DUMMY HEAD
     * ==========================================
     * Problem: Merge Two Sorted Lists
     * Use Dummy to avoid empty head checks.
     */
    @Test
    fun patternDummyHead() {
        println("\n=== PATTERN: DUMMY HEAD (Merge) ===")
        val l1 = makeList(1, 2, 4)
        val l2 = makeList(1, 3, 4)
        
        val dummy = ListNode(-1)
        var curr = dummy
        
        var p1 = l1
        var p2 = l2
        
        while (p1 != null && p2 != null) {
            if (p1.`val` <= p2.`val`) {
                curr.next = p1
                p1 = p1.next
            } else {
                curr.next = p2
                p2 = p2.next
            }
            curr = curr.next!!
        }
        if (p1 != null) curr.next = p1
        if (p2 != null) curr.next = p2
        
        printList(dummy.next) // 1->1->2->3->4->4
    }

    // --- Helpers ---
    fun makeList(vararg nums: Int): ListNode? {
        if (nums.isEmpty()) return null
        val head = ListNode(nums[0])
        var curr = head
        for (i in 1 until nums.size) {
            curr.next = ListNode(nums[i])
            curr = curr.next!!
        }
        return head
    }

    fun printList(head: ListNode?) {
        var curr = head
        val sb = StringBuilder()
        while (curr != null) {
            sb.append(curr.`val`).append("->")
            curr = curr.next
        }
        sb.append("null")
        println(sb.toString())
    }
}
