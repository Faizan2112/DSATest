package com.example.dsatest

import org.junit.Test
import java.util.Stack
import java.util.HashMap

/**
 * ==========================================
 * STRING PROBLEMS: EASY (1-20)
 * ==========================================
 * 
 * Solutions to the top 20 Easy String interview questions.
 */
class StringProblemsEasy {

    /**
     * 1. Valid Anagram
     * Logic: Frequency Array (or Hash Map).
     * Time: O(n), Space: O(1) [26 chars]
     */
    @Test
    fun q1_validAnagram() {
        println("=== Q1: Valid Anagram ===")
        val s = "anagram"; val t = "nagaram"
        if (s.length != t.length) return
        val count = IntArray(26)
        for (c in s) count[c - 'a']++
        for (c in t) count[c - 'a']--
        println("Result: ${count.all { it == 0 }}")
    }

    /**
     * 2. Valid Palindrome
     * Logic: Two Pointers from start and end.
     * Time: O(n), Space: O(1)
     */
    @Test
    fun q2_validPalindrome() {
        println("=== Q2: Valid Palindrome ===")
        val s = "A man, a plan, a canal: Panama"
        var l = 0; var r = s.length - 1
        var res = true
        while (l < r) {
            if (!s[l].isLetterOrDigit()) { l++; continue }
            if (!s[r].isLetterOrDigit()) { r--; continue }
            if (s[l].lowercaseChar() != s[r].lowercaseChar()) { res = false; break }
            l++; r--
        }
        println("Result: $res")
    }

    /**
     * 3. Longest Common Prefix
     * Logic: Take first string as prefix. Shorten it until it matches others.
     * Time: O(S) where S is sum of all chars.
     */
    @Test
    fun q3_longestCommonPrefix() {
        println("=== Q3: Longest Common Prefix ===")
        val strs = arrayOf("flower", "flow", "flight")
        if (strs.isEmpty()) return
        var prefix = strs[0]
        for (i in 1 until strs.size) {
            while (!strs[i].startsWith(prefix)) {
                prefix = prefix.substring(0, prefix.length - 1)
                if (prefix.isEmpty()) break
            }
        }
        println("Result: $prefix")
    }

    /**
     * 4. Reverse String
     * Logic: Two pointers swap in place.
     * Time: O(n), Space: O(1)
     */
    @Test
    fun q4_reverseString() {
        println("=== Q4: Reverse String ===")
        val s = charArrayOf('h','e','l','l','o')
        var l = 0; var r = s.size - 1
        while (l < r) {
            val temp = s[l]
            s[l++] = s[r]
            s[r--] = temp
        }
        println("Result: ${s.joinToString("")}")
    }

    /**
     * 5. Reverse Vowels
     * Logic: Two pointers. Only swap if both are vowels.
     */
    @Test
    fun q5_reverseVowels() {
        println("=== Q5: Reverse Vowels ===")
        val s = "hello" // -> "holle"
        val vowels = setOf('a','e','i','o','u','A','E','I','O','U')
        val charArr = s.toCharArray()
        var l = 0; var r = s.length - 1
        while(l < r) {
            if (charArr[l] !in vowels) { l++; continue }
            if (charArr[r] !in vowels) { r--; continue }
            val temp = charArr[l]; charArr[l++] = charArr[r]; charArr[r--] = temp
        }
        println("Result: ${String(charArr)}")
    }

    /**
     * 6. Isomorphic Strings
     * Logic: Two Maps (s->t and t->s).
     * "egg", "add" -> true | "foo", "bar" -> false
     */
    @Test
    fun q6_isomorphicStrings() {
        println("=== Q6: Isomorphic Strings ===")
        val s = "egg"; val t = "add"
        val mapS = HashMap<Char, Char>()
        val mapT = HashMap<Char, Char>()
        var res = true
        for (i in s.indices) {
            val c1 = s[i]; val c2 = t[i]
            if ((mapS.containsKey(c1) && mapS[c1] != c2) || 
                (mapT.containsKey(c2) && mapT[c2] != c1)) {
                res = false; break
            }
            mapS[c1] = c2; mapT[c2] = c1
        }
        println("Result: $res")
    }

    /**
     * 7. Word Pattern
     * Logic: Same as Isomorphic, but mapping Char to String (Word).
     */
    @Test
    fun q7_wordPattern() {
        println("=== Q7: Word Pattern ===")
        val pattern = "abba"
        val s = "dog cat cat dog"
        val words = s.split(" ")
        if (pattern.length != words.size) return
        
        val pToW = HashMap<Char, String>()
        val wToP = HashMap<String, Char>()
        var res = true
        for (i in pattern.indices) {
            val c = pattern[i]; val w = words[i]
            if ((pToW.containsKey(c) && pToW[c] != w) ||
                (wToP.containsKey(w) && wToP[w] != c)) {
                res = false; break
            }
            pToW[c] = w; wToP[w] = c
        }
        println("Result: $res")
    }

    /**
     * 8. First Unique Character
     * Logic: Frequency Map.
     */
    @Test
    fun q8_firstUniqueChar() {
        println("=== Q8: First Unique Char ===")
        val s = "leetcode"
        val count = IntArray(26)
        for (c in s) count[c - 'a']++
        var idx = -1
        for (i in s.indices) {
            if (count[s[i] - 'a'] == 1) { idx = i; break }
        }
        println("Result Index: $idx")
    }

    /**
     * 9. Length of Last Word
     * Logic: Trim and find last space.
     */
    @Test
    fun q9_lengthOfLastWord() {
        println("=== Q9: Length of Last Word ===")
        val s = "Hello World  "
        val trimmed = s.trim()
        val len = trimmed.length - trimmed.lastIndexOf(' ') - 1
        println("Result: $len")
    }

    /**
     * 10. Add Strings
     * Logic: Schoolbook addition from end.
     */
    @Test
    fun q10_addStrings() {
        println("=== Q10: Add Strings ===")
        val num1 = "11"; val num2 = "123"
        val sb = StringBuilder()
        var i = num1.length - 1; var j = num2.length - 1; var carry = 0
        while (i >= 0 || j >= 0 || carry != 0) {
            val d1 = if (i >= 0) num1[i--] - '0' else 0
            val d2 = if (j >= 0) num2[j--] - '0' else 0
            val sum = d1 + d2 + carry
            sb.append(sum % 10)
            carry = sum / 10
        }
        println("Result: ${sb.reverse()}")
    }

    /**
     * 11. Ransom Note
     * Logic: Frequency Map count check.
     */
    @Test
    fun q11_ransomNote() {
        println("=== Q11: Ransom Note ===")
        val note = "aa"; val mag = "aab"
        val arr = IntArray(26)
        for (c in mag) arr[c - 'a']++
        var res = true
        for (c in note) {
            if (--arr[c - 'a'] < 0) { res = false; break }
        }
        println("Result: $res")
    }

    /**
     * 12. Valid Parentheses
     * Logic: Stack.
     */
    @Test
    fun q12_validParentheses() {
        println("=== Q12: Valid Parentheses ===")
        val s = "()[]{}"
        val stack = Stack<Char>()
        var res = true
        for (c in s) {
            if (c in listOf('(','{','[')) stack.push(c)
            else {
                if (stack.isEmpty()) { res = false; break }
                val top = stack.pop()
                if ((c == ')' && top != '(') || 
                    (c == '}' && top != '{') || 
                    (c == ']' && top != '[')) { res = false; break }
            }
        }
        if (stack.isNotEmpty()) res = false
        println("Result: $res")
    }

    /**
     * 13. Implement strStr()
     * Logic: indexOf.
     */
    @Test
    fun q13_strStr() {
        println("=== Q13: strStr ===")
        val haystack = "hello"; val needle = "ll"
        println("Result: ${haystack.indexOf(needle)}")
    }

    /**
     * 14. Rotate String
     * Logic: A + A contains B if B is a rotation of A.
     */
    @Test
    fun q14_rotateString() {
        println("=== Q14: Rotate String ===")
        val s = "abcde"; val goal = "cdeab"
        val res = (s.length == goal.length) && (s + s).contains(goal)
        println("Result: $res")
    }

    /**
     * 15. To Lower Case
     * Logic: ASCII check.
     */
    @Test
    fun q15_toLowerCase() {
        println("=== Q15: To Lower Case ===")
        val s = "Hello"
        println("Result: ${s.lowercase()}")
    }

    /**
     * 16. Check if Two String Arrays are Equivalent
     * Logic: Join and check.
     */
    @Test
    fun q16_arrayStringsAreEqual() {
        println("=== Q16: Array Strings Equivalent ===")
        val word1 = arrayOf("ab", "c"); val word2 = arrayOf("a", "bc")
        println("Result: ${word1.joinToString("") == word2.joinToString("")}")
    }

    /**
     * 17. Merge Strings Alternately
     * Logic: One pointer per string.
     */
    @Test
    fun q17_mergeAlternately() {
        println("=== Q17: Merge Alternately ===")
        val w1 = "abc"; val w2 = "pqr"
        val sb = StringBuilder()
        val n = Math.max(w1.length, w2.length)
        for(i in 0 until n) {
            if(i < w1.length) sb.append(w1[i])
            if(i < w2.length) sb.append(w2[i])
        }
        println("Result: $sb")
    }

    /**
     * 18. Shuffle String
     * Logic: Place char at indices[i].
     */
    @Test
    fun q18_shuffleString() {
        println("=== Q18: Shuffle String ===")
        val s = "codeleet"
        val indices = intArrayOf(4,5,6,7,0,2,1,3)
        val res = CharArray(s.length)
        for(i in s.indices) {
            res[indices[i]] = s[i]
        }
        println("Result: ${String(res)}")
    }

    /**
     * 19. Defanging an IP Address
     * Logic: Replace . with [.].
     */
    @Test
    fun q19_defangIP() {
        println("=== Q19: Defang IP ===")
        val addr = "1.1.1.1"
        println("Result: ${addr.replace(".", "[.]")}")
    }

    /**
     * 20. Goal Parser
     * Logic: Replace "()" with "o", "(al)" with "al".
     */
    @Test
    fun q20_goalParser() {
        println("=== Q20: Goal Parser ===")
        val cmd = "G()(al)"
        val res = cmd.replace("()", "o").replace("(al)", "al")
        println("Result: $res")
    }
}
