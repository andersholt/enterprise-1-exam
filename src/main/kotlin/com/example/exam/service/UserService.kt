package com.example.exam.service

import com.example.exam.controller.NewUserInfo
import com.example.exam.model.AuthorityEntity
import com.example.exam.model.UserEntity
import com.example.exam.repo.AuthorityRepo
import com.example.exam.repo.UserRepo
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service

@Service
class UserService(@Autowired private val userRepo: UserRepo, @Autowired private val authorityRepo: AuthorityRepo) :
    UserDetailsService {
    override fun loadUserByUsername(username: String?): UserDetails {
        username?.let { it ->
            val user = userRepo.findByEmail(it)
            if (user != null) {
                return User(
                    user.email,
                    user.password,
                    user.authorities.map { authority -> SimpleGrantedAuthority(authority.authorityName) })
            }
        }
        throw Exception("Wrong username or password")
    }

    fun getAuthorities(): List<AuthorityEntity> {
        return authorityRepo.findAll()
    }

    fun getUsers(): List<UserEntity> {
        return userRepo.findAll()
    }

    fun registerUser(newUserInfo: NewUserInfo): UserEntity {
        val newUser =
            UserEntity(email = newUserInfo.email, password = BCryptPasswordEncoder().encode(newUserInfo.password))
        val authority = getAuthority("USER")
        if (authority != null) {
            newUser.authorities.add(authority)
        }
        return userRepo.save(newUser)
    }

    fun deleteUser(id: Long): Boolean {
        //Checking if admin is trying to delete default adminuser which has primarykey of 1
        if (userRepo.existsById(id) && id != 1L) {
            userRepo.deleteById(id)
            return true
        }
        return false
    }

    fun getAuthority(name: String): AuthorityEntity? {
        return authorityRepo.getByAuthorityName(name)
    }

    fun getUserByEmail(email: String): UserEntity? {
        return userRepo.findByEmail(email)
    }

    fun registerWorkUser(newUserInfo: NewUserInfo): UserEntity {
        val newUser =
            UserEntity(email = newUserInfo.email, password = BCryptPasswordEncoder().encode(newUserInfo.password))
        val userAuthority = getAuthority("USER")
        val workerAuthority = getAuthority("WORKUSER")

        if (userAuthority != null && workerAuthority != null) {
            newUser.authorities.addAll(listOf(workerAuthority, userAuthority))
        }
        return userRepo.save(newUser)

    }

    fun updateUser(userId: Long, newUserInfo: NewUserInfo): UserEntity? {
        val user = UserEntity(
            id = userId,
            email = newUserInfo.email,
            password = BCryptPasswordEncoder().encode(newUserInfo.password)
        )
        if (user.id?.let { userRepo.existsById(it) } == true) {
            return userRepo.save(user)
        }
        return null
    }
}