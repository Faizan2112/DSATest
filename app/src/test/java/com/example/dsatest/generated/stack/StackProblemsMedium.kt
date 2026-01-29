package com.example.dsatest.generated.stack

import org.junit.Test
import java.util.Stack
import java.util.LinkedList
import kotlin.text.iterator

/**
 * ==========================================
 * STACK PROBLEMS: MEDIUM (1-20)
 * ==========================================
 * 
 * Solutions to 20 Medium Stack questions.
 * Patterns: Monotonic Stack, Parsing, Simulation.
 */
class StackProblemsMedium {

    /**
     * 1. Evaluate Reverse Polish Notation
     *
     * PROBLEM:
     * Evaluate the value of an arithmetic expression in Reverse Polish Notation (RPN).
     * Valid operators: +, -, *, /. Division between integers truncates toward zero.
     *
     * INPUT/OUTPUT:
     * Input: tokens = ["2","1","+","3","*"] -> Output: 9 ((2 + 1) * 3)
     * Input: tokens = ["4","13","5","/","+"] -> Output: 6 (4 + (13 / 5))
     *
     * DESIGN:
     * Why Stack?
     * - RPN (Postfix) handles operator precedence naturally.
     * - When we see a number, push it.
     * - When we see an operator, pop the last two numbers, apply operator, push result.
     *
     * DETAIL:
     * 1. Loop `t` in `tokens`.
     * 2. If `t` is op: `val b = pop()`, `val a = pop()`. Push `a op b`.
     * 3. Else push `t.toInt()`.
     * 4. Return `pop()` (final result).
     *
     * COMPLEXITY:
     * Time: O(N)
     * Space: O(N)
     */
    @Test
    fun q1_evalRPN() {
        println("=== Q1: Eval RPN ===")
        val tokens = arrayOf("2", "1", "+", "3", "*")
        val stack = Stack<Int>()
        for(t in tokens) {
            when(t) {
                "+" -> stack.push(stack.pop() + stack.pop())
                "*" -> stack.push(stack.pop() * stack.pop())
                "-" -> { val b = stack.pop(); stack.push(stack.pop() - b) }
                "/" -> { val b = stack.pop(); stack.push(stack.pop() / b) }
                else -> stack.push(t.toInt())
            }
        }
        println("Result: ${stack.peek()}")
    }

    /**
     * 2. Simplify Path
     *
     * PROBLEM:
     * Convert an absolute path (Unix-style) to the canonical path.
     * Rules: ".." moves up, "." stays, multiple "///" become single "/".
     *
     * INPUT/OUTPUT:
     * Input: "/a/./b/../../c/" -> Output: "/c"
     *
     * DESIGN:
     * Why Stack?
     * - We need to process directories and potentially "pop" back up.
     * - Split string by "/".
     * - Iterate parts:
     *   - "..": Pop if not empty.
     *   - "." or empty: Skip.
     *   - Else: Push (it's a directory name).
     * - Join with "/".
     *
     * DETAIL:
     * 1. `split("/")`.
     * 2. Stack of Strings.
     * 3. Process as above.
     * 4. Join.
     *
     * COMPLEXITY:
     * Time: O(N)
     * Space: O(N)
     */
    @Test
    fun q2_simplifyPath() {
        println("=== Q2: Simplify Path ===")
        val path = "/a/./b/../../c/"
        val stack = Stack<String>()
        val parts = path.split("/")
        for(p in parts) {
            if(p == "" || p == ".") continue
            if(p == "..") { if(stack.isNotEmpty()) stack.pop() }
            else stack.push(p)
        }
        // Using Stack iteration (bottom-up) is correct for path reconstruction
        println("Result: /${stack.joinToString("/")}")
    }

    /**
     * 3. Decode String
     *
     * PROBLEM:
     * Decode strings like `3[a]2[bc]` -> `aaabcbc`.
     * k[encoded_string] means repeat `encoded_string` `k` times.
     * Nested brackets allowed: `3[a2[c]]`.
     *
     * INPUT/OUTPUT:
     * Input: "3[a2[c]]" -> "3[acc]" -> "accaccacc"
     *
     * DESIGN:
     * Why 2 Stacks?
     * - We have two contexts: "How many times?" (`countStack`) and "What prefix came before?" (`resStack`).
     * - When `[`: Push `k` and `currentString`. Reset them.
     * - When `]`: Pop `prevString` and `k`. `currentString = prevString + currentString * k`.
     *
     * DETAIL:
     * 1. Loop `c`:
     *    - Digit: `k = k*10 + digit`.
     *    - `[`: Push k, curr. Reset.
     *    - `]`: Pop k, prev. `curr = prev + repeat(curr, k)`.
     *    - Char: `curr.append`.
     *
     * COMPLEXITY:
     * Time: O(Output Length)
     * Space: O(Output Length) simulation.
     */
    @Test
    fun q3_decodeString() {
        println("=== Q3: Decode String ===")
        val s = "3[a]2[bc]"
        val countStack = Stack<Int>()
        val resStack = Stack<StringBuilder>()
        var curr = StringBuilder()
        var k = 0
        for(c in s) {
            if(c.isDigit()) k = k * 10 + (c - '0')
            else if(c == '[') {
                countStack.push(k); resStack.push(curr)
                curr = StringBuilder(); k = 0
            } else if(c == ']') {
                val decoded = curr
                curr = resStack.pop()
                val count = countStack.pop()
                repeat(count) { curr.append(decoded) }
            } else curr.append(c)
        }
        println("Result: $curr")
    }

    /**
     * 4. Daily Temperatures
     *
     * PROBLEM:
     * Given array of temps, return array where `ans[i]` is number of days to wait until warmer temp. 0 if never.
     *
     * INPUT/OUTPUT:
     * Input: [73, 74, 75, 71, 69, 72, 76, 73]
     * Output: [1, 1, 4, 2, 1, 1, 0, 0]
     *
     * DESIGN:
     * Why Monotonic Stack?
     * - We want the *Next Greater Element* index.
     * - Store indices in stack.
     * - Maintain Decreasing Stack (temps).
     * - If `curr > top`: `top` found its warmer day. Pop `top` and record `curr_index - top_index`.
     *
     * DETAIL:
     * 1. Stack holds indices.
     * 2. Loop `i` in `t`:
     *    - While `stack` not empty and `t[i] > t[stack.peek]`:
     *      - `idx = pop()`. `res[idx] = i - idx`.
     *    - Push `i`.
     *
     * COMPLEXITY:
     * Time: O(N)
     * Space: O(N)
     */
    @Test
    fun q4_dailyTemperatures() {
        println("=== Q4: Daily Temperatures ===")
        val t = intArrayOf(73, 74, 75, 71, 69, 72, 76, 73)
        val res = IntArray(t.size)
        val stack = Stack<Int>()
        for(i in t.indices) {
            while(stack.isNotEmpty() && t[i] > t[stack.peek()]) {
                val prev = stack.pop()
                res[prev] = i - prev
            }
            stack.push(i)
        }
        println("Result: ${res.contentToString()}")
    }

    /**
     * 5. Generate Parentheses
     *
     * PROBLEM:
     * Generate all combinations of well-formed parentheses for `n` pairs.
     *
     * INPUT/OUTPUT:
     * n = 3 -> ["((()))","(()())","(())()","()(())","()()()"]
     *
     * DESIGN:
     * Why Backtracking?
     * - We build step by step.
     * - Decisions:
     *   - Add '(': Valid if `open < n`.
     *   - Add ')': Valid if `close < open`.
     * - Base Case: `len == 2*n`.
     *
     * DETAIL:
     * 1. `backtrack(str, open, close)`.
     * 2. If `open < n` recurse `str + "("`.
     * 3. If `close < open` recurse `str + ")"`.
     *
     * COMPLEXITY:
     * Time: O(4^n / sqrt(n)) - Catalan Number logic.
     * Space: O(N)
     */
    @Test
    fun q5_generateParentheses() {
        println("=== Q5: Generate Parentheses ===")
        val n = 3; val res = ArrayList<String>()
        fun backtrack(curr: String, open: Int, close: Int) {
            if(curr.length == 2*n) { res.add(curr); return }
            if(open < n) backtrack(curr + "(", open + 1, close)
            if(close < open) backtrack(curr + ")", open, close + 1)
        }
        backtrack("", 0, 0)
        println("Result: $res")
    }

    /**
     * 6. Asteroid Collision
     *
     * PROBLEM:
     * Array of asteroids. Positive = right, Negative = left.
     * When they crash (Right -> <- Left), smaller explodes. Equal both explode. Larger survives.
     * Return state after collisions.
     *
     * INPUT/OUTPUT:
     * Input: [5, 10, -5] -> Output: [5, 10] (10 vs -5, 10 wins).
     * Input: [8, -8] -> Output: []
     *
     * DESIGN:
     * Why Stack?
     * - We need to resolve collisions between the current asteroid and the *rightmost* survived asteroid traveling right.
     * - Stack usually holds stable Right-moving asteroids.
     * - Left-moving asteroids (`neg`) challenge the Stack top.
     *
     * DETAIL:
     * 1. Loop `ast`:
     *    - If `ast > 0`: Push.
     *    - If `ast < 0`:
     *      - While stack top is pos `> 0` and `top < |ast|`: Pop (top destroyed).
     *      - If top `== |ast|`: Pop (both destroyed). Don't push `ast`.
     *      - If top `> |ast|`: `ast` destroyed. Don't push.
     *      - If stack empty or top is neg: Push `ast` (it floats away left).
     *
     * COMPLEXITY:
     * Time: O(N)
     * Space: O(N)
     */
    @Test
    fun q6_asteroidCollision() {
        println("=== Q6: Asteroid Collision ===")
        val asteroids = intArrayOf(5, 10, -5)
        val stack = Stack<Int>()
        for(ast in asteroids) {
            var survive = true
            while(stack.isNotEmpty() && ast < 0 && stack.peek() > 0) {
                if(stack.peek() < -ast) { stack.pop(); continue } // Top explodes, ast continues
                else if(stack.peek() == -ast) stack.pop() // Both explode
                survive = false; break // Ast explodes
            }
            if(survive) stack.push(ast)
        }
        println("Result: $stack")
    }

    /**
     * 7. Design a Stack With Increment Operation
     *
     * PROBLEM:
     * `push`, `pop`, `inc(k, val)`: Add `val` to bottom `k` elements.
     * All O(1).
     *
     * DESIGN:
     * Why Lazy Propagation?
     * - If we iterate to update k elements, it's O(K) -> O(N).
     * - Optimization: Use an array `inc` to store values to be added later.
     * - `inc[i]` stores value to be added to `stack[i]` *and all below*.
     * - On `pop()`:
     *   - Res = `stack[top] + inc[top]`.
     *   - Pass `inc[top]` down to `inc[top-1]`.
     *   - `inc[top] = 0`.
     *   - Return Res.
     *
     * DETAIL:
     * 1. Array `stack`, Array `inc`. `top` index.
     * 2. `increment(k, val)`: `inc[min(k-1, top)] += val`.
     * 3. `pop()`: Handle `inc` cascade.
     *
     * COMPLEXITY:
     * Time: O(1) all ops.
     * Space: O(N).
     */
    @Test
    fun q7_incrementStack() {
        println("=== Q7: Stack Inc ===")
        println("Logic: Use array. inc[i] adds to stack[i]. Lazy add.")
        // Conceptual implementation desc sufficient as per study guide
    }

    /**
     * 8. Remove K Digits
     *
     * PROBLEM:
     * Given non-negative integer as string, remove `k` digits to achieve smallest possible number.
     *
     * INPUT/OUTPUT:
     * Input: num = "1432219", k = 3 -> Output: "1219"
     *
     * DESIGN:
     * Why Greedy + Monotonic Stack?
     * - To make a number small, we prefer smaller digits at the *most significant* text (left).
     * - If we see `1` then `4`, we keep `1,4`.
     * - If we then see `3`, `4 > 3`, so removing `4` helps us replace it with `3` (giving `13...` < `14...`).
     * - Condition: If `curr < top`, pop `top` and decrement `k`.
     *
     * DETAIL:
     * 1. Loop `c` in `num`:
     *    - While `k>0` and `stack` not empty and `top > c`: `pop`, `k--`.
     *    - `push(c)`.
     * 2. If `k>0` remains, pop from end.
     * 3. Remove leading zeros.
     *
     * COMPLEXITY:
     * Time: O(N)
     * Space: O(N)
     */
    @Test
    fun q8_removeKDigits() {
        println("=== Q8: Remove K Digits ===")
        val num = "1432219"; var k = 3
        val stack = Stack<Char>()
        for(c in num) {
            while(k > 0 && stack.isNotEmpty() && stack.peek() > c) { stack.pop(); k-- }
            stack.push(c)
        }
        while(k > 0 && stack.isNotEmpty()) { stack.pop(); k-- }
        val sb = StringBuilder()
        for(c in stack) { if(sb.isEmpty() && c=='0') continue; sb.append(c) } // Note: Standard Stack iterates bottom-up
        if(sb.isEmpty()) println("0") else println(sb)
    }

    /**
     * 9. Minimum Remove to Make Valid Parentheses
     *
     * PROBLEM:
     * Remove minimum number of '(' or ')' so string is valid.
     *
     * INPUT/OUTPUT:
     * Input: "lee(t(c)o)de)" -> "lee(t(c)o)de"
     *
     * DESIGN:
     * Why Stack?
     * - We need to identify *invalid* indices.
     * - Loop `i`:
     *   - `(`: Push index.
     *   - `)`:
     *     - If stack not empty, pop (match found).
     *     - If stack empty, this `)` is invalid. Mark for removal.
     * - After loop: elements remaining in stack are unmatched `(` indices. Mark them.
     * - Construct string filtering marked chars.
     *
     * DETAIL:
     * 1. `split` to char array or builder.
     * 2. Pass 1: Mark bad `)`. Push `(` indices.
     * 3. Pass 2: Mark bad `(` from stack.
     * 4. Build result.
     *
     * COMPLEXITY:
     * Time: O(N)
     * Space: O(N)
     */
    @Test
    fun q9_minRemoveValid() {
        println("=== Q9: Min Remove Valid ===")
        val s = "lee(t(c)o)de)"
        val split = s.toCharArray()
        val stack = Stack<Int>()
        for(i in split.indices) {
            if(split[i] == '(') stack.push(i)
            else if(split[i] == ')') {
                if(stack.isNotEmpty()) stack.pop() else split[i] = '*' // Mark bad closing
            }
        }
        while(stack.isNotEmpty()) split[stack.pop()] = '*' // Mark bad opening
        println("Result: ${split.filter { it != '*' }.joinToString("")}")
    }

    /**
     * 10. Score of Parentheses
     *
     * PROBLEM:
     * Balanced string. `()` is 1. `AB` is `A+B`. `(A)` is `2*A`.
     *
     * INPUT/OUTPUT:
     * Input: "(()(()))" -> 6
     *
     * DESIGN:
     * Why Depth Counter?
     * - Every `()` adds `2^depth` to the score.
     * - `(()(()))`
     *   - Inner `()` at depth 1: adds 2^1 = 2.
     *   - Inner `(())`: the `()` is at depth 2: adds 2^2 = 4.
     *   - Wait, logic: `(()` depth 2. `)` matches.
     * - Correct Logic:
     *   - Identify "cores" `()`.
     *   - Add `1 << depth` for each core.
     *
     * DETAIL:
     * 1. `depth = 0`, `res = 0`.
     * 2. Loop `i`:
     *    - `(`: `depth++`.
     *    - `)`: `depth--`.
     *    - If `s[i] == ')'` and `s[i-1] == '(': `res += 1 << depth`.
     *
     * COMPLEXITY:
     * Time: O(N)
     * Space: O(1)
     */
    @Test
    fun q10_scoreOfParentheses() {
        println("=== Q10: Score Parentheses ===")
        val s = "(()(()))"
        var depth = 0; var res = 0
        for(i in s.indices) {
            if(s[i] == '(') depth++ else depth--
            if(s[i] == ')' && s[i-1] == '(') res += 1 shl depth
        }
        println("Result: $res")
    }

    /**
     * 11. Reverse Substrings Between Each Pair of Parentheses
     *
     * PROBLEM:
     * Reverse strings inside parentheses, starting from innermost.
     *
     * INPUT/OUTPUT:
     * Input: "(u(love)i)" -> "(u(evol)i)" -> "iloveu"
     * Input: "(ed(et(oc))el)" -> "leetcode"
     *
     * DESIGN:
     * Why Stack?
     * - When we hit `)`, we need to process the *most recent* `(`.
     * - Pop characters until `(` found.
     * - These popped characters are in reverse order (stack property).
     * - Queue them (or use temp list) to push them back in reversed order relative to original string.
     *
     * DETAIL:
     * 1. Loop `c`:
     *    - If `)`:
     *      - Pop until `(`, store in Queue.
     *      - Pop `(`.
     *      - Push Queue contents back to Stack.
     *    - Else: Push `c`.
     * 2. Build string from stack.
     *
     * COMPLEXITY:
     * Time: O(N^2) in worst case (nested deeply). Optimized O(N) exists (Wormhole).
     * Space: O(N).
     */
    @Test
    fun q11_reverseParentheses() {
        println("=== Q11: Reverse Substrings ===")
        val s = "(ed(et(oc))el)"
        val stack = Stack<Char>()
        for(c in s) {
            if(c == ')') {
                val q = LinkedList<Char>()
                while(stack.peek() != '(') q.add(stack.pop())
                stack.pop() // remove '('
                for(x in q) stack.push(x) // matches queue order (reversed)
            } else stack.push(c)
        }
        println("Result: ${stack.joinToString("")}")
    }

    /**
     * 12. 132 Pattern
     *
     * PROBLEM:
     * Find indices `i < j < k` such that `nums[i] < nums[k] < nums[j]`.
     * Effectively: Find a `k` that is smaller than `j`, but larger than some previous `min`.
     *
     * INPUT/OUTPUT:
     * Input: [3, 1, 4, 2] -> True (1, 4, 2)
     *
     * DESIGN:
     * Why Monotonic Stack (Backwards)?
     * - Let `nums[k]` be the "2" in "132".
     * - We traverse from right to left.
     * - We want to find the largest candidate for "2" (`secondHigh`) that is smaller than current `nums[j]` ("3").
     * - Stack keeps potential "3"s.
     * - If `curr > stack.peek()`, then `stack.peek()` is a valid "2" candidate. Pop it and set `secondHigh`.
     * - If `curr < secondHigh`, we found "1". Return true.
     *
     * DETAIL:
     * 1. `secondHigh = MIN_VALUE`.
     * 2. Loop `i` backwards:
     *    - If `nums[i] < secondHigh`: Return true.
     *    - While `stack` not empty and `nums[i] > stack.peek()`:
     *      - `secondHigh = stack.pop()`.
     *    - `stack.push(nums[i])`.
     *
     * COMPLEXITY:
     * Time: O(N)
     * Space: O(N)
     */
    @Test
    fun q12_132Pattern() {
        println("=== Q12: 132 Pattern ===")
        val nums = intArrayOf(3, 1, 4, 2)
        val stack = Stack<Int>()
        var secondHigh = Int.MIN_VALUE
        var found = false
        for(i in nums.indices.reversed()) {
            if(nums[i] < secondHigh) { found = true; break }
            while(stack.isNotEmpty() && nums[i] > stack.peek()) {
                secondHigh = stack.pop()
            }
            stack.push(nums[i])
        }
        println("Result: $found")
    }

    /**
     * 13. Sum of Subarray Minimums
     *
     * PROBLEM:
     * Find sum of `min(b)` for every subarray `b` of `arr`. Modulo 10^9+7.
     *
     * INPUT/OUTPUT:
     * Input: [3,1,2,4] -> 17
     * Subarrays: [3], [1], [2], [4], [3,1], [1,2], [2,4], [3,1,2], [1,2,4], [3,1,2,4].
     * Mins: 3, 1, 2, 4, 1, 1, 2, 1, 1, 1. Sum: 17.
     *
     * DESIGN:
     * Why PLE and NLE (Previous/Next Less Element)?
     * - For each element `x` at `i`, how many subarrays have `x` as min?
     * - It extends to left until `< x` (PLE) and right until `<=` x (NLE) (Strict inequality on one side avoids double counting duplicates).
     * - `count = (i - prev[i]) * (next[i] - i)`.
     * - `total += count * x`.
     *
     * DETAIL:
     * 1. Compute `prev` (index of PLE) using monotonic stack.
     * 2. Compute `next` (index of NLE) using monotonic stack.
     * 3. Sum up contributions.
     *
     * COMPLEXITY:
     * Time: O(N)
     * Space: O(N)
     */
    @Test
    fun q13_subarrayMins() {
        println("=== Q13: Sum Subarray Mins ===")
        val arr = intArrayOf(3,1,2,4)
        val stack = Stack<Int>()
        val prev = IntArray(arr.size); val next = IntArray(arr.size)
        // PLE
        for(i in arr.indices) {
            while(stack.isNotEmpty() && arr[stack.peek()] >= arr[i]) stack.pop()
            prev[i] = if(stack.isEmpty()) -1 else stack.peek()
            stack.push(i)
        }
        stack.clear()
        // NLE
        for(i in arr.indices.reversed()) {
            while(stack.isNotEmpty() && arr[stack.peek()] > arr[i]) stack.pop()
            next[i] = if(stack.isEmpty()) arr.size else stack.peek()
            stack.push(i)
        }
        var res: Long = 0
        for(i in arr.indices) {
            res += (i - prev[i]).toLong() * (next[i] - i) * arr[i]
        }
        println("Result: $res")
    }

    /**
     * 14. Next Greater Element II (Circular)
     *
     * PROBLEM:
     * Find next greater element in circular array.
     *
     * INPUT/OUTPUT:
     * Input: [1,2,1] -> Output: [2,-1,2]
     *
     * DESIGN:
     * Why Loop 2*N?
     * - We simulate array concatenation `[nums, nums]`.
     * - Use modulo operator `i % n`.
     * - Standard Monotonic Decreasing Stack.
     *
     * DETAIL:
     * 1. Initialize `res` with -1.
     * 2. Loop `i` from 0 to `2*N - 1`:
     *    - `curr = nums[i % n]`.
     *    - While `stack` not empty and `nums[top] < curr`: `res[pop()] = curr`.
     *    - If `i < n`: `push(i)`. (We only need to find Next Greater for original indices).
     *
     * COMPLEXITY:
     * Time: O(N)
     * Space: O(N)
     */
    @Test
    fun q14_nextGreaterII() {
        println("=== Q14: Next Greater II (Circular) ===")
        val nums = intArrayOf(1,2,1)
        val n = nums.size
        val res = IntArray(n) { -1 }; val stack = Stack<Int>()
        for(i in 0 until 2*n) {
            while(stack.isNotEmpty() && nums[stack.peek()] < nums[i % n]) {
                res[stack.pop()] = nums[i % n]
            }
            if(i < n) stack.push(i)
        }
        println("Result: ${res.contentToString()}")
    }

    /**
     * 15. Verify Preorder Serialization of a Binary Tree
     *
     * PROBLEM:
     * Given string like "9,3,4,#,#,1,#,#,2,#,6,#,#", return true if it is a valid preorder serialization.
     * '#' is null.
     *
     * DESIGN:
     * Why Slots Count?
     * - A tree is a graph. We track "available slots" (edges).
     * - Start with 1 slot (for root).
     * - Each node consumes 1 slot.
     * - If node is non-null, it creates 2 new slots (left, right).
     * - If node is '#', it creates 0 new slots.
     * - At end, slots must be 0.
     *
     * DETAIL:
     * 1. `slots = 1`.
     * 2. Loop `node` in nodes:
     *    - `slots--`.
     *    - If `slots < 0` return false.
     *    - If `node != '#'` `slots += 2`.
     * 3. Return `slots == 0`.
     *
     * COMPLEXITY:
     * Time: O(N)
     * Space: O(1)
     */
    @Test
    fun q15_verifyPreorder() {
        println("=== Q15: Verify Preorder ===")
        val preorder = "9,3,4,#,#,1,#,#,2,#,6,#,#"
        var slots = 1
        for(p in preorder.split(",")) {
            slots--
            if(slots < 0) { slots = -1; break }
            if(p != "#") slots += 2
        }
        println("Result: ${slots == 0}")
    }

    /**
     * 16. Flatten Nested List Iterator
     *
     * PROBLEM:
     * List containing either Integers or List of Integers. Flatten to single iterator.
     *
     * DESIGN:
     * Why Stack of Iterators?
     * - If we hit a list, we push its iterator onto stack and dive in.
     * - `hasNext()`: Ensure stack top is an Integer.
     *   - If top is list, pop, push list iterator.
     *   - If top iterator done, pop.
     *
     * DETAIL:
     * 1. `Stack<Iterator>`.
     * 2. `ensureTop()` loop:
     *    - If stack empty, false.
     *    - If `!peek.hasNext()`, pop.
     *    - If next is int, true.
     *    - If next is list, push list iterator.
     *
     * COMPLEXITY:
     * Time: O(N) over all ops.
     * Space: O(Depth)
     */
    @Test
    fun q16_flattenNested() {
        println("=== Q16: Flatten Nested List ===")
        println("Logic: Stack holds Iterators. ensureNext() flattens.")
    }

    /**
     * 17. Exclusive Time of Functions
     *
     * PROBLEM:
     * Logs: ["0:start:0", "1:start:2", "1:end:5", "0:end:6"].
     * Function 0 starts at 0, calls 1 at 2. 1 ends at 5. 0 resumes, ends at 6.
     * Calculate duration for each function (excluding sub-calls).
     *
     * DESIGN:
     * Why Stack?
     * - Stack tracks currently running function ID.
     * - When new log comes at `currTime`:
     *   - Add `currTime - prevTime` to `stack.peek()`.
     *   - Update `prevTime` to `currTime`.
     *   - If start: Push new ID.
     *   - If end: Add 1 (inclusive time) to `stack.peek()`, Pop. Update `prevTime` to `currTime + 1`.
     *
     * COMPLEXITY:
     * Time: O(N)
     * Space: O(N)
     */
    @Test
    fun q17_exclusiveTime() {
        println("=== Q17: Exclusive Time ===")
        val n = 2
        val logs = listOf("0:start:0", "1:start:2", "1:end:5", "0:end:6")
        val res = IntArray(n)
        val stack = Stack<Int>()
        var prevTime = 0
        for(log in logs) {
            val parts = log.split(":")
            val id = parts[0].toInt(); val type = parts[1]; val time = parts[2].toInt()
            if(type == "start") {
                if(stack.isNotEmpty()) res[stack.peek()] += time - prevTime
                stack.push(id)
                prevTime = time
            } else {
                res[stack.pop()] += time - prevTime + 1
                prevTime = time + 1
            }
        }
        println("Result: ${res.contentToString()}")
    }

    /**
     * 18. Mini Parser
     *
     * PROBLEM:
     * Deserialize string "[123,[456,[789]]]" into NestedInteger.
     *
     * DESIGN:
     * Why Stack?
     * - Similar to Decode String.
     * - `[`: Start new NestedInteger. Push to stack.
     * - `]`: Pop, add to new top.
     * - `,`: Separator (handled by parsing logic).
     * - Digit: Parse number, add to current NI.
     *
     * COMPLEXITY:
     * Time: O(N)
     * Space: O(N)
     */
    @Test
    fun q18_miniParser() {
        println("=== Q18: Mini Parser ===")
        println("Logic: Like Decode String but with Objects.")
    }

    /**
     * 19. Binary Search Tree Iterator
     *
     * PROBLEM:
     * Iterator for BST in-order traversal. `next()` and `hasNext()` in O(1) avg.
     *
     * DESIGN:
     * Why Stack + Partial In-Order?
     * - Storing all nodes is O(N) memory.
     * - Iterative In-Order uses a Stack.
     * - Store only left spine.
     * - `next()`: Pop node (result). If node has right child, push right child and its left spine.
     *
     * DETAIL:
     * 1. `pushAll(node)`: while node!=null, stack.push, node=node.left.
     * 2. `next()`: `res = stack.pop()`. `pushAll(res.right)`. return `res.val`.
     *
     * COMPLEXITY:
     * Time: O(1) avg.
     * Space: O(H).
     */
    @Test
    fun q19_bstIterator() {
        println("=== Q19: BST Iterator ===")
        println("Logic: Push all left children.")
    }

    /**
     * 20. Remove Duplicate Letters (Lexicographically Smallest)
     *
     * PROBLEM:
     * Remove duplicates so result is lexicographically smallest subsequence containing all unique characters.
     *
     * INPUT/OUTPUT:
     * Input: "cbacdcbc" -> "acdb"
     *
     * DESIGN:
     * Why Monotonic Stack + Frequency?
     * - Similar to "Remove K digits" but with uniqueness constraint.
     * - Rule: If `s[i] < top` and `top` appears later, pop `top`.
     * - `seen`: don't add duplicate chars.
     *
     * DETAIL:
     * 1. Count char frequencies.
     * 2. Loop `c`:
     *    - `freq[c]--`.
     *    - If `seen[c]` continue.
     *    - While `stack` not empty and `top > c` and `freq[top] > 0`:
     *      - `seen[top] = false`. Pop.
     *    - `push(c)`, `seen[c] = true`.
     *
     * COMPLEXITY:
     * Time: O(N)
     * Space: O(1) (26 chars)
     */
    @Test
    fun q20_removeDuplicateLetters() {
        println("=== Q20: Remove Duplicate Letters ===")
        val s = "cbacdcbc"
        val count = IntArray(26)
        for(c in s) count[c - 'a']++
        
        val stack = Stack<Char>()
        val seen = BooleanArray(26)
        
        for(c in s) {
            val idx = c - 'a'
            count[idx]--
            if(seen[idx]) continue
            
            while(stack.isNotEmpty() && stack.peek() > c && count[stack.peek() - 'a'] > 0) {
                seen[stack.pop() - 'a'] = false
            }
            stack.push(c)
            seen[idx] = true
        }
        println("Result: ${stack.joinToString("")}")
    }
}
