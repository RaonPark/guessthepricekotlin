package com.example.guesstheprice.member.entity

import com.example.guesstheprice.chat.entity.ChatRoom
import jakarta.persistence.*

@Entity
class ChatRoomAdmin(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chatRoomAuthoritiesId")
    val id: Long?,

    @Column
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "memberId")
    var roomAdmin: Member,

    @Column
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chatRoomId")
    var chatRoom: ChatRoom,

    @Column
    var authorities: MutableList<ChatRoomAuthorities> = mutableListOf(ChatRoomAuthorities.KICK_OUT, ChatRoomAuthorities.ANNOUNCEMENT)
)