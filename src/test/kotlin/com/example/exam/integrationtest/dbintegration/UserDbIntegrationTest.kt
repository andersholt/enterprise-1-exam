package com.example.exam.integrationtest.dbintegration

import com.example.exam.controller.NewUserInfo
import com.example.exam.service.UserService
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.context.annotation.Import
import org.springframework.test.context.ActiveProfiles


@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
@Import(UserService::class)
class UserDbIntegrationTest(@Autowired private val userService: UserService) {
    @Test
    fun shouldGetUsers() {
        val result = userService.getUsers()
        assert(result.size == 4)
    }

    @Test
    fun registerAndFindUser() {
        val newUserInfo = NewUserInfo("test@test.com", "1234")
        val createdUser = userService.registerUser(newUserInfo)
        assert(createdUser.email == "test@test.com")
        val foundUser = userService.loadUserByUsername("test@test.com")
        assert(createdUser.email == foundUser.username)
    }

    @Test
    fun createUserThenGetByEmail() {
        val newUserInfo = NewUserInfo("testman@test.com", "4321")
        val createdUser = userService.registerUser(newUserInfo)
        assert(createdUser.email == "testman@test.com")
        val foundUser = userService.getUserByEmail("testman@test.com")
        assert(foundUser?.email == "testman@test.com")
    }
}