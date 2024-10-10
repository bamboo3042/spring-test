package myTest.toby.springbootTest.user.service

import myTest.toby.springbootTest.user.domain.User

interface UserService {
    fun add(user: User?)
    fun upgradeLevels()
}