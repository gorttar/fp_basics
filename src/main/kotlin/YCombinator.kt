private interface RF<F: Function1<*, *>> : (RF<F>) -> F

fun <A, B> y(f: ((A) -> B) -> (A) -> B): (A) -> B {
    val rf: RF<(A) -> B> = object : RF<(A) -> B> {
        override fun invoke(w: RF<(A) -> B>): (A) -> B = f { a: A -> w(w)(a) }
    }

    return rf(rf)
}

fun main() {
    val factorial = y { f: (Int) -> Int -> { n -> if (n <= 1) 1 else n * f(n - 1) } }
    val fibonacci = y { f: (Int) -> Int -> { n -> if (n <= 2) 1 else f(n - 1) + f(n - 2) } }
    print("Factorial(1..10)   : ")
    (1..10).forEach { i -> print("${factorial(i)}  ") }
    print("\nFibonacci(1..10)   : ")
    (1..10).forEach { i -> print("${fibonacci(i)}  ") }
    println()
}