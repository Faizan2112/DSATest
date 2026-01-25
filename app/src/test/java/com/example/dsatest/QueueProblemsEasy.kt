package com.example.dsatest

import org.junit.Test
import java.util.Stack
import java.util.LinkedList
import java.util.Queue
import java.util.ArrayDeque

/**
 * ==========================================
 * QUEUE PROBLEMS: EASY (1-20)
 * ==========================================
 * 
 * Solutions to 20 Easy Queue questions.
 * Focus: Simulation, Design, BFS Basics.
 */
class QueueProblemsEasy {

    /**
     * 1. Implement Queue using Stacks
     * Logic: InStack + OutStack. Move In->Out when Out is empty.
     */
    @Test
    fun q1_queueUsingStacks() {
        println("=== Q1: Queue using Stacks ===")
        val inStack = Stack<Int>()
        val outStack = Stack<Int>()
        
        fun push(x: Int) = inStack.push(x)
        fun peek(): Int {
            if (outStack.isEmpty()) while(inStack.isNotEmpty()) outStack.push(inStack.pop())
            return outStack.peek()
        }
        fun pop(): Int {
            peek()
            return outStack.pop()
        }
        
        push(1); push(2)
        println("Pop: ${pop()}") // 1
        println("Peek: ${peek()}") // 2
    }

    /**
     * 2. Implement Stack using Queues
     * Logic: Single Queue Rotation.
     */
    @Test
    fun q2_stackUsingQueues() {
        println("=== Q2: Stack using Queues ===")
        // See StackProblemsEasy Q3
        println("Logic: q.add(x); for(1 until n) q.add(q.poll())")
    }

    /**
     * 3. Number of Recent Calls
     * Logic: Queue stores timestamps. Remove values < t - 3000.
     */
    @Test
    fun q3_recentCalls() {
        println("=== Q3: Recent Calls (Ping) ===")
        val q: Queue<Int> = LinkedList()
        fun ping(t: Int): Int {
            q.add(t)
            while(q.peek() < t - 3000) q.poll()
            return q.size
        }
        println("Ping 1: ${ping(1)}")
        println("Ping 100: ${ping(100)}")
        println("Ping 3001: ${ping(3001)}") // 3
        println("Ping 3002: ${ping(3002)}") // 3 (1 removed)
    }

    /**
     * 4. First Unique Character
     * Logic: Count Char Array.
     */
    @Test
    fun q4_firstUniqueChar() {
        println("=== Q4: First Unique Char ===")
        val s = "leetcode"
        val count = IntArray(26)
        for(c in s) count[c - 'a']++
        var res = -1
        for(i in s.indices) {
            if(count[s[i] - 'a'] == 1) { res = i; break }
        }
        println("Result: $res")
    }

    /**
     * 5. Time Needed to Buy Tickets
     * Logic: See StackProblemsEasy Q16.
     */
    @Test
    fun q5_timeTickets() {
        println("=== Q5: Time Needed Tickets ===")
        println("See StackProblemsEasy Q16")
    }

    /**
     * 6. Number of Students Unable to Eat Lunch
     * Logic: Count 0s and 1s.
     */
    @Test
    fun q6_studentsLunch() {
        println("=== Q6: Students Lunch ===")
        val students = intArrayOf(1,1,0,0); val sandwiches = intArrayOf(0,1,0,1)
        var c0 = 0; var c1 = 0
        for(s in students) if(s==0) c0++ else c1++
        for(s in sandwiches) {
            if(s==0 && c0>0) c0--
            else if(s==1 && c1>0) c1--
            else break
        }
        println("Result: ${c0 + c1}")
    }

    /**
     * 7. Moving Average from Data Stream
     * Logic: Queue + Sum variable.
     */
    @Test
    fun q7_movingAverage() {
        println("=== Q7: Moving Average ===")
        val size = 3
        val q: Queue<Int> = LinkedList()
        var sum = 0.0
        fun next(`val`: Int): Double {
            q.add(`val`)
            sum += `val`
            if(q.size > size) sum -= q.poll()
            return sum / q.size
        }
        println("Avg: ${next(1)}")
        println("Avg: ${next(10)}")
        println("Avg: ${next(3)}")
        println("Avg: ${next(5)}")
    }

    /**
     * 8. Design Circular Queue
     * Logic: Array + Front/Rear/Size pointers.
     */
    @Test
    fun q8_circularQueue() {
        println("=== Q8: Design Circular Queue ===")
        val k = 3
        val q = IntArray(k)
        var head = 0; var tail = -1; var count = 0
        fun enQueue(value: Int): Boolean {
            if(count == k) return false
            tail = (tail + 1) % k
            q[tail] = value
            count++
            return true
        }
        fun deQueue(): Boolean {
            if(count == 0) return false
            head = (head + 1) % k
            count--
            return true
        }
        enQueue(1); enQueue(2); deQueue(); enQueue(3)
        println("Front: ${q[head]}")
    }

    /**
     * 9. Design Front Middle Back Queue
     * Logic: Two Deques or LinkedList.
     */
    @Test
    fun q9_frontMidBackQueue() {
        println("=== Q9: Front Middle Back Queue ===")
        // Logic: Keep two deques balanced.
        println("Logic: Two Deques [Left] [Right]. Balance size.")
    }

    /**
     * 10. Reveal Cards In Increasing Order
     * Logic: Simulation Reverse.
     * [2,3,5,7,11,13,17]. Sort.
     * Take from sorted, insert to deque front, rotate last to front.
     */
    @Test
    fun q10_revealCards() {
        println("=== Q10: Reveal Cards ===")
        val deck = intArrayOf(17,13,11,2,3,5,7)
        deck.sort()
        val q = ArrayDeque<Int>()
        for(i in deck.indices.reversed()) {
            if(q.isNotEmpty()) q.addFirst(q.pollLast())
            q.addFirst(deck[i])
        }
        println("Result: $q")
    }

    /**
     * 11. Stamping The Sequence
     * Logic: Reverse greedy. Match target to stamp. turn to ???.
     */
    @Test
    fun q11_stampingSequence() {
        println("=== Q11: Stamping Sequence ===")
        println("Logic: Reverse processing. Find stamp in target, replace with ?, continue.")
    }

    /**
     * 12. Dota2 Senate
     * Logic: Two Queues (Radiant Indices, Dire Indices).
     */
    @Test
    fun q12_dota2Senate() {
        println("=== Q12: Dota2 Senate ===")
        val senate = "RD"
        val q1: Queue<Int> = LinkedList(); val q2: Queue<Int> = LinkedList()
        val n = senate.length
        for(i in senate.indices) if(senate[i]=='R') q1.add(i) else q2.add(i)
        
        while(q1.isNotEmpty() && q2.isNotEmpty()) {
            val r = q1.poll(); val d = q2.poll()
            if(r < d) q1.add(r + n) else q2.add(d + n)
        }
        println("Winner: ${if(q1.size > q2.size) "Radiant" else "Dire"}")
    }

    /**
     * 13. Longest Subarray Limit Diff
     * Logic: MaxDeque and MinDeque (Sliding Window).
     */
    @Test
    fun q13_limitDiffSubarray() {
        println("=== Q13: Longest Subarray Limit Diff ===")
        val nums = intArrayOf(8, 2, 4, 7); val limit = 4
        val maxQ = ArrayDeque<Int>(); val minQ = ArrayDeque<Int>()
        var l = 0; var res = 0
        for(r in nums.indices) {
            while(maxQ.isNotEmpty() && nums[maxQ.peekLast()] < nums[r]) maxQ.pollLast()
            while(minQ.isNotEmpty() && nums[minQ.peekLast()] > nums[r]) minQ.pollLast()
            maxQ.addLast(r); minQ.addLast(r)
            
            while(nums[maxQ.peekFirst()] - nums[minQ.peekFirst()] > limit) {
                l++
                if(maxQ.peekFirst() < l) maxQ.pollFirst()
                if(minQ.peekFirst() < l) minQ.pollFirst()
            }
            res = Math.max(res, r - l + 1)
        }
        println("Result: $res")
    }

    /**
     * 14. Product of Last K Numbers
     * Logic: Prefix Product List.
     */
    @Test
    fun q14_productLastK() {
        println("=== Q14: Product Last K ===")
        val list = ArrayList<Int>().apply { add(1) }
        fun add(num: Int) {
            if(num == 0) { list.clear(); list.add(1) }
            else list.add(list.last() * num)
        }
        fun getProduct(k: Int): Int {
            if(k >= list.size) return 0
            return list.last() / list[list.size - 1 - k]
        }
        add(3); add(0); add(2); add(5)
        println("Last 2: ${getProduct(2)}") // 10
    }

    /**
     * 15. Finding Users Active Minutes
     * Logic: HashMap<ID, Set<Time>>.
     */
    @Test
    fun q15_activeMinutes() {
        println("=== Q15: Active Minutes ===")
        val logs = arrayOf(intArrayOf(1,2), intArrayOf(1,2), intArrayOf(1,5))
        val map = HashMap<Int, HashSet<Int>>()
        for(l in logs) map.computeIfAbsent(l[0]) { HashSet() }.add(l[1])
        val res = IntArray(5)
        for(v in map.values) if(v.size <= 5) res[v.size - 1]++
        println("Result: ${res.contentToString()}")
    }

    /**
     * 16. Operations on Tree
     * Logic: Locking/Unlocking. HashMap parent/child.
     */
    @Test
    fun q16_operationsTree() {
        println("=== Q16: Operations Tree ===")
        println("Design Problem: Lock/Unlock/Upgrade with strict conditions.")
    }

    /**
     * 17. Snake Game
     * Logic: Deque for snake body. Set for collision.
     */
    @Test
    fun q17_snakeGame() {
        println("=== Q17: Snake Game ===")
        println("Logic: Deque<Point>. Move head, remove tail unless food.")
    }

    /**
     * 18. Logger Rate Limiter
     * Logic: HashMap<Msg, Timestamp>.
     */
    @Test
    fun q18_loggerRate() {
        println("=== Q18: Logger Rate Limiter ===")
        val map = HashMap<String, Int>()
        fun shouldPrint(t: Int, m: String): Boolean {
            if(t < map.getOrDefault(m, 0)) return false
            map[m] = t + 10
            return true
        }
        println("Print? ${shouldPrint(1, "foo")}")
    }

    /**
     * 19. Hit Counter
     * Logic: Queue<Timestamp>. Remove old.
     */
    @Test
    fun q19_hitCounter() {
        println("=== Q19: Hit Counter ===")
        val q: Queue<Int> = LinkedList()
        fun hit(t: Int) { q.add(t) }
        fun count(t: Int): Int {
            while(q.isNotEmpty() && q.peek() <= t - 300) q.poll()
            return q.size
        }
        hit(1); hit(2); hit(300)
        println("Count at 300: ${count(300)}") // 3
        println("Count at 301: ${count(301)}") // 2 (1 removed)
    }

    /**
     * 20. Shortest Distance to a Character
     * Logic: Two pass linear or BFS.
     */
    @Test
    fun q20_shortestDistanceChar() {
        println("=== Q20: Shortest Distance to Char ===")
        val s = "loveleetcode"; val c = 'e'
        val n = s.length
        val res = IntArray(n)
        var prev = -10000
        for(i in 0 until n) {
            if(s[i] == c) prev = i
            res[i] = i - prev
        }
        prev = 10000
        for(i in n-1 downTo 0) {
            if(s[i] == c) prev = i
            res[i] = Math.min(res[i], prev - i)
        }
        println("Result: ${res.contentToString()}")
    }
}
