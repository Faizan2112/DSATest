package com.example.dsatest

import org.junit.Test
import java.util.Stack
import java.util.HashMap
import java.util.LinkedList

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
     * Logic: Sliding Window with HashMap to store last index.
     * Time: O(n), Space: O(min(n, m))
     */
    @Test
    fun q1_longestSubstring() {
        println("=== Q1: Longest Substring Without Repeating ===")
        val s = "abcabcbb"
        var l = 0; var maxLen = 0
        val map = HashMap<Char, Int>() // Char -> Index
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
     * Logic: HashMap<String, List<String>>. Key is sorted word.
     * Time: O(NKlogK), Space: O(NK)
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
     * Logic: Expand Around Center.
     * Time: O(n^2), Space: O(1)
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
     * Logic: Handle whitespace, sign, overflow.
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
     * Logic: Simulate row traversal (Down -> Up -> Down).
     */
    @Test
    fun q5_zigzagConversion() {
        println("=== Q5: Zigzag Conversion ===")
        val s = "PAYPALISHIRING"; val numRows = 3
        if (numRows == 1) return
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
     * Logic: Backtracking. Open < n, Close < Open.
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
     * Logic: Manual multiplication logic.
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
     * Logic: Split, filter empty, reverse, join.
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
     * Logic: Stack for directories. Push keys, Pop on "..".
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
     * Logic: Stack for counts and strings. "3[a]2[bc]" -> "aaabcbc"
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
     * 11. Find All Anagrams
     * Logic: Sliding Window with Frequency Arrays.
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
     * Logic: Sliding Window (Exact same logic as Q11).
     */
    @Test
    fun q12_checkInclusion() {
        println("=== Q12: Permutation in String ===")
        println("Result: Same logic as Q11. Returns Boolean instead of Indices.")
    }

    /**
     * 13. Longest Repeating Character Replacement
     * Logic: Sliding Window. WindowLen - MaxCount <= k
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
     * Logic: Expand Around Center (Count them).
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
     * Logic: Split by dot, compare integers.
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
     * Logic: Map<Short, Long>. Use hash or counter for Short.
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
     * Logic: Count chars in T. Append as per S order.
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
     * Logic: Two pointers, write count if > 1.
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
     * 19. Remove All Adjacent Duplicates II
     * Logic: Stack store Pair(Char, Count).
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
     * Logic: Recursion or Loop. Generate next sequence from prev.
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
