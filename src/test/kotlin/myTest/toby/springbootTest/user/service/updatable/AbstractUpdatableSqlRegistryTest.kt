package myTest.toby.springbootTest.user.service.updatable

import myTest.toby.springbootTest.user.sqlService.SqlNotfoundException
import myTest.toby.springbootTest.user.sqlService.SqlUpdateFailureException
import myTest.toby.springbootTest.user.sqlService.updatable.UpdatableSqlRegistry
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

abstract class AbstractUpdatableSqlRegistryTest {
    protected lateinit var sqlRegistry: UpdatableSqlRegistry

    @BeforeEach
    fun setUp() {
        sqlRegistry = createUpdatableSqlRegistry()
        sqlRegistry.registerSql("KEY1", "SQL1")
        sqlRegistry.registerSql("KEY2", "SQL2")
        sqlRegistry.registerSql("KEY3", "SQL3")
    }

    protected abstract fun createUpdatableSqlRegistry(): UpdatableSqlRegistry

    protected fun checkFind(expected1: String, expected2: String, expected3: String) {
        assertThat(sqlRegistry.findSql("KEY1")).isEqualTo(expected1)
        assertThat(sqlRegistry.findSql("KEY2")).isEqualTo(expected2)
        assertThat(sqlRegistry.findSql("KEY3")).isEqualTo(expected3)
    }

    @Test
    fun find() {
        checkFind("SQL1", "SQL2", "SQL3")
    }

    @Test
    fun unknownKey() {
        assertThatThrownBy { sqlRegistry.findSql("SQL9999!@#$") }
            .isInstanceOf(SqlNotfoundException::class.java)
    }

    @Test
    fun updateSingle() {
        sqlRegistry.updateSql("KEY2", "Modified2")
        checkFind("SQL1", "Modified2", "SQL3")
    }

    @Test
    fun updateMulti() {
        val sqlmap = mutableMapOf<String, String>()
        sqlmap["KEY1"] = "Modified1"
        sqlmap["KEY3"] = "Modified3"

        sqlRegistry.updateSql(sqlmap)
        checkFind("Modified1", "SQL2", "Modified3")
    }

    @Test
    fun updateWithNotExistingKey() {
        assertThatThrownBy { sqlRegistry.updateSql("SQL9999!@#$", "Modified2") }
            .isInstanceOf(SqlUpdateFailureException::class.java)
    }
}