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
operator fun <P1, R> TailFunction1<in P1, out R>.get(p1: P1): TailCall<R> = IntermediateCall1(this, p1)

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
operator fun <P1, P2, R> TailFunction2<in P1, in P2, out R>.get(p1: P1, p2: P2): TailCall<R> = IntermediateCall2(this, p1, p2)

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
 * abstract implementation of intermediate [TailCall]
 */
private abstract class IntermediateCall<out R> : TailCall<R> {
    protected abstract val next: TailCall<R>
    override val fix: R
        get() {
            var step = next
            while (step is IntermediateCall) step = step.next
            return step.fix
        }
}

/**
 * implementation of intermediate [TailFunction1] [TailCall]
 * @param nextStep - [TailFunction1] representing next evaluation step
 * @param p1 - input for next step
 */
private class IntermediateCall1<in P1, out R>(private val nextStep: TailFunction1<P1, R>,
                                              private val p1: P1)
    : IntermediateCall<R>() {
    override val next get() = nextStep(p1)
}

/**
 * implementation of intermediate [TailFunction2] [TailCall]
 * @param nextStep - [TailFunction2] representing next evaluation step
 * @param p1 - 1st input for next step
 * @param p2 - 2nd input for next step
 */
private class IntermediateCall2<in P1, in P2, out R>(private val nextStep: TailFunction2<P1, P2, R>,
                                                     private val p1: P1,
                                                     private val p2: P2)
    : IntermediateCall<R>() {
    override val next get() = nextStep(p1, p2)
}