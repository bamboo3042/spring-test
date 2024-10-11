package myTest.toby.springbootTest.user.service

import myTest.toby.springbootTest.user.dao.UserDao
import myTest.toby.springbootTest.user.domain.User
import org.springframework.mail.MailSender

class TestUserServiceImpl(
    private val userDao: UserDao,
    private val mailSender: MailSender
) : UserServiceImpl(userDao, mailSender) {
    private val id: String = "madnite1"

    override fun upgradeLevel(user: User) {
        if (user.id == this.id) throw TestUserServiceException()
        super.upgradeLevel(user)
    }

    override fun getAll(): List<User> {
        for (user in super.getAll()) {
            super.update(user)
        }
        return listOf()
    }
}

internal class TestUserServiceException : RuntimeException()