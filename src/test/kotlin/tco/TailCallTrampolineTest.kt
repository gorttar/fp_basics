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
    fun `fibonacci numbers should be calculated correctly by fib0`() {
        fun fibonacci(x: Int): BigInteger {
            nextAccumulator = ONE
            accumulator = ONE
            n = x.toBigInteger()
            return fib0().fix
        }

        assertEquals(
                (1..10).map(::fibonacci),
                listOf(1, 1, 2, 3, 5, 8, 13, 21, 34, 55).map(Int::toBigInteger)
        )
        println(fibonacci(10000))
    }

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

    @Test
    fun `fibonacci numbers should be calculated correctly by fib3`() {
        fun fibonacci(n: Int): BigInteger = fib3(ONE, ONE, n).fix

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
        private lateinit var nextAccumulator: BigInteger
        private lateinit var accumulator: BigInteger
        private lateinit var n: BigInteger

        private val fib0: TF0<BigInteger> = ::fib0

        private fun fib0(): Call<BigInteger> =
                if (n == ONE) accumulator.ret
                else {
                    val tmp = nextAccumulator
                    nextAccumulator += accumulator
                    accumulator = tmp
                    n--
                    fib0.call()
                }

        private fun fib1(args: Fib1Arguments): Call<BigInteger> =
                if (args.third == 1) args.second.ret
                else {
                    val (nextAccumulator, accumulator, n) = args
                    ::fib1[Triple(nextAccumulator + accumulator, nextAccumulator, n - 1)]
                }

        private fun fib2(accumulators: Fib2Accumulators, n: Int): Call<BigInteger> =
                if (n == 1) accumulators.second.ret
                else {
                    val (nextAccumulator, accumulator) = accumulators
                    ::fib2[nextAccumulator + accumulator to nextAccumulator, n - 1]
                }

        private fun fib3(nextAccumulator: BigInteger, accumulator: BigInteger, n: Int): Call<BigInteger> =
                if (n == 1) accumulator.ret
                else ::fib3[nextAccumulator + accumulator, nextAccumulator, n - 1]

        private val even: TF1<Int, Boolean> = { n -> if (n == 0) true.ret else odd[n - 1] }
        private val odd: TF1<Int, Boolean> = { n -> if (n == 0) false.ret else even[n - 1] }

        private fun evenF(n: Int): Call<Boolean> = if (n == 0) true.ret else ::oddF[n - 1]
        private fun oddF(n: Int): Call<Boolean> = if (n == 0) false.ret else ::evenF[n - 1]
    }
}

