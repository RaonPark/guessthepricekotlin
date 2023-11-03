package com.example.guesstheprice.member.service.impl

import com.example.guesstheprice.member.repository.MemberRepository
import com.example.guesstheprice.member.service.MemberService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class MemberServiceImpl @Autowired constructor(
    private val passwordEncoder: PasswordEncoder,
    private val memberRepository: MemberRepository
): MemberService {

    override fun login(email: String, password: String): Boolean {
        val memberInfo = memberRepository.findByEmail(email)
        return passwordEncoder.matches(password, memberInfo.password)
    }
}