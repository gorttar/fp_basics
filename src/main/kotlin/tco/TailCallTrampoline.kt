/**
 * tco/TailCallTrampoline.kt
 */
package tco

/**
 * specialized type of tail call optimizable 1 argument functions
 * @param P1 - function input type
 * @param R - function output type
 */
typealias TailFunction1<P1, R> = (P1) -> TailCall1<P1, R>

/**
 * optimizable tail call of [TailFunction1]
 * @param P1 - function input type
 * @param R - function output type
 */
interface TailCall1<in P1, out R> {
    /**
     * raw result of [TailFunction1]
     * access will lead to [TailFunction1] evaluation
     */
    val fix: R
}

/**
 * implementation of intermediate [TailFunction1] call
 * @param nextStep - [TailFunction1] representing next evaluation step
 * @param p1 - input for next step
 * @param P1 - function input type
 * @param R - function output type
 */
private class IntermediateCall1<in P1, out R>(private val nextStep: TailFunction1<P1, R>, private val p1: P1) : TailCall1<P1, R> {
    override val fix: R
        get() {
            var step = next
            while (step is IntermediateCall1) step = step.next
            return step.fix
        }

    private val next: TailCall1<P1, R> get() = nextStep(p1)
}

/**
 * tail call invocation of [TailFunction1]
 * creates next [IntermediateCall1] instead of actual evaluation
 * @receiver called [TailFunction1]
 * @param p1 - invocation argument
 * @param P1 - function input type
 * @param R - function output type
 */
operator fun <P1, R> TailFunction1<in P1, out R>.get(p1: P1): TailCall1<P1, R> = IntermediateCall1(this, p1)

/**
 * @receiver wrapped as final [TailFunction1] call
 * @param R - function output type
 */
val <R> R.ret1: TailCall1<Any?, R>
    get() = object : TailCall1<Any?, R> {
        override val fix: R = this@ret1
    }

/**
 * specialized type of tail call optimizable 2 arguments functions
 * @param P1 - 1st function input type
 * @param P2 - 2nd function input type
 * @param R - function output type
 */
typealias TailFunction2<P1, P2, R> = (P1, P2) -> TailCall2<P1, P2, R>

/**
 * optimizable tail call of [TailFunction2]
 * @param P1 - 1st function input type
 * @param P2 - 2nd function input type
 * @param R - function output type
 */
interface TailCall2<in P1, in P2, out R> {
    val fix: R
}

/**
 * implementation of intermediate [TailFunction2] call
 * @param nextStep - [TailFunction2] representing next evaluation step
 * @param p1 - 1st input for next step
 * @param p2 - 2nd input for next step
 * @param P1 - 1st function input type
 * @param P2 - 2nd function input type
 * @param R - function output type
 */
private class IntermediateCall2<in P1, in P2, out R>(
        private val nextStep: TailFunction2<P1, P2, R>,
        private val p1: P1,
        private val p2: P2
) : TailCall2<P1, P2, R> {
    override val fix: R
        get() {
            var step = next
            while (step is IntermediateCall2) step = step.next
            return step.fix
        }

    private val next: TailCall2<P1, P2, R> get() = nextStep(p1, p2)
}

/**
 * tail call invocation of [TailFunction2]
 * creates next [IntermediateCall2] instead of actual evaluation
 * @receiver called [TailFunction2]
 * @param p1 - 1st invocation argument
 * @param p2 - 2nd invocation argument
 * @param P1 - 1st function input type
 * @param P2 - 2nd function input type
 * @param R - function output type
 */
operator fun <P1, P2, R> TailFunction2<in P1, in P2, out R>.get(p1: P1, p2: P2): TailCall2<P1, P2, R> = IntermediateCall2(this, p1, p2)

/**
 * @receiver wrapped as final [TailFunction2] call
 * @param R - function output type
 */
val <R> R.ret2: TailCall2<Any?, Any?, R>
    get() = object : TailCall2<Any?, Any?, R> {
        override val fix: R = this@ret2
    }