package myTest.toby.springbootTest.user.sqlService

interface SqlRegistry {
    fun registerSql(key: String, sql: String)
    @Throws(SqlNotfoundException::class)
    fun findSql(key: String): String
}