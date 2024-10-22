package myTest.toby.springbootTest.user.sqlService.updatable

import myTest.toby.springbootTest.user.sqlService.SqlRegistry
import myTest.toby.springbootTest.user.sqlService.SqlUpdateFailureException

interface UpdatableSqlRegistry: SqlRegistry {
    @Throws(SqlUpdateFailureException::class)
    fun updateSql(key: String, sql: String)
    @Throws(SqlUpdateFailureException::class)
    fun updateSql(sqlmap: Map<String, String>)
}