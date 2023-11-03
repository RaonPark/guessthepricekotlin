package com.example.guesstheprice.member.controller

import com.example.guesstheprice.config.JwtTokenProvider
import com.example.guesstheprice.member.entity.Member
import com.example.guesstheprice.member.service.MemberService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController(value = "memberController")
class MemberController @Autowired constructor(
    private val jwtTokenProvider: JwtTokenProvider,
    private val memberService: MemberService,
    private val redisTemplate: RedisTemplate<String, Any>
) {

    @GetMapping("/token")
    fun generateToken(): String {
        val claims = mutableMapOf<String, Any>()
        val memberId = UUID.randomUUID()
        claims["member_id"] = memberId
        claims["email"] = "kotlin@spring.io"
        val token = jwtTokenProvider.generateAccessToken(claims)
        // redisTemplate.opsForValue().setIfAbsent(memberId.toString(), token)
        return token
    }

    @GetMapping("/main")
    fun mainPage(): String {
        return "main"
    }
}