/**
 * @author Andrey Antipov (andrey.antipov@cxense.com) (2018-06-08 19:16)
 */
val even: (Int) -> Boolean = { n -> n == 0 || odd(n - 1) }
val odd: (Int) -> Boolean = { n -> n != 0 && even(n - 1) }

fun main(args: Array<String>) {
    println(even(10)) // корректно напечатает true
    println(even(10_000_000)) // упадёт со StackOverflowError
}