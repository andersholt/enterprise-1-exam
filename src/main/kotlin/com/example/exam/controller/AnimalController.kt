package com.example.exam.controller

import com.example.exam.model.AnimalEntity
import com.example.exam.service.AnimalService
import com.fasterxml.jackson.databind.JsonNode
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.support.ServletUriComponentsBuilder
import java.net.URI
import java.security.InvalidParameterException


@RestController
@RequestMapping("/api/shelter")
class AnimalController(@Autowired private val animalService: AnimalService) {
    @GetMapping("/useraccess/animal")
    fun getAnimals(): ResponseEntity<List<AnimalEntity>> {
        val animals = animalService.getAnimals()
        return ResponseEntity.ok().body(animals)
    }

    @GetMapping("/useraccess/animal/{animalId}")
    fun getAnimal(@PathVariable animalId: Long?): ResponseEntity<AnimalEntity?> {
        if (animalId == null) throw InvalidParameterException()
        val animal = animalService.getAnimal(animalId)
        if (animal == null) throw AnimalNotFound()
        return ResponseEntity.ok().body(animal)
    }

    @GetMapping("/useraccess/animal/{animalName}/name")
    fun getAnimalsByName(@PathVariable animalName: String?): ResponseEntity<MutableList<AnimalEntity?>> {
        if (animalName == null) throw InvalidParameterException()
        val animalList = animalService.getAnimalsByName(animalName)
        //No need to throw AnimalNotFound here, since getAnimalsByName always returns a mutable list.
        return ResponseEntity.ok().body(animalList)

    }

    @PostMapping("/workeraccess/animal")
    fun createAnimal(@RequestBody animalEntity: AnimalEntity): ResponseEntity<AnimalEntity> {
        val uri = URI.create(
            ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/shelter/workeraccess/animal").toUriString()
        )
        val animal = animalService.createAnimal(animalEntity)
        return ResponseEntity.created(uri).body(animal)
    }

    @PutMapping("/workeraccess/animal/{animalId}")
    fun updateAnimal(
        @PathVariable animalId: Long?,
        @RequestBody animalEntity: AnimalEntity?
    ): ResponseEntity<AnimalEntity?> {
        when {
            animalId == null -> throw InvalidParameterException()
            animalEntity == null -> throw InvalidParameterException()
            else -> {
                val animal = animalService.updateAnimal(animalId, animalEntity) ?: throw AnimalNotFound()
                return ResponseEntity.ok().body(animal)
            }
        }
    }

    @PatchMapping("/workeraccess/animal/{animalId}/health")
    fun updateAnimalHealth(@PathVariable animalId: Long?, @RequestBody params: JsonNode) {
        animalId?.let {
            if (!animalService.updateAnimalHealth(it, params.get("health").asInt())) throw AnimalNotFound()
        }
    }

    @DeleteMapping("/workeraccess/animal/{animalId}/delete")
    fun deleteAnimal(@PathVariable animalId: Long?) {
        animalId?.let {
            if (!animalService.deleteAnimal(it)) throw AnimalNotFound()
        }
    }
}

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Animal not found")
class AnimalNotFound : RuntimeException()