package myTest.toby.springbootTest.user.service

import myTest.toby.springbootTest.user.dao.UserDao
import myTest.toby.springbootTest.user.dao.UserDaoJdbc
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Bean
import org.springframework.jdbc.datasource.SimpleDriverDataSource
import org.springframework.jdbc.datasource.DataSourceTransactionManager
import org.springframework.aop.framework.ProxyFactoryBean
import org.springframework.aop.support.NameMatchMethodPointcut
import org.springframework.aop.support.DefaultPointcutAdvisor
import javax.sql.DataSource

@Configuration
class UserConfig {

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
    fun transactionAdvice(transactionManager: DataSourceTransactionManager): TransactionAdvice {
        return TransactionAdvice(transactionManager)
    }

    @Bean
    fun transactionPointcut(): NameMatchClassMethodPointcut {
        return NameMatchClassMethodPointcut().apply {
            setMappedClassName("*ServiceImpl")
            setMappedName("upgrade*")
        }
    }

    @Bean
    fun transactionAdvisor(
        transactionAdvice: TransactionAdvice,
        transactionPointcut: NameMatchMethodPointcut
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
}