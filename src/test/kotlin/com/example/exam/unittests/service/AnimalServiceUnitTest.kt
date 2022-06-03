package com.example.exam.unittests.service

import com.example.exam.model.AnimalEntity
import com.example.exam.repo.AnimalRepo
import com.example.exam.service.AnimalService
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Test

class AnimalServiceUnitTest {
    private val animalRepo = mockk<AnimalRepo>()
    private val animalService = AnimalService(animalRepo)

    @Test
    fun shouldGetAllAnimals() {
        val testAnimal1 =
            AnimalEntity(name = "Puffers", age = 10, health = 80, breed = "european burmese", type = "cat")
        val testAnimal2 = AnimalEntity(name = "Puffers1", age = 13, health = 30, breed = "ragdoll", type = "cat")
        val testAnimal3 = AnimalEntity(name = "Puffers2", age = 9, health = 60, breed = "ragdoll", type = "cat")
        every { animalRepo.findAll() } answers {
            mutableListOf(testAnimal1, testAnimal2, testAnimal3)
        }
        val animals = animalService.getAnimals()
        assert(animals.size == 3)
        assert(animals.first { it.name == "Puffers" }.health == 80)
        assert(animals.containsAll(listOf(testAnimal2, testAnimal3)))
    }


    @Test
    fun shouldGetAnimalsByName() {
        val testAnimal1 =
            AnimalEntity(name = "Puffers", age = 10, health = 80, breed = "european burmese", type = "cat")
        val testAnimal2 = AnimalEntity(name = "Puffers", age = 2, health = 80, breed = "european burmese", type = "cat")
        every { animalRepo.findByName("Puffers") } answers {
            mutableListOf(testAnimal1, testAnimal2)
        }
        val animals = animalService.getAnimalsByName("Puffers")
        assert(animals.size == 2)
        assert(animals.containsAll(listOf(testAnimal1, testAnimal2)))
    }

    @Test
    fun shouldCreateAnimal() {
        val testAnimal1 =
            AnimalEntity(id = 1, name = "Puffers", age = 10, health = 80, breed = "european burmese", type = "cat")

        every { animalRepo.save(testAnimal1) } answers {
            testAnimal1
        }
        val createdAnimal = animalService.createAnimal(testAnimal1)
        assert(createdAnimal.name == "Puffers")
        assert(createdAnimal.breed == "european burmese")
        assert(createdAnimal.alive == true)
    }

    @Test
    fun shouldCreateUpdateAndGetAnimal() {
        val testAnimal1 =
            AnimalEntity(id = 1, name = "Puffers", age = 10, health = 80, breed = "european burmese", type = "cat")
        val testAnimal2 = AnimalEntity(name = "Puffers2", age = 13, health = 30, breed = "ragdoll", type = "cat")

        every { animalRepo.save(testAnimal1) } answers {
            testAnimal1
        }
        every { animalRepo.save(testAnimal2) } answers {
            testAnimal2
        }
        every { animalRepo.getById(1) } answers {
            testAnimal2
        }
        every { animalRepo.existsById(1) } answers {
            true
        }
        val createdAnimal = animalService.createAnimal(testAnimal1)
        assert(createdAnimal.name == "Puffers")
        assert(createdAnimal.breed == "european burmese")
        assert(createdAnimal.alive == true)
        animalService.updateAnimal(1, testAnimal2)

        val editedAnimal = createdAnimal.id?.let { animalService.getAnimal(it) }
        assert(editedAnimal?.name == "Puffers2")
        assert(editedAnimal?.breed == "ragdoll")
        assert(editedAnimal?.age == 13)
        assert(editedAnimal?.health == 30)
    }
}