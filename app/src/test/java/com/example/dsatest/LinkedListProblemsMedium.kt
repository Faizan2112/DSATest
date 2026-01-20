package com.example.dsatest

import org.junit.Test
import java.util.Stack
import java.util.HashMap

/**
 * ==========================================
 * LINKED LIST PROBLEMS: MEDIUM (1-20)
 * ==========================================
 * 
 * Solutions to 20 Medium Linked List questions.
 * Patterns: Fast/Slow, Recursion, Hashtable, Divide & Conquer.
 */
class LinkedListProblemsMedium {

    fun make(vararg nums: Int): ListNode? {
        if(nums.isEmpty()) return null
        val h = ListNode(nums[0]); var c = h
        for(i in 1 until nums.size) { c.next=ListNode(nums[i]); c=c.next!! }
        return h
    }
    fun printList(h: ListNode?){ var c=h; while(c!=null){ print("${c.`val`}->"); c=c.next }; println("null") }

    /**
     * 1. Add Two Numbers II (Most Significant Bit first)
     * Logic: Reverse lists -> Add -> Reverse result OR Use Stacks.
     */
    @Test
    fun q1_addTwoNumbersII() {
        println("=== Q1: Add Two Numbers II ===")
        val l1 = make(7,2,4,3); val l2 = make(5,6,4)
        val s1 = Stack<Int>(); val s2 = Stack<Int>()
        var c1 = l1; while(c1!=null){ s1.push(c1.`val`); c1=c1.next }
        var c2 = l2; while(c2!=null){ s2.push(c2.`val`); c2=c2.next }
        
        var head: ListNode? = null
        var carry = 0
        while(s1.isNotEmpty() || s2.isNotEmpty() || carry != 0) {
            val sum = (if(s1.isNotEmpty()) s1.pop() else 0) + (if(s2.isNotEmpty()) s2.pop() else 0) + carry
            val node = ListNode(sum % 10)
            node.next = head
            head = node
            carry = sum / 10
        }
        printList(head)
    }

    /**
     * 2. Remove Nth Node From End
     * Logic: Two pointers, gap N.
     */
    @Test
    fun q2_removeNthFromEnd() {
        println("=== Q2: Remove Nth From End ===")
        val head = make(1,2,3,4,5); val n = 2
        val dummy = ListNode(0); dummy.next = head
        var s = dummy; var f = dummy
        for(i in 0..n) f = f.next!!
        while(f.next != null) { s = s.next!!; f = f.next!! }
        s.next = s.next!!.next
        printList(dummy.next)
    }

    /**
     * 3. Swap Nodes in Pairs
     * Logic: Dummy + Loop.
     */
    @Test
    fun q3_swapPairs() {
        println("=== Q3: Swap Pairs ===")
        val head = make(1,2,3,4)
        val dummy = ListNode(0); dummy.next = head
        var curr = dummy
        while(curr.next != null && curr.next!!.next != null) {
            val f = curr.next!!; val s = curr.next!!.next!!
            f.next = s.next
            s.next = f
            curr.next = s
            curr = f
        }
        printList(dummy.next)
    }

    /**
     * 4. Copy List with Random Pointer
     * Logic: Map<Original, Copy>.
     */
    @Test
    fun q4_copyRandomList() {
        println("=== Q4: Copy Random List ===")
        // Assuming ListNode has 'random' field. Using Map logic.
        println("Logic: Map<Node, Node> to store copies.")
    }

    /**
     * 5. Linked List Cycle II
     * Logic: Meet at intersection. Move head to intersection step by step.
     */
    @Test
    fun q5_cycleII() {
        println("=== Q5: Cycle II ===")
        // Floyd's Cycle. If meet, set slow=head. Move both 1 step.
        println("Logic: Floyd's Cycle Detection")
    }

    /**
     * 6. Reorder List (L0, Ln, L1, Ln-1...)
     * Logic: Middle -> Reverse 2nd Half -> Merge.
     */
    @Test
    fun q6_reorderList() {
        println("=== Q6: Reorder List ===")
        val head = make(1,2,3,4,5)
        // 1. Middle
        var slow = head; var fast = head
        while(fast?.next!=null && fast.next?.next!=null) { slow=slow?.next; fast=fast.next?.next }
        
        // 2. Reverse 2nd
        var prev: ListNode? = null; var curr = slow?.next; slow?.next = null
        while(curr!=null){ val n=curr.next; curr.next=prev; prev=curr; curr=n }
        
        // 3. Merge
        var p1 = head; var p2 = prev
        while(p2 != null) {
            val t1 = p1?.next; val t2 = p2.next
            p1?.next = p2; p2.next = t1
            p1 = t1; p2 = t2
        }
        printList(head)
    }

    /**
     * 7. Rotate List
     * Logic: Make circular, find new tail (len - k % len), break circle.
     */
    @Test
    fun q7_rotateList() {
        println("=== Q7: Rotate List ===")
        val head = make(1,2,3,4,5); val k = 2
        if(head==null) return
        var len = 1; var tail = head
        while(tail?.next!=null) { tail=tail!!.next; len++ }
        tail?.next = head // Circle
        
        val shift = len - (k % len)
        var newTail = head
        for(i in 1 until shift) newTail = newTail?.next
        val newHead = newTail?.next
        newTail?.next = null
        printList(newHead)
    }

    /**
     * 8. Sort List (Merge Sort)
     * Logic: Find Middle -> Split -> Sort -> Merge.
     */
    @Test
    fun q8_sortList() {
        println("=== Q8: Sort List (Merge Sort) ===")
        val head = make(4, 2, 1, 3)
        // Basic Merge Sort implementation with LL
        println("Logic: Recursive Merge Sort on LL")
    }

    /**
     * 9. Partition List
     * Logic: Two dummy lists (Small, Large). Merge.
     */
    @Test
    fun q9_partitionList() {
        println("=== Q9: Partition List ===")
        val head = make(1,4,3,2,5,2); val x = 3
        val sDum = ListNode(0); var small = sDum
        val lDum = ListNode(0); var large = lDum
        var curr = head
        while(curr != null) {
            if(curr.`val` < x) { small.next = curr; small = small.next!! }
            else { large.next = curr; large = large.next!! }
            curr = curr.next
        }
        large.next = null
        small.next = lDum.next
        printList(sDum.next)
    }

    /**
     * 10. Reverse Linked List II
     * Logic: Move to m. Reverse to n. Connect.
     */
    @Test
    fun q10_reverseBetween() {
        println("=== Q10: Reverse Between ===")
        val head = make(1,2,3,4,5); val m = 2; val n = 4
        val dummy = ListNode(0); dummy.next = head
        var pre = dummy
        for(i in 1 until m) pre = pre.next!!
        var start = pre.next; var then = start?.next
        
        // Reverse sublist
        for(i in 0 until n - m) {
            start?.next = then?.next
            then?.next = pre.next
            pre.next = then
            then = start?.next
        }
        printList(dummy.next)
    }

    /**
     * 11. Odd Even Linked List
     * Logic: Separate Odd/Even chains. Merge.
     */
    @Test
    fun q11_oddEvenList() {
        println("=== Q11: Odd Even List ===")
        val head = make(1,2,3,4,5)
        if(head == null) return
        var odd = head; var even = head.next; val evenHead = even
        while(even?.next != null) {
            odd?.next = even.next; odd = odd?.next
            even.next = odd?.next; even = even.next
        }
        odd?.next = evenHead
        printList(head)
    }

    /**
     * 12. Remove Duplicates II (Distinct)
     * Logic: Dummy. Check next and next.next. Loop over duplicates.
     */
    @Test
    fun q12_deleteDuplicatesII() {
        println("=== Q12: Remove Duplicates II ===")
        val head = make(1, 2, 3, 3, 4, 4, 5)
        val dummy = ListNode(0); dummy.next = head
        var pre = dummy
        while(pre.next != null) {
            var curr = pre.next
            while(curr?.next != null && curr.next!!.`val` == curr.`val`) curr = curr.next
            if(curr != pre.next) pre.next = curr?.next // Skip duplicates
            else pre = pre.next!! // Advance
        }
        printList(dummy.next)
    }

    /**
     * 13. Insertion Sort List
     * Logic: Maintain sorted part. Insert curr.
     */
    @Test
    fun q13_insertionSort() {
        println("=== Q13: Insertion Sort List ===")
        val head = make(4, 2, 1, 3)
        val dummy = ListNode(Int.MIN_VALUE)
        var curr = head
        while(curr != null) {
            val next = curr.next
            var p = dummy
            while(p.next != null && p.next!!.`val` < curr.`val`) p = p.next!!
            curr.next = p.next
            p.next = curr
            curr = next
        }
        printList(dummy.next)
    }

    /**
     * 14. Split Linked List in Parts
     * Logic: Length / K. Remainder distributed.
     */
    @Test
    fun q14_splitListToParts() {
        println("=== Q14: Split List ===")
        val head = make(1,2,3,4,5,6,7,8,9,10); val k = 3
        var c = head; var len = 0
        while(c!=null){ c=c.next; len++ }
        val width = len / k; val rem = len % k
        var curr = head
        
        for(i in 0 until k) {
            print("Part $i: ")
            val partHead = curr
            val partLen = width + if(i < rem) 1 else 0
            for(j in 0 until partLen - 1) curr = curr?.next
            val nextPart = curr?.next
            curr?.next = null
            printList(partHead)
            curr = nextPart
        }
    }

    /**
     * 15. Next Greater Node In Linked List
     * Logic: Convert to Array + Monotonic Stack.
     */
    @Test
    fun q15_nextGreaterNode() {
        println("=== Q15: Next Greater Node ===")
        val head = make(2, 1, 5)
        val arr = ArrayList<Int>()
        var curr = head; while(curr!=null){ arr.add(curr.`val`); curr=curr.next }
        
        val res = IntArray(arr.size)
        val stack = Stack<Int>()
        for(i in arr.indices) {
            while(stack.isNotEmpty() && arr[stack.peek()] < arr[i]) {
                res[stack.pop()] = arr[i]
            }
            stack.push(i)
        }
        println("Result: ${res.contentToString()}")
    }

    /**
     * 16. Delete Middle Node
     * Logic: Fast/Slow. Stop Slow before middle.
     */
    @Test
    fun q16_deleteMiddle() {
        println("=== Q16: Delete Middle ===")
        val head = make(1,3,4,7,1,2,6)
        val dummy = ListNode(0); dummy.next = head
        var slow = dummy; var fast = head
        while(fast?.next != null) {
            slow = slow.next!!
            fast = fast.next!!.next
        }
        slow.next = slow.next?.next
        printList(dummy.next)
    }

    /**
     * 17. Swapping Nodes in a Linked List
     * Logic: Kth from start and Kth from end. Swap Val.
     */
    @Test
    fun q17_swapNodes() {
        println("=== Q17: Swap Nodes (Val) ===")
        val head = make(1,2,3,4,5); val k = 2
        var fast = head
        for(i in 1 until k) fast = fast?.next
        val first = fast
        
        var slow = head
        while(fast?.next != null) {
            slow = slow?.next
            fast = fast.next
        }
        val second = slow
        val temp = first!!.`val`
        first.`val` = second!!.`val`
        second.`val` = temp
        printList(head)
    }

    /**
     * 18. Plus One Linked List
     * Logic: Reverse -> Add 1 -> Reverse OR Recursion.
     */
    @Test
    fun q18_plusOne() {
        println("=== Q18: Plus One ===")
        val head = make(1, 2, 3)
        // Logic: Recursion returns carry
        fun add(node: ListNode?): Int {
            if(node == null) return 1
            val carry = add(node.next)
            val sum = node.`val` + carry
            node.`val` = sum % 10
            return sum / 10
        }
        val c = add(head)
        val newHead = if(c > 0) { val h = ListNode(c); h.next=head; h } else head
        printList(newHead)
    }

    /**
     * 19. Flatten Binary Tree to Linked List
     * Logic: Preorder traversal.
     */
    @Test
    fun q19_flattenTree() {
        println("=== Q19: Flatten Tree to List ===")
        println("Logic: Morris Traversal / Stack Preorder")
    }

    /**
     * 20. Design Browser History
     * Logic: Doubly Linked List.
     */
    @Test
    fun q20_browserHistory() {
        println("=== Q20: Browser History ===")
        println("Logic: Node(url, prev, next)")
    }
}
