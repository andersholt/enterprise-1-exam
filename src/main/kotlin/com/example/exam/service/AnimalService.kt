package com.example.exam.service

import com.example.exam.model.AnimalEntity
import com.example.exam.repo.AnimalRepo
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class AnimalService(@Autowired private val animalRepo: AnimalRepo) {
    fun createAnimal(animalEntity: AnimalEntity): AnimalEntity {
        return animalRepo.save(animalEntity)
    }

    fun getAnimal(id: Long): AnimalEntity? {
        val exists = animalRepo.existsById(id)
        if (exists) {
            return animalRepo.getById(id)
        }
        return null
    }

    fun getAnimals(): MutableList<AnimalEntity> {
        return animalRepo.findAll()
    }

    fun updateAnimal(animalId: Long, animalEntity: AnimalEntity): AnimalEntity? {
        animalEntity.id = animalId
        if (animalEntity.id?.let { animalRepo.existsById(it) } == true) {
            return animalRepo.save(animalEntity)
        }
        return null
    }

    fun deleteAnimal(id: Long): Boolean {
        if (animalRepo.existsById(id)) {
            animalRepo.deleteById(id)
            return true
        }
        return false
    }


    fun updateAnimalHealth(animalId: Long, health: Int): Boolean {
        animalRepo.findById(animalId).orElse(null)?.let {
            val newAnimal = AnimalEntity(
                it.id,
                it.name,
                it.type,
                it.breed,
                it.added,
                it.alive,
                it.age,
                health
            )

            animalRepo.save(newAnimal)
            return true
        }
        return false
    }

    fun getAnimalsByName(name: String?): MutableList<AnimalEntity?> {
        name?.let {
            val animal = animalRepo.findByName(it)
            return animal
        }
        return mutableListOf()
    }
}