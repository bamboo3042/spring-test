package myTest.toby.springbootTest.user.service.updatable

import myTest.toby.springbootTest.user.sqlService.updatable.ConcurrentHashMapSqlRegistry
import myTest.toby.springbootTest.user.sqlService.SqlNotfoundException
import myTest.toby.springbootTest.user.sqlService.SqlUpdateFailureException
import myTest.toby.springbootTest.user.sqlService.updatable.UpdatableSqlRegistry
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class ConcurrentHashMapSqlRegistryTest: AbstractUpdatableSqlRegistryTest() {
    override fun createUpdatableSqlRegistry(): UpdatableSqlRegistry {
        return ConcurrentHashMapSqlRegistry()
    }
}