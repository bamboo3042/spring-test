package myTest.toby.springbootTest.user

import org.springframework.context.annotation.Import

@Import(value = [SqlServiceContext::class])
annotation class EnableSqlService