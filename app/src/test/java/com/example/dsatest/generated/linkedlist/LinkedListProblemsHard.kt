package com.example.dsatest.generated.linkedlist

import org.junit.Test
import java.util.PriorityQueue
import java.util.HashMap

/**
 * ==========================================
 * LINKED LIST PROBLEMS: HARD
 * ==========================================
 * 
 * Solutions to Hard Linked List questions.
 * Focus: Complex Pointer manipulation, Doubly Linked Lists, System Design.
 */
class LinkedListProblemsHard {

    fun make(vararg nums: Int): ListNode? {
        if(nums.isEmpty()) return null
        val h = ListNode(nums[0]); var c = h
        for(i in 1 until nums.size) { c.next=ListNode(nums[i]); c=c.next!! }
        return h
    }
    fun printList(h: ListNode?){ var c=h; while(c!=null){ print("${c.`val`}->"); c=c.next }; println("null") }

    /**
     * 1. Merge k Sorted Lists
     *
     * PROBLEM:
     * You are given an array of `k` linked-lists `lists`, each linked-list is sorted in ascending order.
     * Merge all the linked-lists into one sorted linked-list and return it.
     *
     * INPUT/OUTPUT:
     * Input: lists = [[1,4,5],[1,3,4],[2,6]] -> Output: [1,1,2,3,4,4,5,6]
     *
     * DESIGN:
     * Why Min-Heap?
     * - We need the smallest element among `k` heads at every step.
     * - A simple comparison of k heads is O(k). Total O(N*k).
     * - A Min-Heap allows finding and extracting the min in O(log k). Total O(N log k).
     * - Add all heads to Heap. Extract min, add its next to Heap.
     *
     * DETAIL:
     * 1. Create `PriorityQueue` of Nodes (ordered by val).
     * 2. Add non-null `head` of all lists to PQ.
     * 3. Create `dummy` head. `curr = dummy`.
     * 4. While PQ not empty:
     *    - `node = pq.poll()`.
     *    - `curr.next = node`.
     *    - `curr = curr.next`.
     *    - If `node.next != null`, `pq.add(node.next)`.
     * 5. Return `dummy.next`.
     *
     * COMPLEXITY:
     * Time: O(N log k) where N is total nodes.
     * Space: O(k) (Heap size).
     */
    @Test
    fun q1_mergeKLists() {
        println("=== Q1: Merge k Sorted Lists ===")
        val l1 = make(1,4,5); val l2 = make(1,3,4); val l3 = make(2,6)
        val pq = PriorityQueue<ListNode> { a, b -> a.`val` - b.`val` }
        if(l1!=null) pq.add(l1); if(l2!=null) pq.add(l2); if(l3!=null) pq.add(l3)
        
        val dummy = ListNode(0); var curr = dummy
        while(pq.isNotEmpty()) {
            val node = pq.poll()
            curr.next = node
            curr = curr.next!!
            if(node.next != null) pq.add(node.next)
        }
        printList(dummy.next)
    }

    /**
     * 2. Reverse Nodes in k-Group
     *
     * PROBLEM:
     * Reverse nodes of list `k` at a time. Left-out nodes remain as is.
     *
     * INPUT/OUTPUT:
     * Input: head = [1,2,3,4,5], k = 2 -> Output: [2,1,4,3,5]
     * Input: head = [1,2,3,4,5], k = 3 -> Output: [3,2,1,4,5]
     *
     * DESIGN:
     * Why Check Length First?
     * - We must guarantee `k` nodes exist before reversing to satisfy the "left-out nodes remain as is" condition.
     * - If check passes, reverse `k` nodes.
     * - Connect reversed group to the result of recursion on the rest.
     *
     * DETAIL:
     * 1. Check if `k` nodes exist starting from `head`. If not, return `head`.
     * 2. Reverse the first `k` nodes.
     *    - `prev` becomes the new head of this segment.
     *    - `head` becomes the tail of this segment.
     * 3. `head.next = reverseKGroup(next_group_start)`.
     * 4. Return `prev`.
     *
     * COMPLEXITY:
     * Time: O(N)
     * Space: O(N/k) (Recursion stack) OR O(1) Iterative.
     */
    @Test
    fun q2_reverseKGroup() {
        println("=== Q2: Reverse k-Group ===")
        val head = make(1,2,3,4,5); val k = 2
        
        fun reverse(start: ListNode, end: ListNode?): ListNode {
            var prev: ListNode? = null; var curr: ListNode? = start
            while(curr != end) {
                val next = curr!!.next; curr.next = prev; prev = curr; curr = next
            }
            return prev!!
        }
        
        var curr = head; var count = 0
        while(curr != null && count != k) { curr = curr.next; count++ }
        
        if(count == k) {
            val newHead = reverse(head!!, curr) 
            // In a real solution: head.next = reverseKGroup(curr, k)
            // Here we just print the result of the first group for demo
            println("Result: Reversed first k ($k) nodes. New head: ${newHead.`val`}")
        } else {
             println("Result: Not enough nodes to reverse.")
        }
    }

    /**
     * 3. LRU Cache
     *
     * PROBLEM:
     * Design a data structure that follows the constraints of a Least Recently Used (LRU) cache.
     * Implement `LRUCache(capacity)`.
     * - `get(key)`: Return value or -1. Move to MRU.
     * - `put(key, value)`: Update/Add. Evict least recently used if full.
     * All ops O(1).
     *
     * INPUT/OUTPUT:
     * put(1,1), put(2,2), get(1)->1, put(3,3)->evicts 2, get(2)->-1.
     *
     * DESIGN:
     * Why HashMap + Doubly Linked List?
     * - `get` needs O(1). HashMap gives node by key.
     * - `put` / `evict` needs O(1) removal and insertion at ends.
     * - Doubly Linked List allows removing a specific node from middle in O(1) (since node has `prev` and `next` ptrs).
     * - HashMap stores `Key -> Node`.
     *
     * DETAIL:
     * 1. `head` (MRU boundary), `tail` (LRU boundary).
     * 2. `get(key)`: If exists, detach node, add to `head`. Return val.
     * 3. `put(key, val)`:
     *    - If exists: update val, move to `head`.
     *    - If new: create node, add to `head`, add to map.
     *    - If over capacity: remove `tail.prev` (LRU), remove from map.
     *
     * COMPLEXITY:
     * Time: O(1)
     * Space: O(C)
     */
    @Test
    fun q3_lruCache() {
        println("=== Q3: LRU Cache ===")
        class LRUCache(val capacity: Int) {
            inner class Node(val key: Int, var `val`: Int) {
                var prev: Node? = null; var next: Node? = null
            }
            val map = HashMap<Int, Node>()
            val head = Node(0,0); val tail = Node(0,0) // head = MRU, tail = LRU markers
            init { head.next = tail; tail.prev = head }
            
            // Add right after head
            fun add(node: Node) {
                node.prev = head; node.next = head.next
                head.next!!.prev = node; head.next = node
            }
            fun remove(node: Node) {
                node.prev!!.next = node.next; node.next!!.prev = node.prev
            }
            fun put(key: Int, value: Int) {
                if(map.containsKey(key)) remove(map[key]!!)
                val node = Node(key, value)
                map[key] = node; add(node)
                if(map.size > capacity) {
                    val lru = tail.prev!!
                    remove(lru); map.remove(lru.key)
                }
            }
            fun get(key: Int): Int {
                if(!map.containsKey(key)) return -1
                val node = map[key]!!
                remove(node); add(node)
                return node.`val`
            }
        }
        val lru = LRUCache(2)
        lru.put(1, 1); lru.put(2, 2)
        println(lru.get(1)) // 1
        lru.put(3, 3) // Evicts 2
        println(lru.get(2)) // -1
    }

    /**
     * 4. LFU Cache
     *
     * PROBLEM:
     * Design a Least Frequently Used (LFU) cache.
     * Evict key with min frequency. If tie, evict LRU among them.
     *
     * DESIGN:
     * Why 2 Maps + MinFreq?
     * - `keyMap`: `key -> Node(val, freq)`. O(1) access.
     * - `freqMap`: `freq -> LinkedHashSet<Node>` (or DLL). Stores nodes of a specific frequency.
     * - `LinkedHashSet` provides O(1) add/remove and maintains insertion order (LRU).
     * - `minFreq`: tracks lowest frequency. If `freqMap[minFreq]` becomes empty, increment `minFreq`.
     *
     * DETAIL:
     * 1. `get(key)`:
     *    - Remove node from `freqMap[node.freq]`.
     *    - Increment `node.freq`.
     *    - Add to `freqMap[node.freq]`.
     *    - Update `minFreq`: if `minFreq` list empty, `minFreq++`.
     * 2. `put(key, val)`:
     *    - If new and full: Remove first element from `freqMap[minFreq]` (LRU of MinFreq). Remove from `keyMap`.
     *    - Insert new node with freq 1. `minFreq = 1`.
     *
     * COMPLEXITY:
     * Time: O(1)
     * Space: O(C)
     */
    @Test
    fun q4_lfuCache() {
        println("=== Q4: LFU Cache ===")
        // Conceptual implementation.
        println("Logic: Map<Key, Node>, Map<Freq, LinkedHashSet<Node>>, minFreq.")
    }

    /**
     * 5. Design SkipList
     *
     * PROBLEM:
     * Design a SkipList check `search`, `add`, `erase` in O(log N).
     *
     * DESIGN:
     * Why Multi-level Linked List?
     * - Standard LL search is O(N).
     * - SkipList adds layers of shortcuts. Layer `i` is a subset of Layer `i-1`.
     * - Search starts at top layer. Move right while `next.val < target`. Move down otherwise.
     * - `add`: Insert key. Flip coin. If heads, insert at level+1. Repeat.
     * - `erase`: Find and remove from all levels.
     *
     * DETAIL:
     * 1. `Node` contains array of `next` pointers (one for each level).
     * 2. `search`: Start top level. `curr = head`.
     *    - While `curr.next[lvl] != null && curr.next[lvl].val < target`: traverse right.
     *    - Drop level.
     *
     * COMPLEXITY:
     * Time: O(log N) average.
     * Space: O(N) average.
     */
    @Test
    fun q5_skipList() {
        println("=== Q5: Design SkipList ===")
        println("Logic: Probabilistic Data Structure (Layers of Linked Lists).")
    }

    /**
     * 6. Flatten a Multilevel Doubly Linked List
     *
     * PROBLEM:
     * Transform a DLL where nodes can have children into a single level DLL.
     * Process `child` immediately after `curr`, pushing `curr.next` for later.
     *
     * INPUT/OUTPUT:
     * Input: 1-2-3(child: 7-8)-4 -> Output: 1-2-3-7-8-4
     *
     * DESIGN:
     * Why Recursive DFS or Stack?
     * - We need to dive into `child` but remember to come back to `next`.
     * - Order: `Curr` -> `Child` ... -> `Next`.
     * - Stack: Push `next` (if exists), then push `child` (if exists).
     * - Link `curr` to `pop()`.
     *
     * DETAIL:
     * 1. `curr`. `stack.push(head)`.
     * 2. Loop while stack not empty:
     *    - `node = stack.pop()`.
     *    - Link `prev` and `node` (doubly).
     *    - If `node.next` exists, push `node.next`.
     *    - If `node.child` exists, push `node.child`.
     *    - `node.child = null`.
     *
     * COMPLEXITY:
     * Time: O(N)
     * Space: O(N)
     */
    @Test
    fun q6_flattenDLL() {
        println("=== Q6: Flatten DLL ===")
        println("Logic: DFS Flattening (Stack or Recursion).")
    }

    /**
     * 7. All O`one Data Structure
     *
     * PROBLEM:
     * Store string counts. `inc`, `dec`, `getMaxKey`, `getMinKey` in O(1).
     *
     * DESIGN:
     * Why Doubly Linked List of Buckets?
     * - Keys with same count change simultaneously.
     * - Map `Key -> BucketNode`. BucketNode contains `count` and `Set<Key>`.
     * - DLL of Buckets: `1 <-> 2 <-> 5 <-> 10 ...`
     * - `inc(key)`:
     *   - Look up bucket `b` (count `c`). Remove key.
     *   - Move key to bucket `c+1`. If `b.next` is not `c+1`, insert new bucket.
     * - `getMax`: `tail.keys.first()`.
     *
     * COMPLEXITY:
     * Time: O(1)
     * Space: O(N)
     */
    @Test
    fun q7_allOne() {
        println("=== Q7: All O`one ===")
        println("Logic: Double Linked List of Frequency Buckets.")
    }

    /**
     * 8. Design a Text Editor
     *
     * PROBLEM:
     * `addText`, `deleteText` at cursor. `cursorLeft`, `cursorRight` returning substring.
     *
     * DESIGN:
     * Why Two Stacks or Doubly Linked List?
     * - Two Stacks: `leftStack` (chars before cursor), `rightStack` (chars after).
     *   - `add`: push to `left`.
     *   - `delete`: pop `left`.
     *   - `moveLeft`: pop `left` push `right`.
     * - Doubly Linked List:
     *   - Node per char. Overhead high but O(1) ops valid.
     *
     * DETAIL:
     * 1. Stacks are easiest.
     * 2. `moveLeft(k)`: pop up to `k` from `left`, push to `right`.
     * 3. Return last 10 chars of `left`.
     *
     * COMPLEXITY:
     * Time: O(K) where K is operation length.
     * Space: O(N)
     */
    @Test
    fun q8_textEditor() {
        println("=== Q8: Text Editor ===")
        println("Logic: Two Stacks (Left of Cursor, Right of Cursor).")
    }
    
    /**
     * 9. Max Stack
     *
     * PROBLEM:
     * `push`, `pop`, `top`, `peekMax`, `popMax`.
     *
     * DESIGN:
     * Why DLL + TreeMap?
     * - `DLL`: maintains stack order for `push` and `pop`.
     * - `TreeMap<Val, List<Node>>`: Sorted map allows finding Max. `List<Node>` handles duplicates (removing most recent).
     * - `popMax`:
     *   - Find max val in TreeMap.
     *   - Get list of nodes. Remove last (most rescent).
     *   - Remove that node from DLL O(1).
     *
     * COMPLEXITY:
     * Time: O(log N) due to TreeMap.
     * Space: O(N)
     */
    @Test
    fun q9_maxStack() {
        println("=== Q9: Max Stack ===")
        println("Logic: DLL for order, TreeMap for O(log n) retrieval of Max.")
    }

    /**
     * 10. Palindrome Linked List (O(1) Space)
     *
     * PROBLEM:
     * Check if linked list is palindrome in O(N) time and O(1) space.
     *
     * DETAIL:
     * See Easy Q8. The "Hard" classification often comes from the constraint of restoring the list or strict O(1) check.
     *
     * ALGORITHM:
     * 1. Find Middle (Slow/Fast).
     * 2. Reverse Second Half.
     * 3. Compare First Half and Reversed Second Half.
     * 4. (Optional but recommended) Reverse Second Half back to restore list.
     *
     * COMPLEXITY:
     * Time: O(N)
     * Space: O(1)
     */
    @Test
    fun q10_palindromeSpaceOpt() {
        println("=== Q10: Palindrome O(1) Space ===")
        println("Result: See Easy Q8. Challenge is restoring list.")
    }
}
