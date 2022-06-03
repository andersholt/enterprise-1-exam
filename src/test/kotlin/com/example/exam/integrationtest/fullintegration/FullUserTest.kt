package com.example.exam.integrationtest.fullintegration

import com.example.exam.controller.NewUserInfo
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.delete
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post


@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
@AutoConfigureMockMvc
class FullUserTest {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @Test
    fun shouldGetAllAuthoritiesWithAdminAccess() {
        val loggedInUser = mockMvc.post("/api/authentication/user/login") {
            contentType = MediaType.APPLICATION_JSON
            content = "{\"email\": \"admin@admin.com\", \"password\": \"pirate\"\n}"
        }
            .andExpect { status { isOk() } }
            .andReturn()

        val cookie = loggedInUser.response.getCookie("access_token")

        mockMvc.get("/api/user/authorized/authority/all") {
            cookie?.let { cookie(it) }
        }
            .andExpect { status { isOk() } }
            .andExpect { content { contentType(MediaType.APPLICATION_JSON) } }
            .andExpect { jsonPath("$") { isArray() } }
    }

    @Test
    fun shouldNotGetAllAuthoritiesWithWorkerAccess() {
        val loggedInUser = mockMvc.post("/api/authentication/user/login") {
            contentType = MediaType.APPLICATION_JSON
            content = "{\"email\": \"workuser@workuser.com\", \"password\": \"pirate\"\n}"
        }
            .andExpect { status { isOk() } }
            .andReturn()

        val cookie = loggedInUser.response.getCookie("access_token")

        mockMvc.get("/api/user/authorized/authority/all") {
            cookie?.let { cookie(it) }
        }
            .andExpect { status { isForbidden() } }
    }

    @Test
    fun shouldRegisterAndLoginNewUser() {
        val newUserInfo = NewUserInfo("testmail@tester.no", "1234")

        mockMvc.post("/api/authentication/user/register") {
            content = objectMapper.writeValueAsString(newUserInfo)
            contentType = (MediaType.APPLICATION_JSON)
        }
            .andExpect { status { isCreated() } }

        mockMvc.post("/api/authentication/user/login") {
            contentType = MediaType.APPLICATION_JSON
            content = "{\"email\": \"testmail@tester.no\", \"password\": \"1234\"}"
        }
            .andExpect { status { isOk() } }
    }

    @Test
    fun shouldGetUserByEmail() {
        val loggedInUser = mockMvc.post("/api/authentication/user/login") {
            contentType = MediaType.APPLICATION_JSON
            content = "{\"email\": \"admin@admin.com\", \"password\": \"pirate\"\n}"
        }
            .andExpect { status { isOk() } }
            .andReturn()

        val cookie = loggedInUser.response.getCookie("access_token")

        mockMvc.get("/api/user/authorized/user@user.com") {
            cookie?.let { cookie(it) }
        }
            .andExpect { status { isOk() } }
    }

    @Test
    fun shouldGetAllUsersWithAdminAccess() {
        val loggedInUser = mockMvc.post("/api/authentication/user/login") {
            contentType = MediaType.APPLICATION_JSON
            content = "{\"email\": \"admin@admin.com\", \"password\": \"pirate\"\n}"
        }
            .andExpect { status { isOk() } }
            .andReturn()

        val cookie = loggedInUser.response.getCookie("access_token")

        mockMvc.get("/api/user/authorized/all") {
            cookie?.let { cookie(it) }
        }
            .andExpect { status { isOk() } }
            .andExpect { content { contentType(MediaType.APPLICATION_JSON) } }
            .andExpect { jsonPath("$") { isArray() } }
    }

    @Test
    fun shouldNotGetAllUsersWithWorkerAccess() {
        val loggedInUser = mockMvc.post("/api/authentication/user/login") {
            contentType = MediaType.APPLICATION_JSON
            content = "{\"email\": \"workuser@workuser.com\", \"password\": \"pirate\"\n}"
        }
            .andExpect { status { isOk() } }
            .andReturn()

        val cookie = loggedInUser.response.getCookie("access_token")

        mockMvc.get("/api/user/authorized/all") {
            cookie?.let { cookie(it) }
        }
            .andExpect { status { isForbidden() } }
    }

    @Test
    fun shouldRegisterAndLoginNewWorkUserWithAdminAccess() {
        val loggedInUser = mockMvc.post("/api/authentication/user/login") {
            contentType = MediaType.APPLICATION_JSON
            content = "{\"email\": \"admin@admin.com\", \"password\": \"pirate\"\n}"
        }
            .andExpect { status { isOk() } }
            .andReturn()

        val cookie = loggedInUser.response.getCookie("access_token")

        val newUserInfo = NewUserInfo("workertester@workertester.no", "4321")

        mockMvc.post("/api/user/authorized/workuser/register") {
            cookie?.let { cookie(it) }
            content = objectMapper.writeValueAsString(newUserInfo)
            contentType = (MediaType.APPLICATION_JSON)
        }
            .andExpect { status { isCreated() } }

        mockMvc.post("/api/authentication/user/login") {
            contentType = MediaType.APPLICATION_JSON
            content = "{\"email\": \"workertester@workertester.no\", \"password\": \"4321\"}"
        }
            .andExpect { status { isOk() } }
    }

    @Test
    fun shouldNotRegisterAndLoginNewWorkUser() {
        val loggedInUser = mockMvc.post("/api/authentication/user/login") {
            contentType = MediaType.APPLICATION_JSON
            content = "{\"email\": \"user@user.com\", \"password\": \"pirate\"\n}"
        }
            .andExpect { status { isOk() } }
            .andReturn()

        val cookie = loggedInUser.response.getCookie("access_token")

        val newUserInfo = NewUserInfo("workertester@workertester.no", "4321")

        mockMvc.post("/api/user/authorized/workuser/register") {
            cookie?.let { cookie(it) }
            content = objectMapper.writeValueAsString(newUserInfo)
            contentType = (MediaType.APPLICATION_JSON)
        }
            .andExpect { status { isForbidden() } }
    }

    @Test
    fun shouldDeleteUsersWithAdminAccess() {
        val newUserInfo = NewUserInfo("anewuser@tester.no", "1234")

        mockMvc.post("/api/authentication/user/register") {
            content = objectMapper.writeValueAsString(newUserInfo)
            contentType = (MediaType.APPLICATION_JSON)
        }
            .andExpect { status { isCreated() } }
            .andReturn()


        val loggedInUser = mockMvc.post("/api/authentication/user/login") {
            contentType = MediaType.APPLICATION_JSON
            content = "{\"email\": \"admin@admin.com\", \"password\": \"pirate\"\n}"
        }
            .andExpect { status { isOk() } }
            .andReturn()

        val cookie = loggedInUser.response.getCookie("access_token")

        mockMvc.delete("/api/user/authorized/4/delete") {
            cookie?.let { cookie(it) }
        }
            .andExpect { status { isOk() } }
    }


}