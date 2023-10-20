package com.example.guesstheprice.member.entity

import com.example.guesstheprice.common.BaseTimeEntity
import com.example.guesstheprice.member.dto.CreateMemberRequest
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import java.util.UUID

@Entity
class Member(
    @Id
    @Column(name = "memberId")
    @GeneratedValue(strategy = GenerationType.UUID)
    val id: UUID?,

    @Column(nullable = false)
    var email: String,

    @Column(nullable = false)
    var password: String,

    @Column(nullable = false)
    var username: String,

    @Column(nullable = false)
    var address: String,

    @Column(nullable = false)
    var birthday: String,
): BaseTimeEntity() {
    companion object {

    }
}