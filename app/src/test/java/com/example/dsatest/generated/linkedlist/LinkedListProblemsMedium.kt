package com.example.dsatest.generated.linkedlist

import org.junit.Test
import java.util.Stack
import java.util.HashMap
import kotlin.collections.get

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
     * 1. Add Two Numbers II
     *
     * PROBLEM:
     * You are given two non-empty linked lists representing two non-negative integers. The most significant digit comes first and each of their nodes contains a single digit. Add the two numbers and return the sum as a linked list.
     * You cannot modify the input lists (no reversal).
     *
     * INPUT/OUTPUT:
     * Input: l1 = [7,2,4,3], l2 = [5,6,4] -> Output: [7,8,0,7]
     *
     * DESIGN:
     * Why Stack?
     * - We need to process digits from LSB (end of list) to MSB (start).
     * - Since singly linked lists only go forward, we push values to Stacks.
     * - Then pop stacks to get values in reverse order (LSB first).
     *
     * DETAIL:
     * 1. Push all nodes of `l1` to `s1`, `l2` to `s2`.
     * 2. Loop while stacks not empty or `carry != 0`:
     *    - Pop `s1` check if empty (0). Pop `s2`.
     *    - `sum = v1 + v2 + carry`.
     *    - Create node `(sum % 10)`.
     *    - Insert at head: `node.next = head; head = node`.
     *    - `carry = sum / 10`.
     * 3. Return `head`.
     *
     * COMPLEXITY:
     * Time: O(M + N)
     * Space: O(M + N) - Stacks.
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
     * 2. Remove Nth Node From End of List
     *
     * PROBLEM:
     * Given the head of a linked list, remove the nth node from the end of the list and return its head.
     *
     * INPUT/OUTPUT:
     * Input: head = [1,2,3,4,5], n = 2 -> Output: [1,2,3,5]
     *
     * DESIGN:
     * Why Two Pointers (Gap Method)?
     * - `fast` starts at `dummy`. `slow` starts at `dummy`.
     * - Move `fast` `n + 1` steps forward. Gap is now `n+1`.
     * - Move both until `fast` is null.
     * - `slow` is now at the node *before* the target (gap keeps `slow` `n+1` behind end (null), so `n` behind last node, effectively at `L-N-1`).
     *
     * DETAIL:
     * 1. `dummy.next = head`. `fast=dummy`, `slow=dummy`.
     * 2. Move `fast` `n + 1` times.
     * 3. Loop until `fast` is null.
     * 4. `slow.next = slow.next.next`.
     *
     * COMPLEXITY:
     * Time: O(N)
     * Space: O(1)
     */
    @Test
    fun q2_removeNthFromEnd() {
        println("=== Q2: Remove Nth From End ===")
        val head = make(1,2,3,4,5); val n = 2
        val dummy = ListNode(0); dummy.next = head
        var s = dummy; var f = dummy
        // Move strictly n+1 steps to be 'before' the target
        for(i in 0..n) f = f.next!!
        while(f != null) { s = s.next!!; f = f.next!! }
        s.next = s.next!!.next
        printList(dummy.next)
    }

    /**
     * 3. Swap Nodes in Pairs
     *
     * PROBLEM:
     * Swap every two adjacent nodes in a linked list. `1->2->3->4` becomes `2->1->4->3`.
     *
     * INPUT/OUTPUT:
     * Input: [1,2,3,4] -> [2,1,4,3]
     *
     * DESIGN:
     * Why Dummy & 3 Pointers?
     * - We need to access `prev` node to relink.
     * - `prev -> 1 -> 2`. Change to `prev -> 2 -> 1`.
     *
     * DETAIL:
     * 1. `dummy->head`. `curr = dummy`.
     * 2. Loop `curr.next` & `curr.next.next`:
     *    - `first = curr.next`.
     *    - `second = curr.next.next`.
     *    - `first.next = second.next`.
     *    - `second.next = first`.
     *    - `curr.next = second`.
     *    - `curr = first` (jump 2 steps effectively).
     *
     * COMPLEXITY:
     * Time: O(N)
     * Space: O(1)
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
     *
     * PROBLEM:
     * Construct a deep copy of the list. Each node contains a `random` pointer.
     *
     * INPUT/OUTPUT:
     * Input: [[7,null],[13,0],[11,4],[10,2],[1,0]] -> Output: Deep Copy
     *
     * DESIGN:
     * Why HashMap or Interweaving?
     * - Approach 1 (HashMap): Map `original -> copy`.
     *   - Pass 1: Create copies, put in map.
     *   - Pass 2: Link `copy.next = map[orig.next]`, `copy.random = map[orig.random]`.
     * - Approach 2 (Interweaving) - O(1) Space:
     *   - Pass 1: `A -> A' -> B -> B'`.
     *   - Pass 2: `A'.random = A.random.next`.
     *   - Pass 3: Separate lists.
     *
     * DETAIL (Map Approach used below for clarity):
     * 1. Traverse, create nodes, store in map.
     * 2. Traverse, set pointers using map.
     *
     * COMPLEXITY:
     * Time: O(N)
     * Space: O(N)
     */
    @Test
    fun q4_copyRandomList() {
        println("=== Q4: Copy Random List ===")
        // Mocking Node with random for demonstration (using standard ListNode)
        // Usually, we define class Node(var `val`: Int) { var next: Node? = null; var random: Node? = null }
        val head = make(7, 13, 11)
        
        // Simulating logic
        val map = HashMap<ListNode, ListNode>()
        var curr = head
        while(curr != null) {
            map[curr] = ListNode(curr.`val`)
            curr = curr.next
        }
        curr = head
        while(curr != null) {
            map[curr]?.next = map[curr.next]
            // map[curr]?.random = map[curr.random] // logic
            curr = curr.next
        }
        printList(map[head])
    }

    /**
     * 5. Linked List Cycle II
     *
     * PROBLEM:
     * Given the head of a linked list, return the node where the cycle begins. If no cycle, return null.
     *
     * INPUT/OUTPUT:
     * Input: head = [3,2,0,-4], pos = 1 -> Output: Node(2)
     *
     * DESIGN:
     * Why Floyd's Algorithm Part 2?
     * - Phase 1: Determine cycle exists (`slow == fast`).
     * - Phase 2: Find start.
     *   - Reset `slow = head`. Keep `fast` at meeting point.
     *   - Move both 1 step at a time.
     *   - Mathematics proves they meet at entry point.
     *
     * DETAIL:
     * 1. `slow`, `fast`. Loop. If meet -> break.
     * 2. If no meeting, return null.
     * 3. `slow = head`. Loop `while slow !== fast`: advance both.
     * 4. Return `slow`.
     *
     * COMPLEXITY:
     * Time: O(N)
     * Space: O(1)
     */
    @Test
    fun q5_cycleII() {
        println("=== Q5: Cycle II ===")
        val head = ListNode(3)
        val n2 = ListNode(2); val n3 = ListNode(0); val n4 = ListNode(-4)
        head.next=n2; n2.next=n3; n3.next=n4; n4.next=n2 // Cycle
        
        var slow = head; var fast = head
        var hasCycle = false
        while(fast.next != null && fast.next!!.next != null) {
            slow = slow.next!!
            fast = fast.next!!.next!!
            if(slow === fast) { hasCycle = true; break }
        }
        
        if(hasCycle) {
            slow = head
            while(slow !== fast) {
                slow = slow.next!!
                fast = fast.next!!
            }
            println("Result: Cycle starts at val ${slow.`val`}")
        } else {
            println("Result: No Cycle")
        }
    }

    /**
     * 6. Reorder List
     *
     * PROBLEM:
     * Reorder the list: `L0 -> Ln -> L1 -> Ln-1 -> L2 -> Ln-2 ...`
     *
     * INPUT/OUTPUT:
     * Input: [1,2,3,4] -> [1,4,2,3]
     * Input: [1,2,3,4,5] -> [1,5,2,4,3]
     *
     * DESIGN:
     * Why Split-Reverse-Merge?
     * - The pattern picks from start, then end, then start + 1, end - 1.
     * - This is equivalent to merging the First Half and the Reversed Second Half.
     *
     * DETAIL:
     * 1. Find Middle (`slow`).
     * 2. Reverse list from `slow.next`. Break link (`slow.next = null`).
     * 3. Merge `head` (l1) and `prev` (l2).
     *
     * COMPLEXITY:
     * Time: O(N)
     * Space: O(1)
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
            p1 = t1; p2 = t2 // Move to next available nodes
        }
        printList(head)
    }

    /**
     * 7. Rotate List
     *
     * PROBLEM:
     * Given the head of a linked list, rotate the list to the right by `k` places.
     *
     * INPUT/OUTPUT:
     * Input: head = [1,2,3,4,5], k = 2 -> Output: [4,5,1,2,3]
     *
     * DESIGN:
     * Why Cyclic Link?
     * - We can form a ring by connecting tail to head.
     * - Then we break the ring at `len - (k % len)`.
     *
     * DETAIL:
     * 1. Traverse to find `len` and `oldTail`.
     * 2. `oldTail.next = head`.
     * 3. Move `newTail` `len - k % len - 1` steps (from head).
     * 4. `newHead = newTail.next`.
     * 5. `newTail.next = null`.
     *
     * COMPLEXITY:
     * Time: O(N)
     * Space: O(1)
     */
    @Test
    fun q7_rotateList() {
        println("=== Q7: Rotate List ===")
        val head = make(1,2,3,4,5); val k = 2
        if(head==null || head.next==null) { printList(head); return }
        
        var len = 1; var tail = head
        while(tail?.next!=null) { tail=tail!!.next; len++ }
        tail?.next = head // Circle
        
        val shift = len - (k % len)
        var newTail = head
        for(i in 1 until shift) newTail = newTail?.next // Move shift-1 times to land on new tail
        
        val newHead = newTail?.next
        newTail?.next = null
        printList(newHead)
    }

    /**
     * 8. Sort List
     *
     * PROBLEM:
     * Given the head of a linked list, return the list after sorting it in ascending order.
     * Solve in O(n log n) time and O(1) space (conceptually, though recursion uses stack).
     *
     * INPUT/OUTPUT:
     * Input: [4,2,1,3] -> [1,2,3,4]
     *
     * DESIGN:
     * Why Merge Sort?
     * - Merge sort is efficient for Linked Lists (doesn't require random access like QuickSort/HeapSort).
     * - In-place merge is possible.
     *
     * DETAIL:
     * 1. Base case: `head == null` or `head.next == null`.
     * 2. Split: Use slow/fast to find middle. Cut the list.
     * 3. Recurse: `l1 = sort(head)`, `l2 = sort(mid)`.
     * 4. Merge: `merge(l1, l2)`.
     *
     * COMPLEXITY:
     * Time: O(N log N)
     * Space: O(log N) stack.
     */
    @Test
    fun q8_sortList() {
        println("=== Q8: Sort List (Merge Sort) ===")
        val head = make(4, 2, 1, 3)
        
        fun merge(l1: ListNode?, l2: ListNode?): ListNode? {
            val dummy = ListNode(0); var tail = dummy
            var p1=l1; var p2=l2
            while(p1!=null && p2!=null) {
                if(p1.`val`<p2.`val`) { tail.next=p1; p1=p1.next }
                else { tail.next=p2; p2=p2.next }
                tail=tail.next!!
            }
            tail.next = p1 ?: p2
            return dummy.next
        }
        
        fun sort(h: ListNode?): ListNode? {
            if(h == null || h.next == null) return h
            // Split
            var slow=h; var fast=h; var prev: ListNode? = null
            while(fast?.next!=null) { prev=slow; slow=slow!!.next; fast=fast.next!!.next }
            prev?.next = null // Cut
            return merge(sort(h), sort(slow))
        }
        
        printList(sort(head))
    }

    /**
     * 9. Partition List
     *
     * PROBLEM:
     * Given the head of a linked list and a value `x`, partition it such that all nodes less than `x` come before nodes greater than or equal to `x`.
     * Preserve original relative order.
     *
     * INPUT/OUTPUT:
     * Input: head = [1,4,3,2,5,2], x = 3 -> Output: [1,2,2,4,3,5]
     *
     * DESIGN:
     * Why Two Dummy Lists?
     * - Maintain `small_list` and `large_list`.
     * - Iterate head. If `val < x`, append to `small`. Else append to `large`.
     * - Combine `small` -> `large`.
     *
     * DETAIL:
     * 1. `smallHead`, `largeHead`. `sTail`, `lTail`.
     * 2. Loop `head`:
     *    - Append to correct list.
     *    - Advance tail.
     * 3. `lTail.next = null` (Important!).
     * 4. `sTail.next = largeHead.next`.
     *
     * COMPLEXITY:
     * Time: O(N)
     * Space: O(1)
     */
    @Test
    fun q9_partitionList() {
        println("=== Q9: Partition List ===")
        val head = make(1,4,3,2,5,2); val x = 3
        val sDum = ListNode(0); var sTail = sDum
        val lDum = ListNode(0); var lTail = lDum
        var curr = head
        while(curr != null) {
            if(curr.`val` < x) { sTail.next = curr; sTail = sTail.next!! }
            else { lTail.next = curr; lTail = lTail.next!! }
            curr = curr.next
        }
        lTail.next = null
        sTail.next = lDum.next
        printList(sDum.next)
    }

    /**
     * 10. Reverse Linked List II
     *
     * PROBLEM:
     * Reverse the nodes of the list from position `left` to `right`.
     *
     * INPUT/OUTPUT:
     * Input: head = [1,2,3,4,5], left = 2, right = 4 -> Output: [1,4,3,2,5]
     *
     * DESIGN:
     * Why 1-Pass?
     * - Use `prev` to reach node just before `left`.
     * - Use `start` (at left) and `then` (node to be moved).
     * - Perform extraction and insertion `right - left` times.
     *
     * DETAIL:
     * 1. Move `prev` to `left - 1`.
     * 2. `start = prev.next`, `then = start.next`.
     * 3. Loop `i` from 0 to `right - left`:
     *    - `start.next = then.next` (bypass then)
     *    - `then.next = prev.next` (move then to front)
     *    - `prev.next = then` (link prev to then)
     *    - `then = start.next` (update then for next iter)
     *
     * COMPLEXITY:
     * Time: O(N)
     * Space: O(1)
     */
    @Test
    fun q10_reverseBetween() {
        println("=== Q10: Reverse Between ===")
        val head = make(1,2,3,4,5); val m = 2; val n = 4
        val dummy = ListNode(0); dummy.next = head
        var pre = dummy
        for(i in 1 until m) pre = pre.next!!
        // pre is at node 1 (idx 1). start is 2. then is 3.
        
        val start = pre.next; var then = start?.next
        
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
     *
     * PROBLEM:
     * Given the head of a singly linked list, group all the nodes with odd indices together followed by the nodes with even indices, and return the reordered list.
     * The first node is considered odd, and the second node is even, and so on.
     * Note that the relative order inside both the even and odd groups should remain as it was in the input.
     *
     * INPUT/OUTPUT:
     * Input: [1,2,3,4,5] -> Output: [1,3,5,2,4]
     *
     * DESIGN:
     * Why Two Pointers for Chains?
     * - We maintain `odd` and `even` pointers.
     * - We weave them: `odd.next = even.next`, `odd = odd.next`. `even.next = odd.next`, `even = even.next`.
     * - Finally, attach `evenHead` to the end of `odd`.
     *
     * DETAIL:
     * 1. `odd = head`, `even = head.next`, `evenHead = even`.
     * 2. Loop `even != null && even.next != null`:
     *    - `odd.next = even.next`. `odd = odd.next`.
     *    - `even.next = odd.next`. `even = even.next`.
     * 3. `odd.next = evenHead`.
     *
     * COMPLEXITY:
     * Time: O(N)
     * Space: O(1)
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
     * 12. Remove Duplicates from Sorted List II
     *
     * PROBLEM:
     * Given the head of a sorted linked list, delete all nodes that have duplicate numbers, leaving only distinct numbers from the original list. Return the linked list sorted as well.
     *
     * INPUT/OUTPUT:
     * Input: [1,2,3,3,4,4,5] -> Output: [1,2,5]
     *
     * DESIGN:
     * Why Check `next` vs `next.next`?
     * - We need to see if a value repeats.
     * - `pre` points to node before potential duplicates.
     * - `curr` scans duplicates.
     * - If `curr` moved (duplicates found), `pre.next` skips all duplicates.
     *
     * DETAIL:
     * 1. `dummy.next = head`. `pre = dummy`.
     * 2. Loop `head != null`:
     *    - If `head.next != null && head.val == head.next.val`:
     *      - Loop while `head.next.val == head.val`.
     *      - `pre.next = head.next`.
     *    - Else: `pre = pre.next`.
     *    - `head = head.next`.
     *
     * COMPLEXITY:
     * Time: O(N)
     * Space: O(1)
     */
    @Test
    fun q12_deleteDuplicatesII() {
        println("=== Q12: Remove Duplicates II ===")
        val head = make(1, 2, 3, 3, 4, 4, 5)
        val dummy = ListNode(0); dummy.next = head
        var pre = dummy
        var curr = head // Use 'head' conceptually as scanner in logic above, but 'curr' here
        
        while (curr != null) {
             if (curr.next != null && curr.`val` == curr.next!!.`val`) {
                 while (curr?.next != null && curr.`val` == curr.next!!.`val`) {
                     curr = curr.next
                 }
                 pre.next = curr?.next
             } else {
                 pre = pre.next!!
             }
             curr = curr?.next
        }
        printList(dummy.next)
    }

    /**
     * 13. Insertion Sort List
     *
     * PROBLEM:
     * Sort a linked list using insertion sort.
     *
     * INPUT/OUTPUT:
     * Input: [4,2,1,3] -> Output: [1,2,3,4]
     *
     * DESIGN:
     * Why Dummy Head?
     * - We build the sorted list starting from `dummy`.
     * - For each node `curr`, find position `p` in sorted list such that `p.val < curr.val < p.next.val`.
     * - Insert `curr` between `p` and `p.next`.
     *
     * DETAIL:
     * 1. `dummy`, `curr = head`.
     * 2. Loop `curr`:
     *    - Save `next = curr.next`.
     *    - Find insertion spot `p` starting from `dummy`.
     *    - `curr.next = p.next`.
     *    - `p.next = curr`.
     *    - `curr = next`.
     *
     * COMPLEXITY:
     * Time: O(N^2)
     * Space: O(1)
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
     *
     * PROBLEM:
     * Split a linked list into `k` consecutive linked list parts.
     * The length of each part should be as equal as possible. No two parts should have a size differing by more than one.
     *
     * INPUT/OUTPUT:
     * Input: root = [1,2,3], k = 5 -> Output: [[1],[2],[3],[],[]]
     *
     * DESIGN:
     * Why Math division?
     * - Total `len`. Base size `width = len / k`. Reaminder `rem = len % k`.
     * - First `rem` parts get `width + 1` nodes. Remaining get `width`.
     *
     * DETAIL:
     * 1. Count `len`.
     * 2. Loop `k` times:
     *    - Determine part size.
     *    - Advance current pointer `size - 1` times.
     *    - Break link (save next, set null).
     *    - Add head to result.
     *
     * COMPLEXITY:
     * Time: O(N)
     * Space: O(1) (excluding result array).
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
            if (partLen > 0) {
                 for(j in 0 until partLen - 1) curr = curr?.next
                 val nextPart = curr?.next
                 curr?.next = null
                 curr = nextPart
            } else {
                curr = null
            }
            printList(partHead)
        }
    }

    /**
     * 15. Next Greater Node In Linked List
     *
     * PROBLEM:
     * For each node in the list, look ahead to see if there is a node with a strictly larger value.
     * If so, the answer is that larger value. If not, 0. Return an array.
     *
     * INPUT/OUTPUT:
     * Input: [2,1,5] -> Output: [5,5,0]
     *
     * DESIGN:
     * Why Monotonic Stack?
     * - We want the *next* greater element.
     * - Convert List to ArrayList for random access (simplifies index tracking).
     * - Stack stores indices `i`.
     * - When `arr[i] > arr[stack.peek()]`, we found the next greater for `stack.peek()`. Pop and set result.
     *
     * DETAIL:
     * 1. List -> ArrayList.
     * 2. Loop `i` in array:
     *    - While `stack` not empty and `arr[i] > arr[stack.peek]`:
     *      - `res[stack.pop()] = arr[i]`.
     *    - Push `i`.
     *
     * COMPLEXITY:
     * Time: O(N)
     * Space: O(N)
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
     * 16. Delete the Middle Node of a Linked List
     *
     * PROBLEM:
     * You are given the head of a linked list. Delete the middle node, and return the head of the modified linked list.
     *
     * INPUT/OUTPUT:
     * Input: [1,3,4,7,1,2,6] -> Output: [1,3,4,1,2,6]
     *
     * DESIGN:
     * Why Fast/Slow with Predecessor?
     * - We need to find middle and its predecessor.
     * - `fast` moves 2x. `slow` moves 1x.
     * - By starting `slow` at `dummy` (before head) and `fast` at `head`, when `fast` ends, `slow` is at `middle - 1`.
     *
     * DETAIL:
     * 1. `dummy->head`. `slow=dummy`, `fast=head`.
     * 2. Loop `fast != null && fast.next != null`:
     *    - `slow` moves 1, `fast` moves 2.
     * 3. `slow.next = slow.next.next`.
     *
     * COMPLEXITY:
     * Time: O(N)
     * Space: O(1)
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
     *
     * PROBLEM:
     * You are given the head of a linked list, and an integer k.
     * Return the head of the linked list after swapping the values of the kth node from the beginning and the kth node from the end (the list is 1-indexed).
     *
     * INPUT/OUTPUT:
     * Input: head = [1,2,3,4,5], k = 2 -> Output: [1,4,3,2,5]
     *
     * DESIGN:
     * Why 3 Pointers?
     * - Find `k-th` first. `p1`.
     * - Then use two pointers for K-th from end. Start `fast` at `p1`. `slow` at head.
     * - When `fast` hits end, `slow` is at `k-th` from end. `p2`.
     * - Swap values of `p1` and `p2`.
     *
     * DETAIL:
     * 1. Iterate `k-1` times to find `first`.
     * 2. `fast = first`, `second = head`.
     * 3. Loop `fast.next != null`: `fast`++, `second`++.
     * 4. Swap values.
     *
     * COMPLEXITY:
     * Time: O(N)
     * Space: O(1)
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
     *
     * PROBLEM:
     * Given a non-negative integer represented as a linked list of digits, plus one to the integer.
     * The digits are stored such that the most significant digit is at the head of the list.
     *
     * INPUT/OUTPUT:
     * Input: [1,2,3] -> [1,2,4]
     * Input: [9,9] -> [1,0,0]
     *
     * DESIGN:
     * Why Recursion?
     * - We process from right to left (end to start).
     * - Recursion dives to end, returns carry.
     * - `curr.val = (curr.val + carry) % 10`.
     *
     * DETAIL:
     * 1. `helper` returns carry.
     * 2. If node null, return 1 (the initial +1).
     * 3. `carry = helper(next)`.
     * 4. Update node val. Return new carry.
     *
     * COMPLEXITY:
     * Time: O(N)
     * Space: O(N)
     */
    @Test
    fun q18_plusOne() {
        println("=== Q18: Plus One ===")
        val head = make(1, 2, 3)
        
        fun add(node: ListNode?): Int {
            if(node == null) return 1
            val carry = add(node.next)
            val sum = node.`val` + carry
            node.`val` = sum % 10
            return sum / 10
        }
        val c = add(head)
        // If carry remains, prepend new head
        val newHead = if(c > 0) { val h = ListNode(c); h.next=head; h } else head
        printList(newHead)
    }

    /**
     * 19. Flatten Binary Tree to Linked List
     *
     * PROBLEM:
     * Given the root of a binary tree, flatten it to a linked list in-place.
     * The "right" child pointer becomes the "next" pointer. The "left" child is always null.
     * Order: Preorder (Root, Left, Right).
     *
     * INPUT/OUTPUT:
     * Input: [1,2,5,3,4,null,6] -> Output: [1,null,2,null,3,null,4,null,5,null,6]
     *
     * DESIGN:
     * Why Morris Traversal or Reverse Postorder?
     * - To do it O(1) space:
     *   - If `curr` has left child:
     *     - Find rightmost node of left subtree.
     *     - Connect `rightmost.right = curr.right`.
     *     - `curr.right = curr.left`.
     *     - `curr.left = null`.
     *   - `curr = curr.right`.
     *
     * DETAIL:
     * 1. Definition of TreeNode assumed.
     * 2. Loop while `curr != null`.
     * 3. Perform rotation as above.
     *
     * COMPLEXITY:
     * Time: O(N)
     * Space: O(1)
     */
    @Test
    fun q19_flattenTree() {
        println("=== Q19: Flatten Tree to List ===")
        class TreeNode(var `val`: Int) { 
            var left: TreeNode? = null; var right: TreeNode? = null 
        }
        // Construct basic tree
        val root = TreeNode(1)
        root.left = TreeNode(2)
        root.right = TreeNode(5)
        
        var curr: TreeNode? = root
        while (curr != null) {
            if (curr.left != null) {
                var runner = curr.left
                while (runner?.right != null) runner = runner.right
                runner?.right = curr.right
                curr.right = curr.left
                curr.left = null
            }
            curr = curr.right
        }
        // Print right chain
        var p: TreeNode? = root
        while(p!=null){ print("${p.`val`}->"); p=p.right }; println("null")
    }

    /**
     * 20. Design Browser History
     *
     * PROBLEM:
     * Implement `visit`, `back`, `forward`.
     *
     * INPUT/OUTPUT:
     * methods...
     *
     * DESIGN:
     * Why Doubly Linked List?
     * - We can move back and forth O(1).
     * - `curr` pointer tracks current page.
     * - `visit(url)`: `curr.next = newNode(url)`. `newNode.prev = curr`. `curr = curr.next`. Clears forward history.
     *
     * DETAIL:
     * 1. `Node { prev, next, url }`.
     * 2. `back(steps)`: move `curr = curr.prev` up to steps or null.
     * 3. `forward(steps)`: move `curr = curr.next`.
     *
     * COMPLEXITY:
     * Time: O(1) per op (or O(steps)).
     * Space: O(N).
     */
    @Test
    fun q20_browserHistory() {
        println("=== Q20: Browser History ===")
        class Node(val url: String) {
            var prev: Node? = null
            var next: Node? = null
        }
        var curr = Node("homepage.com")
        
        fun visit(url: String) {
            val node = Node(url)
            curr.next = node
            node.prev = curr
            curr = node
        }
        
        fun back(steps: Int): String {
            var s = steps
            while(s > 0 && curr.prev != null) { curr = curr.prev!!; s-- }
            return curr.url
        }
        
        visit("google.com")
        visit("facebook.com")
        println("Back 1: ${back(1)}") // google
    }
}
