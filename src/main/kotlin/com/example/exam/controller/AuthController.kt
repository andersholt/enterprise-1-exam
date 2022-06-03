package com.example.exam.controller

import com.example.exam.model.AuthorityEntity
import com.example.exam.model.UserEntity
import com.example.exam.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.support.ServletUriComponentsBuilder
import java.net.URI
import java.security.InvalidParameterException

@RestController
@RequestMapping("/api")
class AuthController(@Autowired private val userService: UserService) {
    @GetMapping("/user/authorized/authority/all")
    fun getAuthorities(): ResponseEntity<List<AuthorityEntity>> {
        return ResponseEntity.ok().body(userService.getAuthorities())
    }

    @PostMapping("/authentication/user/register")
    fun registerUser(@RequestBody newUserInfo: NewUserInfo): ResponseEntity<UserEntity> {
        val createdUser = userService.registerUser(newUserInfo)
        val uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/register").toUriString())
        return ResponseEntity.created(uri).body(createdUser)
    }

    @GetMapping("/user/authorized/all")
    fun getUsers(): ResponseEntity<List<UserEntity>> {
        return ResponseEntity.ok().body(userService.getUsers())
    }

    @GetMapping("/user/authorized/{email}")
    fun getUserByEmail(@PathVariable email: String?): ResponseEntity<UserEntity> {
        if (email == null) throw InvalidParameterException()
        val user = userService.getUserByEmail(email) ?: throw UserNotFound()
        return ResponseEntity.ok().body(user)
    }

    @DeleteMapping("/user/authorized/{userId}/delete")
    fun deleteUser(@PathVariable userId: Long?) {
        if (userId == null) throw InvalidParameterException()
        //Throwing USERNOTFOUND() ONLY FOR ADMIN ACCESS
        if (!userService.deleteUser(userId)) throw UserNotFound()
    }

    @PostMapping("/user/authorized/workuser/register")
    fun createWorkUser(@RequestBody newUserInfo: NewUserInfo): ResponseEntity<UserEntity> {
        val createdUser = userService.registerWorkUser(newUserInfo)
        val uri = URI.create(
            ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/authorized/workuser/register").toUriString()
        )
        return ResponseEntity.created(uri).body(createdUser)
    }

    @PutMapping("/user/authorized/user/{userId}")
    fun updateAnimal(@PathVariable userId: Long?, @RequestBody newUserInfo: NewUserInfo?): ResponseEntity<UserEntity> {
        when {
            userId == null -> throw InvalidParameterException()
            newUserInfo == null -> throw InvalidParameterException()
            else -> {
                val user = userService.updateUser(userId, newUserInfo) ?: throw UserNotFound()
                return ResponseEntity.ok().body(user)
            }
        }
    }
}

data class NewUserInfo(val email: String, val password: String)

//ONLY FOR ADMIN ACCESS
@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "User not found")
class UserNotFound : RuntimeException()