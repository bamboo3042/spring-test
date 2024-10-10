package myTest.toby.springbootTest.user.service

import org.aopalliance.intercept.MethodInterceptor
import org.aopalliance.intercept.MethodInvocation
import org.springframework.stereotype.Component
import org.springframework.transaction.PlatformTransactionManager
import org.springframework.transaction.TransactionStatus
import org.springframework.transaction.support.DefaultTransactionDefinition

class TransactionAdvice(
    private val transactionManager: PlatformTransactionManager
) : MethodInterceptor {
    @Throws(Throwable::class)
    override fun invoke(invocation: MethodInvocation): Any? {
        val status: TransactionStatus = transactionManager.getTransaction(DefaultTransactionDefinition())
        try {
            val ret = invocation.proceed()
            transactionManager.commit(status)
            return ret
        } catch (e: RuntimeException) {
            transactionManager.rollback(status)
            throw e
        }
    }
}