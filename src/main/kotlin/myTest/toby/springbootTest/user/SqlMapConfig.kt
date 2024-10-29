package myTest.toby.springbootTest.user

import org.springframework.core.io.Resource

interface SqlMapConfig {
    fun getSqlMapResource(): Resource
}