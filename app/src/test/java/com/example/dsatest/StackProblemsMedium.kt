package com.example.dsatest

import org.junit.Test
import java.util.Stack
import java.util.ArrayDeque

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
     * Logic: Stack numbers. Op -> pop 2, apply, push.
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
     * Logic: Stack directories. Pop on "..". Ignore ".".
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
        println("Result: /${stack.joinToString("/")}")
    }

    /**
     * 3. Decode String
     * Logic: Stack counts, Stack strings. On ']' pop and build.
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
     * Logic: Monotonic Decreasing Stack (Indices).
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
     * Logic: Backtracking (or Stack simulation).
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
     * Logic: Stack. Positive push. Negative -> collide.
     */
    @Test
    fun q6_asteroidCollision() {
        println("=== Q6: Asteroid Collision ===")
        val asteroids = intArrayOf(5, 10, -5)
        val stack = Stack<Int>()
        for(ast in asteroids) {
            var survive = true
            while(stack.isNotEmpty() && ast < 0 && stack.peek() > 0) {
                if(stack.peek() < -ast) { stack.pop(); continue }
                else if(stack.peek() == -ast) stack.pop()
                survive = false; break
            }
            if(survive) stack.push(ast)
        }
        println("Result: $stack")
    }

    /**
     * 7. Design Stack With Increment
     * Logic: Array + lazy increment array.
     */
    @Test
    fun q7_incrementStack() {
        println("=== Q7: Stack Inc ===")
        println("Logic: Use array. inc[i] adds to stack[i]. Lazy add.")
    }

    /**
     * 8. Remove K Digits
     * Logic: Smallest number -> Monotonic Increasing Stack.
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
        for(c in stack) { if(sb.isEmpty() && c=='0') continue; sb.append(c) } // build correct order
        if(sb.isEmpty()) println("0") else println(sb)
    }

    /**
     * 9. Minimum Remove to Make Valid
     * Logic: Stack indices to remove.
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
                if(stack.isNotEmpty()) stack.pop() else split[i] = '*'
            }
        }
        while(stack.isNotEmpty()) split[stack.pop()] = '*'
        println("Result: ${split.filter { it != '*' }.joinToString("")}")
    }

    /**
     * 10. Score of Parentheses
     * Logic: Depth contribution OR Stack.
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
     * 11. Reverse Substrings Between Pairs
     * Logic: Stack chars OR Wormhole (Pair matching + Iteration).
     */
    @Test
    fun q11_reverseParentheses() {
        println("=== Q11: Reverse Substrings ===")
        val s = "(ed(et(oc))el)"
        val stack = Stack<Char>()
        for(c in s) {
            if(c == ')') {
                val q = java.util.LinkedList<Char>()
                while(stack.peek() != '(') q.add(stack.pop())
                stack.pop() // remove '('
                for(x in q) stack.push(x) // matches queue order (reversed)
            } else stack.push(c)
        }
        println("Result: ${stack.joinToString("")}")
    }

    /**
     * 12. 132 Pattern
     * Logic: Find 3 (nums[k]) < 2 (nums[j]) > 1 (nums[i]).
     * Monotonic Stack backwards.
     */
    @Test
    fun q12_132Pattern() {
        println("=== Q12: 132 Pattern ===")
        val nums = intArrayOf(1, 2, 3, 4) // False
        println("Logic: Stack track '3' (second largest from back).")
    }

    /**
     * 13. Sum of Subarray Minimums
     * Logic: Monotonic Stack (Prev Smaller, Next Smaller).
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
     * Logic: Loop 2x. Modulo size.
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
     * 15. Verify Preorder Serialization
     * Logic: Slots count. (null takes 1, node takes 1 gives 2). Net +1.
     */
    @Test
    fun q15_verifyPreorder() {
        println("=== Q15: Verify Preorder ===")
        val preorder = "9,3,4,#,#,1,#,#,2,#,6,#,#"
        var slots = 1
        for(p in preorder.split(",")) {
            slots--
            if(slots < 0) return
            if(p != "#") slots += 2
        }
        println("Result: ${slots == 0}")
    }

    /**
     * 16. Flatten Nested List Iterator
     * Logic: Stack<Iterator>.
     */
    @Test
    fun q16_flattenNested() {
        println("=== Q16: Flatten Nested List ===")
        println("Logic: Stack holds Iterators. ensureNext() flattens.")
    }

    /**
     * 17. Exclusive Time of Functions
     * Logic: Stack<Id>. Update prev time.
     */
    @Test
    fun q17_exclusiveTime() {
        println("=== Q17: Exclusive Time ===")
        // [0:start:0, 1:start:2, 1:end:5, 0:end:6]
        println("Logic: Current running func on stack top. Add (time - prev).")
    }

    /**
     * 18. Mini Parser (Nested Integers)
     * Logic: Stack<NestedInteger>.
     */
    @Test
    fun q18_miniParser() {
        println("=== Q18: Mini Parser ===")
        println("Logic: Like Decode String but with Objects.")
    }

    /**
     * 19. BST Iterator
     * Logic: Stack holds path to left-most.
     */
    @Test
    fun q19_bstIterator() {
        println("=== Q19: BST Iterator ===")
        println("Logic: Push all left children.")
    }

    /**
     * 20. Remove Duplicate Letters
     * Logic: Monotonic Stack + Count Map.
     */
    @Test
    fun q20_removeDuplicateLetters() {
        println("=== Q20: Remove Duplicate Letters ===")
        // Hard on LC, Medium here? Logic is same as Remove K Digits usually
        println("Logic: Monotonic Stack + Frequency Map.")
    }
}
