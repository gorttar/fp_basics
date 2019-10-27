package control.transaction

import javax.persistence.EntityManager

public sealed class HibernateContext(em: EntityManager) : EntityManager by em
public sealed class SessionContext(em: EntityManager) : HibernateContext(em) {
    public operator fun <T> get(session: Session<T>): T = session(this)
}

public sealed class TransactionContext(em: EntityManager) : HibernateContext(em) {
    private val sessionContext = lazy { SessionContextImpl(em) }
    public operator fun <T> get(session: Session<T>): T = session(sessionContext.value)
    public operator fun <T> invoke(transaction: Transaction<T>): T = transaction(this)
}

public typealias Session<T> = (SessionContext) -> T
public typealias Transaction<T> = (TransactionContext) -> T

public class Transactor(private val em: EntityManager) {
    public operator fun <T> get(session: Session<T>): T {
        /** some session initialization code */
        return try {
            /** some session initialization code */
            val result = SessionContextImpl(em)[session]
            /** some result post processing code */
            result
        } catch (t: Throwable) {
            /** some session exception handler*/
            throw t
        } finally {
            /** some session cleanup code */
        }
    }

    public operator fun <T> invoke(transaction: Transaction<T>): T {
        /** some transaction initialization code */
        return try {
            /** some transaction initialization code */
            val result = TransactionContextImpl(em)(transaction)
            /** some result post processing code */
            result
        } catch (t: Throwable) {
            /** some transaction exception handler*/
            throw t
        } finally {
            /** some transaction cleanup code */
        }
    }
}

private class SessionContextImpl(em: EntityManager) : SessionContext(em)
private class TransactionContextImpl(em: EntityManager) : TransactionContext(em)