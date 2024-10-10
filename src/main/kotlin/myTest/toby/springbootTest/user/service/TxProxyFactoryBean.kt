package myTest.toby.springbootTest.user.service

import org.springframework.beans.factory.FactoryBean
import org.springframework.transaction.PlatformTransactionManager
import java.lang.reflect.Proxy

class TxProxyFactoryBean : FactoryBean<Any> {
    var target: Any? = null
    var transactionManager: PlatformTransactionManager? = null
    var pattern: String? = null
    var serviceInterface: Class<*>? = null

    // FactoryBean �������̽� ���� �޼ҵ�
    @Throws(Exception::class)
    override fun getObject(): Any {
        val txHandler = TransactionHandler()
        txHandler.target = (target)
        txHandler.transactionManager = (transactionManager!!)
        txHandler.pattern = (pattern)
        return Proxy.newProxyInstance(
            javaClass.classLoader, arrayOf(serviceInterface), txHandler
        )
    }

    override fun getObjectType(): Class<*>? {
        return serviceInterface
    }

    override fun isSingleton(): Boolean {
        return false
    }
}