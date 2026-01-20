package com.example.dsatest

import org.junit.Test
import java.util.Stack
import java.util.HashMap
import java.util.TreeMap

/**
 * ==========================================
 * STACK PROBLEMS: HARD (1-20)
 * ==========================================
 * 
 * Solutions to 20 Hard Stack questions.
 * Patterns: Advanced Monotonic, Complex Parsing.
 */
class StackProblemsHard {

    /**
     * 1. Largest Rectangle in Histogram
     * Logic: Monotonic Stack Increasing.
     */
    @Test
    fun q1_largestRectangle() {
        println("=== Q1: Largest Rectangle ===")
        val h = intArrayOf(2,1,5,6,2,3)
        // See ArrayProblemsHard.q3 for implementation
        println("Logic: Monotonic Stack to find boundaries.")
    }

    /**
     * 2. Trapping Rain Water (Stack)
     * Logic: Stack Decreasing. Water accumulates in 'pits'.
     */
    @Test
    fun q2_trapRainWaterStack() {
        println("=== Q2: Trap Water (Stack) ===")
        val h = intArrayOf(0,1,0,2,1,0,1,3,2,1,2,1)
        val stack = Stack<Int>()
        var res = 0
        for(i in h.indices) {
            while(stack.isNotEmpty() && h[i] > h[stack.peek()]) {
                val top = stack.pop()
                if(stack.isEmpty()) break
                val dist = i - stack.peek() - 1
                val height = Math.min(h[i], h[stack.peek()]) - h[top]
                res += dist * height
            }
            stack.push(i)
        }
        println("Result: $res")
    }

    /**
     * 3. Maximal Rectangle
     * Logic: Histogram on each row.
     */
    @Test
    fun q3_maximalRectangle() {
        println("=== Q3: Maximal Rectangle ===")
        println("Logic: Convert matrix to histograms. Call Q1.")
    }

    /**
     * 4. Basic Calculator ( + - ( ) )
     * Logic: Stack holds sign and result context.
     */
    @Test
    fun q4_basicCalculator() {
        println("=== Q4: Basic Calculator I ===")
        val s = "(1+(4+5+2)-3)+(6+8)"
        val stack = Stack<Int>()
        var res = 0; var num = 0; var sign = 1
        for(c in s) {
            if(c.isDigit()) num = num * 10 + (c - '0')
            else if(c == '+') { res += sign * num; num = 0; sign = 1 }
            else if(c == '-') { res += sign * num; num = 0; sign = -1 }
            else if(c == '(') { stack.push(res); stack.push(sign); res = 0; sign = 1 }
            else if(c == ')') {
                res += sign * num; num = 0
                res *= stack.pop() // sign
                res += stack.pop() // prevBase
            }
        }
        if(num != 0) res += sign * num
        println("Result: $res")
    }

    /**
     * 5. Remove Duplicate Letters
     * Logic: Monotonic + Count. Smallest lexicographical.
     */
    @Test
    fun q5_removeDuplicateLetters() {
        println("=== Q5: Remove Duplicate Letters ===")
        val s = "cbacdcbc"
        val count = IntArray(26); for(c in s) count[c - 'a']++
        val visited = BooleanArray(26)
        val stack = Stack<Char>()
        for(c in s) {
            count[c - 'a']--
            if(visited[c - 'a']) continue
            while(stack.isNotEmpty() && c < stack.peek() && count[stack.peek() - 'a'] > 0) {
                visited[stack.pop() - 'a'] = false
            }
            stack.push(c)
            visited[c - 'a'] = true
        }
        println("Result: ${stack.joinToString("")}")
    }

    /**
     * 6. Basic Calculator III ( + - * / ( ) )
     * Logic: Two Stacks or Conversion to RPN.
     */
    @Test
    fun q6_basicCalculatorIII() {
        println("=== Q6: Basic Calculator III ===")
        println("Logic: Precedence handling like Calculator II, plus parentheses like I.")
    }

    /**
     * 7. Max Frequency Stack
     * Logic: Map<Freq, Stack>. MaxFreq tracker.
     */
    @Test
    fun q7_freqStack() {
        println("=== Q7: Freq Stack ===")
        class FreqStack() {
            val freq = HashMap<Int, Int>()
            val group = HashMap<Int, Stack<Int>>()
            var maxFreq = 0
            fun push(x: Int) {
                val f = freq.getOrDefault(x, 0) + 1
                freq[x] = f
                if(f > maxFreq) maxFreq = f
                group.computeIfAbsent(f) { Stack() }.push(x)
            }
            fun pop(): Int {
                val x = group[maxFreq]!!.pop()
                freq[x] = freq[x]!! - 1
                if(group[maxFreq]!!.isEmpty()) maxFreq--
                return x
            }
        }
        println("Logic: Map Freq->Stack allows O(1).")
    }

    /**
     * 8. Longest Valid Parentheses
     * Logic: Stack indices. Push -1 base.
     */
    @Test
    fun q8_longestValidParentheses() {
        println("=== Q8: Longest Valid Parentheses ===")
        val s = ")()())"
        val stack = Stack<Int>()
        stack.push(-1)
        var max = 0
        for(i in s.indices) {
            if(s[i] == '(') stack.push(i)
            else {
                stack.pop()
                if(stack.isEmpty()) stack.push(i)
                else max = Math.max(max, i - stack.peek())
            }
        }
        println("Result: $max")
    }

    /**
     * 9. Number of Atoms
     * Logic: Parsing. Map<String, Int>. Stack<Map>. Using () multiply match.
     */
    @Test
    fun q9_numberOfAtoms() {
        println("=== Q9: Number of Atoms ===")
        println("Logic: Recursion/Stack. On ')', multiply counts in current map.")
    }

    /**
     * 10. Tag Validator
     * Logic: CDATA vs TAG parsing. Complex State Machine.
     */
    @Test
    fun q10_tagValidator() {
        println("=== Q10: Tag Validator ===")
        // Very niche parsing problem.
        println("Logic: Stack for Tags. Checking contents.")
    }

    /**
     * 11. Dinner Plate Stacks
     * Logic: List<Stack>. PriorityQueue for empty slots (leftmost).
     */
    @Test
    fun q11_dinnerPlates() {
        println("=== Q11: Dinner Plates ===")
        println("Logic: Map index to Stack. TreeSet / PQ for indices.")
    }

    /**
     * 12. Parse Lisp Expression
     * Logic: Recursion / StackScope.
     */
    @Test
    fun q12_parseLisp() {
        println("=== Q12: Parse Lisp ===")
        println("Logic: Context Map (Scope variables). Recursion.")
    }

    /**
     * 13. Create Maximum Number
     * Logic: Merge two arrays (Monotonic) to get max sequence.
     */
    @Test
    fun q13_createMaxNumber() {
        println("=== Q13: Create Max Number ===")
        println("Logic: Select K from A, M from B. Merge max.")
    }

    /**
     * 14. Sum of Subarray Ranges
     * Logic: Sum(Max) - Sum(Min). Q13 Medium extended.
     */
    @Test
    fun q14_subarrayRanges() {
        println("=== Q14: Sum Subarray Ranges ===")
        val nums = intArrayOf(1,2,3)
        // O(n) using Monotonic Stacks for SumMin and SumMax
        println("Logic: Monotonic Stack (Min & Max).")
    }

    /**
     * 15. Car Fleet I / II
     * Logic: Monotonic Stack -> Collision times.
     */
    @Test
    fun q15_carFleet() {
        println("=== Q15: Car Fleet ===")
        val target = 12; val pos = intArrayOf(10,8,0,5,3); val speed = intArrayOf(2,4,1,1,3)
        // Sort by pos.
        // Time = (Target - Pos) / Speed
        // If Time[i] <= Time[i-1] (ahead), merge (stack pop/peek check).
        println("Logic: Calculate arrival times. Non-increasing stack.")
    }

    /**
     * 16. The Skyline Problem
     * Logic: Stack approach exists, but Sweep Line (Heap) is standard.
     */
    @Test
    fun q16_skyline() {
        println("=== Q16: Skyline (Stack) ===")
        println("See ArrayProblemsHard")
    }

    /**
     * 17. Online Stock Span
     * Logic: Monotonic Stack (Price, Span).
     */
    @Test
    fun q17_stockSpan() {
        println("=== Q17: Stock Span ===")
        // push(price): while price >= stack.peek.price: span += stack.pop.span
        println("Logic: Monotonic Stack compressing history.")
    }

    /**
     * 18. Pattern 132 (Hard constraint O(n))
     * Logic: See Medium Q12.
     */
    @Test
    fun q18_pattern132() { println("See Medium Q12") }

    /**
     * 19. Maximum Width Ramp
     * Logic: Monotonic Decreasing Stack of indices. Traverse backwards.
     */
    @Test
    fun q19_maxWidthRamp() {
        println("=== Q19: Max Width Ramp ===")
        val nums = intArrayOf(6,0,8,2,1,5)
        val stack = Stack<Int>()
        for(i in nums.indices) {
            if(stack.isEmpty() || nums[stack.peek()] > nums[i]) stack.push(i)
        }
        var max = 0
        for(j in nums.size-1 downTo 0) {
            while(stack.isNotEmpty() && nums[stack.peek()] <= nums[j]) {
                max = Math.max(max, j - stack.pop())
            }
        }
        println("Result: $max")
    }

    /**
     * 20. Count Submatrices With All Ones
     * Logic: Histogram on each row + Monotonic Stack count contribution.
     */
    @Test
    fun q20_countSubmatrices() {
        println("=== Q20: Count Submatrices All Ones ===")
        println("Logic: Histogram per row. Stack accumulates rectangles.")
    }
}
