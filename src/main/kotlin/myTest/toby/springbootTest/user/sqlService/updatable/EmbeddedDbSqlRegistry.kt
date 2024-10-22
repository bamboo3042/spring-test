package myTest.toby.springbootTest.user.sqlService.updatable

import myTest.toby.springbootTest.user.sqlService.SqlNotfoundException
import myTest.toby.springbootTest.user.sqlService.SqlUpdateFailureException
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.datasource.DataSourceTransactionManager
import org.springframework.transaction.TransactionStatus
import org.springframework.transaction.support.TransactionCallbackWithoutResult
import org.springframework.transaction.support.TransactionTemplate
import javax.sql.DataSource

class EmbeddedDbSqlRegistry: UpdatableSqlRegistry {
    private lateinit var jdbc: JdbcTemplate
    private lateinit var transactionTemplate: TransactionTemplate

    fun setDataSource(dataSource: DataSource) {
        this.jdbc = JdbcTemplate(dataSource)
        this.transactionTemplate = TransactionTemplate(
            DataSourceTransactionManager(dataSource)
        )
    }

    @Throws(SqlUpdateFailureException::class)
    override fun updateSql(key: String, sql: String) {
        val affected = jdbc.update("update sqlmap set sql_ = ? where key_ = ?", sql, key)

        if (affected == 0) {
            throw SqlUpdateFailureException("$key not found")
        }
    }

    @Throws(SqlUpdateFailureException::class)
    override fun updateSql(sqlmap: Map<String, String>) {
        transactionTemplate.execute(
            object : TransactionCallbackWithoutResult() {
                override fun doInTransactionWithoutResult(status: TransactionStatus) {
                    for ((key, value) in sqlmap) {
                        updateSql(key, value)
                    }
                }
            }
        )
    }

    override fun registerSql(key: String, sql: String) {
        jdbc.update("insert into sqlmap(key_, sql_) values(?, ?)", key, sql)
    }

    @Throws(SqlNotfoundException::class)
    override fun findSql(key: String): String {
        try {
            return jdbc.queryForObject("select sql_ from sqlmap where key_ = ?", String::class.java, key)
        } catch (e: EmptyResultDataAccessException) {
            throw SqlNotfoundException("$key does not exist")
        }
    }
}