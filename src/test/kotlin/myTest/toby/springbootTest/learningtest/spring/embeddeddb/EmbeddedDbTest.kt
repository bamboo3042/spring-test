package myTest.toby.springbootTest.learningtest.spring.embeddeddb

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType

class EmbeddedDbTest {
    lateinit var db: EmbeddedDatabase
    lateinit var template: JdbcTemplate

    @BeforeEach
    fun setUp() {
        db = EmbeddedDatabaseBuilder()
            .setType(EmbeddedDatabaseType.HSQL)
            .addScript("schema.sql")
            .addScripts("data.sql")
            .build()

        template = JdbcTemplate(db)
    }

    @AfterEach
    fun tearDown() {
        db.shutdown()
    }

    @Test
    fun initData() {
        assertThat(template.queryForObject("select count(*) from sqlmap", Int::class.java)).isEqualTo(2)

        val list = template.queryForList("select * from sqlmap order by key_")
        assertThat(list.size).isEqualTo(2)
        assertThat(list[0]["key_"]).isEqualTo("KEY1")
        assertThat(list[0]["sql_"]).isEqualTo("SQL1")
        assertThat(list[1]["key_"]).isEqualTo("KEY2")
        assertThat(list[1]["sql_"]).isEqualTo("SQL2")
    }

    @Test
    fun insert() {
        template.update("insert into sqlmap(key_, sql_) values(?, ?)", "KEY3", "SQL3")

        assertThat(template.queryForObject("select count(*) from sqlmap", Int::class.java)).isEqualTo(3)
    }
}