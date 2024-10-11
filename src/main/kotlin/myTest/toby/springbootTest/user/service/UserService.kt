package myTest.toby.springbootTest.user.service

import myTest.toby.springbootTest.user.domain.User
import org.springframework.transaction.annotation.Transactional

@Transactional
interface UserService {
    fun add(user: User?)
    fun deleteAll()
    fun update(user: User)
    fun upgradeLevels()

    @Transactional(readOnly = true)
    fun get(id: String): User
    @Transactional(readOnly = true)
    fun getAll(): List<User>
}