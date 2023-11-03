package com.example.guesstheprice.config

import com.nimbusds.jose.JOSEObjectType
import com.nimbusds.jose.JWSAlgorithm
import com.nimbusds.jose.JWSHeader
import com.nimbusds.jose.JWSSigner
import com.nimbusds.jose.crypto.MACSigner
import com.nimbusds.jwt.JWTClaimsSet
import com.nimbusds.jwt.SignedJWT
import java.time.Duration
import java.time.Instant
import java.util.*

class JwtTokenProvider(
    private val secretKey: String
) {
    fun generateAccessToken(claims: Map<String, Any>): String {
        return generateToken(claims, Duration.ofHours(1), "ACCESS")
    }

    fun generateRefreshToken(claims: Map<String, Any>): String {
        return generateToken(claims, Duration.ofHours(1), "REFRESH")
    }

    private fun generateToken(claims: Map<String, Any>, expirationTime: Duration, scope: String): String {
        val signer = MACSigner(secretKey)

        val builder = JWTClaimsSet.Builder()
        claims.forEach {
            builder.claim(it.key, it.value)
        }

        val claimsSet = builder
            .expirationTime(Date.from(Instant.now().plus(expirationTime)))
            .issueTime(Date())
            .claim("scope", scope)
            .build()
        val signedJWT = SignedJWT(JWSHeader.Builder(JWSAlgorithm.HS512)
            .type(JOSEObjectType.JWT)
            .build(), claimsSet)
        signedJWT.sign(signer)

        return signedJWT.serialize()
    }
}