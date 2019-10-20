package control.monad.transaction

fun main() {
    val transaction = pureTransaction(1)
    val session = pureSession(2)
    transaction.join(session)
    transaction.join(session).join(transaction).join(session)
    session.join(session)
    transaction.map { it + 1 }
    transaction.flatMap { pureTransaction((it + 1)) }
    transaction.flatMapS { pureSession((it + 1)) }
    session.map { it * 2 }
    session.flatMap { pureSession((it * 2)) }
    pureTransaction(transaction).flat()
    pureTransaction(session).flatS()
    pureSession(pureSession(session)).flat().flat()
}