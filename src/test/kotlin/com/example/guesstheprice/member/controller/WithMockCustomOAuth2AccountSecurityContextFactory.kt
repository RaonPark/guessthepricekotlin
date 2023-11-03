package com.example.guesstheprice.member.controller

import org.springframework.security.core.context.SecurityContext
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken
import org.springframework.security.oauth2.core.user.DefaultOAuth2User
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.security.oauth2.core.user.OAuth2UserAuthority
import org.springframework.security.test.context.support.WithSecurityContextFactory

class WithMockCustomOAuth2AccountSecurityContextFactory: WithSecurityContextFactory<WithMockCustomOAuth2User> {
    override fun createSecurityContext(annotation: WithMockCustomOAuth2User): SecurityContext {
        val context = SecurityContextHolder.createEmptyContext()

        val attributes = HashMap<String, Any>()
        attributes["username"] = annotation.username
        attributes["email"] = annotation.email
        attributes["password"] = annotation.password
        attributes["roles"] = annotation.roles

        val userList = mutableListOf<OAuth2UserAuthority>()
        annotation.roles.forEach { role ->
            userList.add(OAuth2UserAuthority(role, attributes))
        }

        val principal = DefaultOAuth2User(
            userList,
            attributes,
            annotation.username
        )

        val token = OAuth2AuthenticationToken(
            principal,
            principal.authorities,
            annotation.registrationId
        )

        context.authentication = token
        return context
    }
}
