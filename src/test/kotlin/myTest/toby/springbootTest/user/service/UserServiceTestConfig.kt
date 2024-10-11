package myTest.toby.springbootTest.user.service

import myTest.toby.springbootTest.user.dao.UserDao
import myTest.toby.springbootTest.user.dao.UserDaoJdbc
import org.springframework.aop.framework.AopProxyUtils
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator
import org.springframework.aop.support.DefaultPointcutAdvisor
import org.springframework.aop.support.NameMatchMethodPointcut
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Bean
import org.springframework.jdbc.datasource.DataSourceTransactionManager
import org.springframework.jdbc.datasource.SimpleDriverDataSource
import org.springframework.mail.MailSender
import javax.sql.DataSource

@TestConfiguration
class UserServiceTestConfig {
//    @Bean
//    fun dataSource(): DataSource {
//        return SimpleDriverDataSource().apply {
//            setDriverClass(com.mysql.cj.jdbc.Driver::class.java)
//            url = "jdbc:mysql://localhost/springbook?characterEncoding=UTF-8"
//            username = "root"
//            password = "123456"
//        }
//    }
//
//    @Bean
//    fun transactionManager(): DataSourceTransactionManager {
//        return DataSourceTransactionManager(dataSource())
//    }
//
//    @Bean
//    fun transactionAdvice(transactionManager: DataSourceTransactionManager): TransactionAdvice {
//        return TransactionAdvice(transactionManager)
//    }
//
//    @Bean
//    fun transactionPointcut(): NameMatchClassMethodPointcut {
//        return NameMatchClassMethodPointcut().apply {
//            setMappedClassName("*ServiceImpl")
//            setMappedName("upgrade*")
//        }
//    }
//
//    @Bean
//    fun transactionAdvisor(
//        transactionAdvice: TransactionAdvice,
//        transactionPointcut: NameMatchMethodPointcut
//    ): DefaultPointcutAdvisor {
//        return DefaultPointcutAdvisor(transactionPointcut, transactionAdvice)
//    }
//
//    @Bean
//    fun userService(
//        userDao: UserDao,
//        mailSender: DummyMailSender,
//    ): UserServiceImpl {
//        return UserServiceImpl().apply {
//            setUserDao(userDao)
//            setMailSender(mailSender)
//        }
//    }
//
//    @Bean
//    fun userDao(dataSource: DataSource): UserDaoJdbc {
//        return UserDaoJdbc().apply {
//            setDataSource(dataSource)
//        }
//    }
//
//    @Bean
//    fun mailSender(): DummyMailSender {
//        return DummyMailSender()
//    }
//
//    @Bean
//    fun defaultAdvisorAutoProxyCreator(): DefaultAdvisorAutoProxyCreator {
//        return DefaultAdvisorAutoProxyCreator()
//    }

    @Bean
    fun testUserService(
        mailSender: MailSender,
        userDao: UserDao,
    ): UserServiceTest.TestUserServiceImpl {
        return UserServiceTest.TestUserServiceImpl().apply {
            this.setUserDao(userDao)
            this.setMailSender(mailSender)
        }
    }
}