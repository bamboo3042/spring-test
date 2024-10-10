package myTest.toby.springbootTest.user.dao

import myTest.toby.springbootTest.user.domain.User

interface UserDao {
    fun add(user: User)

    fun get(id: String): User

    fun getAll(): List<User>

    fun deleteAll()

    fun getCount(): Int

    fun update(user: User)

}