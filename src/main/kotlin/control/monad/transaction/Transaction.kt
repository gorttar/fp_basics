package control.monad.transaction

import control.monad.transaction.AbstractTransaction.OperationsImpl.flatMap
import control.monad.transaction.AbstractTransaction.OperationsImpl.flatMapS
import control.monad.transaction.AbstractTransaction.SessionEntityManager
import control.monad.transaction.AbstractTransaction.SessionEntityManager.TransactionEntityManager
import javax.persistence.EntityManager

/** creates [Transaction] from given [body] */
public fun <T> transaction(body: TransactionBody<T>): Transaction<T> = TransactionImpl(body)

/** creates [Session] from given [body] */
public fun <T> session(body: (em: SessionEntityManager) -> T): Session<T> = SessionImpl(body)

/** wraps [t] to [Transaction] */
public fun <T> pureTransaction(t: T): Transaction<T> = transaction { t }

/** wraps [t] to [Session] */
public fun <T> pureSession(t: T): Session<T> = session { t }

/** creates [Transaction] by appending [next] to [this] and discarding result of [this] */
public fun <T> Transaction<*>.join(next: Transaction<T>): Transaction<T> = flatMap { next }

/** creates [Transaction] by appending [next] to [this] and discarding result of [this] */
public fun <T> Transaction<*>.join(next: Session<T>): Transaction<T> = flatMapS { next }

/** creates [Session] by appending [next] to [this] and discarding result of [this] */
public fun <T> Session<*>.join(next: Session<T>): Session<T> = flatMap { next }

/** creates [Transaction] by applying [mapper] to result of [this] */
public fun <T, T2> Transaction<T>.map(mapper: (T) -> T2): Transaction<T2> = flatMap { pureTransaction(mapper(it)) }

/** creates [Session] by applying [mapper] to result of [this] */
public fun <T, T2> Session<T>.map(mapper: (T) -> T2): Session<T2> = flatMap { pureSession(mapper(it)) }

/** flattens nested [Transaction] */
public fun <T> Transaction<Transaction<T>>.flatten(): Transaction<T> = flatMap { it }

/** flattens nested [Transaction] */
public fun <T> Transaction<Session<T>>.flattenS(): Transaction<T> = flatMapS { it }

/** flattens nested [Session] */
public fun <T> Session<Session<T>>.flatten(): Session<T> = flatMap { it }

/** creates [Transaction] by applying [mapper] to result of [this] than flattening */
public fun <T, T2> Transaction<T>.flatMap(mapper: (T) -> Transaction<T2>): Transaction<T2> = flatMap(mapper)

/** creates [Transaction] by applying [mapper] to result of [this] than flattening */
public fun <T, T2> Transaction<T>.flatMapS(mapper: (T) -> Session<T2>): Transaction<T2> = flatMapS(mapper)

/** creates [Session] by applying [mapper] to result of [this] than flattening */
public fun <T, T2> Session<T>.flatMap(mapper: (T) -> Session<T2>): Session<T2> = flatMap(mapper)

/** transaction body evaluated to value of type [T] */
public typealias TransactionBody<T> = (em: TransactionEntityManager) -> T

/** session body evaluated to value of type [T] */
public typealias SessionBody<T> = (em: SessionEntityManager) -> T

/**
 * represents deferred transaction which value of type [T] can be obtained from
 * [TransactionEntityManager.Transactor.get] or [TransactionEntityManager.get] only
 */
public sealed class Transaction<T>(body: TransactionBody<T>) : AbstractTransaction<T, TransactionEntityManager>(body)

/**
 * represents deferred session which value of type [T] can be obtained from
 * [TransactionEntityManager.Transactor.get] or [SessionEntityManager.get] only
 */
public sealed class Session<T>(body: SessionBody<T>) : AbstractTransaction<T, SessionEntityManager>(body)

/** abstract deferred transaction/session evaluation */
public sealed class AbstractTransaction<out T, in EM : SessionEntityManager>(protected val body: (em: EM) -> T) {

    /** [EntityManager] with ability to [get] value of [Session] */
    public sealed class SessionEntityManager(em: EntityManager) : EntityManager by em {
        /** evaluates [session] result */
        public operator fun <T> get(session: Session<T>): T = session.body(this)

        /** [SessionEntityManager] with ability to [get] value of [Transaction] */
        public sealed class TransactionEntityManager(em: EntityManager) : SessionEntityManager(em) {

            /** transaction manager with ability to [get] value of [Session] or [Transaction] */
            public class Transactor(private val em: EntityManager) {
                /** evaluates [session] result */
                public operator fun <T> get(session: Session<T>): T =
                        SessionEntityManagerImpl(em)[session]

                /** evaluates [transaction] result */
                public operator fun <T> get(transaction: Transaction<T>): T =
                        TransactionEntityManagerImpl(em)[transaction]
            }

            /** evaluates [transaction] result */
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