package com.example.guesstheprice.member.controller

import org.springframework.security.test.context.support.WithSecurityContext

@Retention(AnnotationRetention.RUNTIME)
@WithSecurityContext(factory = WithMockCustomOAuth2AccountSecurityContextFactory::class)
annotation class WithMockCustomOAuth2User(
    val username: String = "username",
    val email: String = "mockUser@gmail.com",
    val password: String = "1234",
    val roles: Array<String> = ["ROLE_USER"],
    val registrationId: String = "google",
)