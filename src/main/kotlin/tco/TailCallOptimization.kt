package tco

private val evenOpt: TailFunction1<Int, Boolean> = { n -> if (n == 0) true.ret1 else oddOpt[n - 1] }
private val oddOpt: TailFunction1<Int, Boolean> = { n -> if (n == 0) false.ret1 else evenOpt[n - 1] }

private fun evenOptF(n: Int): TailCall1<Int, Boolean> = if (n == 0) true.ret1 else ::oddOptF[n - 1]
private fun oddOptF(n: Int): TailCall1<Int, Boolean> = if (n == 0) false.ret1 else ::evenOptF[n - 1]

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