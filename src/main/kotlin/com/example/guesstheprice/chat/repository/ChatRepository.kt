package com.example.guesstheprice.chat.repository

import com.example.guesstheprice.chat.entity.Chat
import org.springframework.data.jpa.repository.JpaRepository

interface ChatRepository : JpaRepository<Chat, Long> {
}