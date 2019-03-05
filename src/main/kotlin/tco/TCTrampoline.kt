package tco

/**
 * @author Andrey Antipov (gorttar@gmail.com) (2018-04-09)
 */
typealias TCFunction<T, R> = (T) -> TCResult<T, R>

sealed class TCResult<in T, out R> {
    abstract val fix: R
}

private class Call<in T, out R>(private val a: T, private val nextStep: TCFunction<T, R>) : TCResult<T, R>() {
    override val fix: R
        get() {
            var step = next()
            while (step is Call<T, R>) {
                step = step.next()
            }
            return step.fix
        }

    private fun next(): TCResult<T, R> = nextStep(a)
}

private class Ret<out R>(override val fix: R) : TCResult<Any?, R>()

infix operator fun <A, B> TCFunction<A, B>.get(a: A): TCResult<A, B> = Call(a, this)

val <R> R.ret: TCResult<Any?, R> get() = Ret(this)