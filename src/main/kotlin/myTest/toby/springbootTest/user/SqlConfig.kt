package myTest.toby.springbootTest.user

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties("sql-map")
data class SqlConfig(
    val sql: Map<String, String> = mutableMapOf()
)