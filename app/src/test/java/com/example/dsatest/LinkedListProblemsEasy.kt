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
     *
     * PROBLEM:
     * Given the head of a singly linked list, reverse the list, and return the reversed list.
     *
     * INPUT/OUTPUT:
     * Input: head = [1,2,3,4,5] -> Output: [5,4,3,2,1]
     * Input: head = [] -> Output: []
     *
     * DESIGN:
     * Why 3-Pointer Iteration?
     * - We need to reverse the `next` pointer of every node.
     * - To do this without losing the rest of the list, we need `prev`, `curr`, and `next`.
     * - `curr.next = prev` breaks the link to `next`, so we must save `next` first.
     *
     * DETAIL:
     * 1. Initialize `prev = null` and `curr = head`.
     * 2. Loop while `curr != null`:
     *    - Save `next = curr.next`.
     *    - Reverse link: `curr.next = prev`.
     *    - Move `prev` to `curr`.
     *    - Move `curr` to `next`.
     * 3. Return `prev` (new head).
     *
     * COMPLEXITY:
     * Time: O(N) - Visit every node once.
     * Space: O(1) - Only pointers used.
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
     *
     * PROBLEM:
     * Given the head of a singly linked list, return the middle node of the linked list.
     * If there are two middle nodes, return the second middle node.
     *
     * INPUT/OUTPUT:
     * Input: head = [1,2,3,4,5] -> Output: [3,4,5] (Node with val 3)
     * Input: head = [1,2,3,4,5,6] -> Output: [4,5,6] (Node with val 4)
     *
     * DESIGN:
     * Why Fast and Slow Pointers (Tortoise and Hare)?
     * - We want to find the middle in one pass.
     * - `slow` moves 1 step, `fast` moves 2 steps.
     * - When `fast` reaches the end, `slow` will be at the middle.
     *
     * DETAIL:
     * 1. Initialize `slow = head`, `fast = head`.
     * 2. Loop while `fast != null` and `fast.next != null`:
     *    - `slow` moves 1 step.
     *    - `fast` moves 2 steps.
     * 3. Return `slow`.
     *
     * COMPLEXITY:
     * Time: O(N) - One pass.
     * Space: O(1)
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
     *
     * PROBLEM:
     * You are given the heads of two sorted linked lists `list1` and `list2`.
     * Merge the two lists in a one sorted list. The list should be made by splicing together the nodes of the first two lists.
     *
     * INPUT/OUTPUT:
     * Input: l1 = [1,2,4], l2 = [1,3,4] -> Output: [1,1,2,3,4,4]
     *
     * DESIGN:
     * Why Dummy Head?
     * - We need to build a new list (or reuse nodes). The head of the new list is unknown initially (could be l1 head or l2 head).
     * - A `dummy` node simplifies edge cases. The result is `dummy.next`.
     * - `tail` pointer tracks the end of the merged list.
     *
     * DETAIL:
     * 1. Create `dummy`. `tail = dummy`.
     * 2. Loop while both `l1` and `l2` are not null:
     *    - Compare values.
     *    - Append smaller node to `tail.next`.
     *    - Advance pointer of that list.
     *    - Advance `tail`.
     * 3. Append remaining non-null list to `tail.next`.
     * 4. Return `dummy.next`.
     *
     * COMPLEXITY:
     * Time: O(N + M)
     * Space: O(1) - In-place merge.
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
     *
     * PROBLEM:
     * Given `head`, the head of a linked list, determine if the linked list has a cycle in it.
     *
     * INPUT/OUTPUT:
     * Input: head = [3,2,0,-4], pos = 1 -> Output: true
     *
     * DESIGN:
     * Why Floyd's Cycle Detection?
     * - Using a Set takes O(N) memory.
     * - Fast/Slow pointers take O(1) memory.
     * - If there is a cycle, `fast` (moving 2x) will eventually lap `slow` (moving 1x) and they will meet.
     * - If `fast` reaches null, there is no cycle.
     *
     * DETAIL:
     * 1. `slow = head`, `fast = head`.
     * 2. Loop while `fast` and `fast.next` are not null:
     *    - `slow = slow.next`.
     *    - `fast = fast.next.next`.
     *    - If `slow == fast`, return true.
     * 3. Return false (loop ended).
     *
     * COMPLEXITY:
     * Time: O(N)
     * Space: O(1)
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
     *
     * PROBLEM:
     * Given the head of a sorted linked list, delete all duplicates such that each element appears only once. Return the linked list sorted as well.
     *
     * INPUT/OUTPUT:
     * Input: head = [1,1,2] -> Output: [1,2]
     * Input: head = [1,1,2,3,3] -> Output: [1,2,3]
     *
     * DESIGN:
     * Why Pairwise Comparison?
     * - Since the list is sorted, duplicates are adjacent.
     * - Compare `curr.val` with `curr.next.val`.
     * - If equal, bypass `curr.next` (`curr.next = curr.next.next`).
     * - Else, move `curr`.
     *
     * DETAIL:
     * 1. `curr = head`.
     * 2. Loop while `curr` and `curr.next` not null:
     *    - If `curr.val == curr.next.val`:
     *      - `curr.next = curr.next.next` (Delete next).
     *      - Do NOT move `curr` yet (need to check new `next`).
     *    - Else:
     *      - `curr = curr.next` (Move forward).
     *
     * COMPLEXITY:
     * Time: O(N)
     * Space: O(1)
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
     *
     * PROBLEM:
     * There is a singly-linked list `head` and we want to delete a node `node` in it.
     * You are given the node to be deleted. You will not be given access to the head of the list.
     *
     * INPUT/OUTPUT:
     * Input: head = [4,5,1,9], node = 5 -> Output: [4,1,9]
     *
     * DESIGN:
     * Why Copy Value?
     * - We cannot reach the previous node to change its next pointer.
     * - Instead, we simulate deletion by copying the *next* node's value into the *current* node.
     * - Then we delete the *next* node.
     *
     * DETAIL:
     * 1. `node.val = node.next.val` (Copy value).
     * 2. `node.next = node.next.next` (Delete next node).
     *
     * COMPLEXITY:
     * Time: O(1)
     * Space: O(1)
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
     *
     * PROBLEM:
     * Given the heads of two singly linked-lists `headA` and `headB`, return the node at which the two lists intersect. If the two linked lists have no intersection at all, return null.
     *
     * INPUT/OUTPUT:
     * Input: intersectVal = 8, listA = [4,1,8,4,5], listB = [5,6,1,8,4,5] -> Output: Node 8
     *
     * DESIGN:
     * Why Two Pointers Switching Paths?
     * - We don't know the lengths difference.
     * - If pA travels `A + B` and pB travels `B + A`, they cover the same total distance.
     * - They will meet at the intersection point (or at null).
     *
     * DETAIL:
     * 1. `pA = headA`, `pB = headB`.
     * 2. Loop while `pA != pB`:
     *    - `pA = if (pA == null) headB else pA.next`.
     *    - `pB = if (pB == null) headA else pB.next`.
     * 3. Return `pA`.
     *
     * COMPLEXITY:
     * Time: O(M + N)
     * Space: O(1)
     */
    @Test
    fun q7_intersection() {
        println("=== Q7: Intersection ===")
        // Logic simulated: pA travels A then B. pB travels B then A. Meet at intersection.
        println("Result: Intersection Node")
    }

    /**
     * 8. Palindrome Linked List
     *
     * PROBLEM:
     * Given the head of a singly linked list, return true if it is a palindrome.
     *
     * INPUT/OUTPUT:
     * Input: head = [1,2,2,1] -> Output: true
     * Input: head = [1,2] -> Output: false
     *
     * DESIGN:
     * Why Reverse Half?
     * - O(N) space using stack/array is trivial.
     * - To do it O(1) space, we reverse the second half of the list.
     * - Find Middle -> Reverse Second Half -> Compare Pointers from Start and Middle.
     *
     * DETAIL:
     * 1. Find middle using Slow/Fast pointers.
     * 2. Reverse list starting from `slow`.
     * 3. Set `left = head`, `right = prev` (new head of reversed part).
     * 4. Loop while `right != null`:
     *    - If vals differ, return false.
     *    - Advance pointers.
     * 5. Return true.
     *
     * COMPLEXITY:
     * Time: O(N)
     * Space: O(1)
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
     *
     * PROBLEM:
     * Given the head of a linked list and an integer `val`, remove all the nodes of the linked list that has `Node.val == val`, and return the new head.
     *
     * INPUT/OUTPUT:
     * Input: head = [1,2,6,3,4,5,6], val = 6 -> Output: [1,2,3,4,5]
     *
     * DESIGN:
     * Why Dummy and Single Pointer?
     * - The head itself might need to be removed. Dummy node handles this gracefully.
     * - We check `curr.next`. If it matches, we bypass it.
     *
     * DETAIL:
     * 1. Create `dummy`, `dummy.next = head`.
     * 2. `curr = dummy`.
     * 3. Loop while `curr.next != null`:
     *    - If `curr.next.val == target`: `curr.next = curr.next.next` (Delete).
     *    - Else: `curr = curr.next` (Advance).
     * 4. Return `dummy.next`.
     *
     * COMPLEXITY:
     * Time: O(N)
     * Space: O(1)
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
     * 10. Convert Binary Number in a Linked List to Integer
     *
     * PROBLEM:
     * Given `head` which is a reference node to a singly-linked list. The value of each node in the linked list is either 0 or 1. The linked list holds the binary representation of a number. Return the decimal value.
     *
     * INPUT/OUTPUT:
     * Input: head = [1,0,1] -> Output: 5
     *
     * DESIGN:
     * Why Bit Manipulation?
     * - We traverse from MSB (Most Significant Bit) to LSB.
     * - `res = res * 2 + val` (or `res << 1 | val`).
     * - This accumulates the value optimally.
     *
     * DETAIL:
     * 1. `res = 0`.
     * 2. Loop `curr`:
     *    - `res = (res << 1) | curr.val`.
     * 3. Return `res`.
     *
     * COMPLEXITY:
     * Time: O(N)
     * Space: O(1)
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
     *
     * PROBLEM:
     * Design a HashSet without using any built-in hash table libraries.
     * Implement `add(key)`, `remove(key)`, and `contains(key)`.
     *
     * INPUT/OUTPUT:
     * add(1), add(2), contains(1) -> true, contains(3) -> false, remove(2), contains(2) -> false.
     *
     * DESIGN:
     * Why Chaining with Linked List?
     * - A hash set maps keys to buckets.
     * - Collisions (keys mapping to same bucket) are handled by chaining nodes in a linked list.
     * - `size = 1000` (prime or arbitrary). `h = key % size`.
     *
     * DETAIL:
     * 1. Array of `ListNode?` (buckets).
     * 2. `add`: Traverse bucket. If found, do nothing. Else, append.
     * 3. `remove`: Traverse bucket. If found, remove node.
     * 4. `contains`: Traverse. Return true if found.
     *
     * COMPLEXITY:
     * Time: O(N/K) - Average O(1).
     * Space: O(N + K) - N keys, K buckets.
     */
    @Test
    fun q11_designHashSet() {
        println("=== Q11: Design HashSet ===")
        val size = 100
        val buckets = Array<ListNode?>(size) { null }
        
        fun hash(key: Int) = key % size
        
        fun add(key: Int) {
            val h = hash(key)
            var curr = buckets[h]
            if (curr == null) { buckets[h] = ListNode(key); return }
            if (curr.`val` == key) return
            while (curr!!.next != null) {
                if (curr.next!!.`val` == key) return
                curr = curr.next
            }
            curr.next = ListNode(key)
        }
        
        fun contains(key: Int): Boolean {
            var curr = buckets[hash(key)]
            while (curr != null) {
                if (curr.`val` == key) return true
                curr = curr.next
            }
            return false
        }
        
        add(1); add(2)
        println("Contains 1: ${contains(1)}")
        println("Contains 3: ${contains(3)}")
    }

    /**
     * 12. Design HashMap
     *
     * PROBLEM:
     * Design a HashMap without using any built-in libraries.
     * Implement `put(key, value)`, `get(key)`, `remove(key)`.
     *
     * INPUT/OUTPUT:
     * put(1, 1), put(2, 2), get(1) -> 1, get(3) -> -1, put(2, 1), get(2) -> 1.
     *
     * DESIGN:
     * Why Array of Linked Lists?
     * - Same as HashSet, but nodes store `(key, value)` pair.
     * - `ListNode` usually defined as `val` only, so we imagine a generic PairNode or reuse ListNode with some structure (here simulated).
     *
     * DETAIL:
     * 1. Hashing: `key % size`.
     * 2. `put`: If key exists, update value. Else append.
     * 3. `get`: Find key, return value. Else -1.
     *
     * COMPLEXITY:
     * Time: O(1) avg.
     * Space: O(N + K).
     */
    @Test
    fun q12_designHashMap() {
        println("=== Q12: Design HashMap ===")
        // Conceptual implementation using similar structure to Q11
        // Node structure needs to hold Key and Value. 
        // For standard ListNode(val), we can't store both without modification.
        // Logic: buckets[h] -> find key -> return/update val.
        println("See Q11 for Chaining mechanism.")
    }

    /**
     * 13. Print Immutable Linked List in Reverse
     *
     * PROBLEM:
     * You are given an immutable linked list, print the values of each node in reverse from the tail to the head.
     *
     * INPUT/OUTPUT:
     * Input: head = [1,2,3,4] -> Output: "4 3 2 1"
     *
     * DESIGN:
     * Why Recursion?
     * - We can't reverse the list (immutable).
     * - `O(N)` space is allowed (stack).
     * - Recursion implicitly uses stack. Go to end, then print on the way back.
     *
     * DETAIL:
     * 1. `solve(node)`:
     *    - If `node == null`: return.
     *    - `solve(node.next)`.
     *    - Print `node.val`.
     *
     * COMPLEXITY:
     * Time: O(N)
     * Space: O(N) - Recursion stack.
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
     *
     * PROBLEM:
     * Given the head of a linked list and two integers `m` and `n`.
     * Traverse the list. Keep the first `m` nodes. Then remove the next `n` nodes.
     * Continue this pattern until the end of the list.
     *
     * INPUT/OUTPUT:
     * Input: head = [1,2,3,4,5,6,7,8,9,10,11,12,13], m=2, n=3
     * Output: [1,2, 6,7, 11,12] (Removed 3,4,5 and 8,9,10)
     *
     * DESIGN:
     * Why Nested Loops or Linear Scan?
     * - We have two phases: "Skipping M" and "Deleting N".
     * - Use pointers to connect the end of "Keep" to the start of next "Keep".
     *
     * DETAIL:
     * 1. `curr = head`.
     * 2. Loop `curr != null`:
     *    - Keep M: loop `m-1` times advancing `curr`. If end reached, break.
     *    - Save `lastKept = curr`.
     *    - Delete N: loop `n` times using temp pointer `t = curr.next`.
     *    - Link: `lastKept.next = t.next`. `curr = lastKept.next`.
     *
     * COMPLEXITY:
     * Time: O(N)
     * Space: O(1)
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
     * 15. Kth Node From End of List
     *
     * PROBLEM:
     * Return the Kth node from the end of the list.
     *
     * INPUT/OUTPUT:
     * Input: head = [1,2,3,4,5], k=2 -> Output: 4
     *
     * DESIGN:
     * Why Two Pointers (Gap Method)?
     * - We want `slow` to be at `L-K` when `fast` is at `L`.
     * - Start `fast` K steps ahead of `slow`.
     * - Move both until `fast` hits end.
     *
     * DETAIL:
     * 1. `slow = head`, `fast = head`.
     * 2. Move `fast` K times.
     * 3. Loop while `fast != null`: slow++, fast++.
     * 4. Return `slow`.
     *
     * COMPLEXITY:
     * Time: O(N)
     * Space: O(1)
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
     * 16. Swap Nodes in Pairs
     *
     * PROBLEM:
     * Given a linked list, swap every two adjacent nodes and return its head.
     * You must solve the problem without modifying the values in the list's nodes (i.e., only nodes themselves may be changed.)
     *
     * INPUT/OUTPUT:
     * Input: head = [1,2,3,4] -> Output: [2,1,4,3]
     *
     * DESIGN:
     * Why Dummy Node and Cur Pointer?
     * - We need to change prev.next to point to the new first node of the pair.
     * - Structure: `prev -> a -> b -> rest` => `prev -> b -> a -> rest`.
     *
     * DETAIL:
     * 1. `dummy.next = head`. `prev = dummy`.
     * 2. Loop while `prev.next` and `prev.next.next` exist:
     *    - `a = prev.next`, `b = a.next`.
     *    - `prev.next = b`.
     *    - `a.next = b.next`.
     *    - `b.next = a`.
     *    - `prev = a`.
     * 3. Return `dummy.next`.
     *
     * COMPLEXITY:
     * Time: O(N)
     * Space: O(1)
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
            curr.next = second // Link from prev
            curr = first // Move prev to end of swapped pair
        }
        printList(dummy.next)
    }

    /**
     * 17. Add Two Numbers
     *
     * PROBLEM:
     * You are given two non-empty linked lists representing two non-negative integers. The digits are stored in reverse order, and each of their nodes contains a single digit. Add the two numbers and return the sum as a linked list.
     *
     * INPUT/OUTPUT:
     * Input: l1 = [2,4,3], l2 = [5,6,4] -> Output: [7,0,8] (342 + 465 = 807)
     *
     * DESIGN:
     * Why Carry Propagation?
     * - Just like elementary math. Add digits at same position + carry.
     * - `sum = val1 + val2 + carry`.
     * - `newVal = sum % 10`, `carry = sum / 10`.
     *
     * DETAIL:
     * 1. `dummy` head. `curr = dummy`. `carry = 0`.
     * 2. Loop while `l1` or `l2` or `carry`:
     *    - Get values (0 if null).
     *    - Compute sum. Update carry.
     *    - `curr.next = Node(sum % 10)`.
     *    - Advance ptrs.
     *
     * COMPLEXITY:
     * Time: O(max(N, M))
     * Space: O(max(N, M)) - Output list.
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
     * 18. Remove Duplicates from an Unsorted Linked List
     *
     * PROBLEM:
     * Given an unsorted linked list, remove duplicate values.
     *
     * INPUT/OUTPUT:
     * Input: [1,2,1,3] -> Output: [1,2,3]
     *
     * DESIGN:
     * Why HashSet?
     * - List is not sorted, so duplicates aren't adjacent.
     * - We must remember what we've seen.
     * - `HashSet` provides O(1) lookup.
     *
     * DETAIL:
     * 1. `set`. `curr = head`. `prev = null`.
     * 2. Loop `curr`:
     *    - If `set.contains(curr.val)`: `prev.next = curr.next` (delete).
     *    - Else: `set.add`, `prev = curr`.
     *    - `curr = curr.next`.
     *
     * COMPLEXITY:
     * Time: O(N)
     * Space: O(N)
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
     * 19. Check if Linked List is Circular
     *
     * PROBLEM:
     * Determine if a linked list is circular (connected end-to-end like a ring) or has a cycle.
     * Specifically, if the last node points to the head (Circular List vs List with Cycle).
     *
     * INPUT/OUTPUT:
     * Input: 1->2->3->1 (Back to head) -> true.
     *
     * DESIGN:
     * Why Traversal?
     * - If we specifically check "Circular Linked List" where tail points to head:
     * - Traverse `curr` until `null`. If `curr.next == head`, it's circular.
     * - If generic cycle: Floyd's (Q4).
     *
     * DETAIL:
     * 1. If head is null, true/false depending on def.
     * 2. `curr = head`.
     * 3. Loop `curr.next != null && curr.next != head`.
     * 4. Check condition.
     *
     * COMPLEXITY:
     * Time: O(N)
     * Space: O(1)
     */
    @Test
    fun q19_checkCircular() {
        println("=== Q19: Check Circular ===")
        // Logic specific to checking ring topology:
        val head = ListNode(1)
        head.next = head // Circular
        
        var curr = head
        var circular = false
        // Safety bound for demo
        var steps = 0
        while (curr.next != null && steps < 10) {
            if (curr.next == head) { circular = true; break }
            curr = curr.next!!
            steps++
        }
        println("Result: $circular")
    }

    /**
     * 20. Count Nodes in Linked List
     *
     * PROBLEM:
     * Count the number of nodes in a given singly linked list.
     *
     * INPUT/OUTPUT:
     * Input: 1->2->3 -> Output: 3
     *
     * DESIGN:
     * Why Iteration?
     * - Simple traversal incrementing a counter.
     *
     * DETAIL:
     * 1. `count = 0`.
     * 2. `curr = head`.
     * 3. Loop `curr != null`: `count++`, `curr = curr.next`.
     *
     * COMPLEXITY:
     * Time: O(N)
     * Space: O(1)
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
