package com.example.guesstheprice.chat.entity

import com.example.guesstheprice.common.BaseTimeEntity
import com.example.guesstheprice.member.entity.Member
import jakarta.persistence.*

@Entity
class Chat(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long?,

    @Column(nullable = false)
    var message: String,

    @Column(nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "memberId")
    var sender: Member,

    @Column(nullable = false)
    var isDeleted: Boolean = false,
): BaseTimeEntity()