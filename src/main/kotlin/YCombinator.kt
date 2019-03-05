private interface RF<F : Function1<*, *>> : (RF<F>) -> F

fun <A, B> y(f: ((A) -> B) -> (A) -> B): (A) -> B {
    val rf = object : RF<(A) -> B> {
        override fun invoke(w: RF<(A) -> B>) = f { a -> w(w)(a) }
    }

    return rf(rf)
}

fun main() {
    val factorial = y { f: (Int) -> Int -> { n -> if (n <= 1) 1 else n * f(n - 1) } }
    val fibonacci = y { f: (Int) -> Int -> { n -> if (n <= 2) 1 else f(n - 1) + f(n - 2) } }
    val range = 1..10
    print(
            """Factorial[$range]   : ${range.map(factorial)}
Fibonacci[$range]   : ${range.map(fibonacci)}
""")
}