package tco

/**
 * tco/TailCallTrampolineTest.kt
 */
import org.testng.Assert.assertEquals
import org.testng.annotations.DataProvider
import org.testng.annotations.Test
import java.math.BigInteger
import java.math.BigInteger.ONE

typealias Fib1Arguments = Triple<BigInteger, BigInteger, Int>
typealias Fib2Accumulators = Pair<BigInteger, BigInteger>

class TailCallTrampolineTest {
    @Test
    fun `fibonacci numbers should be calculated correctly by fib1`() {
        fun fibonacci(n: Int): BigInteger = fib1(Triple(ONE, ONE, n)).fix

        assertEquals(
                (1..10).map(::fibonacci),
                listOf(1, 1, 2, 3, 5, 8, 13, 21, 34, 55).map(Int::toBigInteger)
        )
        println(fibonacci(10000))
    }

    @Test
    fun `fibonacci numbers should be calculated correctly by fib2`() {
        fun fibonacci(n: Int): BigInteger = fib2(ONE to ONE, n).fix

        assertEquals(
                (1..10).map(::fibonacci),
                listOf(1, 1, 2, 3, 5, 8, 13, 21, 34, 55).map(Int::toBigInteger)
        )
        println(fibonacci(10000))
    }

    @DataProvider
    fun `data for even tests`() = arrayOf(
            arrayOf(10, true),
            arrayOf(11, false),
            arrayOf(10_000_000, true),
            arrayOf(10_000_001, false)
    )

    @Test(dataProvider = "data for even tests")
    fun `even should produce correct results`(n: Int, expected: Boolean) = assertEquals(even(n).fix, expected)

    @Test(dataProvider = "data for even tests")
    fun `evenF should produce correct results`(n: Int, expected: Boolean) = assertEquals(evenF(n).fix, expected)

    @DataProvider
    fun `data for odd tests`() = arrayOf(
            arrayOf(10, false),
            arrayOf(11, true),
            arrayOf(10_000_000, false),
            arrayOf(10_000_001, true)
    )

    @Test(dataProvider = "data for odd tests")
    fun `odd should produce correct results`(n: Int, expected: Boolean) = assertEquals(odd(n).fix, expected)

    @Test(dataProvider = "data for odd tests")
    fun `oddF should produce correct results`(n: Int, expected: Boolean) = assertEquals(oddF(n).fix, expected)

    companion object {
        private val fib1: TailFunction1<Fib1Arguments, BigInteger> by lazy {
            { (nextAccumulator, accumulator, n): Fib1Arguments ->
                if (n == 1) accumulator.ret1
                else fib1[Triple(nextAccumulator + accumulator, nextAccumulator, n - 1)]
            }
        }

        private val fib2: TailFunction2<Fib2Accumulators, Int, BigInteger> by lazy {
            { (nextAccumulator, accumulator): Fib2Accumulators, n: Int ->
                if (n == 1) accumulator.ret2
                else fib2[nextAccumulator + accumulator to nextAccumulator, n - 1]
            }
        }

        private val even: TailFunction1<Int, Boolean> = { n -> if (n == 0) true.ret1 else odd[n - 1] }
        private val odd: TailFunction1<Int, Boolean> = { n -> if (n == 0) false.ret1 else even[n - 1] }

        private fun evenF(n: Int): TailCall1<Int, Boolean> = if (n == 0) true.ret1 else ::oddF[n - 1]
        private fun oddF(n: Int): TailCall1<Int, Boolean> = if (n == 0) false.ret1 else ::evenF[n - 1]
    }
}

