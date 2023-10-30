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
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser
import org.springframework.security.oauth2.core.oidc.user.OidcUser
import org.springframework.security.oauth2.core.oidc.user.OidcUserAuthority
import org.springframework.security.oauth2.core.user.OAuth2UserAuthority
import org.springframework.security.oauth2.jose.jws.MacAlgorithm
import org.springframework.security.oauth2.jwt.JwtDecoder
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder
import org.springframework.security.provisioning.InMemoryUserDetailsManager
import org.springframework.security.web.SecurityFilterChain
import javax.crypto.spec.SecretKeySpec

@Configuration
@EnableWebSecurity
@ConfigurationProperties(value = "jwt")
class WebSecurityConfig {

    @Value("\${secret-key}")
    lateinit var secretKey: String

    @Order(1)
    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http.invoke {
            authorizeHttpRequests {
                authorize("/", permitAll)
                authorize("/swagger-ui/**", hasAnyAuthority("ROLE_ADMIN"))
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

    private fun userAuthoritiesMapper(): GrantedAuthoritiesMapper = GrantedAuthoritiesMapper {
        authorities: Collection<GrantedAuthority> ->

        val mappedAuthorities = emptySet<GrantedAuthority>()

        authorities.forEach {
            grantedAuthority ->
            if(grantedAuthority is OidcUserAuthority) {
                val idToken = grantedAuthority.idToken
                val userInfo = grantedAuthority.userInfo
            } else if(grantedAuthority is OAuth2UserAuthority) {
                val userAttributes = grantedAuthority.attributes
            }
        }

        mappedAuthorities
    }

    @Bean
    fun oidcUserService(): OAuth2UserService<OidcUserRequest, OidcUser> {
        val delegate = OidcUserService()

        return OAuth2UserService {
            userRequest ->
            var oidcUser = delegate.loadUser(userRequest)

            val accessToken = userRequest.accessToken
            val mappedAuthorities = HashSet<GrantedAuthority>()

            // TODO("Fetch the authority information from the protected resource using accessToken")
            // TODO("Map the authority information to one or more GrantedAuthority's and add it to mappedAuthorities")
            // TODO("Create a copy of oidcUser but use the mappedAuthorities instead")
            oidcUser = DefaultOidcUser(mappedAuthorities, oidcUser.idToken, oidcUser.userInfo)

            oidcUser
        }
    }

    @Bean
    fun userDetailsService() : UserDetailsService {
        val manager = InMemoryUserDetailsManager()
        manager.createUser(User.withUsername("user")
            .password("{noop}1234").roles("ROLE_USER").build())
        manager.createUser(User.withUsername("admin")
            .password("{noop}1234").roles("ROLE_USER", "ROLE_ADMIN").build())
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
}