package control.monad.transaction

import control.monad.transaction.AbstractTransaction.OperationsImpl.flatMap
import control.monad.transaction.AbstractTransaction.OperationsImpl.flatMapS
import control.monad.transaction.AbstractTransaction.SessionEntityManager
import control.monad.transaction.AbstractTransaction.SessionEntityManager.TransactionEntityManager
import javax.persistence.EntityManager

public fun <T> transaction(body: (em: TransactionEntityManager) -> T): Transaction<T> = TransactionImpl(body)
public fun <T> session(body: (em: SessionEntityManager) -> T): Session<T> = SessionImpl(body)

public fun <T> pureTransaction(t: T): Transaction<T> = transaction { t }
public fun <T> pureSession(t: T): Session<T> = session { t }

public fun <T> Transaction<*>.join(next: Transaction<T>): Transaction<T> = flatMap { next }
public fun <T> Transaction<*>.join(next: Session<T>): Transaction<T> = flatMapS { next }
public fun <T> Session<*>.join(next: Session<T>): Session<T> = flatMap { next }

public fun <T, T2> Transaction<T>.map(mapper: (T) -> T2): Transaction<T2> = flatMap { pureTransaction(mapper(it)) }
public fun <T, T2> Session<T>.map(mapper: (T) -> T2): Session<T2> = flatMap { pureSession(mapper(it)) }

public fun <T> Transaction<Transaction<T>>.flat(): Transaction<T> = flatMap { it }
public fun <T> Transaction<Session<T>>.flatS(): Transaction<T> = flatMapS { it }
public fun <T> Session<Session<T>>.flat(): Session<T> = flatMap { it }

public fun <T, T2> Transaction<T>.flatMap(mapper: (T) -> Transaction<T2>): Transaction<T2> = flatMap(mapper)
public fun <T, T2> Transaction<T>.flatMapS(mapper: (T) -> Session<T2>): Transaction<T2> = flatMapS(mapper)
public fun <T, T2> Session<T>.flatMap(mapper: (T) -> Session<T2>): Session<T2> = flatMap(mapper)

public typealias TransactionBody<T> = (em: TransactionEntityManager) -> T
public typealias SessionBody<T> = (em: SessionEntityManager) -> T

public sealed class Transaction<T>(body: TransactionBody<T>) : AbstractTransaction<T, TransactionEntityManager>(body)
public sealed class Session<T>(body: SessionBody<T>) : AbstractTransaction<T, SessionEntityManager>(body)
public sealed class AbstractTransaction<out T, in EM : SessionEntityManager>(protected val body: (em: EM) -> T) {

    public sealed class SessionEntityManager(em: EntityManager) : EntityManager by em {
        public operator fun <T> get(session: Session<T>): T = session.body(this)

        public sealed class TransactionEntityManager(em: EntityManager) : SessionEntityManager(em) {

            public class Transactor(private val em: EntityManager) {
                public operator fun <T> get(session: Session<T>): T =
                        SessionEntityManagerImpl(em)[session]

                public operator fun <T> get(transaction: Transaction<T>): T =
                        TransactionEntityManagerImpl(em)[transaction]
            }

            public operator fun <T> get(transaction: Transaction<T>): T = transaction.body(this)

            private class TransactionEntityManagerImpl(em: EntityManager) : TransactionEntityManager(em)
        }

        private class SessionEntityManagerImpl(em: EntityManager) : SessionEntityManager(em)
    }

    internal object OperationsImpl {
        internal fun <T, T2> Session<T>.flatMap(mapper: (T) -> Session<T2>) = session(body.join(mapper))
        internal fun <T, T2> Transaction<T>.flatMap(mapper: (T) -> Transaction<T2>) = transaction(body.join(mapper))
        internal fun <T, T2> Transaction<T>.flatMapS(mapper: (T) -> Session<T2>) = transaction(body.join(mapper))

        private inline fun <T, T2, EM : SessionEntityManager, TX : AbstractTransaction<T2, EM>> ((EM) -> T).join(
                crossinline mapper: (T) -> TX
        ) = { em: EM -> mapper(invoke(em)).body(em) }
    }
}

private class TransactionImpl<T>(body: TransactionBody<T>) : Transaction<T>(body)
private class SessionImpl<T>(body: SessionBody<T>) : Session<T>(body)