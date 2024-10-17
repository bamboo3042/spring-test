package myTest.toby.springbootTest.user.dao

import myTest.toby.springbootTest.user.domain.Level
import myTest.toby.springbootTest.user.domain.User
import myTest.toby.springbootTest.user.sqlService.SqlService
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.core.queryForObject
import javax.sql.DataSource

class UserDaoJdbc : UserDao {
    private lateinit var sqlService: SqlService

    fun setSqlService(sqlService: SqlService) {
        this.sqlService = sqlService
    }

    fun setDataSource(dataSource: DataSource?) {
        this.jdbcTemplate = JdbcTemplate(dataSource!!)
    }

    lateinit var jdbcTemplate: JdbcTemplate

    private val userMapper: RowMapper<User> =
        RowMapper { rs, rowNum ->
            val user: User = User()
            user.id = rs.getString("id")
            user.name = (rs.getString("name"))
            user.password = (rs.getString("password"))
            user.email = (rs.getString("email"))
            user.level = (Level.valueOf(rs.getInt("level")))
            user.login = (rs.getInt("login"))
            user.recommend = (rs.getInt("recommend"))

            user
        }

    override fun add(user: User) {
        jdbcTemplate.update(
            this.sqlService.getSql("userAdd"),
            user.id, user.name, user.password, user.email,
            user.level?.intValue(), user.login, user.recommend
        )
    }

    override fun get(id: String): User {
        return jdbcTemplate.queryForObject(
            this.sqlService.getSql("userGet"),
            arrayOf<Any>(id), this.userMapper
        )!!
    }

    override fun    deleteAll() {
        jdbcTemplate.update(this.sqlService.getSql("userDeleteAll"))
    }

    override fun getCount(): Int {
        return jdbcTemplate.queryForObject(this.sqlService.getSql("userGetCount")) as Int
    }

    override fun getAll(): List<User> {
        return jdbcTemplate.query(this.sqlService.getSql("userGetAll"), this.userMapper)
    }

    override fun update(user: User) {
        jdbcTemplate.update(
            this.sqlService.getSql("userUpdate"), user.name, user.password, user.email,
            user.level?.intValue(), user.login, user.recommend,
            user.id
        )
    }
}