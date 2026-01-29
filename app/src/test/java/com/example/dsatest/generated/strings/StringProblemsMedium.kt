package com.example.dsatest.generated.strings

import org.junit.Test
import java.util.Stack
import java.util.HashMap
import kotlin.text.iterator

/**
 * ==========================================
 * STRING PROBLEMS: MEDIUM (1-20)
 * ==========================================
 * 
 * Solutions to the top 20 Medium String interview questions.
 * Key Patterns: Sliding Window, Hashing, Stack, Two Pointers.
 */
class StringProblemsMedium {

    /**
     * 1. Longest Substring Without Repeating Characters
     *
     * PROBLEM:
     * Given a string `s`, find the length of the longest substring without repeating characters.
     *
     * INPUT/OUTPUT:
     * Input: s = "abcabcbb" -> Output: 3 ("abc")
     * Input: s = "bbbbb" -> Output: 1 ("b")
     *
     * DESIGN:
     * Why Sliding Window?
     * - We need to find the longest valid window `[l, r]`.
     * - As `r` moves forward, if we find a duplicate, we shrink `l` past the previous occurrence.
     * - A HashMap stores the last seen index of each character to allow jumping `l` directly.
     *
     * DETAIL:
     * 1. Initialize `l = 0`, `maxLen = 0`.
     * 2. Initialize `map` (Char -> Index).
     * 3. Loop `r` from 0 to `s.length - 1`:
     *    - If `s[r]` is in `map`, update `l = max(l, map[s[r]] + 1)`.
     *    - Update `map[s[r]] = r`.
     *    - `maxLen = max(maxLen, r - l + 1)`.
     * 4. Return `maxLen`.
     *
     * COMPLEXITY:
     * Time: O(n) - One pass.
     * Space: O(min(m, n)) - Map size bounded by alphabet.
     */
    @Test
    fun q1_longestSubstring() {
        println("=== Q1: Longest Substring Without Repeating ===")
        val s = "abcabcbb"
        var l = 0; var maxLen = 0
        val map = HashMap<Char, Int>()
        for (r in s.indices) {
            val c = s[r]
            if (map.containsKey(c)) {
                l = Math.max(l, map[c]!! + 1)
            }
            map[c] = r
            maxLen = Math.max(maxLen, r - l + 1)
        }
        println("Result: $maxLen")
    }

    /**
     * 2. Group Anagrams
     *
     * PROBLEM:
     * Given an array of strings `strs`, group the anagrams together. You can return the answer in any order.
     *
     * INPUT/OUTPUT:
     * Input: strs = ["eat","tea","tan","ate","nat","bat"]
     * Output: [["bat"],["nat","tan"],["ate","eat","tea"]]
     *
     * DESIGN:
     * Why Sort as Key?
     * - Anagrams have the same characters. Sorting the string yields the same "signature".
     * - Map Key: Sorted String. Value: List of original strings.
     * - Alternative: Use a frequency count string "1a2b..." as key (O(NK)).
     *
     * DETAIL:
     * 1. Initialize `map` (String -> List<String>).
     * 2. Loop through each string `s`:
     *    - Convert `s` to char array, sort it, convert back to string `key`.
     *    - If key not in map, add new list.
     *    - Add `s` to the list for `key`.
     * 3. Return `map.values`.
     *
     * COMPLEXITY:
     * Time: O(N * K log K) where N is number of strings, K is max length of a string.
     * Space: O(N * K)
     */
    @Test
    fun q2_groupAnagrams() {
        println("=== Q2: Group Anagrams ===")
        val strs = arrayOf("eat", "tea", "tan", "ate", "nat", "bat")
        val map = HashMap<String, MutableList<String>>()
        for (s in strs) {
            val key = s.toCharArray().sorted().joinToString("")
            map.computeIfAbsent(key) { mutableListOf() }.add(s)
        }
        println("Result: ${map.values}")
    }

    /**
     * 3. Longest Palindromic Substring
     *
     * PROBLEM:
     * Given a string `s`, return the longest palindromic substring in `s`.
     *
     * INPUT/OUTPUT:
     * Input: s = "babad" -> Output: "bab" (or "aba")
     * Input: s = "cbbd" -> Output: "bb"
     *
     * DESIGN:
     * Why Expand Around Center?
     * - A palindrome mirrors around its center.
     * - There are 2N - 1 centers (N single characters, N-1 between characters).
     * - For each center, expand left and right as long as characters match.
     *
     * DETAIL:
     * 1. Iterate `i` from 0 to `s.length`.
     * 2. Call `expand(i, i)` for odd length palindromes.
     * 3. Call `expand(i, i + 1)` for even length palindromes.
     * 4. In `expand(l, r)`:
     *    - While `l, r` in bounds and `s[l] == s[r]`:
     *      - Check if current len > `maxLen`. If so update start/end.
     *      - `l--`, `r++`.
     *
     * COMPLEXITY:
     * Time: O(N^2)
     * Space: O(1)
     */
    @Test
    fun q3_longestPalindrome() {
        println("=== Q3: Longest Palindrome ===")
        val s = "babad"
        var maxStr = ""
        fun expand(l: Int, r: Int) {
            var left = l; var right = r
            while (left >= 0 && right < s.length && s[left] == s[right]) {
                if (right - left + 1 > maxStr.length) {
                    maxStr = s.substring(left, right + 1)
                }
                left--; right++
            }
        }
        for (i in s.indices) {
            expand(i, i)     // Odd length
            expand(i, i + 1) // Even length
        }
        println("Result: $maxStr")
    }

    /**
     * 4. String to Integer (atoi)
     *
     * PROBLEM:
     * Implement the `myAtoi(string s)` function, similar to C++'s `atoi`.
     * Step 1: Ignore whitespace.
     * Step 2: Check sign (+/-).
     * Step 3: Read digits.
     * Step 4: Clamp to Int.MIN_VALUE / Int.MAX_VALUE on overflow.
     *
     * INPUT/OUTPUT:
     * Input: s = "   -42" -> Output: -42
     * Input: s = "4193 with words" -> Output: 4193
     *
     * DESIGN:
     * Why Careful State Machine?
     * - Edge cases are the main difficulty (overflow, invalid chars, signs).
     * - We handle each step sequentially.
     *
     * DETAIL:
     * 1. Skip leading spaces.
     * 2. Check for '+' or '-'. Set `sign`.
     * 3. Loop while char is digit:
     *    - Check overflow BEFORE updating `res`.
     *    - If `res > MAX/10` or (`res == MAX/10` and `digit > 7`), return MAX/MIN.
     *    - `res = res * 10 + digit`.
     * 4. Return `res * sign`.
     *
     * COMPLEXITY:
     * Time: O(N)
     * Space: O(1)
     */
    @Test
    fun q4_myAtoi() {
        println("=== Q4: String to Integer (atoi) ===")
        val s = "   -42"
        var i = 0; var sign = 1; var res = 0
        while (i < s.length && s[i] == ' ') i++
        if (i < s.length && (s[i] == '-' || s[i] == '+')) {
            if (s[i] == '-') sign = -1
            i++
        }
        while (i < s.length && s[i].isDigit()) {
            val digit = s[i] - '0'
            // Check Overflow
            if (res > Int.MAX_VALUE / 10 || (res == Int.MAX_VALUE / 10 && digit > 7)) {
                println("Result: ${if (sign == 1) Int.MAX_VALUE else Int.MIN_VALUE}")
                return
            }
            res = res * 10 + digit
            i++
        }
        println("Result: ${res * sign}")
    }

    /**
     * 5. Zigzag Conversion
     *
     * PROBLEM:
     * The string "PAYPALISHIRING" is written in a zigzag pattern on a given number of rows like this:
     * P   A   H   N
     * A P L s I I G
     * Y   I   R
     * And then read line by line: "PAHNAPLSIIGYIR"
     *
     * INPUT/OUTPUT:
     * Input: s = "PAYPALISHIRING", numRows = 3 -> Output: "PAHNAPLSIIGYIR"
     *
     * DESIGN:
     * Why Row Simulation?
     * - We can simulate putting characters into `numRows` lists.
     * - We oscillate the row index: 0->1->2->1->0...
     *
     * DETAIL:
     * 1. If `numRows == 1` return s.
     * 2. Create an array of StringBuilders, size `min(numRows, s.length)`.
     * 3. Track `curRow` (0) and `goingDown` (false).
     * 4. Iterate chars in s:
     *    - Append char to `rows[curRow]`.
     *    - If `curRow == 0` or `curRow == last`, toggle `goingDown`.
     *    - Update `curRow` (+1 or -1).
     * 5. Join all rows.
     *
     * COMPLEXITY:
     * Time: O(N)
     * Space: O(N)
     */
    @Test
    fun q5_zigzagConversion() {
        println("=== Q5: Zigzag Conversion ===")
        val s = "PAYPALISHIRING"; val numRows = 3
        if (numRows == 1) { println("Result: $s"); return }
        
        val rows = Array(Math.min(numRows, s.length)) { StringBuilder() }
        var curRow = 0; var goingDown = false
        for (c in s) {
            rows[curRow].append(c)
            if (curRow == 0 || curRow == numRows - 1) goingDown = !goingDown
            curRow += if (goingDown) 1 else -1
        }
        val ret = StringBuilder()
        for (row in rows) ret.append(row)
        println("Result: $ret")
    }

    /**
     * 6. Generate Parentheses
     *
     * PROBLEM:
     * Given `n` pairs of parentheses, write a function to generate all combinations of well-formed parentheses.
     *
     * INPUT/OUTPUT:
     * Input: n = 3
     * Output: ["((()))","(()())","(())()","()(())","()()()"]
     *
     * DESIGN:
     * Why Backtracking?
     * - We construct strings character by character.
     * - Constraint 1: Can add "(" if `open < n`.
     * - Constraint 2: Can add ")" if `close < open`.
     *
     * DETAIL:
     * 1. Define recursive `backtrack(currentString, openCount, closeCount)`.
     * 2. Base case: if `currentString.length == 2*n`, add to results.
     * 3. Recursive steps:
     *    - If `open < n`, append '(' and recurse.
     *    - If `close < open`, append ')' and recurse.
     *
     * COMPLEXITY:
     * Time: O(4^n / sqrt(n)) - Catalan number.
     * Space: O(n) recursion depth.
     */
    @Test
    fun q6_generateParentheses() {
        println("=== Q6: Generate Parentheses ===")
        val n = 3
        val res =  mutableListOf<String>()
        fun backtrack(s: String, open: Int, close: Int) {
            if (s.length == n * 2) { res.add(s); return }
            if (open < n) backtrack("$s(", open + 1, close)
            if (close < open) backtrack("$s)", open, close + 1)
        }
        backtrack("", 0, 0)
        println("Result: $res")
    }

    /**
     * 7. Multiply Strings
     *
     * PROBLEM:
     * Given two non-negative integers `num1` and `num2` represented as strings, return the product of `num1` and `num2`, also represented as a string.
     *
     * INPUT/OUTPUT:
     * Input: num1 = "123", num2 = "456" -> Output: "56088"
     *
     * DESIGN:
     * Why Array of Digits?
     * - The product of len(M) and len(N) numbers is at most len(M+N).
     * - `num1[i] * num2[j]` contributes to indices `i+j` and `i+j+1` in the result array.
     *
     * DETAIL:
     * 1. Create `pos` array of size `m + n`.
     * 2. Iterate `i` from `m-1` down to 0, `j` from `n-1` down to 0.
     * 3. `mul = num1[i] * num2[j]`.
     * 4. Add `mul` to `pos[i+j+1]`.
     * 5. Handle carry: `pos[i+j] += pos[i+j+1] / 10`. `pos[i+j+1] %= 10`.
     * 6. Convert array to string, skipping leading zeros.
     *
     * COMPLEXITY:
     * Time: O(M*N)
     * Space: O(M+N)
     */
    @Test
    fun q7_multiplyStrings() {
        println("=== Q7: Multiply Strings ===")
        val num1 = "2"; val num2 = "3"
        val n1 = num1.length; val n2 = num2.length
        val pos = IntArray(n1 + n2)
        
        for (i in n1 - 1 downTo 0) {
            for (j in n2 - 1 downTo 0) {
                val mul = (num1[i] - '0') * (num2[j] - '0')
                val p1 = i + j; val p2 = i + j + 1
                val sum = mul + pos[p2]
                
                pos[p1] += sum / 10
                pos[p2] = sum % 10
            }
        }
        val sb = StringBuilder()
        for (p in pos) if (!(sb.isEmpty() && p == 0)) sb.append(p)
        println("Result: ${if (sb.isEmpty()) "0" else sb.toString()}")
    }

    /**
     * 8. Reverse Words in a String
     *
     * PROBLEM:
     * Given an input string `s`, reverse the order of the words.
     * A word is defined as a sequence of non-space characters. The words in `s` will be separated by at least one space.
     * Return a string of the words in reverse order concatenated by a single space.
     *
     * INPUT/OUTPUT:
     * Input: s = "the sky is blue" -> Output: "blue is sky the"
     * Input: s = "  hello world  " -> Output: "world hello"
     *
     * DESIGN:
     * Why Split/Reverse/Join?
     * - The high level operations are straightforward.
     * - Kotlin/Java regex `\\s+` handles multiple spaces efficiently.
     * - Then reverse the list and join with " ".
     *
     * DETAIL:
     * 1. `s.trim()` to remove leading/trailing.
     * 2. `s.split("\\s+".toRegex())` to get words.
     * 3. `words.reversed()`.
     * 4. `joinToString(" ")`.
     *
     * COMPLEXITY:
     * Time: O(N)
     * Space: O(N)
     */
    @Test
    fun q8_reverseWords() {
        println("=== Q8: Reverse Words ===")
        val s = "the sky is blue"
        val words = s.trim().split("\\s+".toRegex()) // Split by multiple spaces
        println("Result: ${words.reversed().joinToString(" ")}")
    }

    /**
     * 9. Simplify Path
     *
     * PROBLEM:
     * Given a string `path`, which is an absolute path (starting with a slash '/') to a file or directory in a Unix-style file system, convert it to the simplified canonical path.
     * Rules:
     * - ".." means move up a level.
     * - "." means current directory.
     * - "//" treated as single slash.
     *
     * INPUT/OUTPUT:
     * Input: path = "/a/./b/../../c/" -> Output: "/c"
     *
     * DESIGN:
     * Why Stack?
     * - ".." cancels the previous directory (pop).
     * - Normal names append (push).
     * - "." does nothing.
     *
     * DETAIL:
     * 1. Split path by "/".
     * 2. Iterate components:
     *    - If "..": pop stack if not empty.
     *    - If "." or "": continue.
     *    - Else: push to stack.
     * 3. Join stack with "/" and prepend "/".
     *
     * COMPLEXITY:
     * Time: O(N)
     * Space: O(N)
     */
    @Test
    fun q9_simplifyPath() {
        println("=== Q9: Simplify Path ===")
        val path = "/a/./b/../../c/"
        val stack = Stack<String>()
        val components = path.split("/")
        for (dir in components) {
            if (dir == "." || dir.isEmpty()) continue
            if (dir == "..") {
                if (stack.isNotEmpty()) stack.pop()
            } else {
                stack.push(dir)
            }
        }
        println("Result: /${stack.joinToString("/")}")
    }

    /**
     * 10. Decode String
     *
     * PROBLEM:
     * Given an encoded string, return its decoded string.
     * The encoding rule is: `k[encoded_string]`, where the `encoded_string` inside the square brackets is being repeated exactly `k` times.
     *
     * INPUT/OUTPUT:
     * Input: s = "3[a]2[bc]" -> Output: "aaabcbc"
     * Input: s = "3[a2[c]]" -> Output: "accaccacc"
     *
     * DESIGN:
     * Why Two Stacks?
     * - We need to remember "state" when we enter a bracket: the count `k` and the string built so far `currStr`.
     * - `countStack` stores multiplier. `strStack` stores the prefix before the bracket.
     *
     * DETAIL:
     * 1. Iterate chars.
     * 2. If digit: update `k`.
     * 3. If '[': push `k`, push `currStr`, reset `k=0, currStr=""`.
     * 4. If ']':
     *    - `decoded = currStr`.
     *    - `prevStr = strStack.pop()`.
     *    - `count = countStack.pop()`.
     *    - `currStr = prevStr + decoded * count`.
     * 5. If char: append to `currStr`.
     *
     * COMPLEXITY:
     * Time: O(N)
     * Space: O(N)
     */
    @Test
    fun q10_decodeString() {
        println("=== Q10: Decode String ===")
        val s = "3[a]2[bc]"
        val countStack = Stack<Int>()
        val strStack = Stack<StringBuilder>()
        var currStr = StringBuilder()
        var k = 0
        for (c in s) {
            if (c.isDigit()) {
                k = k * 10 + (c - '0')
            } else if (c == '[') {
                countStack.push(k)
                strStack.push(currStr)
                currStr = StringBuilder() // Reset
                k = 0
            } else if (c == ']') {
                val decoded = currStr
                currStr = strStack.pop()
                val count = countStack.pop()
                repeat(count) { currStr.append(decoded) }
            } else {
                currStr.append(c)
            }
        }
        println("Result: $currStr")
    }

    /**
     * 11. Find All Anagrams in a String
     *
     * PROBLEM:
     * Given two strings `s` and `p`, return an array of all the start indices of `p`'s anagrams in `s`.
     * You may return the answer in any order.
     *
     * INPUT/OUTPUT:
     * Input: s = "cbaebabacd", p = "abc" -> Output: [0,6]
     * Explanation:
     * Substring with start index = 0 is "cba", which is an anagram of "abc".
     * Substring with start index = 6 is "bac", which is an anagram of "abc".
     *
     * DESIGN:
     * Why Sliding Window + Frequency Array?
     * - `p` has fixed length. We need to check all windows of that length in `s`.
     * - We maintain a running frequency count of the current window in `s`.
     * - Compare it with `p`'s frequency count (O(1) comparison since size is 26).
     *
     * DETAIL:
     * 1. If `s.length < p.length`, return empty.
     * 2. Compute `pCount`.
     * 3. Iterate `i` through `s.indices`:
     *    - Add `s[i]` to `sCount`.
     *    - If `i >= p.length`, remove `s[i - p.length]` from `sCount`.
     *    - If `sCount` equals `pCount`, add `i - p.length + 1` to results.
     *
     * COMPLEXITY:
     * Time: O(N)
     * Space: O(1) (size 26 arrays).
     */
    @Test
    fun q11_findAllAnagrams() {
        println("=== Q11: Find All Anagrams ===")
        val s = "cbaebabacd"; val p = "abc"
        val pCount = IntArray(26); val sCount = IntArray(26)
        val res = mutableListOf<Int>()
        for (c in p) pCount[c - 'a']++
        
        for (i in s.indices) {
            sCount[s[i] - 'a']++
            if (i >= p.length) sCount[s[i - p.length] - 'a']-- // Shrink
            if (sCount.contentEquals(pCount)) res.add(i - p.length + 1)
        }
        println("Result: $res")
    }

    /**
     * 12. Permutation in String
     *
     * PROBLEM:
     * Given two strings `s1` and `s2`, return true if `s2` contains a permutation of `s1`, or false otherwise.
     * In other words, return true if one of `s1`'s permutations is the substring of `s2`.
     *
     * INPUT/OUTPUT:
     * Input: s1 = "ab", s2 = "eidbaooo" -> Output: true ("ba")
     *
     * DESIGN:
     * Why Sliding Window?
     * - Exactly the same problem as "Find All Anagrams", just return boolean on first match.
     *
     * DETAIL:
     * 1. Same logic as Q11.
     * 2. Return `true` immediately if `sCount.contentEquals(pCount)`.
     *
     * COMPLEXITY:
     * Time: O(N)
     * Space: O(1)
     */
    @Test
    fun q12_checkInclusion() {
        println("=== Q12: Permutation in String ===")
        val s1 = "ab"; val s2 = "eidbaooo"
        
        fun solve(s1: String, s2: String): Boolean {
             if (s1.length > s2.length) return false
             val s1Count = IntArray(26); val s2Count = IntArray(26)
             for (c in s1) s1Count[c - 'a']++
             
             for (i in s2.indices) {
                 s2Count[s2[i] - 'a']++
                 if (i >= s1.length) s2Count[s2[i - s1.length] - 'a']--
                 if (s1Count.contentEquals(s2Count)) return true
             }
             return false
        }
        println("Result: ${solve(s1, s2)}")
    }

    /**
     * 13. Longest Repeating Character Replacement
     *
     * PROBLEM:
     * You are given a string `s` and an integer `k`. You can choose any character of the string and change it to any other uppercase English character. You can perform this operation at most `k` times.
     * Return the length of the longest substring containing the same letter you can get after performing the above operations.
     *
     * INPUT/OUTPUT:
     * Input: s = "ABAB", k = 2 -> Output: 4 ("AAAA" or "BBBB")
     * Input: s = "AABABBA", k = 1 -> Output: 4
     *
     * DESIGN:
     * Why Sliding Window?
     * - We want to maximize `(r - l + 1)` such that `(r - l + 1) - maxFreq <= k`.
     * - `maxFreq` is the count of the most frequent character in the current window.
     * - If replacements needed (`len - maxFreq`) > `k`, we shrink the window from left.
     *
     * DETAIL:
     * 1. `l = 0`, `maxCount = 0`, `maxLen = 0`.
     * 2. Loop `r` in indices.
     * 3. Add `s[r]` to count. Update `maxCount`.
     * 4. While `(r - l + 1) - maxCount > k`:
     *    - Decrement count for `s[l]`.
     *    - `l++`.
     * 5. `maxLen = max(maxLen, r - l + 1)`.
     *
     * COMPLEXITY:
     * Time: O(N)
     * Space: O(1)
     */
    @Test
    fun q13_characterReplacement() {
        println("=== Q13: Longest Repeating Char Replacement ===")
        val s = "ABAB"; val k = 2
        val count = IntArray(26)
        var maxCount = 0; var l = 0; var maxLen = 0
        for (r in s.indices) {
            maxCount = Math.max(maxCount, ++count[s[r] - 'A'])
            // If replacements needed > k, shrink window
            while (r - l + 1 - maxCount > k) {
                count[s[l] - 'A']--
                l++
            }
            maxLen = Math.max(maxLen, r - l + 1)
        }
        println("Result: $maxLen")
    }

    /**
     * 14. Palindromic Substrings
     *
     * PROBLEM:
     * Given a string `s`, return the number of palindromic substrings in it.
     * A substring is a contiguous sequence of characters within the string.
     *
     * INPUT/OUTPUT:
     * Input: s = "abc" -> Output: 3 ("a", "b", "c")
     * Input: s = "aaa" -> Output: 6 ("a", "a", "a", "aa", "aa", "aaa")
     *
     * DESIGN:
     * Why Expand Around Center?
     * - Similar to Longest Palindromic Substring.
     * - Just increment a counter every time we extend.
     * - Centers: `i` (odd) and `i, i+1` (even).
     *
     * DETAIL:
     * 1. Initialize `count = 0`.
     * 2. Loop `i` from 0 to `s.length - 1`.
     * 3. `expand(i, i)` and `expand(i, i+1)`.
     * 4. In `expand`: While match, `count++`, expand.
     *
     * COMPLEXITY:
     * Time: O(N^2)
     * Space: O(1)
     */
    @Test
    fun q14_countSubstrings() {
        println("=== Q14: Valindromic Substrings (Count) ===")
        val s = "abc"
        var count = 0
        fun expand(l: Int, r: Int) {
            var left = l; var right = r
            while (left >= 0 && right < s.length && s[left] == s[right]) {
                count++
                left--; right++
            }
        }
        for (i in s.indices) { expand(i, i); expand(i, i+1) }
        println("Result: $count") 
    }

    /**
     * 15. Compare Version Numbers
     *
     * PROBLEM:
     * Given two version numbers, `version1` and `version2`.
     * If `version1 > version2` return 1, if `version1 < version2` return -1, otherwise return 0.
     * Revisions are separated by dots. Ignore leading zeros.
     *
     * INPUT/OUTPUT:
     * Input: v1 = "1.01", v2 = "1.001" -> Output: 0 (1 = 1)
     * Input: v1 = "1.0", v2 = "1.0.0" -> Output: 0
     * Input: v1 = "0.1", v2 = "1.1" -> Output: -1
     *
     * DESIGN:
     * Why Split and Pad?
     * - We split by "." to get chunks.
     * - If one version has fewer chunks, treat missing chunks as 0.
     * - Compare integers of corresponding chunks.
     *
     * DETAIL:
     * 1. Split `v1` and `v2` by `.`.
     * 2. Loop `max(chunks1, chunks2)`.
     * 3. Get chunk value `i1` and `i2` (default 0).
     * 4. Compare `i1` vs `i2`. Return if different.
     * 5. If loop ends, return 0.
     *
     * COMPLEXITY:
     * Time: O(N+M)
     * Space: O(N+M)
     */
    @Test
    fun q15_compareVersion() {
        println("=== Q15: Compare Version ===")
        val v1 = "1.01"; val v2 = "1.001"
        val p1 = v1.split("."); val p2 = v2.split(".")
        val n = Math.max(p1.size, p2.size)
        var res = 0
        for (i in 0 until n) {
            val i1 = if (i < p1.size) p1[i].toInt() else 0
            val i2 = if (i < p2.size) p2[i].toInt() else 0
            if (i1 < i2) { res = -1; break }
            if (i1 > i2) { res = 1; break }
        }
        println("Result: $res")
    }

    /**
     * 16. Encode and Decode TinyURL
     *
     * PROBLEM:
     * Design a class to encode a URL and decode a tiny URL.
     *
     * INPUT/OUTPUT:
     * Input: "https://leetcode.com/problems/design-tinyurl"
     * Output: "http://tinyurl.com/4e9iAk" (example)
     *
     * DESIGN:
     * Why HashMap and Counter?
     * - Simplest approach: Maintain a Map `id -> longUrl`.
     * - Encode: Return `base + id`. Store `id: longUrl`.
     * - Decode: Extract `id`, lookup map.
     * - Using hashCode or Random also works.
     *
     * DETAIL:
     * 1. `encode`: `key = longUrl.hashCode()`. `map[key] = longUrl`. return `tiny/key`.
     * 2. `decode`: extract `key`. return `map[key]`.
     *
     * COMPLEXITY:
     * Time: O(1)
     * Space: O(N) URLs
     */
    @Test
    fun q16_tinyURL() {
        println("=== Q16: TinyURL ===")
        val map = HashMap<String, String>()
        val longUrl = "https://google.com"
        val key = longUrl.hashCode().toString()
        map[key] = longUrl
        println("Encoded: http://tiny/$key")
        println("Decoded: ${map[key]}")
    }

    /**
     * 17. Custom Sort String
     *
     * PROBLEM:
     * You are given two strings `order` and `s`. All the characters of `order` are unique and were sorted in some custom order previously.
     * Permute the characters of `s` so that they match the order that `order` was sorted.
     *
     * INPUT/OUTPUT:
     * Input: order = "cba", s = "abcd" -> Output: "cbad"
     *
     * DESIGN:
     * Why Frequency Counting?
     * - We need to output chars of `s` in `order`.
     * - Count frequency of all chars in `s`.
     * - Iterate `order`: Append char `count` times.
     * - Append remaining chars (not in `order`) at the end.
     *
     * DETAIL:
     * 1. Count `s` into `IntArray(26)`.
     * 2. For each `c` in `order`:
     *    - While `count[c] > 0`: append `c`, decrement.
     * 3. For `c` in `a..z`:
     *    - While `count[c] > 0`: append `c`, decrement.
     *
     * COMPLEXITY:
     * Time: O(N + M)
     * Space: O(1)
     */
    @Test
    fun q17_customSortString() {
        println("=== Q17: Custom Sort String ===")
        val order = "cba"; val s = "abcd"
        val count = IntArray(26)
        for (c in s) count[c - 'a']++
        val sb = StringBuilder()
        
        // 1. Add chars in 'order'
        for (c in order) {
            while (count[c - 'a']-- > 0) sb.append(c)
        }
        // 2. Add remaining chars
        for (c in 'a'..'z') {
            while (count[c - 'a']-- > 0) sb.append(c)
        }
        println("Result: $sb")
    }

    /**
     * 18. String Compression
     *
     * PROBLEM:
     * Compress an array of characters in-place.
     * Begin with an empty string `s`. For each group of consecutive repeating characters:
     * - If the group's length is 1, append the character to `s`.
     * - Otherwise, append the character followed by the group's length.
     * Write result to the character array and return new length.
     *
     * INPUT/OUTPUT:
     * Input: chars = ["a","a","b","b","c","c","c"] -> Output: 6, chars = ["a","2","b","2","c","3"]
     *
     * DESIGN:
     * Why Two Pointers (Read/Write)?
     * - We read with `i`, identify group.
     * - We write to `res`.
     * - `res` is always <= `i`, so we can overwrite in-place safely.
     *
     * DETAIL:
     * 1. Loop `i < size`.
     * 2. Identify `groupLen` by checking `chars[i] == chars[i+len]`.
     * 3. Write `chars[i]` to `chars[res++]`.
     * 4. If `groupLen > 1`, convert to string and write digits to `chars[res++]`.
     * 5. `i += groupLen`.
     *
     * COMPLEXITY:
     * Time: O(N)
     * Space: O(1)
     */
    @Test
    fun q18_stringCompression() {
        println("=== Q18: String Compression ===")
        val chars = charArrayOf('a','a','b','b','c','c','c')
        var i = 0; var res = 0
        while (i < chars.size) {
            var groupLen = 1
            while (i + groupLen < chars.size && chars[i + groupLen] == chars[i]) groupLen++
            chars[res++] = chars[i]
            if (groupLen > 1) {
                for (c in groupLen.toString()) chars[res++] = c
            }
            i += groupLen
        }
        println("Result: ${String(chars).substring(0, res)}")
    }

    /**
     * 19. Remove All Adjacent Duplicates in String II
     *
     * PROBLEM:
     * You are given a string `s` and an integer `k`, a k-duplicate removal consists of choosing `k` adjacent and equal letters from `s` and removing them.
     * We repeatedly make `k`-duplicate removals on `s` until we no longer can.
     *
     * INPUT/OUTPUT:
     * Input: s = "deeedbbcccbdaa", k = 3 -> Output: "aa"
     *
     * DESIGN:
     * Why Stack of Pairs?
     * - We need to track the count of consecutive characters.
     * - `Pair(Char, Count)`.
     * - If `stack.peek().char == c`, increment count.
     * - If count reaches `k`, pop.
     *
     * DETAIL:
     * 1. Stack of `Pair`.
     * 2. Iterate `c` in `s`:
     *    - If matches top, `newCount = top.count + 1`.
     *      - If `newCount == k`, pop.
     *      - Else replace top (or pop/push) with new count. (Stack in Kotlin/Java usually requires pop then push new).
     *    - Else push `(c, 1)`.
     * 3. Reconstruct string.
     *
     * COMPLEXITY:
     * Time: O(N)
     * Space: O(N)
     */
    @Test
    fun q19_removeDuplicatesII() {
        println("=== Q19: Remove Adj Duplicates II ===")
        val s = "deeedbbcccbdaa"; val k = 3
        val stack = Stack<Pair<Char, Int>>()
        for (c in s) {
            if (stack.isNotEmpty() && stack.peek().first == c) {
                val newCount = stack.pop().second + 1
                if (newCount < k) stack.push(c to newCount)
            } else {
                stack.push(c to 1)
            }
        }
        val sb = StringBuilder()
        for ((c, count) in stack) repeat(count) { sb.append(c) }
        println("Result: $sb")
    }

    /**
     * 20. Count and Say
     *
     * PROBLEM:
     * The count-and-say sequence is a sequence of digit strings defined by the recursive formula:
     * countAndSay(1) = "1"
     * countAndSay(n) is the way you would "say" the digit string from countAndSay(n-1).
     * Example: "3322251" -> "23 32 15 11" -> "23321511".
     *
     * INPUT/OUTPUT:
     * Input: n = 4 -> Output: "1211"
     * ("1" -> "11" -> "21" -> "1211")
     *
     * DESIGN:
     * Why Simulation?
     * - We just need to implement the run-length encoding logic iteratively `n` times.
     *
     * DETAIL:
     * 1. Start `s = "1"`.
     * 2. Loop `i` from 1 to `n-1`.
     * 3. Process `s` to build `next`:
     *    - Iterate `j` to len-1.
     *    - If `s[j] == s[j+1]`, count++.
     *    - Else append `count` + `s[j]`, reset count.
     *    - Append last group after loop.
     * 4. Return `s`.
     *
     * COMPLEXITY:
     * Time: O(N * L) L is length of string.
     * Space: O(L)
     */
    @Test
    fun q20_countAndSay() {
        println("=== Q20: Count and Say ===")
        var s = "1"
        val n = 4
        for (i in 1 until n) {
            val sb = StringBuilder()
            var count = 1
            for (j in 0 until s.length - 1) {
                if (s[j] == s[j+1]) count++
                else {
                    sb.append(count).append(s[j])
                    count = 1
                }
            }
            sb.append(count).append(s[s.length - 1])
            s = sb.toString()
        }
        println("Result: $s")
    }
}
