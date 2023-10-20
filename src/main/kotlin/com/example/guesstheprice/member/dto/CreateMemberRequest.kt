package com.example.guesstheprice.member.dto

data class CreateMemberRequest(
    val email: String,
    val password: String,
    val username: String,
    val address: String,
    val birthday: String
)
