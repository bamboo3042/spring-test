package myTest.toby.springbootTest.user.service

import org.springframework.stereotype.Component
import org.springframework.transaction.PlatformTransactionManager
import org.springframework.transaction.support.DefaultTransactionDefinition
import java.lang.reflect.InvocationHandler
import java.lang.reflect.InvocationTargetException
import java.lang.reflect.Method

class TransactionHandler : InvocationHandler {
    var target: Any? = null
    lateinit var transactionManager: PlatformTransactionManager
    var pattern: String? = null

    @Throws(Throwable::class)
    override fun invoke(proxy: Any, method: Method, args: Array<Any>): Any {
        return if (method.name.startsWith(pattern!!)) {
            invokeInTransaction(method, args)
        } else {
            method.invoke(target, *args)
        }
    }

    @Throws(Throwable::class)
    private fun invokeInTransaction(method: Method, args: Array<Any>): Any {
        val status = transactionManager
            .getTransaction(DefaultTransactionDefinition())
        try {
            val ret = method.invoke(target, *args)
            transactionManager.commit(status)
            return ret
        } catch (e: InvocationTargetException) {
            transactionManager.rollback(status)
            throw e.targetException
        }
    }
}