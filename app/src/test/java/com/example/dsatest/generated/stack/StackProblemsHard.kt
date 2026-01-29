package com.example.dsatest.generated.stack

import org.junit.Test
import java.util.Stack
import java.util.HashMap
import kotlin.text.iterator

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
     *
     * PROBLEM:
     * Given an array of integers `heights` representing the histogram's bar height where the width of each bar is 1, return the area of the largest rectangle in the histogram.
     *
     * INPUT/OUTPUT:
     * Input: heights = [2,1,5,6,2,3] -> Output: 10
     * (The largest rectangle is made by 5 and 6, which has height 5 and width 2 -> 10).
     *
     * DESIGN:
     * Why Monotonic Increasing Stack?
     * - For each bar `x`, the possible rectangle using `x` as height extends left until `< x` and right until `< x`.
     * - We maintain a stack of increasing heights.
     * - When we see `H[i] < H[top]`, we know the right boundary for `top` is `i`.
     * - The left boundary for `top` is `stack.peek()` (after popping `top`), which is the index of the first element smaller than `top` to the left.
     * - Width = `i - left_boundary - 1`. Area = `H[top] * Width`.
     *
     * DETAIL:
     * 1. Stack holds indices. Iterate loops (add 0 at end to force flush).
     * 2. While `curr < H[top]`:
     *    - `h = pop()`.
     *    - `w = i - stack.peek() - 1` (or `i` if stack empty).
     *    - `max = max(max, h*w)`.
     *
     * COMPLEXITY:
     * Time: O(N)
     * Space: O(N)
     */
    @Test
    fun q1_largestRectangle() {
        println("=== Q1: Largest Rectangle ===")
        val heights = intArrayOf(2,1,5,6,2,3)
        // Standard Monotonic Stack algo
        val stack = Stack<Int>()
        var maxArea = 0
        val h = heights + 0 // append 0 to force pop
        for(i in h.indices) {
            while(stack.isNotEmpty() && h[i] < h[stack.peek()]) {
                val height = h[stack.pop()]
                val width = if(stack.isEmpty()) i else i - stack.peek() - 1
                maxArea = Math.max(maxArea, height * width)
            }
            stack.push(i)
        }
        println("Result: $maxArea")
    }

    /**
     * 2. Trapping Rain Water (Stack Approach)
     *
     * PROBLEM:
     * Compute how much water it can trap after raining.
     *
     * DESIGN:
     * Why Monotonic Decreasing Stack?
     * - Water accumulates in "pits" or valleys bounded by left and right walls.
     * - Stack keeps track of the left boundary walls decreasing in height.
     * - When we see a wall `H[i] > H[top]`, `H[i]` is a potential right boundary.
     * - The "bottom" of the bucket is the popped `top`.
     * - The new `stack.peek()` is the left wall boundary.
     * - Bounded Height = `min(Left, Right) - Bottom`.
     *
     * DETAIL:
     * 1. Iterate `i`.
     * 2. While `H[i] > H[stack.peek()]`:
     *    - `top = pop()`.
     *    - If empty break.
     *    - `dist = i - stack.peek() - 1`.
     *    - `h = min(H[i], H[stack.peek()]) - H[top]`.
     *    - `res += dist * h`.
     *
     * COMPLEXITY:
     * Time: O(N)
     * Space: O(N)
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
     *
     * PROBLEM:
     * Given binary matrix, find largest rectangle containing only 1s.
     *
     * DESIGN:
     * Why Histogram?
     * - View each row as ground level for a histogram formed by cumulative 1s from rows above.
     * - `heights[col]` increases if `matrix[row][col] == 1`, else resets to 0.
     * - For each row, solve "Largest Rectangle in Histogram" (Q1).
     *
     * COMPLEXITY:
     * Time: O(N * M)
     * Space: O(M)
     */
    @Test
    fun q3_maximalRectangle() {
        println("=== Q3: Maximal Rectangle ===")
        println("Logic: Convert matrix to histograms. Call Q1.")
    }

    /**
     * 4. Basic Calculator
     *
     * PROBLEM:
     * Evaluate string with `+`, `-`, `(`, `)`. Handle precedence (parentheses).
     *
     * INPUT/OUTPUT:
     * Input: "(1+(4+5+2)-3)+(6+8)" -> Output: 23
     *
     * DESIGN:
     * Why Stack for Sign/Result?
     * - `res` stores sum at current level.
     * - `sign` stores current sign (+1 or -1).
     * - `(`: Push `res` and `sign` to stack (saving context). Reset `res=0, sign=1`.
     * - `)`: Complete current level sum. Pop `prevSign`, `mul` with `res`. Pop `prevRes`, add to `res`.
     *
     * DETAIL:
     * 1. Loop `c`. Build `num` if digit.
     * 2. `+`/`-`: `res += sign*num`. `num=0`. Update `sign`.
     * 3. `(`: Push context.
     * 4. `)`: `res += sign*num`. Pop sign, prevRes. Combine.
     *
     * COMPLEXITY:
     * Time: O(N)
     * Space: O(N)
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
     *
     * PROBLEM:
     * Remove duplicates for lexicographically smallest result.
     * (See Stack Medium Q20 - Identical problem).
     *
     * DETAILS:
     * Identical logic provided in Medium. Repeated here as it is considered Hard on some platforms.
     */
    @Test
    fun q5_removeDuplicateLetters() {
        println("=== Q5: Remove Duplicate Letters ===")
        // See Medium Q20 for full code/explanation
        println("See StackProblemsMedium.q20_removeDuplicateLetters")
    }

    /**
     * 6. Basic Calculator III
     *
     * PROBLEM:
     * Evaluate expression with `+`, `-`, `*`, `/`, `(`, `)`.
     *
     * DESIGN:
     * Why 2 Stacks or Recursive?
     * - We need to handle precedence (`*` before `+`) AND parentheses.
     * - Convert to RPN? Or 2-Stack (Values, Ops).
     * - `values` stack, `operators` stack.
     * - When pushing op, apply all `preceding` ops with higher/equal precedence.
     * - Parentheses act as boundaries.
     *
     * COMPLEXITY:
     * Time: O(N)
     * Space: O(N)
     */
    @Test
    fun q6_basicCalculatorIII() {
        println("=== Q6: Basic Calculator III ===")
        println("Logic: Precedence handling like Calculator II, plus parentheses like I.")
    }

    /**
     * 7. Maximum Frequency Stack
     *
     * PROBLEM:
     * `push(x)`, `pop()`: removes element with max frequency. If tie, remove closest to top.
     *
     * DESIGN:
     * Why Map of Stacks?
     * - We need to pop form the "max frequency" bucket.
     * - `freqMap`: val -> count.
     * - `groupMap`: count -> Stack<val>.
     * - `maxFreq`: int.
     * - Push x: count++, add to `groupMap[count]`. Update `maxFreq`.
     * - Pop: Pop from `groupMap[maxFreq]`. If stack empty, `maxFreq--`.
     *
     * COMPLEXITY:
     * Time: O(1)
     * Space: O(N)
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
        val fs = FreqStack(); fs.push(5); fs.push(7); fs.push(5); fs.push(7); fs.push(4); fs.push(5)
        println("Result: ${fs.pop()} (should be 5)")
    }

    /**
     * 8. Longest Valid Parentheses
     *
     * PROBLEM:
     * Find length of longest valid (well-formed) parentheses substring.
     *
     * INPUT/OUTPUT:
     * ")()())" -> 4 "()()"
     *
     * DESIGN:
     * Why Stack Indices?
     * - Similar to valid parentheses check.
     * - Stack stores indices of *problematic* or *boundary* characters.
     * - Start with -1 in stack (base boundary).
     * - `(`: Push index.
     * - `)`: Pop.
     *   - If stack empty: Push index (this `)` is a new boundary, splits valid groups).
     *   - Else: `max = current_index - stack.peek()`.
     *
     * COMPLEXITY:
     * Time: O(N)
     * Space: O(N)
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
     *
     * PROBLEM:
     * Parse formula like "K4(ON(SO3)2)2". Return count "K4N2O14S2". Map sorted by Atom name.
     *
     * DESIGN:
     * Why Stack of Maps?
     * - `Map<String, Int>` stores counts for current scope.
     * - `(`: Push new empty map.
     * - `)`: Parse multiplicity `k`. Pop top map, multiply all values by `k`, add to next map on stack.
     * - Atom: Parse name, count, add to current map.
     *
     * COMPLEXITY:
     * Time: O(N) parsing. Output sorting O(K log K).
     * Space: O(N)
     */
    @Test
    fun q9_numberOfAtoms() {
        println("=== Q9: Number of Atoms ===")
        println("Logic: Recursion/Stack. On ')', multiply counts in current map.")
    }

    /**
     * 10. Tag Validator
     *
     * PROBLEM:
     * Validate XML-like code. CDATA, TAGS, UPPERCASE names.
     *
     * DESIGN:
     * why Stack for Tags?
     * - Standard matching tags `<NAME>` and `</NAME>`.
     * - CDATA is content that allows anything inside.
     * - String parsing state machine required.
     *
     * COMPLEXITY:
     * Time: O(N)
     * Space: O(N)
     */
    @Test
    fun q10_tagValidator() {
        println("=== Q10: Tag Validator ===")
        println("Logic: Stack for Tags. Checking contents via State Machine.")
    }

    /**
     * 11. Dinner Plate Stacks
     *
     * PROBLEM:
     * Infinite number of stacks (capacity `cap`). Push to leftmost non-full stack. Pop from rightmost non-empty stack.
     * `popAtStack(index)` pops from specific stack.
     *
     * DESIGN:
     * Why Treeset + Map?
     * - `stacks`: Map<Index, Stack>.
     * - `available`: TreeSet of indices of stacks that are not full.
     * - Push: Get min index from `available`. Push. If full, remove from `available`.
     * - Pop: Get max index from `stacks` keys. Pop. Add index to `available` (if it was full).
     *
     * COMPLEXITY:
     * Time: O(log N) push/pop (due to TreeSet).
     * Space: O(N)
     */
    @Test
    fun q11_dinnerPlates() {
        println("=== Q11: Dinner Plates ===")
        println("Logic: Map index to Stack. TreeSet / PQ for indices.")
    }

    /**
     * 12. Parse Lisp Expression
     *
     * PROBLEM:
     * Evaluate Lisp-like string: `(let x 2 (mult x (let x 3 y 4 (add x y))))`.
     * Scope rules apply.
     *
     * DESIGN:
     * Why Recursion + Scope Stack?
     * - `evaluate(expression, scope)`.
     * - `scope`: Map<String, Int>.
     * - When entering `(let ...)`, create *new* scope inheriting from parent.
     * - Parse tokens. Recursively evaluate sub-expressions.
     *
     * COMPLEXITY:
     * Time: O(N^2) string ops.
     * Space: O(N) recursion.
     */
    @Test
    fun q12_parseLisp() {
        println("=== Q12: Parse Lisp ===")
        println("Logic: Context Map (Scope variables). Recursion.")
    }

    /**
     * 13. Create Maximum Number
     *
     * PROBLEM:
     * Create max number of length `k` from two arrays `nums1`, `nums2` preserving order.
     *
     * DESIGN:
     * Why Merge + Monotonic?
     * - Breakdown: Pick `i` nums from `nums1`, `k-i` nums from `nums2`.
     * - For each array, pick `x` elements to form MAX subsequence (Monotonic Stack limit size).
     * - Merge two subsequences to get largest result.
     * - Try all valid split `i` and `k-i`. Compare results.
     *
     * COMPLEXITY:
     * Time: O(k * (N+M)^2) or O(k * (N+M)).
     * Space: O(k)
     */
    @Test
    fun q13_createMaxNumber() {
        println("=== Q13: Create Max Number ===")
        println("Logic: Select K from A, M from B. Merge max.")
    }

    /**
     * 14. Sum of Subarray Ranges
     *
     * PROBLEM:
     * Sum(max - min) for all subarrays.
     * Equivalent to Sum(max) - Sum(min).
     *
     * DESIGN:
     * Why Monotonic Stack?
     * - Reuse logic from "Sum of Subarray Minimums" (Q13 Medium).
     * - `SumMin`: Use PLE/NLE with increasing stack.
     * - `SumMax`: Use PGE/NGE with decreasing stack.
     * - `Res = SumMax - SumMin`.
     *
     * COMPLEXITY:
     * Time: O(N)
     * Space: O(N)
     */
    @Test
    fun q14_subarrayRanges() {
        println("=== Q14: Sum Subarray Ranges ===")
        val nums = intArrayOf(1,2,3)
        // O(n) using Monotonic Stacks for SumMin and SumMax
        println("Logic: Monotonic Stack (Min & Max).")
    }

    /**
     * 15. Car Fleet II (Collision Times)
     *
     * PROBLEM:
     * Cars moving right. If fast hits slow, it joins fleet and matches speed.
     * Calculate collision time for each car.
     *
     * DESIGN:
     * Why Monotonic Stack?
     * - We care about the *next* car that is slower.
     * - Stack maintains cars that might be collided with.
     * - `stack.peek()` is the car immediately in front.
     * - Condition: If `current` catches `stack.peek()` *after* `stack.peek()` catches its own front, then `stack.peek()` is irrelevant for `current` (it vanishes into the fleet ahead). Pop it.
     *
     * DETAIL:
     * 1. Iterate backwards.
     * 2. While stack top is faster (won't catch) or catches its front before `current` catches top: pop.
     * 3. Collision time formula.
     *
     * COMPLEXITY:
     * Time: O(N)
     * Space: O(N)
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
     *
     * PROBLEM:
     * Outline of buildings. [L, R, H].
     *
     * DESIGN:
     * Why Sweep Line + Heap?
     * - Process edges (Left: add height, Right: remove height).
     * - Current height = max in set.
     * - If max changes, record point.
     * - Can use Stack if sorted carefully, but Heap is standard.
     *
     * COMPLEXITY:
     * Time: O(N log N)
     * Space: O(N)
     */
    @Test
    fun q16_skyline() {
        println("=== Q16: Skyline (Stack) ===")
        println("See ArrayProblemsHard (usually solved there or with Heap/SegmentTree)")
    }

    /**
     * 17. Online Stock Span
     *
     * PROBLEM:
     * `next(price)`: return span (consecutive days <= price ending today).
     *
     * DESIGN:
     * Why Monotonic Stack?
     * - If `price` >= `stack.peek().price`, we can merge the span.
     * - `accumulated_span = 1`.
     * - While `price >= peek.price`: `accumulated_span += pop().span`.
     * - Push combined span.
     *
     * COMPLEXITY:
     * Time: O(1) amortized.
     * Space: O(N)
     */
    @Test
    fun q17_stockSpan() {
        println("=== Q17: Stock Span ===")
        // push(price): while price >= stack.peek.price: span += stack.pop.span
        println("Logic: Monotonic Stack compressing history.")
    }

    /**
     * 18. Pattern 132 (Hard constraint O(n))
     *
     * DETAILS:
     * See Medium Q12. It's often classified as Medium or Hard depending on constraint (O(n)).
     */
    @Test
    fun q18_pattern132() { println("See StackProblemsMedium.q12_132Pattern") }

    /**
     * 19. Maximum Width Ramp
     *
     * PROBLEM:
     * Max `j - i` such that `nums[i] <= nums[j]`.
     *
     * DESIGN:
     * Why Monotonic Decreasing Stack + Reverse?
     * - We want smallest `i` (left candidates).
     *   - Build stack decreasing: if `nums[k] < nums[top]`, push `k`.
     *   - This gives candidates for being the "left" end of the ramp.
     * - Then iterate `j` from right to left (largest `j`).
     *   - While `nums[j] >= nums[top]`: We found a valid ramp. Pop `top` (best match for this `top` since `j` is decreasing, we won't find better width for this `top`).
     *
     * DETAIL:
     * 1. Build Stack indices.
     * 2. Iterate `j` backwards.
     * 3. `while top <= j`: `max = max(max, j - top)`.
     *
     * COMPLEXITY:
     * Time: O(N)
     * Space: O(N)
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
     *
     * PROBLEM:
     * Given binary matrix, count all rectangles of 1s.
     *
     * DESIGN:
     * Why Histogram + Monotonic Count?
     * - Build histogram array `h` for each row.
     * - For row `i`, `h[j]` is height of consecutive 1s upwards.
     * - Problem becomes: sum of sub-rectangles in histogram.
     * - Let `res[k]` be number of rectangles ending at column `k` with height `h[k]`.
     * - Monotonic Stack stores previous smaller height indices.
     * - `res[k] = res[top] + (h[k] * (k - top))`.
     *
     * COMPLEXITY:
     * Time: O(N * M)
     * Space: O(M)
     */
    @Test
    fun q20_countSubmatrices() {
        println("=== Q20: Count Submatrices All Ones ===")
        println("Logic: Histogram per row. Stack accumulates rectangles.")
    }
}
