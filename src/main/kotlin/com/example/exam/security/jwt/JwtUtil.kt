package com.example.exam.security.jwt

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.interfaces.DecodedJWT
import org.springframework.http.HttpStatus
import org.springframework.security.core.userdetails.User
import org.springframework.web.bind.annotation.ResponseStatus
import java.util.*

object JwtUtil {
    private const val secret = "i_know_that_i_cant_store_this_here..."
    private val algorithm = Algorithm.HMAC256(secret)

    fun createToken(user: User, issuer: String): String {
        return JWT.create()
            .withSubject(user.username)
            //token to last for 30 minutes
            .withExpiresAt(Date(System.currentTimeMillis() + 10 * 60 * 60 * 30))
            .withIssuer(issuer)
            .withClaim("authorities", user.authorities.map { it.authority })
            .sign(algorithm)
    }

    fun decodeToken(token: String): DecodedJWT {
        val jwtVerifier = JWT.require(algorithm).build()
        val jwt = JWT.decode(token)
        if (!jwt.expiresAt.before(Date())) {
            return jwtVerifier.verify(token)
        }
        throw TokenExpired()
    }
}

@ResponseStatus(value = HttpStatus.UNAUTHORIZED, reason = "Token has expired")
class TokenExpired : RuntimeException()