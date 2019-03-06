/**
 * tco/TailCallTrampoline.kt
 */
package tco

/**
 * specialized type of tail call optimizable functions
 * @param T - function input type
 * @param R - function output type
 */
typealias TailFunction<T, R> = (T) -> TailCall<T, R>

/**
 * optimizable tail call of [TailFunction]
 * @param T - function input type
 * @param R - function output type
 */
interface TailCall<in T, out R> {
    /**
     * raw result of [TailFunction]
     * access will lead to [TailFunction] evaluation
     */
    val fix: R
}

/**
 * implementation of intermediate [TailFunction] call
 * @param nextStep - [TailFunction] representing next evaluation step
 * @param t - input for next step
 * @param T - function input type
 * @param R - function output type
 */
private class IntermediateCall<in T, out R>(private val nextStep: TailFunction<T, R>, private val t: T) : TailCall<T, R> {
    override val fix: R
        get() {
            var step = next
            while (step is IntermediateCall) step = step.next
            return step.fix
        }

    private val next: TailCall<T, R> get() = nextStep(t)
}

/**
 * tail call invocation of [TailFunction]
 * creates next [IntermediateCall] instead of actual evaluation
 * @receiver called [TailFunction]
 * @param t - invocation argument
 * @param T - function input type
 * @param R - function output type
 */
infix operator fun <T, R> TailFunction<in T, out R>.get(t: T): TailCall<T, R> = IntermediateCall(this, t)

/**
 * @receiver wrapped as final [TailFunction] call
 * @param R - function output type
 */
val <R> R.ret: TailCall<Any?, R>
    get() = object : TailCall<Any?, R> {
        override val fix: R = this@ret
    }