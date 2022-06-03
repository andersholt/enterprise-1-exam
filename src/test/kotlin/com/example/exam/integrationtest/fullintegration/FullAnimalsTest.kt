package com.example.exam.integrationtest.fullintegration

import com.example.exam.model.AnimalEntity
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.*


@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
@AutoConfigureMockMvc
class FullAnimalsTest {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @Test
    fun shouldGetAllAnimalsWithUserAccess() {
        val loggedInUser = mockMvc.post("/api/authentication/user/login") {
            contentType = MediaType.APPLICATION_JSON
            content = "{\"email\": \"user@user.com\", \"password\": \"pirate\"}"
        }
            .andExpect { status { isOk() } }
            .andReturn()

        val cookie = loggedInUser.response.getCookie("access_token")

        mockMvc.get("/api/shelter/useraccess/animal") {
            cookie?.let { cookie(it) }
        }
            .andExpect { status { isOk() } }
            .andExpect { content { contentType(MediaType.APPLICATION_JSON) } }
            .andExpect { jsonPath("$") { isArray() } }
    }

    @Test
    fun shouldGetAnimalWithUserAccess() {
        val loggedInUser = mockMvc.post("/api/authentication/user/login") {
            contentType = MediaType.APPLICATION_JSON
            content = "{\"email\": \"user@user.com\", \"password\": \"pirate\"}"
        }
            .andExpect { status { isOk() } }
            .andReturn()

        val cookie = loggedInUser.response.getCookie("access_token")

        mockMvc.get("/api/shelter/useraccess/animal/2") {
            cookie?.let { cookie(it) }
        }
            .andExpect { status { isOk() } }
            .andExpect { content { contentType(MediaType.APPLICATION_JSON) } }
    }

    @Test
    fun shouldCreateAnimalWithAdminAccess() {
        val testAnimal1 =
            AnimalEntity(id = 1, name = "Puffers", age = 10, health = 80, breed = "european burmese", type = "cat")
        val loggedInUser = mockMvc.post("/api/authentication/user/login") {
            contentType = MediaType.APPLICATION_JSON
            content = "{\"email\": \"admin@admin.com\", \"password\": \"pirate\"}"
        }
            .andExpect { status { isOk() } }
            .andReturn()

        val cookie = loggedInUser.response.getCookie("access_token")

        mockMvc.post("/api/shelter/workeraccess/animal") {
            cookie?.let { cookie(it) }
            content = objectMapper.writeValueAsString(testAnimal1)
            contentType = (MediaType.APPLICATION_JSON)
        }
            .andExpect { status { isCreated() } }
            .andExpect { content { contentType(MediaType.APPLICATION_JSON) } }
    }

    @Test
    fun shouldCreateAnimalWithWorkerAccess() {
        val testAnimal1 =
            AnimalEntity(id = 1, name = "Puffers", age = 10, health = 80, breed = "european burmese", type = "cat")
        val loggedInUser = mockMvc.post("/api/authentication/user/login") {
            contentType = MediaType.APPLICATION_JSON
            content = "{\"email\": \"workuser@workuser.com\", \"password\": \"pirate\"}"
        }
            .andExpect { status { isOk() } }
            .andReturn()

        val cookie = loggedInUser.response.getCookie("access_token")

        mockMvc.post("/api/shelter/workeraccess/animal") {
            cookie?.let { cookie(it) }
            content = objectMapper.writeValueAsString(testAnimal1)
            contentType = (MediaType.APPLICATION_JSON)
        }
            .andExpect { status { isCreated() } }
            .andExpect { content { contentType(MediaType.APPLICATION_JSON) } }
    }


    @Test
    fun shouldNotCreateAnimalWithUserAccess() {
        val testAnimal1 =
            AnimalEntity(id = 1, name = "Puffers", age = 10, health = 80, breed = "european burmese", type = "cat")

        val loggedInUser = mockMvc.post("/api/authentication/user/login") {
            contentType = MediaType.APPLICATION_JSON
            content = "{\"email\": \"user@user.com\", \"password\": \"pirate\"}"
        }
            .andExpect { status { isOk() } }
            .andReturn()

        val cookie = loggedInUser.response.getCookie("access_token")

        mockMvc.post("/api/shelter/workeraccess/animal") {
            cookie?.let { cookie(it) }
            content = objectMapper.writeValueAsString(testAnimal1)
            contentType = (MediaType.APPLICATION_JSON)
        }
            .andExpect { status { isForbidden() } }
    }

    @Test
    fun shouldGetAnimalsByName() {
        val loggedInUser = mockMvc.post("/api/authentication/user/login") {
            contentType = MediaType.APPLICATION_JSON
            content = "{\"email\": \"workuser@workuser.com\", \"password\": \"pirate\"}"
        }
            .andExpect { status { isOk() } }
            .andReturn()

        val cookie = loggedInUser.response.getCookie("access_token")

        mockMvc.get("/api/shelter/useraccess/animal/Pebbles/name") {
            cookie?.let { cookie(it) }
        }
            .andExpect { status { isOk() } }
            .andExpect { content { contentType(MediaType.APPLICATION_JSON) } }
            .andExpect { jsonPath("$") { isArray() } }
    }

    @Test
    fun shouldUpdateAnimalWithWorkerAccess() {
        val testAnimal1 =
            AnimalEntity(id = 1, name = "Puffers", age = 10, health = 80, breed = "european burmese", type = "cat")

        val loggedInUser = mockMvc.post("/api/authentication/user/login") {
            contentType = MediaType.APPLICATION_JSON
            content = "{\"email\": \"workuser@workuser.com\", \"password\": \"pirate\"}"
        }
            .andExpect { status { isOk() } }
            .andReturn()

        val cookie = loggedInUser.response.getCookie("access_token")

        mockMvc.put("/api/shelter/workeraccess/animal/2") {
            cookie?.let { cookie(it) }
            content = objectMapper.writeValueAsString(testAnimal1)
            contentType = (MediaType.APPLICATION_JSON)
        }
            .andExpect { status { isOk() } }
            .andExpect { content { contentType(MediaType.APPLICATION_JSON) } }
    }

    @Test
    fun shouldNotUpdateAnimalWithUserAccess() {
        val testAnimal1 =
            AnimalEntity(id = 1, name = "Puffers", age = 10, health = 80, breed = "european burmese", type = "cat")

        val loggedInUser = mockMvc.post("/api/authentication/user/login") {
            contentType = MediaType.APPLICATION_JSON
            content = "{\"email\": \"user@user.com\", \"password\": \"pirate\"}"
        }
            .andExpect { status { isOk() } }
            .andReturn()

        val cookie = loggedInUser.response.getCookie("access_token")

        mockMvc.put("/api/shelter/workeraccess/animal/2") {
            cookie?.let { cookie(it) }
            content = objectMapper.writeValueAsString(testAnimal1)
            contentType = (MediaType.APPLICATION_JSON)
        }
            .andExpect { status { isForbidden() } }
    }

    @Test
    fun shouldUpdateAnimalHealthWithWorkerAccess() {
        val testAnimal1 =
            AnimalEntity(id = 1, name = "Puffers", age = 10, health = 80, breed = "european burmese", type = "cat")

        val loggedInUser = mockMvc.post("/api/authentication/user/login") {
            contentType = MediaType.APPLICATION_JSON
            content = "{\"email\": \"workuser@workuser.com\", \"password\": \"pirate\"}"
        }
            .andExpect { status { isOk() } }
            .andReturn()

        val cookie = loggedInUser.response.getCookie("access_token")

        mockMvc.patch("/api/shelter/workeraccess/animal/1/health") {
            cookie?.let { cookie(it) }
            content = objectMapper.writeValueAsString(testAnimal1)
            contentType = (MediaType.APPLICATION_JSON)
        }
            .andExpect { status { isOk() } }
    }

    @Test
    fun shouldNotUpdateAnimalHealthWithUserAccess() {
        val testAnimal1 =
            AnimalEntity(id = 1, name = "Puffers", age = 10, health = 80, breed = "european burmese", type = "cat")

        val loggedInUser = mockMvc.post("/api/authentication/user/login") {
            contentType = MediaType.APPLICATION_JSON
            content = "{\"email\": \"user@user.com\", \"password\": \"pirate\"}"
        }
            .andExpect { status { isOk() } }
            .andReturn()

        val cookie = loggedInUser.response.getCookie("access_token")

        mockMvc.patch("/api/shelter/workeraccess/animal/1/health") {
            cookie?.let { cookie(it) }
            content = objectMapper.writeValueAsString(testAnimal1)
            contentType = (MediaType.APPLICATION_JSON)
        }
            .andExpect { status { isForbidden() } }
    }

    @Test
    fun shouldDeleteAnimalWithWorkerAccess() {
        val loggedInUser = mockMvc.post("/api/authentication/user/login") {
            contentType = MediaType.APPLICATION_JSON
            content = "{\"email\": \"workuser@workuser.com\", \"password\": \"pirate\"}"
        }
            .andExpect { status { isOk() } }
            .andReturn()

        val cookie = loggedInUser.response.getCookie("access_token")

        mockMvc.delete("/api/shelter/workeraccess/animal/1/delete") {
            cookie?.let { cookie(it) }
        }
            .andExpect { status { isOk() } }

        mockMvc.get("/api/shelter/useraccess/animal/1") {
            cookie?.let { cookie(it) }
        }
            .andExpect { status { isNotFound() } }
    }

    @Test
    fun shouldNotDeleteAnimalWithUserAccess() {
        val loggedInUser = mockMvc.post("/api/authentication/user/login") {
            contentType = MediaType.APPLICATION_JSON
            content = "{\"email\": \"user@user.com\", \"password\": \"pirate\"}"
        }
            .andExpect { status { isOk() } }
            .andReturn()

        val cookie = loggedInUser.response.getCookie("access_token")

        mockMvc.delete("/api/shelter/workeraccess/animal/1/delete") {
            cookie?.let { cookie(it) }
        }
            .andExpect { status { isForbidden() } }
    }
}