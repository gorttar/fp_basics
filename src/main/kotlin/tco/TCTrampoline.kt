/**
 * tco/TCTrampoline.kt
 */
package tco

/**
 * specialized type of tail call optimizable functions
 * @param T - function input type
 * @param R - function output type
 */
typealias TCFunction<T, R> = (T) -> TCResult<T, R>

/**
 * wrapped result of [TCFunction]
 * @param T - function input type
 * @param R - function output type
 */
interface TCResult<in T, out R> {
    /**
     * raw result of [TCFunction]
     * access will lead to [TCFunction] evaluation
     */
    val fix: R
}

/**
 * implementation of intermediate [TCFunction] evaluation step
 * @param T - function input type
 * @param R - function output type
 */
private class Step<in T, out R>(private val t: T, private val nextStep: TCFunction<T, R>) : TCResult<T, R> {
    override val fix: R
        get() {
            var step = next
            while (step is Step) step = step.next
            return step.fix
        }

    private val next: TCResult<T, R> get() = nextStep(t)
}

/**
 * tail call invocation of [TCFunction]
 * creates next [Step] instead of actual evaluation
 * @param T - function input type
 * @param R - function output type
 */
infix operator fun <T, R> TCFunction<in T, out R>.get(t: T): TCResult<T, R> = Step(t, this)

/**
 * receiver wrapped as final [TCFunction] evaluation step
 * @param R - function output type
 */
val <R> R.ret: TCResult<Any?, R>
    get() = object : TCResult<Any?, R> {
        override val fix: R = this@ret
    }