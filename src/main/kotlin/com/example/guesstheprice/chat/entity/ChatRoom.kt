package com.example.guesstheprice.chat.entity

import com.example.guesstheprice.common.BaseTimeEntity
import com.example.guesstheprice.member.entity.Member
import jakarta.persistence.*

@Entity
class ChatRoom(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long?,

    @Column(nullable = false)
    var roomName: String,

    @Column(nullable = false)
    var numberOfMembers: Int,

    @Column(nullable = false)
    var roomThumbnail: String,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "memberId")
    var roomAdmin: Member,
) : BaseTimeEntity()