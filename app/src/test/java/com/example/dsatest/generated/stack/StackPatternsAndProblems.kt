package com.example.dsatest.generated.stack

import org.junit.Test
import java.util.Stack
import kotlin.text.iterator

/**
 * ==========================================
 * STACKS: PATTERNS & QUESTIONS
 * ==========================================
 * 
 * Stacks seem simple (LIFO), but they power complex algorithms.
 * 
 * INTERVIEW CHEATSHEET:
 * 1. Monotonic Stack: Finding "Next Greater" or "Previous Smaller" element.
 *    - Logic: Keep stack sorted. Pop when strictly smaller/greater.
 *    - Complexity: O(N) (Each element pushed/popped at most once).
 * 2. Parentheses/Parsing: Matching brackets or evaluating math.
 *    - Logic: Push Openers, Pop Closers.
 * 3. DFS / Backtracking: Explicit stack mimics recursion.
 * 4. Two Stacks: Min Stack, Queue implementation.
 * 
 * MASTER THESE PATTERNS:
 * 1. Monotonic Stack
 * 2. Parentheses Matching
 * 3. Calculator / Expression Parsing
 * 4. DFS Simulation
 * 
 * ==========================================
 * THE QUESTION BUCKET LIST
 * ==========================================
 * 
 * === EASY (20) ===
 * 1. Valid Parentheses [Implemented Below]
 * 2. Min Stack [Implemented Below]
 * 3. Implement Stack using Queues
 * 4. Implement Queue using Stacks
 * 5. Backspace String Compare
 * 6. Baseball Game
 * 7. Remove All Adjacent Duplicates In String
 * 8. Next Greater Element I (Monotonic) [Implemented Below]
 * 9. Make The String Great
 * 10. Crawler Log Folder
 * 11. Build an Array With Stack Operations
 * 12. Final Prices With a Special Discount
 * 13. Maximum Nesting Depth of the Parentheses
 * 14. Remove Outermost Parentheses
 * 15. Number of Students Unable to Eat Lunch
 * 16. Time Needed to Buy Tickets
 * 17. Minimum Add to Make Parentheses Valid (Easy version)
 * 18. Check if Word is Valid After Substitutions
 * 19. Validate Stack Sequences
 * 20. Palindrome Linked List (Stack Method)
 * 
 * === MEDIUM (20) ===
 * 1. Evaluate Reverse Polish Notation (RPN)
 * 2. Simplify Path
 * 3. Decode String
 * 4. Min Stack (Optimized)
 * 5. Daily Temperatures (Monotonic)
 * 6. Generate Parentheses (Backtracking/Stack)
 * 7. Asteroid Collision
 * 8. Design A Stack With Increment Operation
 * 9. Validate Stack Sequences
 * 10. Minimum Remove to Make Valid Parentheses
 * 11. Score of Parentheses
 * 12. Reverse Substrings Between Each Pair of Parentheses
 * 13. 132 Pattern
 * 14. Remove K Digits (Monotonic)
 * 15. Sum of Subarray Minimums (Monotonic)
 * 16. Next Greater Element II (Circular)
 * 17. Verify Preorder Serialization of a Binary Tree
 * 18. Flatten Nested List Iterator
 * 19. Exclusive Time of Functions
 * 20. Design Browser History (Stack approach)
 * 
 * === HARD (20) ===
 * 1. Largest Rectangle in Histogram (Monotonic)
 * 2. Trapping Rain Water (Stack approach)
 * 3. Maximal Rectangle
 * 4. Basic Calculator (I, II, III)
 * 5. Remove Duplicate Letters (Monotonic)
 * 6. Tag Validator
 * 7. Check If Word Is Valid After Substitutions
 * 8. Parse Lisp Expression
 * 9. Exclusive Time of Functions (Complex cases)
 * 10. Max Stack (Design)
 * 11. Dinner Plate Stacks
 * 12. Number of Atoms
 * 13. Cracking the Safe
 * 14. Longest Valid Parentheses
 * 15. Create Maximum Number
 * 16. Sum of Subarray Ranges
 * 17. Car Fleet I / II
 * 18. Maximum Frequency Stack
 * 19. Online Stock Span
 * 20. The Skyline Problem (Stack approach)
 */
class StackPatternsAndProblems {

    /**
     * ==========================================
     * PATTERN 1: VALID PARENTHESES
     * ==========================================
     * Logic: Push openers. Pop and check closers.
     */
    @Test
    fun patternParentheses() {
        println("=== PATTERN: PARENTHESES ===")
        val s = "()[]{}"
        val stack = Stack<Char>()
        var valid = true
        for (c in s) {
            when (c) {
                '(', '[', '{' -> stack.push(c)
                ')' -> if(stack.isEmpty() || stack.pop() != '(') valid = false
                ']' -> if(stack.isEmpty() || stack.pop() != '[') valid = false
                '}' -> if(stack.isEmpty() || stack.pop() != '{') valid = false
            }
        }
        println("Valid: ${valid && stack.isEmpty()}")
    }

    /**
     * ==========================================
     * PATTERN 2: MONOTONIC STACK (Next Greater)
     * ==========================================
     * Problem: Next Greater Element I
     * Logic: Keep stack decreasing. If curr > peek, peek's next greater is curr.
     */
    @Test
    fun patternMonotonicStack() {
        println("\n=== PATTERN: MONOTONIC STACK ===")
        val nums = intArrayOf(2, 1, 2, 4, 3)
        val res = IntArray(nums.size) { -1 }
        val stack = Stack<Int>() // Stores INDICES
        
        for (i in nums.indices) {
            while (stack.isNotEmpty() && nums[i] > nums[stack.peek()]) {
                val idx = stack.pop()
                res[idx] = nums[i]
            }
            stack.push(i)
        }
        println("Next Greater: ${res.contentToString()}")
    }

    /**
     * ==========================================
     * PATTERN 3: MIN STACK
     * ==========================================
     * Logic: Pair<Val, MinSoFar> OR Two Stacks.
     */
    @Test
    fun patternMinStack() {
        println("\n=== PATTERN: MIN STACK ===")
        val stackVal = Stack<Int>()
        val stackMin = Stack<Int>()
        
        fun push(x: Int) {
            stackVal.push(x)
            if (stackMin.isEmpty() || x <= stackMin.peek()) stackMin.push(x)
        }
        fun pop() {
            val v = stackVal.pop()
            if (v == stackMin.peek()) stackMin.pop()
        }
        fun getMin(): Int = stackMin.peek()
        
        push(-2); push(0); push(-3)
        println("Min: ${getMin()}") // -3
        pop()
        println("Min: ${getMin()}") // -2
    }
}
