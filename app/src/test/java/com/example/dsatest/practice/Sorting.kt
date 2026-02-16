/*
package com.example.dsatest.practice

import org.junit.Test
// recursion
class Sorting {
  // define a base case
   // define a base case
    // abcsef -> a -> reverser(bcsef) , b ->(csef) , c -> (sef)
    @Test
    fun rever() {
       // val  str = "abcsef"
       // reverseString(str)
      var arr = intArrayOf(1,3,4,3,5,6,9)
      reverseArray(arr)
      print(arr.contentToString())
    }

    fun reverseArray(arr : IntArray){
        if (arr.isNotEmpty()){
            reverseArray(arr, 0 ,arr.size-1)
        }
    }

    private fun reverseArray(arr: IntArray, start: Int, end: Int) {
        if(start>end){
            return
        }
        var temp = arr[start]
        arr[start] = arr[end]
        arr[end] = temp
        reverseArray(arr, start+1, end-1)


    }

    fun reverseString(str:String){

        if(str.length > 0){
            reverseString(str.substring(str.length-1,0))
            println(str)
        }
    }



    @Test
    fun mergeSort(){
        var arr = intArrayOf(11,6,3,7,2,9) //[11,6,3] [7,2,9] , [11,6] [3] , [11 ][6 ]

        var mergeAndSort =  mergeSortArrayys(arr)
       // print(mergeAndSort.contentToString())
     */
/*   val lA = leftArr(arr[])

         1 3 6   7 2 9
        1 3   6    7 2  9
        1  3       7  2

        1 3        2  7
       1 3 6      2  7  9
     1 2 3 6 7 9*//*


    }

    private fun mergeSortArrayys(arr: IntArray) {
        var lA = intArrayOf()
        var rA = intArrayOf()
        var middelItem =  0
        if (arr.size > 1 ) {


            if (arr.size % 2 == 0) {
                middelItem = arr.size / 2

                lA = arr.copyOfRange(0, middelItem)
                rA = arr.copyOfRange(middelItem, arr.size)
            } else {
                middelItem = arr.size / 2 + 1
                lA = arr.copyOfRange(0, middelItem)
                rA = arr.copyOfRange(middelItem, arr.size)
            }

            mergeSortArrayys(lA)
            mergeSortArrayys(rA)
            mergeArr(arr ,lA, rA)
        }
    }

    private fun mergeArr(arr: IntArray, lA: IntArray, rA: IntArray) {
        var l = 0
        var r = 0

        while(l < lA.size && r < rA.size){

            if(lA[l] < rA[r]){
                r++
            }else{
                var tem = rA[r]
                rA[r] = lA[l]
                lA[l] =tem
                l++
            }
        }

        println(lA.contentToString() + " " +rA.contentToString())
    }


}
*/
package com.example.dsatest.practice

import org.junit.Test
// recursion
class Sorting {
    // define a base case
    // define a base case
    // abcsef -> a -> reverser(bcsef) , b ->(csef) , c -> (sef)
    @Test
    fun rever() {
        // val  str = "abcsef"
        // reverseString(str)
        var arr = intArrayOf(1, 3, 4, 3, 5, 6, 9)
        reverseArray(arr)
        print(arr.contentToString())
    }

    fun reverseArray(arr: IntArray) {
        if (arr.isNotEmpty()) {
            reverseArray(arr, 0, arr.size - 1)
        }
    }

    private fun reverseArray(arr: IntArray, start: Int, end: Int) {
        if (start > end) {
            return
        }
        var temp = arr[start]
        arr[start] = arr[end]
        arr[end] = temp
        reverseArray(arr, start + 1, end - 1)
    }

    fun reverseString(str: String) {
        if (str.length > 0) {
            // This line for reverseString is also problematic.
            // It tries to call substring(length-1, 0) which is an invalid range
            // and then prints the original string.
            // A common recursive reverse string would be:
            // if (str.isEmpty()) return ""
            // return reverseString(str.substring(1)) + str[0]
            reverseString(str.substring(str.length - 1, 0)) // This needs to be fixed if you want to use it
            println(str)
        }
    }

    @Test
    fun mergeSort() {
        var arr = intArrayOf(11, 6, 3, 7, 2, 9) // [11,6,3] [7,2,9] , [11,6] [3] , [11 ][6 ]

        mergeSortArrayys(arr) // Call the sorting function
        print(arr.contentToString()) // Print the sorted array
    }

    private fun mergeSortArrayys(arr: IntArray) {
        if (arr.size > 1) {
            val mid = arr.size / 2

            val lA = arr.copyOfRange(0, mid)
            val rA = arr.copyOfRange(mid, arr.size)

            mergeSortArrayys(lA)
            mergeSortArrayys(rA)

            mergeArr(arr, lA, rA)
        }
    }

    private fun mergeArr(arr: IntArray, lA: IntArray, rA: IntArray) {
        var i = 0 // Pointer for left sub-array (lA)
        var j = 0 // Pointer for right sub-array (rA)
        var k = 0 // Pointer for the main array (arr), where merged elements are placed

        while (i < lA.size && j < rA.size) {
            if (lA[i] <= rA[j]) {
                arr[k] = lA[i]
                i++
            } else {
                arr[k] = rA[j]
                j++
            }
            k++
        }

        while (i < lA.size) {
            arr[k] = lA[i]
            i++
            k++
        }

        while (j < rA.size) {
            arr[k] = rA[j]
            j++
            k++
        }

        println(arr.contentToString())
    }
}
