package com.example.guesstheprice.chat.service.impl

import com.example.guesstheprice.chat.entity.Chat
import com.example.guesstheprice.chat.entity.ChatType
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.data.redis.connection.Message
import org.springframework.data.redis.connection.MessageListener
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.stereotype.Service

@Service
class RedisMessageSubscriber (
    private val objectMapper: ObjectMapper,
    private val redisTemplate: RedisTemplate<String, Object>,
    private val messagingTemplate: SimpMessagingTemplate
): MessageListener {

    override fun onMessage(message: Message, pattern: ByteArray?) {
        val publishedMessage = redisTemplate.stringSerializer.deserialize(message.body)

        val deserializedMessage = objectMapper.readValue(publishedMessage, Chat::class.java)

        if(deserializedMessage.chatType == ChatType.CHAT) {
            messagingTemplate.convertAndSend("/sub/chat/room", deserializedMessage)
        }
    }
}