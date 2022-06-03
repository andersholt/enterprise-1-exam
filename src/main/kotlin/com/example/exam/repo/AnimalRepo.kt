package com.example.exam.repo

import com.example.exam.model.AnimalEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface AnimalRepo : JpaRepository<AnimalEntity, Long> {
    fun findByName(name: String): MutableList<AnimalEntity?>
}