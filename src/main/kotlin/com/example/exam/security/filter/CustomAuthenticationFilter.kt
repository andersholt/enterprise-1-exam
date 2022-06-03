package com.example.exam.security.filter

import com.example.exam.security.jwt.JwtUtil
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType.APPLICATION_JSON_VALUE
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.userdetails.User
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import java.util.stream.Collectors
import javax.servlet.FilterChain
import javax.servlet.http.Cookie
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class CustomAuthenticationFilter(@Autowired private val authManager: AuthenticationManager): UsernamePasswordAuthenticationFilter() {
    override fun attemptAuthentication(request: HttpServletRequest, response: HttpServletResponse): Authentication {
        val body = request.reader.lines().collect(Collectors.joining())
        val userRequest = jacksonObjectMapper().readValue(body, LoginInfo::class.java)
        val auth = UsernamePasswordAuthenticationToken(userRequest.email, userRequest.password)
        return authManager.authenticate(auth)
    }

    override fun successfulAuthentication(
        request: HttpServletRequest?,
        response: HttpServletResponse,
        chain: FilterChain?,
        authResult: Authentication
    ) {
        val token = JwtUtil.createToken(authResult.principal as User, request?.requestURL.toString())
        val cookie = Cookie("access_token", token)
        response.contentType = APPLICATION_JSON_VALUE
        response.addCookie(cookie)
    }
}

data class LoginInfo(val email: String, val password: String)