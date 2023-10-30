package com.example.guesstheprice.chat.controller

import com.example.guesstheprice.chat.dto.MessagingChatRoomRequest
import com.example.guesstheprice.chat.service.impl.RedisMessageSubscriber
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.stereotype.Controller

@Controller
class ChatController(
    private val messageSubscriber: RedisMessageSubscriber
) {
    @MessageMapping("/chat/message")
    fun messageToRoom(messageChatRoomRequest: MessagingChatRoomRequest) {

    }
}