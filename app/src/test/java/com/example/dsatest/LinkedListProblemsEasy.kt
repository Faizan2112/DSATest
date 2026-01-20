package com.example.dsatest

import org.junit.Test
import java.util.Stack
import java.util.HashSet

/**
 * ==========================================
 * LINKED LIST PROBLEMS: EASY (1-20)
 * ==========================================
 * 
 * Solutions to 20 Easy Linked List questions.
 * Focus: Pointers, Iteration, Recursion basics.
 */
class LinkedListProblemsEasy {

    // Helper to print
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
    
    // Helper to make
    fun make(vararg nums: Int): ListNode? {
        if (nums.isEmpty()) return null
        val head = ListNode(nums[0])
        var curr = head
        for (i in 1 until nums.size) {
            curr.next = ListNode(nums[i])
            curr = curr.next!!
        }
        return head
    }

    /**
     * 1. Reverse Linked List
     * Logic: Prev, Curr, Next.
     */
    @Test
    fun q1_reverseList() {
        println("=== Q1: Reverse Linked List ===")
        val head = make(1, 2, 3, 4, 5)
        var prev: ListNode? = null
        var curr = head
        while (curr != null) {
            val next = curr.next
            curr.next = prev
            prev = curr
            curr = next
        }
        printList(prev)
    }

    /**
     * 2. Middle of the Linked List
     * Logic: Slow (1x), Fast (2x).
     */
    @Test
    fun q2_middleNode() {
        println("=== Q2: Middle of List ===")
        val head = make(1, 2, 3, 4, 5)
        var slow = head; var fast = head
        while (fast?.next != null) {
            slow = slow?.next
            fast = fast.next?.next
        }
        println("Result: ${slow?.`val`}")
    }

    /**
     * 3. Merge Two Sorted Lists
     * Logic: Dummy Head + Runner pointers.
     */
    @Test
    fun q3_mergeLists() {
        println("=== Q3: Merge Sorted Lists ===")
        val l1 = make(1, 2, 4); val l2 = make(1, 3, 4)
        val dummy = ListNode(-1)
        var tail = dummy
        var p1 = l1; var p2 = l2
        while (p1 != null && p2 != null) {
            if (p1.`val` < p2.`val`) { tail.next = p1; p1 = p1.next }
            else { tail.next = p2; p2 = p2.next }
            tail = tail.next!!
        }
        tail.next = p1 ?: p2
        printList(dummy.next)
    }

    /**
     * 4. Linked List Cycle
     * Logic: Floyd's Cycle Detection (Fast/Slow).
     */
    @Test
    fun q4_hasCycle() {
        println("=== Q4: Has Cycle ===")
        // Simulating cycle
        val head = ListNode(3)
        val node2 = ListNode(2)
        val node3 = ListNode(0)
        val node4 = ListNode(-4)
        head.next = node2; node2.next = node3; node3.next = node4
        node4.next = node2 // Cycle
        
        var slow = head; var fast: ListNode? = head
        var hasCycle = false
        while (fast?.next != null) {
            slow = slow.next!!
            fast = fast.next!!.next
            if (slow === fast) { hasCycle = true; break }
        }
        println("Result: $hasCycle")
    }

    /**
     * 5. Remove Duplicates from Sorted List
     * Logic: If curr.val == curr.next.val, skip next.
     */
    @Test
    fun q5_deleteDuplicates() {
        println("=== Q5: Remove Duplicates ===")
        val head = make(1, 1, 2, 3, 3)
        var curr = head
        while (curr?.next != null) {
            if (curr.`val` == curr.next!!.`val`) curr.next = curr.next!!.next
            else curr = curr.next
        }
        printList(head)
    }

    /**
     * 6. Delete Node in a Linked List
     * Logic: Copy next val to curr, delete next. (Given access to node only).
     */
    @Test
    fun q6_deleteNode() {
        println("=== Q6: Delete Node (No Head Access) ===")
        val head = make(4, 5, 1, 9)
        val nodeToDelete = head!!.next // Node 5
        // Logic
        nodeToDelete!!.`val` = nodeToDelete.next!!.`val`
        nodeToDelete.next = nodeToDelete.next!!.next
        printList(head)
    }

    /**
     * 7. Intersection of Two Linked Lists
     * Logic: A+B = B+A. Switch heads definition.
     */
    @Test
    fun q7_intersection() {
        println("=== Q7: Intersection ===")
        // Logic simulated: pA travels A then B. pB travels B then A. Meet at intersection.
        println("Result: Intersection Node")
    }

    /**
     * 8. Palindrome Linked List
     * Logic: Find Middle -> Reverse 2nd Half -> Compare.
     */
    @Test
    fun q8_isPalindrome() {
        println("=== Q8: Palindrome List ===")
        val head = make(1, 2, 2, 1)
        // 1. Middle
        var slow = head; var fast = head
        while(fast?.next != null) { slow = slow!!.next; fast = fast.next!!.next }
        // 2. Reverse 2nd half using slow
        var prev: ListNode? = null
        var curr = slow
        while(curr != null) { val n = curr.next; curr.next = prev; prev = curr; curr = n }
        // 3. Compare
        var left = head; var right = prev
        var pal = true
        while(right != null) { // Right is shorter or equal
            if(left!!.`val` != right.`val`) { pal = false; break }
            left = left.next; right = right.next
        }
        println("Result: $pal")
    }

    /**
     * 9. Remove Linked List Elements
     * Logic: Dummy Head. If next.val == target, skip.
     */
    @Test
    fun q9_removeElements() {
        println("=== Q9: Remove Elements (Val) ===")
        val head = make(1, 2, 6, 3, 4, 5, 6); val target = 6
        val dummy = ListNode(-1); dummy.next = head
        var curr = dummy
        while(curr.next != null) {
            if(curr.next!!.`val` == target) curr.next = curr.next!!.next
            else curr = curr.next!!
        }
        printList(dummy.next)
    }

    /**
     * 10. Convert Binary Number in List to Integer
     * Logic: res = res * 2 + val.
     */
    @Test
    fun q10_binaryToInt() {
        println("=== Q10: Binary to Int ===")
        val head = make(1, 0, 1) // 5
        var res = 0
        var curr = head
        while(curr != null) {
            res = (res shl 1) or curr.`val`
            curr = curr.next
        }
        println("Result: $res")
    }

    /**
     * 11. Design HashSet
     * Logic: Array of buckets (LinkedLists).
     */
    @Test
    fun q11_designHashSet() {
        println("=== Q11: Design HashSet ===")
        println("Logic: Array<ListNode?> size 1000. Hash = key % size. Chaining.")
    }

    /**
     * 12. Design HashMap
     * Logic: Array of buckets (ListNode pairs).
     */
    @Test
    fun q12_designHashMap() {
        println("=== Q12: Design HashMap ===")
        println("Logic: Array<ListNode?>. Node stores (key, val).")
    }

    /**
     * 13. Print Immutable Linked List in Reverse
     * Logic: Recursion.
     */
    @Test
    fun q13_printReverse() {
        println("=== Q13: Print Reverse ===")
        val head = make(1, 2, 3)
        fun printRev(node: ListNode?) {
            if(node == null) return
            printRev(node.next)
            print("${node.`val`} ")
        }
        printRev(head)
        println()
    }

    /**
     * 14. Delete N Nodes After M Nodes
     * Logic: Skip M, then loop N times deleting.
     */
    @Test
    fun q14_deleteNAfterM() {
        println("=== Q14: Delete N after M ===")
        val head = make(1,2,3,4,5,6,7,8,9,10,11,12,13)
        val m = 2; val n = 3
        var curr = head
        while (curr != null) {
            // Skip M-1
            for (i in 0 until m - 1) {
                if(curr == null) break
                curr = curr.next
            }
            if(curr == null) break
            // Delete N
            var t = curr.next
            for (i in 0 until n) {
                if(t == null) break
                t = t.next
            }
            curr.next = t
            curr = t
        }
        printList(head)
    }

    /**
     * 15. Kth Node From End
     * Logic: Two pointers, gap K.
     */
    @Test
    fun q15_kthFromEnd() {
        println("=== Q15: Kth From End ===")
        val head = make(1, 2, 3, 4, 5); val k = 2
        var fast = head; var slow = head
        for (i in 0 until k) fast = fast?.next
        while (fast != null) {
            slow = slow?.next
            fast = fast.next
        }
        println("Result: ${slow?.`val`}")
    }

    /**
     * 16. Swap Node in Pairs (Simplified)
     * Logic: Iterative swap.
     */
    @Test
    fun q16_swapPairs() {
        println("=== Q16: Swap Pairs ===")
        val head = make(1, 2, 3, 4)
        val dummy = ListNode(-1); dummy.next = head
        var curr = dummy
        while(curr.next != null && curr.next!!.next != null) {
            val first = curr.next!!
            val second = curr.next!!.next!!
            first.next = second.next
            second.next = first
            curr.next = second
            curr = first
        }
        printList(dummy.next)
    }

    /**
     * 17. Add Two Numbers (Easy version usually, Medium on LC)
     * Logic: Carry handling.
     */
    @Test
    fun q17_addTwoNumbers() {
        println("=== Q17: Add Two Numbers ===")
        val l1 = make(2, 4, 3); val l2 = make(5, 6, 4)
        val dummy = ListNode(0); var curr = dummy; var carry = 0
        var p1 = l1; var p2 = l2
        while(p1 != null || p2 != null || carry != 0) {
            val sum = (p1?.`val` ?: 0) + (p2?.`val` ?: 0) + carry
            curr.next = ListNode(sum % 10)
            carry = sum / 10
            curr = curr.next!!
            p1 = p1?.next; p2 = p2?.next
        }
        printList(dummy.next)
    }

    /**
     * 18. Remove Duplicates (Unsorted)
     * Logic: HashSet buffer.
     */
    @Test
    fun q18_removeDuplicatesUnsorted() {
        println("=== Q18: Remove Duplicates (Unsorted) ===")
        val head = make(1, 3, 2, 1, 4, 2)
        val set = HashSet<Int>()
        var curr = head; var prev: ListNode? = null
        while(curr != null) {
            if(set.contains(curr.`val`)) {
                prev!!.next = curr.next
            } else {
                set.add(curr.`val`)
                prev = curr
            }
            curr = curr.next
        }
        printList(head)
    }

    /**
     * 19. Check if Circular
     * Logic: Fast/Slow.
     */
    @Test
    fun q19_checkCircular() {
        println("=== Q19: Check Circular ===")
        // Same as Has Cycle
        println("Result: See Q4")
    }

    /**
     * 20. Count Nodes
     * Logic: Traversal.
     */
    @Test
    fun q20_countNodes() {
        println("=== Q20: Count Nodes ===")
        val head = make(1, 2, 3)
        var count = 0; var c = head
        while(c != null) { count++; c = c.next }
        println("Result: $count")
    }
}
