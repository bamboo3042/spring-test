package myTest.toby.springbootTest.user

import myTest.toby.springbootTest.user.dao.UserDaoJdbc
import myTest.toby.springbootTest.user.service.DummyMailSender
import myTest.toby.springbootTest.user.sqlService.SqlService
import myTest.toby.springbootTest.user.sqlService.SqlServiceImpl
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.jdbc.datasource.DataSourceTransactionManager
import org.springframework.jdbc.datasource.SimpleDriverDataSource
import javax.sql.DataSource

@Configuration
class AppConfig {

    @Bean
    fun dataSource(): DataSource {
        return SimpleDriverDataSource().apply {
            setDriverClass(com.mysql.cj.jdbc.Driver::class.java)
            url = "jdbc:mysql://localhost/springbook?characterEncoding=UTF-8"
            username = "root"
            password = "123456"
        }
    }

    @Bean
    fun transactionManager(): DataSourceTransactionManager {
        return DataSourceTransactionManager(dataSource())
    }

    @Bean
    fun userDao(dataSource: DataSource, sqlService: SqlService): UserDaoJdbc {
        return UserDaoJdbc().apply {
            setDataSource(dataSource)
            setSqlService(sqlService)
        }
    }

    @Bean
    fun sqlService(): SqlService {
        val sqlMap = mapOf(
            "userAdd" to "insert into users(id, name, password, email, level, login, recommend) values(?,?,?,?,?,?,?)",
            "userGet" to "select * from users where id = ?",
            "userGetAll" to "select * from users order by id",
            "userDeleteAll" to "delete from users",
            "userGetCount" to "select count(*) from users",
            "userUpdate" to "update users set name = ?, password = ?, email = ?, level = ?, login = ?, recommend = ? where id = ?"
        )

        return SqlServiceImpl().apply {
            setSqlMap(sqlMap)
        }
    }

    @Bean
    fun mailSender(): DummyMailSender {
        return DummyMailSender()
    }
}