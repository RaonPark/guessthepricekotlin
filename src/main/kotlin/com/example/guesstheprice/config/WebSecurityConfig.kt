package com.example.guesstheprice.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.annotation.Order
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.invoke
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser
import org.springframework.security.oauth2.core.oidc.user.OidcUser
import org.springframework.security.oauth2.core.oidc.user.OidcUserAuthority
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.security.oauth2.core.user.OAuth2UserAuthority
import org.springframework.security.oauth2.jose.jws.MacAlgorithm
import org.springframework.security.oauth2.jwt.JwtDecoder
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder
import org.springframework.security.provisioning.InMemoryUserDetailsManager
import org.springframework.security.web.SecurityFilterChain
import javax.crypto.spec.SecretKeySpec

@Configuration
@EnableWebSecurity
class WebSecurityConfig {

    @Value("\${jwt.secret-key}")
    lateinit var secretKey: String

    @Order(1)
    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http.invoke {
            authorizeHttpRequests {
                authorize("/**", hasAnyRole("USER", "ADMIN"))
                authorize("/token", permitAll)
                authorize("/v3/api-docs/**", hasAnyRole("ADMIN"))
                authorize(anyRequest, authenticated)
            }
            headers {
                frameOptions {
                    sameOrigin
                }
            }
            sessionManagement {
                SessionCreationPolicy.STATELESS
            }
            oauth2Login {
                // userInfoEndpoint에 해당하는 설정으로
                // DefaultOAuth2UserService는 OAuth2UserService를 구현하는 구현체인데, 이 클래스를 커스터마이징하고 싶다면
                // OAuth2UserService를 상속받은 클래스를 하나 만들던가 서비스를 구현하면 된다.
                userInfoEndpoint {

                }
            }
            oauth2ResourceServer {
                jwt {
                    jwtAuthenticationConverter = CustomAuthenticationConverter()
                    authenticationEntryPoint = CustomAuthenticationEntryPoint()
                    accessDeniedHandler = CustomAccessDeniedHandler()
                }
            }
        }

        return http.build()
    }



    @Bean
    fun userDetailsService() : UserDetailsService {
        val manager = InMemoryUserDetailsManager()
        manager.createUser(User.withUsername("user")
            .password("{noop}1234").roles("USER").build())
        manager.createUser(User.withUsername("admin")
            .password("{noop}1234").roles("USER", "ADMIN").build())
        return manager
    }

    @Bean
    fun passwordEncoder() = BCryptPasswordEncoder()

    // JWT Configuration
    @Bean
    fun jwtDecoder(): JwtDecoder {
        val algorithm = MacAlgorithm.HS512
        val secret = SecretKeySpec(secretKey.toByteArray(Charsets.UTF_8), algorithm.name)
        return NimbusJwtDecoder.withSecretKey(secret).build()
    }

    @Bean
    fun jwtTokenProvider(): JwtTokenProvider {
        return JwtTokenProvider(secretKey)
    }
}