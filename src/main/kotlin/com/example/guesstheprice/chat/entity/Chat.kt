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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "memberId")
    var sender: Member,

    @Enumerated(value = EnumType.STRING)
    var chatType: ChatType,

    @Column(nullable = false)
    var isDeleted: Boolean = false,

    @ManyToOne
    @JoinColumn(name = "chatRoomId")
    var chatRoom: ChatRoom,
): BaseTimeEntity()