package com.example.exam.unittests.service

import com.example.exam.controller.NewUserInfo
import com.example.exam.model.AuthorityEntity
import com.example.exam.model.UserEntity
import com.example.exam.repo.AuthorityRepo
import com.example.exam.repo.UserRepo
import com.example.exam.service.UserService
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Test

class UserServiceUnitTest {
    private val userRepo = mockk<UserRepo>()
    private val authorityRepo = mockk<AuthorityRepo>()
    private val userService = UserService(userRepo, authorityRepo)

    @Test
    fun shouldGetUsers() {
        val testMan = UserEntity(email = "testman@tester.com", password = "password")
        val testGuy = UserEntity(email = "testguy@tester.com", password = "drowssap")
        val testPerson = UserEntity(email = "testperson@tester.com", password = "12345")
        every { userRepo.findAll() } answers {
            mutableListOf(testMan, testGuy, testPerson)
        }
        val users = userService.getUsers()
        assert(users.size == 3)
        assert(users.first { it.email == "testman@tester.com" }.password == "password")
        assert(users.containsAll(listOf(testGuy, testPerson)))
    }

    @Test
    fun shouldRegisterNewUser() {
        every { userRepo.save(any()) } answers {
            firstArg()
        }
        every { authorityRepo.getByAuthorityName(any()) } answers {
            AuthorityEntity(authorityName = "USER")
        }

        val createdUser = userService.registerUser(NewUserInfo("programmerman@theman.com", "programmermanrules123"))
        assert(createdUser.email == "programmerman@theman.com")
        assert(createdUser.enabled == true)
    }
}