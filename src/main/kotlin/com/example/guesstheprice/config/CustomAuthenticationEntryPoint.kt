package com.example.guesstheprice.config

import com.nimbusds.oauth2.sdk.token.BearerTokenError
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.security.core.AuthenticationException
import org.springframework.security.oauth2.core.OAuth2AuthenticationException
import org.springframework.security.oauth2.core.OAuth2Error
import org.springframework.security.web.AuthenticationEntryPoint
import java.nio.charset.Charset

class CustomAuthenticationEntryPoint: AuthenticationEntryPoint {
    override fun commence(
        request: HttpServletRequest?,
        response: HttpServletResponse?,
        authException: AuthenticationException?
    ) {
        val error: OAuth2Error = when(authException) {
            OAuth2AuthenticationException::class -> OAuth2Error(authException.message)
            else -> OAuth2Error(BearerTokenError.INVALID_TOKEN.code)
        }

        response!!.contentType = MediaType.APPLICATION_JSON_VALUE
        response.status = HttpStatus.UNAUTHORIZED.value()
        response.characterEncoding = Charset.defaultCharset().name()

        response.writer.write(error.errorCode)
        response.writer.flush()
    }
}