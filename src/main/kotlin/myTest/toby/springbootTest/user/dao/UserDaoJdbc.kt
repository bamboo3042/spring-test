package myTest.toby.springbootTest.user.dao

import myTest.toby.springbootTest.user.domain.Level
import myTest.toby.springbootTest.user.domain.User
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.core.queryForObject
import javax.sql.DataSource

class UserDaoJdbc : UserDao {
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
        jdbcTemplate!!.update(
            "insert into users(id, name, password, email, level, login, recommend) " +
                    "values(?,?,?,?,?,?,?)",
            user.id, user.name, user.password, user.email,
            user.level?.intValue(), user.login, user.recommend
        )
    }

    override fun get(id: String): User {
        return jdbcTemplate.queryForObject(
            "select * from users where id = ?",
            arrayOf<Any>(id), this.userMapper
        )!!
    }

    override fun deleteAll() {
        jdbcTemplate.update("delete from users")
    }

    override fun getCount(): Int {
        return jdbcTemplate.queryForObject("select count(*) from users") as Int
    }

    override fun getAll(): List<User> {
        return jdbcTemplate.query("select * from users order by id", this.userMapper)
    }

    override fun update(user: User) {
        jdbcTemplate!!.update(
            "update users set name = ?, password = ?, email = ?, level = ?, login = ?, " +
                    "recommend = ? where id = ? ", user.name, user.password, user.email,
            user.level?.intValue(), user.login, user.recommend,
            user.id
        )
    }
}