package com.example.guesstheprice.chat.repository

import com.example.guesstheprice.chat.entity.Chat
import jakarta.annotation.Resource
import org.springframework.data.redis.core.HashOperations
import org.springframework.data.redis.core.ListOperations
import org.springframework.data.redis.core.ValueOperations
import org.springframework.stereotype.Repository

@Repository
class ChatRedisRepository {
    @Resource(name = "redisTemplate")
    lateinit var chatHashOperations: HashOperations<Long, String, Chat>

    fun save(chatRoomId: Long, chatKey: String, chat: Chat) {
        chatHashOperations.put(chatRoomId, chatKey, chat)
    }

    fun updateChat(chatRoomId: Long, chatKey: String, chatMessage: String) {
        val chat = chatHashOperations[chatRoomId, chatKey]
        chat!!.message = chatMessage
        chatHashOperations.put(chatRoomId, chatKey, chat)
    }

    fun deleteChat(chatRoomId: Long, chatKey: String) {
        val chat = chatHashOperations[chatRoomId, chatKey]
        chat!!.message = "삭제된 메세지입니다."
        chat.isDeleted = true
        chatHashOperations.put(chatRoomId, chatKey, chat)
    }

    fun selectAllChats(chatRoomId: Long) : MutableMap<String, Chat> = chatHashOperations.entries(chatRoomId)
}