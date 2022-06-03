package com.example.exam.security.filter

import com.example.exam.security.jwt.JwtUtil
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.filter.OncePerRequestFilter
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse


class CustomAuthorizationFilter : OncePerRequestFilter() {
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {

        val token = request.cookies?.firstOrNull { it.name == "access_token" }?.value

        when {
            request.servletPath.contains("/api/login") -> filterChain.doFilter(request, response)
            request.servletPath.contains("/api/register") -> {
                filterChain.doFilter(request, response)
            }
            token.isNullOrBlank() -> filterChain.doFilter(request, response)
            else -> {
                val decodedJwt = JwtUtil.decodeToken(token)
                val email = decodedJwt.subject
                val authority =
                    decodedJwt.getClaim("authorities").asList(String::class.java).map { SimpleGrantedAuthority(it) }
                val authenticationToken = UsernamePasswordAuthenticationToken(email, null, authority)
                SecurityContextHolder.getContext().authentication = authenticationToken
                filterChain.doFilter(request, response)
            }
        }
    }
}