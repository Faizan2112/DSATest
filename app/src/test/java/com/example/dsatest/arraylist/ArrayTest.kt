package com.example.dsatest.arraylist

import org.junit.Test

class ArrayTest {
    // check max common text
    // array has size
    // string has length
    // arr[0].length will if we print it it will give array index out of bound

    @Test
    fun testMaxCommonText() {
        val arr = arrayOf("flows", "flower", "flow")
        var strbuilder = ""
        var minMatch = arr[0].length
        // edge case if fist two does't match retur
        // scenrio = first matches all but next misses ,
        for (i in 1 until arr.size) {
            var j = 0
            var k = 0
            var currentMax = 0
            while (j <= arr[0].length - 1 && k <= arr[i].length - 1) {
                if (arr[0][0] != arr[i][0]) {
                    return
                }
                if (arr[0][j] == arr[i][k]) {
                    currentMax++
                    j++
                    k++
                } else {
                    break
                }
            }

            if (currentMax < minMatch) {
                minMatch = currentMax
            }
        }

        for (l in 0 until minMatch) {
            strbuilder = strbuilder + "" + arr[0][l]
        }
        print(strbuilder)
    }

    @Test
    fun firstAndLastPositionOfTarget() {
        // when (log n) time complexity and array is sorted use binary search
        // break into two halfs then check which side is data
        val intArr = intArrayOf(5, 7, 7, 8, 8, 10)
        var t = 8

    }
    /*
        Example 1:

        Input: s = "babad"
        Output: "bab"
        Explanation: "aba" is also a valid answer.*/

    @Test
    fun longestPalindromicSubstring() {
        var s = "babad".toCharArray()
        var currentString = ""
        var maxLenth = 1
        var arrayOfSubstring = arrayListOf<String>()
        if (s.size == 0 || s.size < 2) {
            println("no substring")
        }


        for (i in 1..s.size - 1) {
            var lp = 1
            var rp = 1
            var j = i - lp
            var k = i + rp
            var currentLength = -1
            currentString = s.get(i).toString()
            while (j >= 0 && k < s.size - 1) {

                if (s[j] == s[k]) {
                    currentLength = k - j
                    currentString = s[j] + currentString + s[k]
                    j = i - lp
                    k = i + rp
                    lp++
                    rp++


                } else {
                    lp =1
                    rp =1
                    currentString=""
                    break
                }
            }

            if (currentLength > maxLenth) {
                maxLenth = currentLength
                arrayOfSubstring.clear()
                arrayOfSubstring.add(currentString)
            } else if (currentLength == maxLenth) {
                arrayOfSubstring.add(currentString)
            }


        }

        arrayOfSubstring.forEach {
            println(it)
        }


    }
    @Test
    fun longestPalindromicSubstring1() {
        val s = "babad"
        if (s.isEmpty() || s.length < 2) {
            println("no substring")
            return
        }

        var start = 0
        var end = 0
        val arrayOfSubstring = ArrayList<String>()

        for (i in s.indices) {
            // Check for odd-length palindromes (center at i)
            val len1 = expandAroundCenter(s, i, i)
            // Check for even-length palindromes (center between i and i+1)
            val len2 = expandAroundCenter(s, i, i + 1)

            val maxLen = maxOf(len1, len2)

            if (maxLen > end - start) {
                start = i - (maxLen - 1) / 2
                end = i + maxLen / 2
                arrayOfSubstring.clear()
                arrayOfSubstring.add(s.substring(start, end + 1))
            } else if (maxLen == end - start + 1) {
                arrayOfSubstring.add(s.substring(i - (maxLen - 1) / 2, i + maxLen / 2 + 1))
            }
        }

        arrayOfSubstring.forEach { println(it) }
    }

    private fun expandAroundCenter(s: String, left: Int, right: Int): Int {
        var l = left
        var r = right
        while (l >= 0 && r < s.length && s[l] == s[r]) {
            l--
            r++
        }
        return r - l - 1
    }

}

