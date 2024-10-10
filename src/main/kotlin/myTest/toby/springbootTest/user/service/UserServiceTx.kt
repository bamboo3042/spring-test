package myTest.toby.springbootTest.user.service

import myTest.toby.springbootTest.user.domain.User
import org.springframework.transaction.PlatformTransactionManager
import org.springframework.transaction.support.DefaultTransactionDefinition

class UserServiceTx : UserService {
    lateinit var userService: UserService
    lateinit var transactionManager: PlatformTransactionManager

    override fun add(user: User?) {
        userService.add(user)
    }

    override fun upgradeLevels() {
        val status = transactionManager.getTransaction(DefaultTransactionDefinition())
        try {
            userService.upgradeLevels()
            transactionManager.commit(status)
        } catch (e: RuntimeException) {
            transactionManager.rollback(status)
            throw e
        }
    }
}