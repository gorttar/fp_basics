/**
 * tco/TailCallTrampoline.kt
 */
package tco

/**
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~ trampoline public API ~~~~~~~~~~~~~~~~~~~~~~~~~~
 */

/**
 * optimizable tail call of function
 * @param R - function output type
 */
interface TailCall<out R> {

    /**
     * raw result of tail call optimizable function
     * access will lead to function evaluation
     */
    val fix: R
}

/**
 * specialized type of tail call optimizable 1 argument functions
 * @param P1 - function input type
 * @param R - function output type
 */
typealias TailFunction1<P1, R> = (P1) -> TailCall<R>

/**
 * specialized type of tail call optimizable 2 arguments functions
 * @param P1 - 1st function input type
 * @param P2 - 2nd function input type
 * @param R - function output type
 */
typealias TailFunction2<P1, P2, R> = (P1, P2) -> TailCall<R>

/**
 * tail call invocation of [TailFunction1]
 * creates next intermediate [TailCall] instead of actual evaluation
 * @receiver called [TailFunction1]
 * @param p1 - invocation argument
 * @param P1 - function input type
 * @param R - function output type
 */
operator fun <P1, R> TailFunction1<in P1, out R>.get(p1: P1): TailCall<R> = IntermediateCall { this(p1) }

/**
 * tail call invocation of [TailFunction2]
 * creates next intermediate [TailCall] instead of actual evaluation
 * @receiver called [TailFunction2]
 * @param p1 - 1st invocation argument
 * @param p2 - 2nd invocation argument
 * @param P1 - 1st function input type
 * @param P2 - 2nd function input type
 * @param R - function output type
 */
operator fun <P1, P2, R> TailFunction2<in P1, in P2, out R>.get(p1: P1, p2: P2): TailCall<R> = IntermediateCall { this(p1, p2) }

/**
 * @receiver wrapped as final [TailCall]
 * @param R - function output type
 */
val <R> R.ret: TailCall<R>
    get() = object : TailCall<R> {
        override val fix: R = this@ret
    }

/**
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~ trampoline implementation ~~~~~~~~~~~~~~~~~~~~~~~~~~
 */

/**
 * implementation of intermediate [TailCall]
 */
private class IntermediateCall<out R>(private val next: () -> TailCall<R>) : TailCall<R> {
    override val fix: R
        get() {
            var step = next()
            while (step is IntermediateCall) step = step.next()
            return step.fix
        }
}