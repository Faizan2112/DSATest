package com.example.dsatest.generated.stack

import org.junit.Test
import java.util.Stack
import java.util.LinkedList
import java.util.Queue
import kotlin.text.iterator

/**
 * ==========================================
 * STACK PROBLEMS: EASY (1-20)
 * ==========================================
 * 
 * Solutions to 20 Easy Stack Linked List questions.
 */
class StackProblemsEasy {

    /**
     * 1. Valid Parentheses
     *
     * PROBLEM:
     * Given a string `s` containing just the characters '(', ')', '{', '}', '[' and ']', determine if the input string is valid.
     * An input string is valid if:
     * 1. Open brackets must be closed by the same type of brackets.
     * 2. Open brackets must be closed in the correct order.
     *
     * INPUT/OUTPUT:
     * Input: s = "()[]{}" -> Output: true
     * Input: s = "(]" -> Output: false
     *
     * DESIGN:
     * Why Stack?
     * - We need to match the *most recently* opened bracket with the current closing bracket.
     * - Last-In-First-Out (LIFO) is perfect for this.
     * - When we see an opener, we push the *expected closer* onto the stack.
     * - When we see a closer, we check if it matches the top of the stack.
     *
     * DETAIL:
     * 1. Loop through char `c` in `s`.
     * 2. If '(', push ')'. If '{', push '}'. If '[', push ']'.
     * 3. Else (it's a closer):
     *    - If stack empty or `stack.pop() != c`, return false.
     * 4. Return `stack.isEmpty()` (all openers matched).
     *
     * COMPLEXITY:
     * Time: O(N)
     * Space: O(N)
     */
    @Test
    fun q1_validParentheses() {
        println("=== Q1: Valid Parentheses ===")
        val s = "()[]{}"
        val stack = Stack<Char>()
        var valid = true
        for(c in s) {
            if(c == '(') stack.push(')')
            else if(c == '{') stack.push('}')
            else if(c == '[') stack.push(']')
            else if(stack.isEmpty() || stack.pop() != c) { valid = false; break }
        }
        println("Result: ${valid && stack.isEmpty()}")
    }

    /**
     * 2. Min Stack
     *
     * PROBLEM:
     * Design a stack that supports push, pop, top, and retrieving the minimum element in constant time.
     *
     * DESIGN:
     * Why Two Stacks?
     * - `mainStack`: stores actual values.
     * - `minStack`: stores the minimum value encountered *so far*.
     * - When pushing `val`: `mainStack.push(val)`. `minStack.push(min(val, minStack.peek()))`.
     * - When popping: Pop both.
     *
     * COMPLEXITY:
     * Time: O(1) for all ops.
     * Space: O(N).
     */
    @Test
    fun q2_minStack() {
        println("=== Q2: Min Stack ===")
        // See Patterns file for implementation
        println("See StackPatternsAndProblems for full Class implementation.")
    }

    /**
     * 3. Implement Stack using Queues
     *
     * PROBLEM:
     * Implement a last-in-first-out (LIFO) stack using only two queues (or one).
     *
     * DESIGN:
     * Why One Queue with Rotation?
     * - Queue is FIFO. Stack is LIFO.
     * - To make `pop` return the last element added, we must make the last element added the *front* of the queue.
     * - On `push(x)`:
     *   - `q.add(x)` (x is at back).
     *   - Rotate `size - 1` times: `q.add(q.remove())`.
     *   - Now `x` is at front.
     *
     * DETAIL:
     * 1. `push(x)`: Enqueue `x`. Rotate `n-1` elements.
     * 2. `pop()`: Dequeue.
     *
     * COMPLEXITY:
     * Time: O(N) push, O(1) pop.
     * Space: O(N)
     */
    @Test
    fun q3_stackUsingQueues() {
        println("=== Q3: Stack using Queues ===")
        val q: Queue<Int> = LinkedList()
        fun push(x: Int) {
            q.add(x)
            for(i in 0 until q.size - 1) q.add(q.poll())
        }
        push(1); push(2)
        println("Top: ${q.peek()}") // 2
    }

    /**
     * 4. Implement Queue using Stacks
     *
     * PROBLEM:
     * Implement a first-in-first-out (FIFO) queue using only two stacks.
     *
     * DESIGN:
     * Why Two Stacks (In/Out)?
     * - Stack reverses order. Two stacks reverse twice -> original order (FIFO).
     * - `s1` (Input): Accepts new elements.
     * - `s2` (Output): Stores elements in FIFO order for reading/popping.
     * - `peek`/`pop`: If `s2` empty, pour all `s1` into `s2`. Then peek/pop `s2`.
     *
     * DETAIL:
     * 1. `push`: `s1.push(x)`.
     * 2. `pop`: If `s2` empty, move `s1`->`s2`. Return `s2.pop()`.
     *
     * COMPLEXITY:
     * Time: Amortized O(1).
     * Space: O(N).
     */
    @Test
    fun q4_queueUsingStacks() {
        println("=== Q4: Queue using Stacks ===")
        val s1 = Stack<Int>(); val s2 = Stack<Int>()
        fun push(x: Int) = s1.push(x)
        fun peek(): Int {
            if(s2.isEmpty()) while(s1.isNotEmpty()) s2.push(s1.pop())
            return s2.peek()
        }
        push(1); push(2)
        println("Peek: ${peek()}") // 1
    }

    /**
     * 5. Backspace String Compare
     *
     * PROBLEM:
     * Given two strings `s` and `t`, return true if they are equal when both are typed into empty text editors. '#' means a backspace character.
     *
     * INPUT/OUTPUT:
     * Input: s = "ab#c", t = "ad#c" -> Output: true ("ac" == "ac")
     *
     * DESIGN:
     * Why Stack?
     * - We process characters sequentially.
     * - If char is regular, add to "kept" characters (push).
     * - If char is '#', remove the last "kept" character (pop).
     * - Finally compare the stacks.
     *
     * DETAIL:
     * 1. `build(str)`:
     *    - Loop `c`:
     *    - If `c != '#'`: `push`.
     *    - Else if `stack not empty`: `pop`.
     * 2. Compare `build(s) == build(t)`.
     *
     * COMPLEXITY:
     * Time: O(M + N)
     * Space: O(M + N)
     */
    @Test
    fun q5_backspaceCompare() {
        println("=== Q5: Backspace Compare ===")
        val s = "ab#c"; val t = "ad#c"
        fun build(str: String): String {
            val stk = Stack<Char>()
            for(c in str) {
                if(c != '#') stk.push(c)
                else if(stk.isNotEmpty()) stk.pop()
            }
            return stk.toString()
        }
        println("Result: ${build(s) == build(t)}")
    }

    /**
     * 6. Baseball Game
     *
     * PROBLEM:
     * You are keeping the scores for a baseball game with specific rules involving integer scores, "C" (Cancel), "D" (Double), and "+" (Sum of last two). Return total score.
     *
     * INPUT/OUTPUT:
     * Input: ops = ["5","2","C","D","+"]
     * Output: 30
     *
     * DESIGN:
     * Why Stack?
     * - The operations depend on the *most recent* valid scores.
     * - `C`: invalidates last score (pop).
     * - `D`: doubles last score (peek * 2).
     * - `+`: sums last two scores (top + second_top).
     *
     * DETAIL:
     * 1. Loop `op`:
     *    - If `+`: pop top, add top + new_peek, push top back, push new_sum.
     *    - If `D`: push `peek * 2`.
     *    - If `C`: pop.
     *    - Else: push integer value.
     * 2. Return stack sum.
     *
     * COMPLEXITY:
     * Time: O(N)
     * Space: O(N)
     */
    @Test
    fun q6_baseballGame() {
        println("=== Q6: Baseball Game ===")
        val ops = arrayOf("5","2","C","D","+")
        val stack = Stack<Int>()
        for(op in ops) {
            when(op) {
                "+" -> { 
                    val top = stack.pop()
                    val new = top + stack.peek()
                    stack.push(top)
                    stack.push(new) 
                }
                "D" -> stack.push(2 * stack.peek())
                "C" -> stack.pop()
                else -> stack.push(op.toInt())
            }
        }
        println("Result: ${stack.sum()}")
    }

    /**
     * 7. Remove All Adjacent Duplicates In String
     *
     * PROBLEM:
     * Repeatedly make duplicate removals on `s` until we no longer can. A removal consists of choosing two adjacent and equal letters and removing them.
     *
     * INPUT/OUTPUT:
     * Input: s = "abbaca" -> Output: "ca" ("bb" removed -> "aaca" -> "aa" removed -> "ca")
     *
     * DESIGN:
     * Why Stack (or StringBuilder as Stack)?
     * - When we add a character, we check it against the *previous* character (top of stack).
     * - If equal, they annihilate (pop).
     * - If not, we keep it (push).
     *
     * DETAIL:
     * 1. Use `StringBuilder` as a stack.
     * 2. Loop `c` in `s`:
     *    - If `sb` not empty and `sb.last() == c`: `sb.deleteCharAt(last)`.
     *    - Else: `sb.append(c)`.
     * 3. Return `sb`.
     *
     * COMPLEXITY:
     * Time: O(N)
     * Space: O(N)
     */
    @Test
    fun q7_removeDuplicates() {
        println("=== Q7: Remove Adjacent Duplicates ===")
        val s = "abbaca"
        val sb = StringBuilder()
        for(c in s) {
            if(sb.isNotEmpty() && sb.last() == c) sb.deleteCharAt(sb.length - 1)
            else sb.append(c)
        }
        println("Result: $sb")
    }

    /**
     * 8. Next Greater Element I
     *
     * PROBLEM:
     * Find the next greater element for each element of `nums1` in `nums2`.
     * `nums1` is a subset of `nums2`.
     *
     * INPUT/OUTPUT:
     * Input: nums1 = [4,1,2], nums2 = [1,3,4,2]
     * Output: [-1,3,-1]
     *
     * DESIGN:
     * Why Monotonic Stack?
     * - We want to find the *first* element to the right that is larger.
     * - Traverse `nums2`. Maintain a decreasing stack.
     * - If `current > stack.peek()`, then `current` is the Next Greater Element for `stack.peek()`.
     * - `Map` stores the result for lookup by `nums1`.
     *
     * DETAIL:
     * 1. `stack` (monotonic decreasing), `map`.
     * 2. Loop `n` in `nums2`:
     *    - While `stack` not empty and `n > stack.peek()`: `map[stack.pop()] = n`.
     *    - `stack.push(n)`.
     * 3. Build result for `nums1` using `map`. Check `getOrDefault(..., -1)`.
     *
     * COMPLEXITY:
     * Time: O(N + M)
     * Space: O(N)
     */
    @Test
    fun q8_nextGreaterElement() {
        println("=== Q8: Next Greater Element I ===")
        val nums1 = intArrayOf(4,1,2); val nums2 = intArrayOf(1,3,4,2)
        val map = HashMap<Int, Int>() // val -> nextGreater
        val stack = Stack<Int>()
        for(n in nums2) {
            while(stack.isNotEmpty() && n > stack.peek()) map[stack.pop()] = n
            stack.push(n)
        }
        val res = IntArray(nums1.size)
        for(i in nums1.indices) res[i] = map.getOrDefault(nums1[i], -1)
        println("Result: ${res.contentToString()}")
    }

    /**
     * 9. Make The String Great
     *
     * PROBLEM:
     * A string is "bad" if `s[i]` is lowercase and `s[i+1]` is same letter but uppercase (or vice versa). Remove bad pairings.
     *
     * INPUT/OUTPUT:
     * Input: "leEeetcode" -> "leetcode" ("eE" removed)
     *
     * DESIGN:
     * Why Stack?
     * - Similar to "Remove Adjacent Duplicates".
     * - Bad pair condition: `abs(char1 - char2) == 32` (ASCII diff between 'a' and 'A').
     *
     * DETAIL:
     * 1. Loop `c` in `s`.
     * 2. If stack not empty and diff is 32: `pop` (annihilate).
     * 3. Else `push`.
     *
     * COMPLEXITY:
     * Time: O(N)
     * Space: O(N)
     */
    @Test
    fun q9_makeStringGreat() {
        println("=== Q9: Make String Great ===")
        val s = "leEeetcode"
        val stack = Stack<Char>()
        for(c in s) {
            if(stack.isNotEmpty() && Math.abs(stack.peek() - c) == 32) stack.pop()
            else stack.push(c)
        }
        println("Result: ${stack.joinToString("")}")
    }

    /**
     * 10. Crawler Log Folder
     *
     * PROBLEM:
     * Operations: "../" (parent), "./" (stay), "d/" (child). Start at main. Return steps to main.
     *
     * INPUT/OUTPUT:
     * Input: ["d1/","d2/","../","d21/","./"] -> Output: 2
     *
     * DESIGN:
     * Why Counter or Stack?
     * - Stack simulates folder structure. Size is depth.
     * - Since we only need *depth*, a simple integer counter suffices (Space optimized).
     *
     * DETAIL:
     * 1. `depth = 0`.
     * 2. Loop `log`:
     *    - `../`: `depth = max(0, depth - 1)`.
     *    - `./`: no op.
     *    - Else: `depth++`.
     *
     * COMPLEXITY:
     * Time: O(N)
     * Space: O(1)
     */
    @Test
    fun q10_crawlerLog() {
        println("=== Q10: Crawler Log ===")
        val logs = arrayOf("d1/","d2/","../","d21/","./")
        var depth = 0
        for(log in logs) {
            if(log == "../") depth = Math.max(0, depth - 1)
            else if(log != "./") depth++
        }
        println("Result: $depth")
    }

    /**
     * 11. Build an Array With Stack Operations
     *
     * PROBLEM:
     * Given an integer `n` and an integer array `target`, you have an integer range `[1, n]`.
     * You use a stack to build the `target` array from the stream `[1, n]`.
     * Return the stack operations ("Push", "Pop") needed to build `target`.
     *
     * INPUT/OUTPUT:
     * Input: target = [1,3], n = 3 -> Output: ["Push", "Push", "Pop", "Push"]
     * (Read 1: Push. Match 1. Read 2: Push. 2 not in target. Pop. Read 3: Push. Match 3. Stop)
     *
     * DESIGN:
     * Why Simulation?
     * - Iterate through numbers `i` from 1 to `n`.
     * - Also use a pointer `idx` for `target`.
     * - We always "Push" `i`.
     * - If `i == target[idx]`, we keep it (move `idx`).
     * - If `i != target[idx]`, we discard it ("Pop").
     * - Optimization: We can deduce the ops. If we see a gap between `current` and `target[i]`, we need `(Push, Pop)` pairs for the missing numbers.
     *
     * DETAIL:
     * 1. `curr = 1`.
     * 2. Loop `t` in `target`:
     *    - While `curr < t`: Add "Push", "Pop". `curr++`.
     *    - Add "Push". `curr++`.
     *
     * COMPLEXITY:
     * Time: O(N)
     * Space: O(N) (Output)
     */
    @Test
    fun q11_buildArray() {
        println("=== Q11: Build Array Stack Ops ===")
        val target = intArrayOf(1, 3); val n = 3
        val res = ArrayList<String>()
        var curr = 1
        for(t in target) {
            while(curr < t) { res.add("Push"); res.add("Pop"); curr++ }
            res.add("Push"); curr++
        }
        println("Result: $res")
    }

    /**
     * 12. Final Prices With a Special Discount in a Shop
     *
     * PROBLEM:
     * For each item `i`, you receive a discount equivalent to `prices[j]` where `j` is the minimum index satisfying `j > i` and `prices[j] <= prices[i]`.
     * Effectively: Find the Next Smaller Element on the right.
     *
     * INPUT/OUTPUT:
     * Input: prices = [8,4,6,2,3] -> Output: [4,2,4,2,3]
     * (8-4=4, 4-2=2, 6-2=4, 2-0=2, 3-0=3)
     *
     * DESIGN:
     * Why Monotonic Stack?
     * - Similar to Next Greater Element, but finding Next Smaller or Equal.
     * - Maintain Increasing Stack (Wait, we want first smaller).
     * - Actually we want the first element *smaller* than current.
     * - Maintain *Monotonic Increasing* stack of indices.
     * - If `price[i] <= price[stack.peek()]`, then `price[i]` is the discount for `stack.peek()`. Pop and apply discount.
     *
     * DETAIL:
     * 1. Stack stores indices.
     * 2. Loop `i` in `prices`:
     *    - While `stack` not empty and `prices[i] <= prices[stack.peek()]`:
     *      - `idx = stack.pop()`.
     *      - `prices[idx] -= prices[i]`.
     *    - `push(i)`.
     *
     * COMPLEXITY:
     * Time: O(N)
     * Space: O(N)
     */
    @Test
    fun q12_finalPrices() {
        println("=== Q12: Final Prices (Next Smaller) ===")
        val prices = intArrayOf(8, 4, 6, 2, 3) // Mutable array
        val stack = Stack<Int>()
        for(i in prices.indices) {
            while(stack.isNotEmpty() && prices[i] <= prices[stack.peek()]) {
                val idx = stack.pop()
                prices[idx] -= prices[i]
            }
            stack.push(i)
        }
        println("Result: ${prices.contentToString()}")
    }

    /**
     * 13. Maximum Nesting Depth of the Parentheses
     *
     * PROBLEM:
     * Return the maximum number of nested parentheses.
     *
     * INPUT/OUTPUT:
     * Input: s = "(1+(2*3)+((8)/4))+1" -> Output: 3
     *
     * DESIGN:
     * Why Counter?
     * - We just need the depth, not to match brackets specifically (input is guaranteed valid or we just count valid depth).
     * - Increment `curr` on '(', decrement on ')'.
     * - Update `max` on '('.
     *
     * DETAIL:
     * 1. `max = 0`, `curr = 0`.
     * 2. Loop `c`:
     *    - `(`: `curr++`, `max = max(max, curr)`.
     *    - `)`: `curr--`.
     *
     * COMPLEXITY:
     * Time: O(N)
     * Space: O(1)
     */
    @Test
    fun q13_maxDepth() {
        println("=== Q13: Max Nesting Depth ===")
        val s = "(1+(2*3)+((8)/4))+1"
        var max = 0; var curr = 0
        for(c in s) {
            if(c == '(') {
                curr++
                if(curr > max) max = curr
            } else if(c == ')') {
                curr--
            }
        }
        println("Result: $max")
    }

    /**
     * 14. Remove Outermost Parentheses
     *
     * PROBLEM:
     * A valid parentheses string is "primitive" if it is nonempty and cannot be split into P = A + B.
     * Given s, remove the outermost parentheses of every primitive decomposition.
     * Example: "(()())(())" -> "()()" + "()" -> "()()()"
     *
     * INPUT/OUTPUT:
     * Input: "(()())(())" -> Output: "()()()"
     *
     * DESIGN:
     * Why Balance Counter?
     * - We exclude the 'first' open parenthesis and the 'last' matching close parenthesis of each primitive group.
     * - If `open > 0` before incrementing on '(', it means this '(' is inner.
     * - If `open > 0` after decrementing on ')', it means this ')' is inner.
     *
     * DETAIL:
     * 1. Loop `c`:
     *    - If `(`: If `open > 0`, append. `open++`.
     *    - If `)`: `open--`. If `open > 0`, append.
     *
     * COMPLEXITY:
     * Time: O(N)
     * Space: O(N)
     */
    @Test
    fun q14_removeOutermost() {
        println("=== Q14: Remove Outermost Parentheses ===")
        val s = "(()())(())"
        val sb = StringBuilder()
        var open = 0
        for(c in s) {
            if(c == '(') {
                if(open > 0) sb.append(c)
                open++
            } else if(c == ')') {
                open--
                if(open > 0) sb.append(c)
            }
        }
        println("Result: $sb")
    }

    /**
     * 15. Number of Students Unable to Eat Lunch
     *
     * PROBLEM:
     * Queue of students (0 or 1 preference). Stack of sandwiches (0 or 1).
     * If student at front matches top sandwich, they eat and leave.
     * Else, student goes to back of queue.
     * Return count of students left when none can eat.
     *
     * INPUT/OUTPUT:
     * Input: students=[1,1,0,0], sandwiches=[0,1,0,1] -> Output: 0
     *
     * DESIGN:
     * Why Count Preferences?
     * - The order of students doesn't matter much because they cycle.
     * - What matters is if there is *any* student left who wants the top sandwich.
     * - If top sandwich is '0' and we have Students who want '0', one of them will eventually come to front and eat it.
     * - Process halts only when top sandwich is '0' but *no* student wants '0'.
     *
     * DETAIL:
     * 1. Count 0s and 1s in students.
     * 2. Iterate `sand` in `sandwiches`:
     *    - If `count[sand] > 0`: `count[sand]--`.
     *    - Else: Break (Cannot eat this sandwich).
     * 3. Return `count[0] + count[1]` (remaining).
     *
     * COMPLEXITY:
     * Time: O(N)
     * Space: O(1)
     */
    @Test
    fun q15_studentsLunch() {
        println("=== Q15: Students Lunch ===")
        val students = intArrayOf(1,1,0,0); val sandwiches = intArrayOf(0,1,0,1)
        val count = intArrayOf(0, 0)
        for(s in students) count[s]++
        
        var remaining = students.size
        for(sand in sandwiches) {
            if(count[sand] > 0) {
                count[sand]--
                remaining--
            } else {
                break // No student wants this sandwich
            }
        }
        println("Result: $remaining")
    }

    /**
     * 16. Time Needed to Buy Tickets
     *
     * PROBLEM:
     * `tickets[i]` is time needed for person `i`.
     * Queue processes 1 ticket per second in circular order.
     * Return time until person `k` is done.
     *
     * INPUT/OUTPUT:
     * Input: tickets = [2,3,2], k = 2 -> Output: 6
     *
     * DESIGN:
     * Why 1-Pass Simulation?
     * - We calculate contribution of each person `i` to the wait time.
     * - If `i <= k`: They will buy at most `tickets[k]` tickets (or `tickets[i]` if fewer).
     * - If `i > k`: They will buy at most `tickets[k] - 1` tickets (because simulation stops when k reaches 0).
     *
     * DETAIL:
     * 1. `time = 0`.
     * 2. Loop `i` through `tickets`.
     *    - Add `min(tickets[i], tickets[k] - (if i > k then 1 else 0))`
     *
     * COMPLEXITY:
     * Time: O(N)
     * Space: O(1)
     */
    @Test
    fun q16_timeToBuyTickets() {
        println("=== Q16: Time Tickets ===")
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
     * 17. Minimum Add to Make Parentheses Valid
     *
     * PROBLEM:
     * Return min moves to make string valid. Move = insert '(' or ')'.
     *
     * INPUT/OUTPUT:
     * Input: "())" -> Output: 1
     *
     * DESIGN:
     * Why Balance Check?
     * - If we have extra `)`, we need `(` to fix it immediately (increment `need`).
     * - If we have extra `(`, we count them (`open` or `right_need`).
     * - Result is `need_left + need_right`.
     *
     * DETAIL:
     * 1. `open = 0`, `needed = 0`.
     * 2. Loop `c`:
     *    - `(`: `open++`.
     *    - `)`:
     *      - If `open > 0` (matched), `open--`.
     *      - Else (unmatched close): `needed++`.
     * 3. Return `needed + open`.
     *
     * COMPLEXITY:
     * Time: O(N)
     * Space: O(1)
     */
    @Test
    fun q17_minAddToMakeValid() {
        println("=== Q17: Min Add Valid ===")
        val s = "())"
        var open = 0; var need = 0
        for(c in s) {
            if(c == '(') open++
            else if(open > 0) open--
            else need++
        }
        println("Result: ${need + open}")
    }

    /**
     * 18. Check if Word is Valid After Substitutions
     *
     * PROBLEM:
     * Valid string is "abc". Recursively insert "abc" into valid strings.
     * Check if `s` is valid.
     * Basically: Collapse "abc" repeatedly.
     *
     * INPUT/OUTPUT:
     * Input: "aabcbc" -> true ("abc" inside "abc")
     *
     * DESIGN:
     * Why Stack?
     * - Put chars on stack.
     * - If top 3 are 'a','b','c', pop them.
     * - If stack empty at end, valid.
     *
     * DETAIL:
     * 1. Loop `c` in `s`:
     *    - Push `c`.
     *    - If `size >= 3` and top is 'c', peek-1 is 'b', peek-2 is 'a':
     *      - Pop 3 times.
     * 2. Return `stack.isEmpty()`.
     *
     * COMPLEXITY:
     * Time: O(N)
     * Space: O(N)
     */
    @Test
    fun q18_validSubstitutions() {
        println("=== Q18: Valid After Substitutions (abc) ===")
        val s = "aabcbc"
        val stk = Stack<Char>()
        for(c in s) {
            stk.push(c)
            if(stk.size >= 3 && stk.peek() == 'c' && stk[stk.size-2] == 'b' && stk[stk.size-3] == 'a') {
                stk.pop(); stk.pop(); stk.pop()
            }
        }
        println("Result: ${stk.isEmpty()}")
    }

    /**
     * 19. Validate Stack Sequences
     *
     * PROBLEM:
     * Given `pushed` and `popped` arrays, distinct values, return true if this could be result of push and pop ops on empty stack.
     *
     * INPUT/OUTPUT:
     * Input: pushed = [1,2,3,4,5], popped = [4,5,3,2,1] -> Output: true
     *
     * DESIGN:
     * Why Simulation?
     * - We simulate the push flow.
     * - Push value.
     * - While `top == popped[j]`, pop and increment `j`.
     * - If stack empty at end, true.
     *
     * DETAIL:
     * 1. `j = 0`.
     * 2. Loop `x` in `pushed`:
     *    - `s.push(x)`.
     *    - While `s.isNotEmpty() && s.peek() == popped[j]`: `s.pop()`, `j++`.
     * 3. Return `s.isEmpty()`.
     *
     * COMPLEXITY:
     * Time: O(N)
     * Space: O(N)
     */
    @Test
    fun q19_validatesequences() {
        println("=== Q19: Validate Stack Sequences ===")
        val pushed = intArrayOf(1,2,3,4,5); val popped = intArrayOf(4,5,3,2,1)
        val s = Stack<Int>()
        var j = 0
        for(x in pushed) {
            s.push(x)
            while(s.isNotEmpty() && s.peek() == popped[j]) {
                s.pop(); j++
            }
        }
        println("Result: ${s.isEmpty()}")
    }

    /**
     * 20. Palindrome Linked List (Using Stack)
     *
     * PROBLEM:
     * Check if LL is palindrome.
     *
     * DESIGN:
     * Why Stack?
     * - Stack allows O(N) traversal in reverse.
     * - First pass: Push values.
     * - Second pass: Pop and compare.
     *
     * DETAIL:
     * 1. Loop `curr`: push `val`.
     * 2. Loop `curr`: pop. If mismatch, false.
     *
     * COMPLEXITY:
     * Time: O(N)
     * Space: O(N)
     */
    @Test
    fun q20_palindromeStack() {
        println("=== Q20: Palindrome (Stack) ===")
        // Assuming ListNode defined
        class ListNode(val `val`: Int) { var next: ListNode? = null }
        val head = ListNode(1).apply { next = ListNode(2).apply { next = ListNode(1) } }
        
        val stack = Stack<Int>()
        var curr: ListNode? = head
        while(curr != null) { stack.push(curr.`val`); curr = curr.next }
        
        curr = head
        var isPal = true
        while(curr != null) {
            if(stack.pop() != curr.`val`) { isPal = false; break }
            curr = curr.next
        }
        println("Result: $isPal")
    }
}
