private interface W<A, B> : (W<A, B>) -> (A) -> B

fun <A, B> y(f: ((A) -> B) -> (A) -> B): (A) -> B {
    val w = object : W<A, B> {
        override fun invoke(w: W<A, B>) = f { x -> w(w)(x) }
    }

    return w(w)
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