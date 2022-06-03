package com.example.exam.integrationtest.dbintegration

import com.example.exam.model.AnimalEntity
import com.example.exam.service.AnimalService
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.context.annotation.Import
import org.springframework.test.context.ActiveProfiles


@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
@Import(AnimalService::class)
class AnimalDbIntegrationTest(@Autowired private val animalService: AnimalService) {
    @Test
    fun shouldGetAnimals() {
        val result = animalService.getAnimals()
        assert(result.size == 3)
    }

    @Test
    fun createAndFindAnimal() {
        val testAnimal1 = AnimalEntity(name = "Puffers", age = 2, health = 80, breed = "european burmese", type = "cat")
        val createdAnimal = animalService.createAnimal(testAnimal1)
        assert(createdAnimal.name == "Puffers")
        val foundAnimal = testAnimal1.id?.let { animalService.getAnimal(it) }
        assert(foundAnimal?.name == createdAnimal.name)
        assert(foundAnimal?.health == createdAnimal.health)
    }

    @Test
    fun createAnimalAndFindByName() {
        val testAnimal1 =
            AnimalEntity(name = "Snuffers", age = 4, health = 80, breed = "european burmese", type = "cat")
        val createdAnimal = animalService.createAnimal(testAnimal1)
        assert(createdAnimal.name == "Snuffers")
        val foundAnimal = animalService.getAnimalsByName("Snuffers")[0]
        assert(foundAnimal?.name == createdAnimal.name)
        assert(foundAnimal?.health == createdAnimal.health)
    }
}