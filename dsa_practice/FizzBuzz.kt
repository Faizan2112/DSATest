/**
 * Fizz Buzz
 *
 * Given an integer n, return a string array answer (1-indexed) where:
 * answer[i] == "FizzBuzz" if i is divisible by 3 and 5.
 * answer[i] == "Fizz" if i is divisible by 3.
 * answer[i] == "Buzz" if i is divisible by 5.
 * answer[i] == i (as a string) if none of the above conditions are true.
 */

fun main() {
    val n = 15
    val result = fizzBuzz(n)
    println("FizzBuzz for n=$n:")
    result.forEach { println(it) }
}

fun fizzBuzz(n: Int): List<String> {
    val result = mutableListOf<String>()
    
    for (i in 1..n) {
        // TODO: Implement logic here
        // if (i % 3 == 0 && i % 5 == 0) ...
        // else if ...
        
        // Placeholder implementation to let you practice
        result.add(i.toString())
    }
    
    return result
}
