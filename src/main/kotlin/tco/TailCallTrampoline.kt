/**
 * tco/TailCallTrampoline.kt
 */
package tco

/**
 * optimizable call of function
 * @param R - function output type
 */
interface Call<out R> {
    /**
     * raw result of tail call optimizable function
     * access will lead to function evaluation
     */
    val fix: R
}

/**
 * specialized type of tail call optimizable 0 argument functions
 * @param R - function output type
 */
typealias TF0<R> = () -> Call<R>

/**
 * specialized type of tail call optimizable 1 argument functions
 * @param P1 - function input type
 */
typealias TF1<P1, R> = (P1) -> Call<R>

/**
 * specialized type of tail call optimizable 2 arguments functions
 * @param P1 - 1st function input type
 * @param P2 - 2nd function input type
 */
typealias TF2<P1, P2, R> = (P1, P2) -> Call<R>

/**
 * specialized type of tail call optimizable 3 arguments functions
 * @param P1 - 1st function input type
 * @param P2 - 2nd function input type
 * @param P3 - 3rd function input type
 */
typealias TF3<P1, P2, P3, R> = (P1, P2, P3) -> Call<R>

/**
 * tail call invocation of [TF0]
 * creates next intermediate [Call] instead of actual evaluation
 * @receiver called [TF0]
 */
fun <R> TF0<out R>.call(): Call<R> = Step { this() }

/**
 * tail call invocation of [TF1]
 * creates next intermediate [Call] instead of actual evaluation
 * @receiver called [TF1]
 * @param p1 - invocation argument
 */
operator fun <P1, R> TF1<in P1, out R>.get(p1: P1): Call<R> = Step { this(p1) }

/**
 * tail call invocation of [TF2]
 * creates next intermediate [Call] instead of actual evaluation
 * @receiver called [TF2]
 * @param p1 - 1st invocation argument
 * @param p2 - 2nd invocation argument
 */
operator fun <P1, P2, R> TF2<in P1, in P2, out R>.get(p1: P1, p2: P2): Call<R> = Step { this(p1, p2) }

/**
 * tail call invocation of [TF3]
 * creates next intermediate [Call] instead of actual evaluation
 * @receiver called [TF3]
 * @param p1 - 1st invocation argument
 * @param p2 - 2nd invocation argument
 * @param p3 - 3rd invocation argument
 */
operator fun <P1, P2, P3, R> TF3<in P1, in P2, in P3, out R>.get(p1: P1, p2: P2, p3: P3): Call<R> = Step { this(p1, p2, p3) }

/**
 * @receiver wrapped as final [Call]
 */
val <R> R.ret: Call<R>
    get() = object : Call<R> {
        override val fix: R = this@ret
    }

/**
 * implementation of intermediate [Call]
 */
private class Step<out R>(private val next: TF0<R>) : Call<R> {
    override val fix: R
        get() {
            var step = next()
            while (step is Step) step = step.next()
            return step.fix
        }
}