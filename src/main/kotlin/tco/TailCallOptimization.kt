package tco

private val evenOpt: TCFunction<Int, Boolean> = { n -> if (n == 0) true.ret else oddOpt[n - 1] }
private val oddOpt: TCFunction<Int, Boolean> = { n -> if (n == 0) false.ret else evenOpt[n - 1] }

private val oddOptRef = ::oddOptF
private val evenOptRef = ::evenOptF
private fun evenOptF(n: Int): TCResult<Int, Boolean> = if (n == 0) true.ret else oddOptRef[n - 1]
private fun oddOptF(n: Int): TCResult<Int, Boolean> = if (n == 0) false.ret else evenOptRef[n - 1]

fun main() {
    println(evenOpt(10).fix) // корректно напечатает true
    println(evenOpt(10_000_000).fix) // корректно напечатает true
    println(oddOpt(10).fix) // корректно напечатает false
    println(oddOpt(10_000_000).fix) // корректно напечатает false
    println(evenOptF(10).fix) // корректно напечатает true
    println(evenOptF(10_000_000).fix) // корректно напечатает true
    println(oddOptF(10).fix) // корректно напечатает false
    println(oddOptF(10_000_000).fix) // корректно напечатает false
}