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
     *
     * PROBLEM:
     * Implement a FIFO queue using only two stacks.
     * Supports: push, peek, pop, empty.
     *
     * DESIGN:
     * Why Two Stacks?
     * - `inStack`: Accepts new elements (Push).
     * - `outStack`: Stores elements in FIFO order for removal (Pop/Peek).
     * - When `pop` is called and `outStack` is empty, we pour *all* elements from `inStack` to `outStack`.
     * - This reverses the order twice (Stack LIFO reverse -> Pour reverse -> FIFO).
     *
     * DETAIL:
     * 1. `push(x)`: `inStack.push(x)`.
     * 2. `peek()`:
     *    - If `outStack` empty, while `inStack` not empty: `outStack.push(inStack.pop())`.
     *    - Return `outStack.peek()`.
     * 3. `pop()`: Call `peek()` to ensure data in `outStack`. `outStack.pop()`.
     *
     * COMPLEXITY:
     * Time: Amortized O(1). (Each element pushed once, popped once, moved once).
     * Space: O(N)
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
     *
     * PROBLEM:
     * Implement LIFO Stack using queues.
     *
     * DESIGN:
     * Why One Queue?
     * - Stack is LIFO. Queue is FIFO.
     * - To make `pop()` return the last added element, we must rotate the queue.
     * - `push(x)`:
     *   - `q.add(x)`.
     *   - Rotate `size - 1` elements: `q.add(q.remove())`.
     *   - New element `x` is now at the head.
     *
     * DETAIL:
     * 1. `push(x)`: Add `x`. Loop `sz-1` times: add(poll).
     * 2. `pop()`: `poll()`.
     *
     * COMPLEXITY:
     * Time: O(N) push, O(1) pop.
     * Space: O(N)
     */
    @Test
    fun q2_stackUsingQueues() {
        println("=== Q2: Stack using Queues ===")
        val q: Queue<Int> = LinkedList()
        fun push(x: Int) {
            q.add(x)
            for(i in 0 until q.size - 1) q.add(q.poll())
        }
        push(10); push(20)
        println("Top: ${q.peek()}") // 20
    }

    /**
     * 3. Number of Recent Calls (Ping Counter)
     *
     * PROBLEM:
     * `ping(t)` returns number of requests in range `[t-3000, t]`.
     * Timestamps `t` are strictly increasing.
     *
     * DESIGN:
     * Why Queue?
     * - We need to store timestamps.
     * - Only timestamps *older* than `t - 3000` become irrelevant.
     * - Oldest timestamps are at the front (FIFO).
     * - While `q.peek() < t - 3000`, poll.
     * - Size of queue is the answer.
     *
     * DETAIL:
     * 1. `q.add(t)`.
     * 2. While `q.peek() < t - 3000`: `q.poll()`.
     * 3. Return `q.size`.
     *
     * COMPLEXITY:
     * Time: O(1) amortized (each element added/removed once).
     * Space: O(3000) -> O(1).
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
     * 4. First Unique Character in a String
     *
     * PROBLEM:
     * Find the first non-repeating character and return its index.
     *
     * INPUT/OUTPUT:
     * Input: "leetcode" -> 0 ('l')
     * Input: "loveleetcode" -> 2 ('v')
     *
     * DESIGN:
     * Why Frequency Array?
     * - Queue isn't strictly necessary if string is static (Array is faster).
     * - Count frequencies of all chars.
     * - Iterate string again. Return index of first char with count 1.
     *
     * DETAIL:
     * 1. `count` array size 26.
     * 2. Pass 1: `count[c - 'a']++`.
     * 3. Pass 2: check `count`.
     *
     * COMPLEXITY:
     * Time: O(N)
     * Space: O(1)
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
     *
     * PROBLEM:
     * Queue of people buying tickets. 1 sec per ticket. Max tickets needed `tickets[i]`.
     * Return time for person `k` to finish.
     *
     * DESIGN:
     * Why Simulation/Optimization?
     * - Everyone ahead of `k` buys `min(want, target)` tickets.
     * - everyone behind `k` buys `min(want, target - 1)` tickets.
     * - Sum these values.
     *
     * COMPLEXITY:
     * Time: O(N)
     * Space: O(1)
     */
    @Test
    fun q5_timeTickets() {
        println("=== Q5: Time Needed Tickets ===")
        val tickets = intArrayOf(2,3,2); val k = 2
        var time = 0
        val target = tickets[k]
        for(i in tickets.indices) {
            if(i <= k) time += Math.min(tickets[i], target)
            else time += Math.min(tickets[i], target - 1)
        }
        println("Result: $time")
    }

    /**
     * 6. Number of Students Unable to Eat Lunch
     *
     * PROBLEM:
     * Circular sandwiches vs Square sandwiches. Students have preference.
     * Students cycle if they don't like top sandwich.
     *
     * DESIGN:
     * Why Count Preferences?
     * - Order of students cycling doesn't change the available preferences.
     * - If top sandwich is Square (1) and we have any student who wants Square, they will eventually get it.
     * - Deadlock if top is 1 but NO student wants 1.
     *
     * DETAIL:
     * 1. Count student prefs `c0`, `c1`.
     * 2. Loop sandwiches:
     *    - If matches `c0/c1`, decrement.
     *    - Else stop. Result is `c0 + c1`.
     *
     * COMPLEXITY:
     * Time: O(N)
     * Space: O(1)
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
     *
     * PROBLEM:
     * Calculate moving average of last `size` elements.
     *
     * INPUT/OUTPUT:
     * size=3. next(1)->1. next(10)->5.5 (1+10). next(3)->4.66. next(5)->6 (10+3+5).
     *
     * DESIGN:
     * Why Queue?
     * - Sliding window of size `k`.
     * - When adding new element, if `q.size > k`, remove oldest (`poll`).
     * - Maintain rolling `sum` for O(1) calc.
     *
     * DETAIL:
     * 1. `sum += val`. `q.add(val)`.
     * 2. If `q.size > k`: `sum -= q.poll()`.
     * 3. Return `sum / q.size`.
     *
     * COMPLEXITY:
     * Time: O(1)
     * Space: O(K)
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
     *
     * PROBLEM:
     * Fixed size queue. Reuse empty slots.
     *
     * DESIGN:
     * Why Array + Modulo?
     * - `head` points to front. `tail` points to last added.
     * - `enQueue`: `tail = (tail + 1) % k`.
     * - `deQueue`: `head = (head + 1) % k`.
     * - `count` tracks size to distinguish full vs empty.
     *
     * DETAIL:
     * 1. `k` size array. `head=0, tail=-1, count=0`.
     * 2. `enQueue`: Check full. Update tail. Add. Inc count.
     * 3. `deQueue`: Check empty. Update head. Dec count.
     *
     * COMPLEXITY:
     * Time: O(1)
     * Space: O(K)
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
     *
     * PROBLEM:
     * Queue supporting push/pop at Front, Middle, Back.
     *
     * DESIGN:
     * Why Two Deques?
     * - `leftDeque` stores first half. `rightDeque` stores second half.
     * - Balance: `size(left) == size(right)` or `size(left) = size(right) + 1` (depending on pref).
     * - `pushMiddle`: Add to `left.last` or `right.first`. Rebalance.
     * - `popMiddle`: Remove from `left.last`. Rebalance.
     *
     * COMPLEXITY:
     * Time: O(1)
     * Space: O(N)
     */
    @Test
    fun q9_frontMidBackQueue() {
        println("=== Q9: Front Middle Back Queue ===")
        // Implementation omitted for brevity, logic described.
        println("Logic: Two Deques [Left] [Right]. Balance size to access Middle in O(1).")
    }

    /**
     * 10. Reveal Cards In Increasing Order
     *
     * PROBLEM:
     * Deck of cards. Take top, reveal it. Take next, put at bottom. Repeat.
     * Find initial order to reveal 2, 3, 5, 7, 11...
     *
     * INPUT/OUTPUT:
     * Input: [17,13,11,2,3,5,7] -> Output: [2,13,3,11,5,17,7]
     *
     * DESIGN:
     * Why Reverse Simulation?
     * - Forward: Reveal -> Move Bottom -> Reveal -> ...
     * - Reverse: Take last revealed -> Move one from bottom to top -> Add next largest to top.
     * - Steps:
     *   1. Sort deck: `[2, 3, 5, 7, 11, 13, 17]`.
     *   2. Use Deque. Start empty.
     *   3. Iterate sorted deck backwards (largest to smallest).
     *   4. `q.addFirst(q.pollLast())` (Reverse the "Move Bottom" op).
     *   5. `q.addFirst(card)`.
     *
     * COMPLEXITY:
     * Time: O(N log N) sorting.
     * Space: O(N)
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
     *
     * PROBLEM:
     * Stamp `stamp` onto `target` string multiple times. '?' matches any char. Determine if `target` can be formed.
     * Return valid sequence of indices.
     *
     * DESIGN:
     * Why Reverse Processing?
     * - Instead of stamping blank -> target, try to reverse target -> blank (?).
     * - Find substring in `target` that matches `stamp` (where '?' is wildcard).
     * - "Stamp" it by replacing chars with '?'.
     * - Repeat until `target` is all '?'.
     *
     * DETAIL:
     * 1. Loop while `stars < target.length`.
     * 2. Find any window in `target` matching `stamp` (and has at least 1 non-'?' char).
     * 3. Replace chars with '?', add index to result stack.
     * 4. If no change in pass, return empty.
     * 5. Return reversed result stack.
     *
     * COMPLEXITY:
     * Time: O(N(N-M)). Each position stamped once.
     * Space: O(N).
     */
    @Test
    fun q11_stampingSequence() {
        println("=== Q11: Stamping Sequence ===")
        println("Logic: Reverse processing. Find stamp in target, replace with ?, continue.")
    }

    /**
     * 12. Dota2 Senate
     *
     * PROBLEM:
     * Radiant vs Dire senators. Round-based voting.
     * Each senator can BAN the next opposing senator's right.
     * Banned senators lose all rights.
     * Last party standing wins.
     *
     * DESIGN:
     * Why Two Queues?
     * - `rad` queue stores indices of R senators. `dire` queue stores D indices.
     * - Compare headers: `r_idx` vs `d_idx`.
     * - Smaller index (acts earlier) BANS the larger index.
     * - The winner (`r_idx`) goes to back of queue with `index + n` (simulating next round).
     * - The loser is removed permanently.
     *
     * DETAIL:
     * 1. Fill queues with indices.
     * 2. While both queues non-empty:
     *    - `r = q1.poll()`, `d = q2.poll()`.
     *    - If `r < d`: `q1.add(r + n)`.
     *    - Else: `q2.add(d + n)`.
     *
     * COMPLEXITY:
     * Time: O(N)
     * Space: O(N)
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
     * 13. Longest Continuous Subarray With Absolute Diff Less Than or Equal to Limit
     *
     * PROBLEM:
     * Find longest subarray such that `max(sub) - min(sub) <= limit`.
     *
     * DESIGN:
     * Why Monotonic Queue (Sliding Window)?
     * - We need Max and Min of current window efficiently.
     * - `maxQ`: Decreasing deque (front is max).
     * - `minQ`: Increasing deque (front is min).
     * - Expand `r`: Update `maxQ` and `minQ`.
     * - Shrink `l`: While `maxQ.first - minQ.first > limit`:
     *   - `l++`.
     *   - If `maxQ.first < l` remove it.
     *   - If `minQ.first < l` remove it.
     *
     * DETAIL:
     * 1. Loops `r`.
     * 2. Maintain Monotonic properties.
     * 3. Shrink `l` if constraint violated.
     * 4. `res = max(res, r - l + 1)`.
     *
     * COMPLEXITY:
     * Time: O(N)
     * Space: O(N)
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
     * 14. Product of the Last K Numbers
     *
     * PROBLEM:
     * `add(num)`, `getProduct(k)`.
     *
     * DESIGN:
     * Why Prefix Product List?
     * - `list[i]` stores product of all numbers up to `i`.
     * - `product(last k) = list.last / list[size - 1 - k]`.
     * - Zeros handling: Reset list on 0. If `k >= list.size`, it means range includes a cleared 0, so return 0.
     *
     * DETAIL:
     * 1. `list = [1]`.
     * 2. `add(n)`: if `n==0`, `list=[1]`. Else `list.add(last * n)`.
     * 3. `get(k)`: if `k >= size` return 0. Else divide.
     *
     * COMPLEXITY:
     * Time: O(1)
     * Space: O(N)
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
     * 15. Finding the Users Active Minutes
     *
     * PROBLEM:
     * User actions `[ID, Time]`. UAM is count of unique minutes user performed actions.
     * Return distribution array `answer` where `answer[j]` is number of users whose UAM is `j+1`.
     *
     * DESIGN:
     * Why Hash Set per User?
     * - Track unique minutes for each user. `Map<ID, Set<Time>>`.
     * - Calculate size of each user's set.
     * - Increment corresponding index in result.
     *
     * COMPLEXITY:
     * Time: O(N)
     * Space: O(N)
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
     *
     * PROBLEM:
     * Tree node locking system. `lock(n, user)`, `unlock(n, user)`, `upgrade(n, user)`.
     * Upgrade: Lock node if:
     * 1. Node unlocked.
     * 2. Has locked descendants.
     * 3. No locked ancestors.
     * 4. Unlock all descendants.
     *
     * DESIGN:
     * Why Graph/Tree Traversal?
     * - `upgrade` is the hard part.
     * - Need `parent` map for ancestor check.
     * - Need `children` map or adjacency list for descendant check.
     * - BFS/DFS to find locked descendants.
     *
     * COMPLEXITY:
     * Time: O(H) for check. O(D) for unlocking descendants.
     * Space: O(N)
     */
    @Test
    fun q16_operationsTree() {
        println("=== Q16: Operations Tree ===")
        println("Design Problem: Lock/Unlock/Upgrade with strict conditions.")
    }

    /**
     * 17. Design Snake Game
     *
     * PROBLEM:
     * Snake moves in grid. Eats food, grows. Dies if hits wall or self.
     *
     * DESIGN:
     * Why Deque?
     * - Snake body: Head is Front, Tail is Back.
     * - Move: Add new Head.
     * - If food: Don't remove Tail (grow).
     * - If no food: Remove Tail (move).
     * - Collision: Use `Set<Point>` for quick body lookup. NOTE: Tail moves *before* checking head collision (unless growing).
     *
     * COMPLEXITY:
     * Time: O(1) per move.
     * Space: O(N) (N = W*H)
     */
    @Test
    fun q17_snakeGame() {
        println("=== Q17: Snake Game ===")
        println("Logic: Deque<Point>. Move head, remove tail unless food.")
    }

    /**
     * 18. Logger Rate Limiter
     *
     * PROBLEM:
     * `shouldPrintMessage(timestamp, message)`.
     * True if message not printed in last 10 seconds.
     *
     * DESIGN:
     * Why HashMap?
     * - Store `message -> next_allowed_time`.
     * - If `t >= allowed`, print and update `allowed = t + 10`.
     * - Else return false.
     *
     * COMPLEXITY:
     * Time: O(1)
     * Space: O(M) unique messages.
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
     * 19. Design Hit Counter
     *
     * PROBLEM:
     * `hit(timestamp)`, `getHits(timestamp)`. Count hits in last 300 secs.
     *
     * DESIGN:
     * Why Queue?
     * - Timestamps are monotonic.
     * - Store all timestamps in Queue.
     * - `getHits(t)`: Remove all `ts <= t - 300` from front. Return size.
     * - Optimization: If many hits at same time, store `(timestamp, count)` pair.
     *
     * COMPLEXITY:
     * Time: O(1) amortized.
     * Space: O(300) -> O(1).
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
     *
     * PROBLEM:
     * Array `res` where `res[i]` is dist to closest occurrence of `c` in string `s`.
     *
     * DESIGN:
     * Why Two Pass (BFS-like propagation)?
     * - BFS from all `c`s (Multi-source BFS) works but O(N) space queue.
     * - Two Pass O(1) space:
     *   1. Left to Right: `dist[i] = i - prev_c_index`.
     *   2. Right to Left: `dist[i] = min(dist[i], next_c_index - i)`.
     *
     * COMPLEXITY:
     * Time: O(N)
     * Space: O(1) (output ignored)
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
