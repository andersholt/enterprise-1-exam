package com.example.exam.unittests.controller

import com.example.exam.model.AnimalEntity
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
import org.springframework.test.web.servlet.put


@WebMvcTest
@AutoConfigureMockMvc(addFilters = false)
class AnimalControllerUnitTest {
    @TestConfiguration
    class ControllerTestConfig {
        @Bean
        fun userService() = mockk<UserService>()

        @Bean
        fun animalService() = mockk<AnimalService>()
    }

    @Autowired
    private lateinit var animalService: AnimalService

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Test
    fun shouldGetAllAnimals() {
        val testAnimal = AnimalEntity(name = "Puffers", age = 10, health = 80, breed = "european burmese", type = "cat")
        every { animalService.getAnimals() } answers {
            mutableListOf(testAnimal)
        }
        mockMvc.get("/api/shelter/useraccess/animal") {
        }
            .andExpect { status { isOk() } }
            .andExpect { content { contentType(MediaType.APPLICATION_JSON) } }
    }

    @Test
    fun shouldGetAnimalsByName() {
        val testAnimal =
            AnimalEntity(name = "Snuffers", age = 10, health = 80, breed = "european burmese", type = "cat", id = 1L)
        every { animalService.getAnimalsByName("Snuffers") } answers {
            mutableListOf(testAnimal)
        }
        mockMvc.get("/api/shelter/useraccess/animal/Snuffers/name") {
        }.andExpect { status { isOk() } }.andExpect { content { contentType(MediaType.APPLICATION_JSON) } }
    }


    @Test
    fun shouldGetAnimalById() {
        val testAnimal =
            AnimalEntity(name = "Snuffers", age = 10, health = 80, breed = "european burmese", type = "cat", id = 1L)
        every { animalService.getAnimal(1) } answers {
            testAnimal
        }
        mockMvc.get("/api/shelter/useraccess/animal/1") {
        }.andExpect { status { isOk() } }.andExpect { content { contentType(MediaType.APPLICATION_JSON) } }
    }

    @Test
    fun shouldDeleteAnimal() {
        every { animalService.deleteAnimal(1) } answers {
            true
        }
        mockMvc.delete("/api/shelter/workeraccess/animal/1/delete") {
        }.andExpect { status { isOk() } }
    }

    @Test
    fun shouldNotUpdateAnimal() {
        every { animalService.updateAnimalHealth(1, 10) } answers {
            false
        }

        mockMvc.put("/api/shelter/workeraccess/animal/1/10") {
        }.andExpect { status { isNotFound() } }
    }

    @Test
    fun shouldNotDeleteAnimal() {
        every { animalService.deleteAnimal(1) } answers {
            false
        }
        mockMvc.delete("/api/shelter/workeraccess/animal/1/delete") {
        }.andExpect { status { isNotFound() } }
    }
}