package myTest.toby.springbootTest.user.sqlService.updatable

import myTest.toby.springbootTest.user.sqlService.SqlNotfoundException
import myTest.toby.springbootTest.user.sqlService.SqlUpdateFailureException
import java.util.concurrent.ConcurrentHashMap

class ConcurrentHashMapSqlRegistry: UpdatableSqlRegistry {
    private val sqlMap: ConcurrentHashMap<String, String> = ConcurrentHashMap()

    @Throws(SqlUpdateFailureException::class)
    override fun updateSql(key: String, sql: String) {
        if (!sqlMap.containsKey(key)) throw SqlUpdateFailureException("Key $key does not exist")
        sqlMap[key] = sql
    }

    @Throws(SqlUpdateFailureException::class)
    override fun updateSql(sqlmap: Map<String, String>) {
        sqlmap.forEach { (key, value) -> updateSql(key, value) }
    }

    override fun registerSql(key: String, sql: String) {
        sqlMap[key] = sql
    }

    override fun findSql(key: String): String {
        return sqlMap[key] ?: throw SqlNotfoundException("Key $key does not exist")
    }
}