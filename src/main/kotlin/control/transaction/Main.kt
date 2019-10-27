package control.transaction

import java.lang.reflect.Proxy.newProxyInstance
import javax.persistence.EntityManager
import javax.persistence.EntityTransaction

fun main() {
    println(
            """transactor[fooSession(3, 4)] = ${transactor[fooSession(3, 4)]}
                
            """.trimIndent()
    )
    println(
            """transactor(barTransaction(42)) = "${transactor(barTransaction(42))}"
                
            """.trimIndent())
    println(
            """transactor[{ it[fooSession(it[fooSession(3, 4)], 5)] }] = ${
            transactor[{ it[fooSession(it[fooSession(3, 4)], 5)] }]
            }
            
            """.trimIndent()
    )
    println(
            """transactor { it(barTransaction(it(barTransaction(it[fooSession(3, 4)])))) } = "${
            transactor { it(barTransaction(it(barTransaction(it[fooSession(3, 4)])))) }
            }"""".trimIndent()
    )
}

private fun fooSession(x: Int, y: Int): Session<Int> = { x * y }

private fun barTransaction(x: Any): Transaction<String> = { "${x}bar$x" }

private val transactor = Transactor(
        object : EntityManager by explodingT() {
            private val tx = object : EntityTransaction by explodingT() {
                private var isActive = false
                override fun isActive(): Boolean = isActive
                override fun begin() = if (isActive) error("Transaction is already active") else isActive = true
                override fun commit() = if (isActive) isActive = false else error("Transaction is not active")
                override fun rollback() = commit()
            }

            override fun getTransaction(): EntityTransaction = tx
        }
)

private inline fun <reified T> explodingT(): T = newProxyInstance(
        T::class.java.classLoader,
        arrayOf(T::class.java)
) { _, _, _ -> error("!!!BOOM!!!") } as T