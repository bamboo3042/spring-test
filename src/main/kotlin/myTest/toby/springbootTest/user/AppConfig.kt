package myTest.toby.springbootTest.user

import myTest.toby.springbootTest.user.dao.UserDaoJdbc
import myTest.toby.springbootTest.user.service.DummyMailSender
import myTest.toby.springbootTest.user.sqlService.*
import myTest.toby.springbootTest.user.sqlService.jaxb.Sqlmap
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.ClassPathResource
import org.springframework.jdbc.datasource.DataSourceTransactionManager
import org.springframework.jdbc.datasource.SimpleDriverDataSource
import org.springframework.oxm.Unmarshaller
import org.springframework.oxm.jaxb.Jaxb2Marshaller
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
    fun sqlReader(): SqlReader {
        return JaxbXmlSqlReader().also {
            it.setSqlMapFile("sqlmap.xml")
        }
    }

    @Bean
    fun sqlRegistry(): SqlRegistry {
        return HashMapSqlRegistry()
    }

    @Bean
    fun sqlService(unmarshaller: Unmarshaller): SqlService {
        return OxmSqlService(unmarshaller, ClassPathResource("sqlmap.xml"))
    }

    @Bean
    fun mailSender(): DummyMailSender {
        return DummyMailSender()
    }

    @Bean
    fun unmarshaller(): Unmarshaller {
        return Jaxb2Marshaller().also {
            it.setClassesToBeBound(Sqlmap::class.java)
        }
    }
}