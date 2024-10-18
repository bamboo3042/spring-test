package myTest.toby.springbootTest.user.sqlService

interface UpdatableSqlRegistry: SqlRegistry {
    @Throws(SqlUpdateFailureException::class)
    fun updateSql(key: String, sql: String)
    @Throws(SqlUpdateFailureException::class)
    fun updateSql(sqlmap: Map<String, String>)
}