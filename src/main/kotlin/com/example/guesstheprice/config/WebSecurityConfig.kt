package com.example.guesstheprice.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.annotation.Order
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.config.web.server.ServerHttpSecurity.http
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.provisioning.InMemoryUserDetailsManager
import org.springframework.security.web.SecurityFilterChain

@Configuration
@EnableWebSecurity
class WebSecurityConfig {
    @Order(1)
    @Bean
    open fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http.authorizeHttpRequests {
            it.requestMatchers("/", "/swagger-ui/**").permitAll()
                .anyRequest().authenticated()
        }
            .headers { header -> header.frameOptions { frame -> frame.sameOrigin() } }
            .sessionManagement { session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }

        return http.build()!!
    }

    @Bean
    fun userDetailsService() : InMemoryUserDetailsManager {
        val user = User.withUsername("user")
            .password("{noop}userPw")
            .authorities("ROLE_USER")
            .build()
        val admin = User.withUsername("admin")
            .password("{noop}adminPw")
            .authorities("ROLE_ADMIN")
            .build()

        return InMemoryUserDetailsManager(user, admin)
    }

    @Bean
    fun passwordEncoder() = BCryptPasswordEncoder()
}