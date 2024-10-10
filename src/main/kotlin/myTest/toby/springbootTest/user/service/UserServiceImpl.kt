package myTest.toby.springbootTest.user.service

import myTest.toby.springbootTest.user.dao.UserDao
import myTest.toby.springbootTest.user.domain.Level
import myTest.toby.springbootTest.user.domain.User
import org.springframework.mail.MailSender
import org.springframework.mail.SimpleMailMessage
import org.springframework.stereotype.Component

open class UserServiceImpl : UserService {
    private lateinit var userDao: UserDao
    private lateinit var mailSender: MailSender

    fun setUserDao(userDao: UserDao) {
        this.userDao = userDao
    }

    fun setMailSender(mailSender: MailSender) {
        this.mailSender = mailSender
    }

    override fun upgradeLevels() {
        val users: List<User> = userDao.getAll()
        for (user in users) {
            if (canUpgradeLevel(user)) {
                upgradeLevel(user)
            }
        }
    }

    private fun canUpgradeLevel(user: User): Boolean {
        val currentLevel = user.level
        return when (currentLevel) {
            Level.BASIC -> user.login >= MIN_LOGCOUNT_FOR_SILVER
            Level.SILVER -> user.recommend >= MIN_RECCOMEND_FOR_GOLD
            Level.GOLD -> false
            else -> throw IllegalArgumentException("Unknown Level: $currentLevel")
        }
    }

    protected open fun upgradeLevel(user: User) {
        user.upgradeLevel()
        userDao.update(user)
        sendUpgradeEMail(user)
    }

    private fun sendUpgradeEMail(user: User) {
        val mailMessage = SimpleMailMessage()
        mailMessage.setTo(user.email)
        mailMessage.from = "useradmin@ksug.org"
        mailMessage.subject = "Upgrade �ȳ�"
        mailMessage.text = "����ڴ��� ����� " + user.level?.name

        mailSender!!.send(mailMessage)
    }

    override fun add(user: User?) {
        if (user?.level == null) user?.level = (Level.BASIC)
        userDao.add(user!!)
    }

    companion object {
        const val MIN_LOGCOUNT_FOR_SILVER: Int = 50
        const val MIN_RECCOMEND_FOR_GOLD: Int = 30
    }
}