package com.example.guesstheprice.chat.service

interface MessagePublisher {
    fun publish(message: String)
}