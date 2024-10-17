package myTest.toby.springbootTest.user.sqlService

class HashMapSqlRegistry: SqlRegistry {
    private val sqlMap = mutableMapOf<String, String>()

    override fun registerSql(key: String, sql: String) {
        sqlMap[key] = sql
    }

    override fun findSql(key: String): String {
        return sqlMap[key] ?: throw SqlNotfoundException("${key}에 대한 Sql을 찾을 수 없습니다")
    }
}