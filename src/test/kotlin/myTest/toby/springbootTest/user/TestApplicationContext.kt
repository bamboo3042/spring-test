package myTest.toby.springbootTest.user

import myTest.toby.springbootTest.user.dao.UserDao
import myTest.toby.springbootTest.user.service.UserService
import myTest.toby.springbootTest.user.service.UserServiceImpl
import myTest.toby.springbootTest.user.service.UserServiceTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.mail.MailSender
import org.springframework.transaction.annotation.EnableTransactionManagement

@TestConfiguration
@EnableTransactionManagement
class TestApplicationContext {
    @Bean
    fun userService(userDao: UserDao, mailSender: MailSender): UserService {
        return UserServiceImpl(
            userDao = userDao,
            mailSender = mailSender
        )
    }

    @Bean
    fun testUserService(userDao: UserDao, mailSender: MailSender): UserService {
        return UserServiceTest.Companion.TestUserService(
            userDao = userDao,
            mailSender = mailSender
        )
    }
}