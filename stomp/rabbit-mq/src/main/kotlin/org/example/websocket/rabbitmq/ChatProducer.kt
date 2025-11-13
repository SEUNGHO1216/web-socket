package org.example.websocket.rabbitmq

import org.example.websocket.rabbitmq.config.logger
import org.example.websocket.rabbitmq.dto.ChatMessageDTO
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.stereotype.Component

@Component
class ChatProducer(
  private val rabbitTemplate: RabbitTemplate,
) {
  val log = logger()

  fun sendMessage(roomId: String, chatMessage: ChatMessageDTO) {
    log.info("sending message: $chatMessage")
    rabbitTemplate.convertAndSend("chat.topicExchange", "chat.room.$roomId", chatMessage)
  }

  fun sendMessagePublic(chatMessage: ChatMessageDTO) {
    log.info("sending message: $chatMessage")
    rabbitTemplate.convertAndSend("chat.fanoutExchange", "", chatMessage)
  }
}