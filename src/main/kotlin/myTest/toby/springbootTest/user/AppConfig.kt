package myTest.toby.springbootTest.user

import myTest.toby.springbootTest.user.dao.UserDao
import myTest.toby.springbootTest.user.dao.UserDaoJdbc
import myTest.toby.springbootTest.user.service.DummyMailSender
import myTest.toby.springbootTest.user.service.NameMatchClassMethodPointcut
import myTest.toby.springbootTest.user.service.TransactionAdvice
import myTest.toby.springbootTest.user.service.UserServiceImpl
import org.springframework.aop.Pointcut
import org.springframework.aop.PointcutAdvisor
import org.springframework.aop.aspectj.AspectJExpressionPointcut
import org.springframework.aop.framework.ProxyFactoryBean
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator
import org.springframework.aop.support.DefaultPointcutAdvisor
import org.springframework.aop.support.NameMatchMethodPointcut
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.jdbc.datasource.DataSourceTransactionManager
import org.springframework.jdbc.datasource.SimpleDriverDataSource
import javax.sql.DataSource

@Configuration
class AppConfig {

    @Bean
    fun dataSource(): DataSource {
        return SimpleDriverDataSource().apply {
            setDriverClass(com.mysql.cj.jdbc.Driver::class.java)
            url = "jdbc:mysql://localhost/springbook?characterEncoding=UTF-8"
            username = "root"
            password = "123456"
        }
    }

    @Bean
    fun transactionManager(): DataSourceTransactionManager {
        return DataSourceTransactionManager(dataSource())
    }

    @Bean
    fun transactionPointcut(): AspectJExpressionPointcut {
        return AspectJExpressionPointcut().apply {
            this.expression = "execution(* *..*ServiceImpl.upgrade*(..))"
        }
    }

    @Bean
    fun transactionAdvice(transactionManager: DataSourceTransactionManager): TransactionAdvice {
        return TransactionAdvice(transactionManager)
    }

    @Bean
    fun transactionAdvisor(
        transactionAdvice: TransactionAdvice,
        transactionPointcut: Pointcut
    ): DefaultPointcutAdvisor {
        return DefaultPointcutAdvisor(transactionPointcut, transactionAdvice)
    }

    @Bean
    fun userService(
        userDao: UserDao,
        mailSender: DummyMailSender,
    ): UserServiceImpl {
        return UserServiceImpl().apply {
            setUserDao(userDao)
            setMailSender(mailSender)
        }
    }

    @Bean
    fun userDao(dataSource: DataSource): UserDaoJdbc {
        return UserDaoJdbc().apply {
            setDataSource(dataSource)
        }
    }

    @Bean
    fun mailSender(): DummyMailSender {
        return DummyMailSender()
    }

    @Bean
    fun defaultAdvisorAutoProxyCreator(): DefaultAdvisorAutoProxyCreator {
        return DefaultAdvisorAutoProxyCreator()
    }
}