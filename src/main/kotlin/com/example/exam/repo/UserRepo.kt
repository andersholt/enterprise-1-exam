package com.example.exam.repo

import com.example.exam.model.UserEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepo:JpaRepository<UserEntity, Long> {
    fun findByEmail(email: String): UserEntity?
}