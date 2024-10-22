package myTest.toby.springbootTest.user.service.updatable

import myTest.toby.springbootTest.user.sqlService.SqlUpdateFailureException
import myTest.toby.springbootTest.user.sqlService.updatable.EmbeddedDbSqlRegistry
import myTest.toby.springbootTest.user.sqlService.updatable.UpdatableSqlRegistry
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.fail
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType

class EmbeddedDbSqlRegistryTest: AbstractUpdatableSqlRegistryTest(){
    private lateinit var db: EmbeddedDatabase

    override fun createUpdatableSqlRegistry(): UpdatableSqlRegistry {
        db = EmbeddedDatabaseBuilder()
            .setType(EmbeddedDatabaseType.HSQL)
            .addScript("schema.sql")
            .build()

        return EmbeddedDbSqlRegistry().also {
            it.setDataSource(db)
        }
    }

    @AfterEach
    fun tearDown() {
        db.shutdown()
    }

    @Test
    fun transactionUpdate() {
        checkFind("SQL1", "SQL2", "SQL3")

        val sqlmap = HashMap<String, String>()
        sqlmap["KEY1"] = "Modified"
        sqlmap["KEY9999!@#$"] = "Modified9999"

        try {
            sqlRegistry.updateSql(sqlmap)
            fail("")
        } catch (e: SqlUpdateFailureException) {
        }

        checkFind("SQL1", "SQL2", "SQL3")
    }
}