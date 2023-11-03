package com.example.guesstheprice.member.repository

import com.example.guesstheprice.member.entity.Member
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface MemberRepository: JpaRepository<Member, UUID> {
    fun findByEmail(email: String): Member
}