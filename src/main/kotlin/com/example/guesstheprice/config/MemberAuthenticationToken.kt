package com.example.guesstheprice.config

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken
import java.util.*

class MemberAuthenticationToken(
    private val jwt: Jwt,
    private val authorities: Collection<GrantedAuthority>
): JwtAuthenticationToken(jwt, authorities) {

    fun getMemberId(): UUID? = UUID.fromString(jwt.claims["member_id"].toString())
}