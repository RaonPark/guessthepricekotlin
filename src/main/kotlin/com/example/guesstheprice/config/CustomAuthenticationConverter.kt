package com.example.guesstheprice.config

import org.springframework.core.convert.converter.Converter
import org.springframework.security.authentication.AbstractAuthenticationToken
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter

// 이 컨버터는 AbstractAuthenticationToken을 Jwt로 컨버팅을 해준다.
class CustomAuthenticationConverter: Converter<Jwt, AbstractAuthenticationToken> {

    private val jwtAuthenticationConverter = JwtAuthenticationConverter()

    override fun convert(source: Jwt): AbstractAuthenticationToken? {
        val token = jwtAuthenticationConverter.convert(source)
        val authorities = token!!.authorities
        return MemberAuthenticationToken(source, authorities)
    }
}
