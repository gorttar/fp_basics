package tco

/**
 * tco/TailCallTrampolineTest.kt
 */
import org.testng.Assert.assertEquals
import org.testng.annotations.DataProvider
import org.testng.annotations.Test
import java.math.BigInteger
import java.math.BigInteger.ONE

typealias FibArgs = Triple<BigInteger, BigInteger, Int>

class TailCallTrampolineTest {
    @Test
    fun `fibonacci numbers should be calculated correctly`() {
        fun fibonacci(n: Int): BigInteger = fib(Triple(ONE, ONE, n)).fix

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
        private val fib: TailFunction<FibArgs, BigInteger> by lazy {
            { args: FibArgs ->
                val (nextAcc, acc, n) = args
                if (n == 1) acc.ret else fib[Triple(nextAcc + acc, nextAcc, n - 1)]
            }
        }

        private val even: TailFunction<Int, Boolean> = { n -> if (n == 0) true.ret else odd[n - 1] }
        private val odd: TailFunction<Int, Boolean> = { n -> if (n == 0) false.ret else even[n - 1] }

        private fun evenF(n: Int): TailCall<Int, Boolean> = if (n == 0) true.ret else ::oddF[n - 1]
        private fun oddF(n: Int): TailCall<Int, Boolean> = if (n == 0) false.ret else ::evenF[n - 1]
    }
}

