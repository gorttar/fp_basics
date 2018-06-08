/**
 * @author Andrey Antipov (andrey.antipov@cxense.com) (2018-06-08 20:33)
 */
typealias F<A, B> = (A) -> B

private interface RF<Fun> : F<RF<Fun>, Fun>

fun <A, B> y(f: F<F<A, B>, F<A, B>>): F<A, B> {
    val rf: RF<F<A, B>> = object : RF<F<A, B>> {
        override fun invoke(w: RF<F<A, B>>): F<A, B> = f { a: A -> w(w)(a) }
    }

    return rf(rf)
}

fun main(args: Array<String>) {
    val factorial = y { f: F<Int, Int> -> { n -> if (n <= 1) 1 else n * f(n - 1) } }
    val fibonacci = y { f: F<Int, Int> -> { n -> if (n <= 2) 1 else f(n - 1) + f(n - 2) } }
    print("Factorial(1..10)   : ")
    (1..10).forEach { i -> print("${factorial(i)}  ") }
    print("\nFibonacci(1..10)   : ")
    (1..10).forEach { i -> print("${fibonacci(i)}  ") }
    println()
}