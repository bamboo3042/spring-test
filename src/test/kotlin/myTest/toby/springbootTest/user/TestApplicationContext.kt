package myTest.toby.springbootTest.user

import myTest.toby.springbootTest.user.dao.UserDao
import myTest.toby.springbootTest.user.dao.UserDaoJdbc
import myTest.toby.springbootTest.user.service.DummyMailSender
import myTest.toby.springbootTest.user.service.UserService
import myTest.toby.springbootTest.user.service.UserServiceImpl
import myTest.toby.springbootTest.user.service.UserServiceTest
import myTest.toby.springbootTest.user.sqlService.OxmSqlService
import myTest.toby.springbootTest.user.sqlService.SqlRegistry
import myTest.toby.springbootTest.user.sqlService.SqlService
import myTest.toby.springbootTest.user.sqlService.jaxb.Sqlmap
import myTest.toby.springbootTest.user.sqlService.updatable.EmbeddedDbSqlRegistry
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.ClassPathResource
import org.springframework.jdbc.datasource.DataSourceTransactionManager
import org.springframework.jdbc.datasource.SimpleDriverDataSource
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType
import org.springframework.mail.MailSender
import org.springframework.oxm.Unmarshaller
import org.springframework.oxm.jaxb.Jaxb2Marshaller
import org.springframework.transaction.PlatformTransactionManager
import org.springframework.transaction.annotation.EnableTransactionManagement
import javax.sql.DataSource

@Configuration
@EnableTransactionManagement
class TestApplicationContext {
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
    fun transactionManager(dataSource: DataSource): PlatformTransactionManager {
        return DataSourceTransactionManager(dataSource)
    }

    @Bean
    fun userDao(dataSource: DataSource, sqlService: SqlService): UserDaoJdbc {
        return UserDaoJdbc().apply {
            setDataSource(dataSource)
            setSqlService(sqlService)
        }
    }

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

    @Bean
    fun unmarshaller(): Unmarshaller {
        return Jaxb2Marshaller().also {
            it.setClassesToBeBound(Sqlmap::class.java)
        }
    }
    @Bean
    fun sqlRegistry(embeddedDatabase: EmbeddedDatabase): EmbeddedDbSqlRegistry {
        return EmbeddedDbSqlRegistry().also {
            it.setDataSource(embeddedDatabase)
        }
    }

    @Bean
    fun sqlService(unmarshaller: Unmarshaller, sqlRegistry: SqlRegistry): SqlService {
        return OxmSqlService().also {
            it.setUnMarshaller(unmarshaller)
            it.setSqlmap(ClassPathResource("sqlmap.xml"))
            it.setSqlRegistry(sqlRegistry)
        }
    }

    @Bean
    fun embeddedDatabase(): EmbeddedDatabase {
        return EmbeddedDatabaseBuilder()
            .setType(EmbeddedDatabaseType.HSQL)
            .addScript("schema.sql")
            .build()
    }
}