package myTest.toby.springbootTest.user

import myTest.toby.springbootTest.user.service.DummyMailSender
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.jdbc.datasource.DataSourceTransactionManager
import org.springframework.jdbc.datasource.SimpleDriverDataSource
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType
import org.springframework.transaction.PlatformTransactionManager
import javax.sql.DataSource

@Configuration
@ComponentScan(basePackages = ["myTest.toby.springbootTest.user.dao"])
@Import(SqlServiceContext::class)
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
    fun embeddedDatabase(): EmbeddedDatabase {
        return EmbeddedDatabaseBuilder()
            .setType(EmbeddedDatabaseType.HSQL)
            .addScript("schema.sql")
            .build()
    }

    @Bean
    fun transactionManager(dataSource: DataSource): PlatformTransactionManager {
        return DataSourceTransactionManager(dataSource)
    }

    @Bean
    fun mailSender(): DummyMailSender {
        return DummyMailSender()
    }
}