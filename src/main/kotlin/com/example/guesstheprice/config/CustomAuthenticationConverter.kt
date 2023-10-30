package com.example.guesstheprice.config

import org.springframework.core.convert.converter.Converter
import org.springframework.security.authentication.AbstractAuthenticationToken
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter

class CustomAuthenticationConverter: Converter<Jwt, AbstractAuthenticationToken> {

    private val jwtAuthenticationConverter = JwtAuthenticationConverter()

    override fun convert(source: Jwt): AbstractAuthenticationToken? {
        val token = jwtAuthenticationConverter.convert(source)
        val authorities = token!!.authorities
        return convert(source, authorities)
    }

    private fun convert(jwt: Jwt, authorities: Collection<GrantedAuthority>): AbstractAuthenticationToken {
        return MemberAuthenticationToken(jwt, authorities)
    }
}
