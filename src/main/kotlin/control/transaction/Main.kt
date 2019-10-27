package control.transaction

import java.lang.reflect.Proxy.newProxyInstance
import javax.persistence.EntityManager

fun main() {
    println(transactor[fooSession(3, 4)])
    println(transactor(barTransaction(42)))
    println(transactor[{
        val fooSessionResult = it[fooSession(3, 4)]
        it[fooSession(fooSessionResult, 5)]
    }])
    println(transactor {
        val fooSessionResult = it[fooSession(3, 4)]
        val barTransactionResult = it(barTransaction(fooSessionResult))
        it(barTransaction(barTransactionResult))
    })
}

private fun fooSession(x: Int, y: Int): Session<Int> = { x * y }

private fun barTransaction(x: Any): Transaction<String> = { "${x}bar$x" }

private val transactor = Transactor(
        newProxyInstance(
                EntityManager::class.java.classLoader,
                arrayOf(EntityManager::class.java)
        ) { _, _, _ -> error("!!!BOOM!!!") } as EntityManager
)