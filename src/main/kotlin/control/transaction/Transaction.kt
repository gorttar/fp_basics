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
    public operator fun <T> get(session: Session<T>): T = tryTransaction(
            SessionContextImpl(em),
            Handlers(
                    beforeTry = { println("Session before try") },
                    beforeBody = { println("Session before body") },
                    onSuccess = { println("Session on success") },
                    onError = { println("Session on error") },
                    cleanup = { println("Session cleanup") }
            ), session
    )

    public operator fun <T> invoke(transaction: Transaction<T>): T =
            tryTransaction(
                    TransactionContextImpl(em),
                    Handlers(
                            beforeTry = { println("Transaction before try") },
                            beforeBody = {
                                println("Transaction before body")
                                em.transaction.begin()
                            },
                            onSuccess = {
                                println("Transaction on success")
                                em.transaction.commit()
                            },
                            onError = {
                                println("Transaction on error")
                                em.transaction.rollback()
                            },
                            cleanup = { println("Transaction cleanup") }
                    ),
                    transaction
            )

    private fun <R, HC : HibernateContext> tryTransaction(
            hc: HC,
            handlers: Handlers = Handlers(),
            body: (HC) -> R
    ): R = if (hc.transaction.isActive) body(hc) else with(handlers) {
        beforeTry()
        try {
            beforeBody()
            val r = body(hc)
            onSuccess()
            r
        } catch (t: Throwable) {
            onError()
            throw t
        } finally {
            cleanup()
        }
    }

    private class Handlers(
            val beforeTry: () -> Unit = {},
            val beforeBody: () -> Unit = {},
            val onSuccess: () -> Unit = {},
            val onError: () -> Unit = {},
            val cleanup: () -> Unit = {}
    )
}

private class SessionContextImpl(em: EntityManager) : SessionContext(em)
private class TransactionContextImpl(em: EntityManager) : TransactionContext(em)