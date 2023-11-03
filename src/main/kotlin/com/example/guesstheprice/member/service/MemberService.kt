package com.example.guesstheprice.member.service

interface MemberService {
    fun login(email: String, password: String): Boolean
}