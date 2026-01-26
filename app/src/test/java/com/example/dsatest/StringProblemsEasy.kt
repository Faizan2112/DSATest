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
     *
     * PROBLEM:
     * Given two strings `s` and `t`, return `true` if `t` is an anagram of `s`, and `false` otherwise.
     * An Anagram is a word or phrase formed by rearranging the letters of a different word or phrase, typically using all the original letters exactly once.
     *
     * INPUT/OUTPUT:
     * Input: s = "anagram", t = "nagaram"
     * Output: true
     *
     * DESIGN:
     * Why Frequency Array?
     * - We need to check if both strings have the exact same counts of every character.
     * - Sorting both strings and comparing would be O(N log N).
     * - A Frequency Array (or HashMap) allows us to count occurrences in O(N) time.
     * - Since the input contains only lowercase English letters, a fixed-size integer array of size 26 is optimal (O(1) space).
     *
     * DETAIL:
     * 1. Check if `s.length != t.length`. If so, they cannot be anagrams; return false.
     * 2. Initialize an integer array `count` of size 26.
     * 3. Iterate through string `s` and increment the count for each character.
     * 4. Iterate through string `t` and decrement the count for each character.
     * 5. Finally, check if all elements in `count` are 0. If yes, it's an anagram.
     *
     * COMPLEXITY:
     * Time: O(n) - Single pass through both strings.
     * Space: O(1) - Fixed size array of 26 elements.
     */
    @Test
    fun q1_validAnagram() {
        println("=== Q1: Valid Anagram ===")
        val s = "anagram"; val t = "nagaram"
        
        fun solve(s: String, t: String): Boolean {
            if (s.length != t.length) return false
            val count = IntArray(26)
            for (c in s) count[c - 'a']++
            for (c in t) count[c - 'a']--
            return count.all { it == 0 }
        }
        println("Result: ${solve(s, t)}")
    }

    /**
     * 2. Valid Palindrome
     *
     * PROBLEM:
     * A phrase is a palindrome if, after converting all uppercase letters into lowercase letters and removing all non-alphanumeric characters, it reads the same forward and backward.
     * Given a string `s`, return `true` if it is a palindrome, or `false` otherwise.
     *
     * INPUT/OUTPUT:
     * Input: s = "A man, a plan, a canal: Panama"
     * Output: true
     * Explanation: "amanaplanacanalpanama" is a palindrome.
     *
     * DESIGN:
     * Why Two Pointers?
     * - We need to compare characters from the outside moving in.
     * - Creating a filtered reversed string would take O(N) extra space.
     * - Two pointers allow us to verify the property in-place with O(1) space.
     *
     * DETAIL:
     * 1. Initialize two pointers: `l` (left, 0) and `r` (right, s.length - 1).
     * 2. Iterate while `l < r`:
     *    - If `s[l]` is not alphanumeric, increment `l` and continue.
     *    - If `s[r]` is not alphanumeric, decrement `r` and continue.
     *    - Determine equality: `s[l].lowercaseChar() == s[r].lowercaseChar()`.
     *    - If they don't match, return `false`.
     *    - Otherwise, move both pointers: `l++`, `r--`.
     * 3. If the loop completes, return `true`.
     *
     * COMPLEXITY:
     * Time: O(n) - Traverse the string once.
     * Space: O(1) - No extra data structures used.
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
     *
     * PROBLEM:
     * Write a function to find the longest common prefix string amongst an array of strings.
     * If there is no common prefix, return an empty string "".
     *
     * INPUT/OUTPUT:
     * Input: strs = ["flower","flow","flight"]
     * Output: "fl"
     *
     * DESIGN:
     * Why Horizontal Scanning?
     * - The LCP of a set of strings is `LCP(LCP(s1, s2), s3)...`
     * - We can take the first string as a candidate `prefix`.
     * - We compare it with the next string, shortening the `prefix` from the end until it matches the start of the next string.
     * - If `prefix` becomes empty, we stop.
     *
     * DETAIL:
     * 1. Start with `prefix = strs[0]`.
     * 2. Iterate through `strs` from index 1.
     * 3. checking `strs[i].startsWith(prefix)`:
     *    - While false, shorten `prefix` by removing the last character.
     *    - If `prefix` becomes empty, return "".
     * 4. Return the final `prefix`.
     *
     * COMPLEXITY:
     * Time: O(S) - Where S is sum of all characters in all strings. In worst case all identical.
     * Space: O(1) - Modifying the prefix string (or O(P) where P is length of prefix).
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
     *
     * PROBLEM:
     * Write a function that reverses a string. The input string is given as an array of characters `s`.
     * You must do this by modifying the input array in-place with O(1) extra memory.
     *
     * INPUT/OUTPUT:
     * Input: s = ['h','e','l','l','o']
     * Output: ['o','l','l','e','h']
     *
     * DESIGN:
     * Why Two Pointers?
     * - To strictly satisfy O(1) space, we cannot create a new array.
     * - We swap the first element with the last, second with second-to-last, etc.
     *
     * DETAIL:
     * 1. Initialize `l = 0` and `r = s.size - 1`.
     * 2. While `l < r`:
     *    - Store `s[l]` in `temp`.
     *    - Assign `s[r]` to `s[l]`.
     *    - Assign `temp` to `s[r]`.
     *    - Increment `l`, decrement `r`.
     *
     * COMPLEXITY:
     * Time: O(n) - We swap n/2 times.
     * Space: O(1) - In-place.
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
     * 5. Reverse Vowels of a String
     *
     * PROBLEM:
     * Given a string `s`, reverse only all the vowels in the string and return it.
     * The vowels are 'a', 'e', 'i', 'o', 'u', and they can appear in both lower and upper cases, more than once.
     *
     * INPUT/OUTPUT:
     * Input: s = "hello"
     * Output: "holle"
     * Input: s = "leetcode"
     * Output: "leotcede"
     *
     * DESIGN:
     * Why Two Pointers?
     * - We need to swap characters at specific conditions (both are vowels).
     * - We can scan from both ends towards the center.
     *
     * DETAIL:
     * 1. Identify vowels: Set containing a,e,i,o,u,A,E,I,O,U.
     * 2. Convert string to mutable char array.
     * 3. Initialize `l = 0` (left) and `r = n-1` (right).
     * 4. While `l < r`:
     *    - Move `l` forward until a vowel is found.
     *    - Move `r` backward until a vowel is found.
     *    - If `l < r`, swap them and move both indices.
     *
     * COMPLEXITY:
     * Time: O(n) - Single pass.
     * Space: O(1) - If ignoring output char array (since Strings are immutable in Java/Kotlin).
     */
    @Test
    fun q5_reverseVowels() {
        println("=== Q5: Reverse Vowels ===")
        val s = "hello"
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
     *
     * PROBLEM:
     * Given two strings `s` and `t`, determine if they are isomorphic.
     * Two strings are isomorphic if the characters in `s` can be replaced to get `t`.
     * All occurrences of a character must be replaced with another character while preserving both ordering and one-to-one mapping.
     *
     * INPUT/OUTPUT:
     * Input: s = "egg", t = "add" -> Output: true (e->a, g->d)
     * Input: s = "foo", t = "bar" -> Output: false (o cannot map to both a and r)
     *
     * DESIGN:
     * Why Two Maps?
     * - We need a bijection (one-to-one mapping).
     * - `mapS` tracks s->t mapping.
     * - `mapT` tracks t->s mapping.
     * - If we see a character again, we check if it maps to the same target as before.
     *
     * DETAIL:
     * 1. Iterate `s` and `t` simultaneously (index `i`).
     * 2. Let `c1 = s[i]` and `c2 = t[i]`.
     * 3. Check `mapS`: If `c1` is in map, does it map to `c2`? If no, return false.
     * 4. Check `mapT`: If `c2` is in map, does it map to `c1`? If no, return false.
     * 5. Add mappings: `mapS[c1] = c2`, `mapT[c2] = c1`.
     *
     * COMPLEXITY:
     * Time: O(n)
     * Space: O(1) - ASCII size (max 256 keys).
     */
    @Test
    fun q6_isomorphicStrings() {
        println("=== Q6: Isomorphic Strings ===")
        val s = "egg"; val t = "add"
        
        fun solve(s: String, t: String): Boolean {
            val mapS = HashMap<Char, Char>()
            val mapT = HashMap<Char, Char>()
            for (i in s.indices) {
                val c1 = s[i]; val c2 = t[i]
                if ((mapS.containsKey(c1) && mapS[c1] != c2) || 
                    (mapT.containsKey(c2) && mapT[c2] != c1)) {
                    return false
                }
                mapS[c1] = c2; mapT[c2] = c1
            }
            return true
        }
        println("Result: ${solve(s, t)}")
    }

    /**
     * 7. Word Pattern
     *
     * PROBLEM:
     * Given a `pattern` and a string `s`, find if `s` follows the same pattern.
     * Here follow means a full match, such that there is a bijection between a letter in `pattern` and a non-empty word in `s`.
     *
     * INPUT/OUTPUT:
     * Input: pattern = "abba", s = "dog cat cat dog" -> Output: true
     * Input: pattern = "abba", s = "dog cat cat fish" -> Output: false
     *
     * DESIGN:
     * Why Two Maps?
     * - Similar to Isomorphic Strings, but mapping `Char` -> `String` (Word).
     * - `pToW` maps pattern char -> word.
     * - `wToP` maps word -> pattern char.
     *
     * DETAIL:
     * 1. Split `s` by space into `words`.
     * 2. If `pattern.length != words.size`, return false.
     * 3. Iterate through indices.
     * 4. Check consistency in both maps. If mismatch, return false.
     * 5. Store mappings.
     *
     * COMPLEXITY:
     * Time: O(n)
     * Space: O(m) where m is number of unique words/chars.
     */
    @Test
    fun q7_wordPattern() {
        println("=== Q7: Word Pattern ===")
        val pattern = "abba"
        val s = "dog cat cat dog"
        
        fun solve(pattern: String, s: String): Boolean {
            val words = s.split(" ")
            if (pattern.length != words.size) return false
            val pToW = HashMap<Char, String>()
            val wToP = HashMap<String, Char>()
            
            for (i in pattern.indices) {
                val c = pattern[i]; val w = words[i]
                if ((pToW.containsKey(c) && pToW[c] != w) ||
                    (wToP.containsKey(w) && wToP[w] != c)) {
                    return false
                }
                pToW[c] = w; wToP[w] = c
            }
            return true
        }
        println("Result: ${solve(pattern, s)}")
    }

    /**
     * 8. First Unique Character in a String
     *
     * PROBLEM:
     * Given a string `s`, find the first non-repeating character in it and return its index. If it does not exist, return -1.
     *
     * INPUT/OUTPUT:
     * Input: s = "leetcode" -> Output: 0 ('l')
     * Input: s = "loveleetcode" -> Output: 2 ('v')
     *
     * DESIGN:
     * Why Frequency Array?
     * - Two passes are needed.
     * - Pass 1: Count frequency of all characters.
     * - Pass 2: Iterate string again, check if count is 1. The first one found is the answer.
     *
     * DETAIL:
     * 1. Create `count` array of size 26.
     * 2. Loop through `s` to populate `count`.
     * 3. Loop through `s` by index `i`.
     * 4. Check `count[s[i] - 'a']`. If 1, return `i`.
     * 5. If loop ends, return -1.
     *
     * COMPLEXITY:
     * Time: O(n)
     * Space: O(1)
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
     *
     * PROBLEM:
     * Given a string `s` consisting of words and spaces, return the length of the last word in the string.
     *
     * INPUT/OUTPUT:
     * Input: s = "Hello World" -> Output: 5 ("World")
     * Input: s = "   fly me   to   the moon  " -> Output: 4 ("moon")
     *
     * DESIGN:
     * Why Trim and Scan?
     * - We care about the last non-empty sequence of characters.
     * - Built-in `trim()` removes trailing spaces.
     * - Then finding the last space gives us the start of the last word.
     *
     * DETAIL:
     * 1. `s.trim()` to remove leading/trailing whitespace.
     * 2. Find `lastIndexOf(' ')`.
     * 3. Length is `totalLength - lastSpaceIndex - 1`.
     *
     * COMPLEXITY:
     * Time: O(n)
     * Space: O(1) (or O(n) if trim creates new string).
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
     *
     * PROBLEM:
     * Given two non-negative integers `num1` and `num2` represented as strings, return the sum of `num1` and `num2` as a string.
     * You must not use any built-in BigInteger library or convert the inputs to integers directly.
     *
     * INPUT/OUTPUT:
     * Input: num1 = "11", num2 = "123" -> Output: "134"
     * Input: num1 = "456", num2 = "77" -> Output: "533"
     *
     * DESIGN:
     * Why Digit-by-Digit Addition?
     * - Simulates manual "schoolbook" math from right to left.
     * - Handles numbers larger than Long.MAX_VALUE.
     *
     * DETAIL:
     * 1. Initialize pointers `i` and `j` to ends of `num1`, `num2`.
     * 2. Initialize `carry = 0`.
     * 3. Loop while `i >= 0` OR `j >= 0` OR `carry != 0`.
     * 4. Get digit `d1` from `num1[i]` (or 0 if out of bounds).
     * 5. Get digit `d2` from `num2[j]` (or 0 if out of bounds).
     * 6. `sum = d1 + d2 + carry`.
     * 7. Append `sum % 10` to result.
     * 8. Update `carry = sum / 10`.
     * 9. Reverse result at the end.
     *
     * COMPLEXITY:
     * Time: O(max(N, M))
     * Space: O(max(N, M)) for output.
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
     *
     * PROBLEM:
     * Given two strings `ransomNote` and `magazine`, return true if `ransomNote` can be constructed by using the letters from `magazine` and false otherwise.
     * Each letter in `magazine` can only be used once in `ransomNote`.
     *
     * INPUT/OUTPUT:
     * Input: ransomNote = "aa", magazine = "aab" -> Output: true
     * Input: ransomNote = "aa", magazine = "ab" -> Output: false
     *
     * DESIGN:
     * Why Frequency Map?
     * - We need to count available characters in `magazine`.
     * - Then verify we have enough of each character to build `ransomNote`.
     * - An array of size 26 suffices for lowercase English letters.
     *
     * DETAIL:
     * 1. Create `count` array of size 26.
     * 2. Iterate `magazine` and increment counts.
     * 3. Iterate `ransomNote` and decrement counts.
     * 4. If any count drops below 0, return false (not enough letters).
     * 5. If loop completes, return true.
     *
     * COMPLEXITY:
     * Time: O(M + N) - M is length of magazine, N is length of ransomNote.
     * Space: O(1) - Fixed size array.
     */
    @Test
    fun q11_ransomNote() {
        println("=== Q11: Ransom Note ===")
        val note = "aa"; val mag = "aab"
        
        fun solve(note: String, mag: String): Boolean {
            val arr = IntArray(26)
            for (c in mag) arr[c - 'a']++
            for (c in note) {
                if (--arr[c - 'a'] < 0) return false
            }
            return true
        }
        println("Result: ${solve(note, mag)}")
    }

    /**
     * 12. Valid Parentheses
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
     * - The "last opened" bracket must be the "first closed" (LIFO property).
     * - A Stack is perfect for tracking open brackets.
     *
     * DETAIL:
     * 1. Initialize an empty Stack.
     * 2. Iterate through characters of `s`:
     *    - If it's an opening bracket '(', '{', '[', push to stack.
     *    - If it's a closing bracket:
     *      - Check if stack is empty (invalid).
     *      - Pop top element. Determine if it matches the current closing bracket.
     *      - If mismatch, return false.
     * 3. Finally, check if stack is empty (all opened brackets count matching closed ones).
     *
     * COMPLEXITY:
     * Time: O(n)
     * Space: O(n)
     */
    @Test
    fun q12_validParentheses() {
        println("=== Q12: Valid Parentheses ===")
        val s = "()[]{}"
        
        fun solve(s: String): Boolean {
            val stack = Stack<Char>()
            for (c in s) {
                if (c == '(' || c == '{' || c == '[') {
                    stack.push(c)
                } else {
                    if (stack.isEmpty()) return false
                    val top = stack.pop()
                    if ((c == ')' && top != '(') || 
                        (c == '}' && top != '{') || 
                        (c == ']' && top != '[')) return false
                }
            }
            return stack.isEmpty()
        }
        println("Result: ${solve(s)}")
    }

    /**
     * 13. Implement strStr()
     *
     * PROBLEM:
     * Return the index of the first occurrence of `needle` in `haystack`, or -1 if `needle` is not part of `haystack`.
     *
     * INPUT/OUTPUT:
     * Input: haystack = "hello", needle = "ll" -> Output: 2
     * Input: haystack = "aaaaa", needle = "bba" -> Output: -1
     *
     * DESIGN:
     * Why Linear Scan or Built-in?
     * - For this problem, a naive sliding window check is sufficient.
     * - We check every substring of `haystack` of length `needle.length`.
     * - Java/Kotlin's `indexOf` implements this optimization well.
     *
     * DETAIL:
     * 1. Iterate `i` from 0 to `haystack.length - needle.length`.
     * 2. Check if substring starting at `i` matches `needle`.
     * 3. Return `i` on match.
     * 4. Return -1 if no match found.
     *
     * COMPLEXITY:
     * Time: O(N*M) - Naive implementation.
     * Space: O(1)
     */
    @Test
    fun q13_strStr() {
        println("=== Q13: strStr ===")
        val haystack = "hello"; val needle = "ll"
        println("Result: ${haystack.indexOf(needle)}")
    }

    /**
     * 14. Rotate String
     *
     * PROBLEM:
     * Given two strings `s` and `goal`, return true if and only if `s` can become `goal` after some number of shifts on `s`.
     * A shift consists of moving the leftmost character of `s` to the rightmost position.
     *
     * INPUT/OUTPUT:
     * Input: s = "abcde", goal = "cdeab" -> Output: true
     * Input: s = "abcde", goal = "abced" -> Output: false
     *
     * DESIGN:
     * Why Concatenation?
     * - If `s` is rotated to form `goal`, then `goal` must be a substring of `s + s`.
     * - Example: "abcde" + "abcde" = "abcdeabcde". "cdeab" is clearly visible inside.
     * - Must also check if lengths are equal.
     *
     * DETAIL:
     * 1. Check if `s.length == goal.length`. If not, return false.
     * 2. Check if `(s + s).contains(goal)`.
     *
     * COMPLEXITY:
     * Time: O(N^2) - String matching.
     * Space: O(N) - Creating `s + s`.
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
     *
     * PROBLEM:
     * Given a string `s`, return the string after replacing every uppercase letter with the same lowercase letter.
     *
     * INPUT/OUTPUT:
     * Input: s = "Hello" -> Output: "hello"
     *
     * DESIGN:
     * Why ASCII manipulation?
     * - Characters are integers. 'a' is 97, 'A' is 65. Difference is 32.
     * - Usually built-in functions are preferred for locale handling, but ASCII is simple: `c + 32`.
     *
     * DETAIL:
     * 1. Iterate through characters.
     * 2. If 'A' <= c <= 'Z', convert to `c + 32` char.
     * 3. Else keep as is.
     *
     * COMPLEXITY:
     * Time: O(n)
     * Space: O(n) - New string.
     */
    @Test
    fun q15_toLowerCase() {
        println("=== Q15: To Lower Case ===")
        val s = "Hello"
        println("Result: ${s.lowercase()}")
    }

    /**
     * 16. Check if Two String Arrays are Equivalent
     *
     * PROBLEM:
     * Given two string arrays `word1` and `word2`, return true if the two arrays represent the same string, and false otherwise.
     * A string is represented by an array if the array elements concatenated in order form the string.
     *
     * INPUT/OUTPUT:
     * Input: word1 = ["ab", "c"], word2 = ["a", "bc"] -> Output: true
     *
     * DESIGN:
     * Why Join or Iterator?
     * - The simplest way is to join all strings and compare result.
     * - An optimized way uses iterators to avoid O(N) space for the joined string.
     * - For simplicity here, we verify the logic using `joinToString` or manual iteration.
     *
     * DETAIL:
     * 1. Concatenate all elements of `word1`.
     * 2. Concatenate all elements of `word2`.
     * 3. return `str1 == str2`.
     *
     * COMPLEXITY:
     * Time: O(N*K) where N is array length and K is avg string length.
     * Space: O(N*K) for new string.
     */
    @Test
    fun q16_arrayStringsAreEqual() {
        println("=== Q16: Array Strings Equivalent ===")
        val word1 = arrayOf("ab", "c"); val word2 = arrayOf("a", "bc")
        println("Result: ${word1.joinToString("") == word2.joinToString("")}")
    }

    /**
     * 17. Merge Strings Alternately
     *
     * PROBLEM:
     * You are given two strings `word1` and `word2`. Merge the strings by adding letters in alternating order, starting with `word1`.
     * If a string is longer than the other, append the additional letters onto the end of the merged string.
     *
     * INPUT/OUTPUT:
     * Input: word1 = "abc", word2 = "pqr" -> Output: "apbqcr"
     * Input: word1 = "ab", word2 = "pqrs" -> Output: "apbqrs"
     *
     * DESIGN:
     * Why Single Loop?
     * - We can iterate using an index `i` from 0 to the max length of both strings.
     * - In each iteration, if `i` is within bounds of `word1`, append `word1[i]`.
     * - Steps repeated for `word2`.
     *
     * DETAIL:
     * 1. Initialize `i = 0`.
     * 2. Determine `n = max(word1.length, word2.length)`.
     * 3. Loop `i` from 0 to `n-1`.
     * 4. Check if `i < word1.length`, append char.
     * 5. Check if `i < word2.length`, append char.
     *
     * COMPLEXITY:
     * Time: O(N+M)
     * Space: O(N+M) for result.
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
     *
     * PROBLEM:
     * You are given a string `s` and an integer array `indices` of the same length.
     * The string `s` will be shuffled such that the character at the ith position moves to `indices[i]` in the shuffled string.
     *
     * INPUT/OUTPUT:
     * Input: s = "codeleet", indices = [4,5,6,7,0,2,1,3]
     * Output: "leetcode"
     *
     * DESIGN:
     * Why Direct Placement?
     * - We know exactly where each character goes.
     * - Create a result char array and fill it directly.
     *
     * DETAIL:
     * 1. Create `res` array of size `s.length`.
     * 2. Iterate `i` from 0 to `s.length - 1`.
     * 3. `res[indices[i]] = s[i]`.
     * 4. Convert `res` to String.
     *
     * COMPLEXITY:
     * Time: O(n)
     * Space: O(n)
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
     *
     * PROBLEM:
     * Given a valid (IPv4) IP `address`, return a defanged version of that IP address.
     * A defanged IP address replaces every period "." with "[.]".
     *
     * INPUT/OUTPUT:
     * Input: address = "1.1.1.1" -> Output: "1[.]1[.]1[.]1"
     *
     * DESIGN:
     * Why String Replacement?
     * - We simply need to traverse and replace or construct a new string inserting brackets.
     * - `replace` method is efficient enough for this.
     *
     * DETAIL:
     * 1. Call `addr.replace(".", "[.]")`.
     *
     * COMPLEXITY:
     * Time: O(n)
     * Space: O(n)
     */
    @Test
    fun q19_defangIP() {
        println("=== Q19: Defang IP ===")
        val addr = "1.1.1.1"
        println("Result: ${addr.replace(".", "[.]")}")
    }

    /**
     * 20. Goal Parser Interpretation
     *
     * PROBLEM:
     * You own a Goal Parser that can interpret a string `command`.
     * The command consists of an alphabet of "G", "()" and/or "(al)" in some order.
     * The Goal Parser will interpret "G" as the string "G", "()" as the string "o", and "(al)" as the string "al".
     *
     * INPUT/OUTPUT:
     * Input: command = "G()(al)" -> Output: "Goal"
     *
     * DESIGN:
     * Why Replacement?
     * - We can replace tokens with their mapped values.
     * - Order matters: replace longer tokens first if they contain shorter ones (not the case here, but good practice).
     * - Or simply scan linearly and form the result.
     *
     * DETAIL:
     * 1. `replace("()", "o")`
     * 2. `replace("(al)", "al")`
     * 3. Return result.
     *
     * COMPLEXITY:
     * Time: O(n)
     * Space: O(n)
     */
    @Test
    fun q20_goalParser() {
        println("=== Q20: Goal Parser ===")
        val cmd = "G()(al)"
        val res = cmd.replace("()", "o").replace("(al)", "al")
        println("Result: $res")
    }
}
