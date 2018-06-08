/**
 * @author Andrey Antipov (gorttar@gmail.com) (2018-04-09)
 */
typealias TCFunction<A, B> = (A) -> TCResult<A, B>

@Suppress("unused")
sealed class TCResult<A, out B> : () -> B

private class Call<A, out B>(private val a: A, private val nextStep: TCFunction<A, B>) : TCResult<A, B>() {
    override fun invoke(): B {
        var step = next()
        while (step is Call<A, B>) {
            step = step.next()
        }
        return step()
    }

    private fun next(): TCResult<A, B> = nextStep(a)
}

private class Ret<A, out B>(private val b: B) : TCResult<A, B>() {
    override fun invoke(): B = b
}

infix fun <A, B> TCFunction<A, B>.ap(a: A): TCResult<A, B> = Call(a, this)

fun <A, B> B.ret(): TCResult<A, B> = Ret(this)

private val evenOpt: TCFunction<Int, Boolean> = { n -> if (n == 0) true.ret() else oddOpt ap n - 1 }

private val oddOpt: TCFunction<Int, Boolean> = { n -> if (n == 0) false.ret() else evenOpt ap n - 1 }

fun main(args: Array<String>) {
    println(evenOpt(10)()) // корректно напечатает true
    println(evenOpt(10_000_000)()) // корректно напечатает true
    println(oddOpt(10)()) // корректно напечатает false
    println(oddOpt(10_000_000)()) // корректно напечатает false
}