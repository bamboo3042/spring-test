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
    @Bean
    fun testUserService(
        mailSender: MailSender,
        userDao: UserDao,
    ): TestUserServiceImpl {
        return TestUserServiceImpl(userDao = userDao, mailSender = mailSender)
    }
}