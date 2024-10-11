package myTest.toby.springbootTest.user.service

import io.mockk.*
import myTest.toby.springbootTest.user.AppConfig
import myTest.toby.springbootTest.user.dao.UserDao
import myTest.toby.springbootTest.user.domain.Level
import myTest.toby.springbootTest.user.domain.User
import org.assertj.core.api.Assertions.assertThat
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.fail
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Import
import org.springframework.mail.MailException
import org.springframework.mail.MailSender
import org.springframework.mail.SimpleMailMessage
import org.springframework.test.context.ContextConfiguration
import org.springframework.transaction.PlatformTransactionManager
import java.lang.reflect.Proxy
import java.util.*

//@Import(UserServiceTestConfig::class)
@SpringBootTest
@ContextConfiguration(classes = [AppConfig::class, UserServiceTestConfig::class])
class UserServiceTest {
    @Autowired
    lateinit var userService: UserService

    @Autowired
    lateinit var testUserService: UserService

    @Autowired
    lateinit var userDao: UserDao

    @Autowired
    lateinit var mailSender: MailSender

    @Autowired
    lateinit var transactionManager: PlatformTransactionManager

    @Autowired
    lateinit var context: ApplicationContext

    var users: List<User> = listOf() // test fixture

    @BeforeEach
    fun setUp() {
        users = listOf<User>(
            User("bumjin", "�ڹ���", "p1", "user1@ksug.org", Level.BASIC, UserServiceImpl.MIN_LOGCOUNT_FOR_SILVER - 1, 0),
            User("joytouch", "����", "p2", "user2@ksug.org", Level.BASIC, UserServiceImpl.MIN_LOGCOUNT_FOR_SILVER, 0),
            User("erwins", "�Ž���", "p3", "user3@ksug.org", Level.SILVER, 60, UserServiceImpl.MIN_RECCOMEND_FOR_GOLD - 1),
            User("madnite1", "�̻�ȣ", "p4", "user4@ksug.org", Level.SILVER, 60, UserServiceImpl.MIN_RECCOMEND_FOR_GOLD),
            User("green", "���α�", "p5", "user5@ksug.org", Level.GOLD, 100, Int.MAX_VALUE)
        )
    }

    @Test
    @Throws(Exception::class)
    fun upgradeLevels() {
        val userServiceImpl = UserServiceImpl()

        val mockUserDao = MockUserDao(users)
        userServiceImpl.setUserDao(mockUserDao)

        val mockMailSender = MockMailSender()
        userServiceImpl.setMailSender(mockMailSender)

        userServiceImpl.upgradeLevels()

        val updated: List<User> = mockUserDao.getUpdated()

        assertThat(updated.size).isEqualTo(2)
        checkUserAndLevel(updated[0], "joytouch", Level.SILVER)
        checkUserAndLevel(updated[1], "madnite1", Level.GOLD)

        val request = mockMailSender.getRequests()
        assertThat(request.size).isEqualTo(2)
        MatcherAssert.assertThat(request.size, CoreMatchers.`is`(2))
        MatcherAssert.assertThat(request[0], CoreMatchers.`is`(users[1].email))
        MatcherAssert.assertThat(request[1], CoreMatchers.`is`(users[3].email))
    }

    private fun checkUserAndLevel(updated: User, expectedId: String, expectedLevel: Level) {
        assertThat(updated.id).isEqualTo(expectedId)
        assertThat(updated.level).isEqualTo(expectedLevel)
    }

    class MockUserDao(private val users: List<User>) : UserDao {
        private val updated: MutableList<User> = ArrayList<User>()

        fun getUpdated(): List<User> {
            return this.updated
        }

        override fun getAll(): List<User> {
            return this.users
        }

        override fun update(user: User) {
            updated.add(user)
        }

        override fun add(user: User) {
            throw UnsupportedOperationException()
        }

        override fun deleteAll() {
            throw UnsupportedOperationException()
        }

        override fun get(id: String): User {
            throw UnsupportedOperationException()
        }

        override fun getCount(): Int {
            throw UnsupportedOperationException()
        }
    }

    internal class MockMailSender : MailSender {
        private val requests: MutableList<String> = ArrayList()

        fun getRequests(): List<String> {
            return requests
        }

        @Throws(MailException::class)
        override fun send(mailMessage: SimpleMailMessage) {
            requests.add(mailMessage.to!![0])
        }

        @Throws(MailException::class)
        override fun send(mailMessage: Array<SimpleMailMessage>) {
        }
    }

    @Test
    @Throws(Exception::class)
    fun mockUpgradeLevels() {
        val userServiceImpl = UserServiceImpl()

        val mockUserDao: UserDao = mockk<UserDao>()
        every { mockUserDao.getAll() } returns this.users
        every { mockUserDao.update(any()) } just Runs
        userServiceImpl.setUserDao(mockUserDao)

        val mockMailSender = mockk<MockMailSender>()
        userServiceImpl.setMailSender(mockMailSender)

        val mailMessageArg = mutableListOf<SimpleMailMessage>()
        every { mockMailSender.send(capture(mailMessageArg)) } just Runs

        userServiceImpl.upgradeLevels()

        verify(exactly = 1) { mockUserDao.getAll() }
        verify(exactly = 2) { mockUserDao.update(any()) }
        verify { mockUserDao.update(users[1]) }
        assertThat(users[1].level).isEqualTo(Level.SILVER)
        verify { mockUserDao.update(users[3]) }
        assertThat(users[3].level).isEqualTo(Level.GOLD)

        verify(exactly = 2) { mockMailSender.send(any<SimpleMailMessage>()) }
        assertThat(mailMessageArg[0].to!![0]).isEqualTo(users[1].email)
        assertThat(mailMessageArg[1].to!![0]).isEqualTo(users[3].email)
    }

    private fun checkLevelUpgraded(user: User, upgraded: Boolean) {
        val userUpdate: User = userDao.get(user.id)
        if (upgraded) {
            MatcherAssert.assertThat(userUpdate.level,CoreMatchers.`is`(user.level?.nextLevel()))
        } else {
            MatcherAssert.assertThat(userUpdate.level,CoreMatchers.`is`(user.level))
        }
    }

    @Test
    fun add() {
        userDao.deleteAll()

        val userWithLevel: User = users[4] // GOLD ����
        val userWithoutLevel: User = users[0]
        userWithoutLevel.level = (null)

        userService.add(userWithLevel)
        userService.add(userWithoutLevel)

        val userWithLevelRead: User = userDao.get(userWithLevel.id)
        val userWithoutLevelRead: User = userDao.get(userWithoutLevel.id)

        MatcherAssert.assertThat(userWithLevelRead.level,CoreMatchers.`is`(userWithLevel.level))
        MatcherAssert.assertThat(userWithoutLevelRead.level, CoreMatchers.`is`(Level.BASIC))
    }

    @Test
    fun upgradeAllOrNothing() {
        userDao.deleteAll()
        for (user in users) userDao.add(user)

        try {
            this.testUserService.upgradeLevels()
            fail("TestUserServiceException expected")
        } catch (e: TestUserServiceException) {
        }

        checkLevelUpgraded(users[1], false)
    }

    @Test
    fun advisorAutoProxyCreator() {
        assertThat(testUserService).isInstanceOf(Proxy::class.java)
    }

    class TestUserServiceImpl(
        private val id: String = "madnite1"
    ) : UserServiceImpl() {
        override fun upgradeLevel(user: User) {
            if (user.id.equals(this.id)) throw TestUserServiceException()
            super.upgradeLevel(user)
        }
    }

    internal class TestUserServiceException : RuntimeException()
}