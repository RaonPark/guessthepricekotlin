package com.example.guesstheprice.config

import com.example.guesstheprice.chat.service.MessagePublisher
import com.example.guesstheprice.chat.service.impl.RedisMessagePublisher
import io.lettuce.core.ReadFrom
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.data.redis.RedisProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.connection.RedisSentinelConfiguration
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.listener.ChannelTopic
import org.springframework.data.redis.listener.RedisMessageListenerContainer
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter
import org.springframework.data.redis.serializer.GenericToStringSerializer
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer
import org.springframework.data.redis.serializer.StringRedisSerializer

@Configuration
class RedisConfig {

    @Autowired
    private lateinit var redisProperties: RedisProperties

    @Bean
    fun redisConnectionFactory() : LettuceConnectionFactory {
        val clientConfig = LettuceClientConfiguration.builder()
            .readFrom(ReadFrom.REPLICA_PREFERRED)
            .build()

        val sentinelConfig = RedisSentinelConfiguration()
            .master(redisProperties.sentinel.master)

        redisProperties.sentinel.nodes.forEach {
            node -> sentinelConfig.sentinel(
                node.split(":")[0],
                Integer.valueOf(node.split(":")[1])
            )
        }

        return LettuceConnectionFactory(sentinelConfig, clientConfig)
    }

    @Bean
    fun redisTemplate(): RedisTemplate<String, Any> {
        val redisTemplate = RedisTemplate<String, Any>()
        redisTemplate.keySerializer = StringRedisSerializer()
        redisTemplate.hashKeySerializer = GenericToStringSerializer(Object::class.java)
        redisTemplate.hashValueSerializer = JdkSerializationRedisSerializer()
        redisTemplate.valueSerializer = StringRedisSerializer()
        redisTemplate.connectionFactory = redisConnectionFactory()
        return redisTemplate
    }

    @Bean
    fun messageListenerAdapter(): MessageListenerAdapter {
        return MessageListenerAdapter()
    }

    @Bean
    fun redisContainer(): RedisMessageListenerContainer {
        val container = RedisMessageListenerContainer()
        container.setConnectionFactory(redisConnectionFactory())
        container.addMessageListener(messageListenerAdapter(), chatTopic())
        return container
    }

    @Bean
    fun redisPublisher(): MessagePublisher {
        return RedisMessagePublisher(redisTemplate(), chatTopic())
    }

    @Bean
    fun chatTopic(): ChannelTopic {
        return ChannelTopic("chat")
    }
}