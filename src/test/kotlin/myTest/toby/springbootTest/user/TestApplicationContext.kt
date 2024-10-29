package myTest.toby.springbootTest.user

import myTest.toby.springbootTest.user.dao.UserDao
import myTest.toby.springbootTest.user.service.DummyMailSender
import myTest.toby.springbootTest.user.service.UserService
import myTest.toby.springbootTest.user.service.UserServiceImpl
import myTest.toby.springbootTest.user.service.UserServiceTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Import
import org.springframework.context.annotation.Profile
import org.springframework.mail.MailSender

@TestConfiguration
@Profile("test")
@Import(AppContext::class)
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

    @Bean
    fun mailSender(): MailSender {
        return DummyMailSender()
    }
}