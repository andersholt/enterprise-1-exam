package com.example.exam.unittests.controller

import com.example.exam.model.AuthorityEntity
import com.example.exam.model.UserEntity
import com.example.exam.service.AnimalService
import com.example.exam.service.UserService
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.delete
import org.springframework.test.web.servlet.get

@WebMvcTest
@AutoConfigureMockMvc(addFilters = false)
class AuthControllerUnitTest {
    @TestConfiguration
    class ControllerTestConfig {
        @Bean
        fun userService() = mockk<UserService>()

        @Bean
        fun animalService() = mockk<AnimalService>()
    }

    @Autowired
    private lateinit var userService: UserService

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Test
    fun shouldGetAllUsers() {
        val testUser = UserEntity(email = "test@test.com", password = "password")
        every { userService.getUsers() } answers {
            mutableListOf(testUser)
        }

        mockMvc.get("/api/user/authorized/all") {

        }.andExpect { status { isOk() } }.andExpect { content { contentType(MediaType.APPLICATION_JSON) } }
    }

    @Test
    fun shouldGetAllAuthorities() {
        val userAuthority = AuthorityEntity(authorityName = "USER")
        val adminAuthority = AuthorityEntity(authorityName = "ADMIN")

        every { userService.getAuthorities() } answers {
            mutableListOf(userAuthority, adminAuthority)
        }

        mockMvc.get("/api/user/authorized/authority/all") {
        }.andExpect { status { isOk() } }.andExpect { content { contentType(MediaType.APPLICATION_JSON) } }
    }

    @Test
    fun shouldAndShouldNotDeleteUser() {
        every { userService.deleteUser(1L) } answers {
            true
        }

        mockMvc.delete("/api/user/authorized/1/delete") {
        }.andExpect { status { isOk() } }

        every { userService.deleteUser(2L) } answers {
            false
        }
        mockMvc.delete("/api/user/authorized/2/delete") {
        }.andExpect { status { isNotFound() } }
    }

}