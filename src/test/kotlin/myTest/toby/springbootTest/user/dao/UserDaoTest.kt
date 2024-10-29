package myTest.toby.springbootTest.user.dao

import myTest.toby.springbootTest.user.AppConfig
import myTest.toby.springbootTest.user.TestApplicationContext
import myTest.toby.springbootTest.user.domain.Level
import myTest.toby.springbootTest.user.domain.User
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.dao.DuplicateKeyException
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.jdbc.support.SQLErrorCodeSQLExceptionTranslator
import org.springframework.jdbc.support.SQLExceptionTranslator
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.sql.SQLException
import javax.sql.DataSource

@ExtendWith(SpringExtension::class)
@ContextConfiguration(classes = [TestApplicationContext::class, AppConfig::class])
class UserDaoTest {
    @Autowired
    lateinit var dao: UserDao

    @Autowired
    lateinit var dataSource: DataSource

    private lateinit var user1: User
    private lateinit var user2: User
    private lateinit var user3: User

    @BeforeEach
    fun setUp() {
        this.user1 = User("gyumee", "�ڼ�ö", "springno1", "user1@ksug.org", Level.BASIC, 1, 0)
        this.user2 = User("leegw700", "�̱��", "springno2", "user2@ksug.org", Level.SILVER, 55, 10)
        this.user3 = User("bumjin", "�ڹ���", "springno3", "user3@ksug.org", Level.GOLD, 100, 40)
    }

    @Test
    fun andAndGet() {
        dao.deleteAll()
        assertThat(dao.getCount()).isEqualTo(0)

        dao.add(user1)
        dao.add(user2)
        assertThat(dao.getCount()).isEqualTo(2)

        val userget1: User = dao.get(user1.id)
        checkSameUser(userget1, user1)

        val userget2: User = dao.get(user2.id)
        checkSameUser(userget2, user2)
    }

    @Test
    fun userFailure() {
        dao.deleteAll()
        assertThat(dao.getCount()).isEqualTo(0)

        assertThatThrownBy { dao.get("unknown_id") }.isInstanceOf(EmptyResultDataAccessException::class.java)
    }


    @Test
    fun count() {
        dao.deleteAll()
        assertThat(dao.getCount()).isEqualTo(0)

        dao.add(user1)
        assertThat(dao.getCount()).isEqualTo(1)

        dao.add(user2)
        assertThat(dao.getCount()).isEqualTo(2)

        dao.add(user3)
        assertThat(dao.getCount()).isEqualTo(3)
    }

    @Test
    fun all() {
        dao.deleteAll()

        val users0: List<User> = dao.getAll()
        assertThat(users0.size).isEqualTo(0)

        dao.add(user1) // Id: gyumee
        val users1: List<User> = dao.getAll()
        assertThat(users1.size).isEqualTo(1)
        checkSameUser(user1, users1[0])

        dao.add(user2) // Id: leegw700
        val users2: List<User> = dao.getAll()
        assertThat(users2.size).isEqualTo(2)
        checkSameUser(user1, users2[0])
        checkSameUser(user2, users2[1])

        dao.add(user3) // Id: bumjin
        val users3: List<User> = dao.getAll()
        assertThat(users3.size).isEqualTo(3)
        checkSameUser(user3, users3[0])
        checkSameUser(user1, users3[1])
        checkSameUser(user2, users3[2])
    }

    private fun checkSameUser(user1: User, user2: User) {
        assertThat(user1.id).isEqualTo(user2.id)
        assertThat(user1.name).isEqualTo(user2.name)
        assertThat(user1.password).isEqualTo(user2.password)
        assertThat(user1.level).isEqualTo(user2.level)
        assertThat(user1.level).isEqualTo(user2.level)
        assertThat(user1.login).isEqualTo(user2.login)
        assertThat(user1.recommend).isEqualTo(user2.recommend)
    }

    @Test
    fun duplicateKey() {
        dao.deleteAll()

        dao.add(user1)
        assertThatThrownBy { dao.add(user1) }.isInstanceOf(DuplicateKeyException::class.java)
    }

    @Test
    fun sqlExceptionTranslate() {
        dao.deleteAll()

        try {
            dao.add(user1)
            dao.add(user1)
        } catch (ex: DuplicateKeyException) {
            val sqlEx = ex.cause as SQLException
            val set: SQLExceptionTranslator = SQLErrorCodeSQLExceptionTranslator(dataSource)
            val transEx = set.translate("", null, sqlEx)

            assertThat(transEx).isInstanceOf(DuplicateKeyException::class.java)
        }
    }

    @Test
    fun update() {
        dao.deleteAll()

        dao.add(user1)
        dao.add(user2)

        user1.name = ("���α�")
        user1.password = ("springno6")
        user1.email = ("user6@ksug.org")
        user1.level = (Level.GOLD)
        user1.login = (1000)
        user1.recommend = (999)

        dao.update(user1)

        val user1update: User = dao.get(user1.id)
        checkSameUser(user1, user1update)
        val user2same: User = dao.get(user2.id)
        checkSameUser(user2, user2same)
    }
}