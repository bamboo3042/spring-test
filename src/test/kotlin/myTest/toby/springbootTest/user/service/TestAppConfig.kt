package myTest.toby.springbootTest.user.service

import myTest.toby.springbootTest.user.dao.UserDao
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.mail.MailSender

@TestConfiguration
class TestAppConfig {
    @Bean
    fun testUserServiceImpl(
        userDao: UserDao,
        mailSender: MailSender
    ): UserService {
        return TestUserServiceImpl(userDao, mailSender)
    }
}