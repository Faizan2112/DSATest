package com.example.dsatest

import org.junit.Test
import java.util.HashMap

/**
 * ==========================================
 * STRINGS: 60 TOP INTERVIEW QUESTIONS & PATTERNS
 * ==========================================
 * 
 * Strings are the most common interview topic.
 * They usually involve: Two Pointers, Sliding Window, or Hashing.
 * 
 * INTERVIEW CHEATSHEET:
 * 1. Two Pointers: Palindromes, Reversing. O(N).
 * 2. Sliding Window: Longest/Shortest Substring problems. O(N).
 *    - Logic: Expand R, Shrink L. Use Map/Set to track window state.
 * 3. Hashing: Anagrams, Count frequencies. O(N).
 *    - Tip: Use `IntArray(26)` for 'a'-'z' instead of HashMap for speed.
 * 4. KMP / Rolling Hash: Pattern matching (Advanced).
 * 
 * ==========================================
 * THE 60 QUESTION BUCKET LIST
 * ==========================================
 * 
 * === EASY (20) ===
 * 1. Valid Anagram (Hashing) [Implemented Below]
 * 2. Valid Palindrome (Two Pointers) [Implemented Below]
 * 3. Longest Common Prefix
 * 4. Reverse String
 * 5. Reverse Vowels of a String
 * 6. Isomorphic Strings
 * 7. Word Pattern
 * 8. First Unique Character in a String
 * 9. Length of Last Word
 * 10. Add Strings (Math)
 * 11. Ransom Note
 * 12. Valid Parentheses (Stack)
 * 13. Implement strStr()
 * 14. Rotate String
 * 15. To Lower Case
 * 16. Check if Strings are Equivalent
 * 17. Merge Strings Alternately
 * 18. Shuffle String
 * 19. Defanging an IP Address
 * 20. Goal Parser Interpretation
 * 
 * === MEDIUM (20) ===
 * 1. Longest Substring Without Repeating Characters (Sliding Window) [Implemented Below]
 * 2. Group Anagrams (Hashing)
 * 3. Longest Palindromic Substring (DP/Center Expansion)
 * 4. String to Integer (atoi)
 * 5. Zigzag Conversion
 * 6. Generate Parentheses
 * 7. Multiply Strings
 * 8. Reverse Words in a String
 * 9. Simplify Path (Stack)
 * 10. Decode String (Stack/Recursion)
 * 11. Find All Anagrams in a String (Sliding Window)
 * 12. Permutation in String
 * 13. Longest Repeating Character Replacement
 * 14. Palindromic Substrings
 * 15. Compare Version Numbers
 * 16. Encode and Decode TinyURL
 * 17. Custom Sort String
 * 18. String Compression
 * 19. Remove All Adjacent Duplicates in String II
 * 20. Count and Say
 * 
 * === HARD (20) ===
 * 1. Minimum Window Substring (Sliding Window) [Implemented Below]
 * 2. Longest Valid Parentheses
 * 3. Edit Distance (DP)
 * 4. Distinct Subsequences
 * 5. Text Justification
 * 6. Regular Expression Matching
 * 7. Wildcard Matching
 * 8. Scramble String
 * 9. Substring with Concatenation of All Words
 * 10. Shortest Palindrome
 * 11. Word Ladder (Graph BFS)
 * 12. Word Search II (Trie)
 * 13. Palindrome Pairs (Trie)
 * 14. Integer to English Words
 * 15. Basic Calculator
 * 16. Remove Invalid Parentheses
 * 17. Longest Duplicate Substring
 * 18. Smallest Sufficient Team
 * 19. Orderly Queue
 * 20. Distinct Subsequences II
 */
class StringPatternsAndProblems {

    /**
     * ==========================================
     * PATTERN 1: HASHING (Frequency Map)
     * ==========================================
     * Problem: Valid Anagram (Easy)
     * "anagram", "nagaram" -> true
     */
    @Test
    fun validAnagram() {
        println("=== PATTERN: HASHING (Valid Anagram) ===")
        val s = "anagram"
        val t = "nagaram"
        
        if (s.length != t.length) { 
            println("False (Length mismatch)"); return 
        }

        val counts = IntArray(26) // 'a'..'z'
        for (i in s.indices) {
            counts[s[i] - 'a']++
            counts[t[i] - 'a']--
        }

        val isAnagram = counts.all { it == 0 }
        println("Is Anagram: $isAnagram")
    }

    /**
     * ==========================================
     * PATTERN 2: TWO POINTERS
     * ==========================================
     * Problem: Valid Palindrome (Easy)
     * "A man, a plan, a canal: Panama" -> true
     */
    @Test
    fun validPalindrome() {
        println("\n=== PATTERN: TWO POINTERS (Valid Palindrome) ===")
        val s = "A man, a plan, a canal: Panama"
        var left = 0
        var right = s.length - 1
        var result = true
        
        while (left < right) {
            // Skip non-alphanumeric
            while (left < right && !s[left].isLetterOrDigit()) left++
            while (left < right && !s[right].isLetterOrDigit()) right--
            
            if (s[left].lowercaseChar() != s[right].lowercaseChar()) {
                result = false
                break
            }
            left++
            right--
        }
        println("Is Palindrome: $result")
    }

    /**
     * ==========================================
     * PATTERN 3: SLIDING WINDOW (Variable Size)
     * ==========================================
     * Problem: Longest Substring Without Repeating Characters (Medium)
     * "abcabcbb" -> 3 ("abc")
     */
    @Test
    fun longestSubstringNoRepeats() {
        println("\n=== PATTERN: SLIDING WINDOW (Longest Substring) ===")
        val s = "abcabcbb"
        var maxLen = 0
        var left = 0
        val charIndexMap = HashMap<Char, Int>()
        
        for (right in s.indices) {
            val char = s[right]
            // If we have seen this char, move left pointer to (prevIndex + 1)
            // BUT, strictly to the right (don't move left pointer backwards)
            if (charIndexMap.containsKey(char)) {
                left = Math.max(left, charIndexMap[char]!! + 1)
            }
            
            charIndexMap[char] = right
            maxLen = Math.max(maxLen, right - left + 1)
        }
        println("Max Length: $maxLen")
    }

    /**
     * ==========================================
     * PATTERN 4: MINIMUM WINDOW (Hard)
     * ==========================================
     * Problem: Minimum Window Substring
     * s = "ADOBECODEBANC", t = "ABC" -> "BANC"
     * Logic: Expand Right until valid. Shrink Left to minimize.
     */
    @Test
    fun minWindowSubstring() {
        println("\n=== PATTERN: MIN WINDOW (Hard) ===")
        val s = "ADOBECODEBANC"
        val t = "ABC"
        
        if (t.length > s.length) { println(""); return }
        
        val targetFreq = HashMap<Char, Int>()
        for (c in t) targetFreq[c] = targetFreq.getOrDefault(c, 0) + 1
        
        var left = 0
        var matchedCount = 0
        var minLen = Int.MAX_VALUE
        var startIdx = 0
        val windowFreq = HashMap<Char, Int>()
        
        for (right in s.indices) {
            // 1. Expand
            val charR = s[right]
            windowFreq[charR] = windowFreq.getOrDefault(charR, 0) + 1
            
            if (targetFreq.containsKey(charR) && windowFreq[charR]!! <= targetFreq[charR]!!) {
                matchedCount++
            }
            
            // 2. Shrink when valid
            while (matchedCount == t.length) {
                if (right - left + 1 < minLen) {
                    minLen = right - left + 1
                    startIdx = left
                }
                
                // Remove from left
                val charL = s[left]
                windowFreq[charL] = windowFreq[charL]!! - 1
                if (targetFreq.containsKey(charL) && windowFreq[charL]!! < targetFreq[charL]!!) {
                    matchedCount--
                }
                left++
            }
        }
        
        val result = if (minLen == Int.MAX_VALUE) "" else s.substring(startIdx, startIdx + minLen)
        println("Min Window: $result")
    }
}
