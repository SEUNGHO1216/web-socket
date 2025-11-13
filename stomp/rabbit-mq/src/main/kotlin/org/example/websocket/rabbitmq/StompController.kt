package org.example.websocket.rabbitmq

import org.example.websocket.rabbitmq.config.logger
import org.example.websocket.rabbitmq.dto.ChatMessageDTO
import org.springframework.messaging.handler.annotation.DestinationVariable
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.handler.annotation.SendTo
import org.springframework.stereotype.Controller

@Controller
class StompController(
  private val chatProducer: ChatProducer
) {

  val log = logger()

  @MessageMapping("chat.room.{roomId}")
  fun publishMessage(@DestinationVariable roomId: String, chatMessage: ChatMessageDTO) {
    log.info("chat room Id >> $roomId // message >> ${chatMessage.message}")
    chatProducer.sendMessage(roomId, chatMessage)
  }

  @MessageMapping("chat.public")
  @SendTo("/exchange/chat.fanoutExchange") // 이것은 정적인 경로에만 가능. 동적으로 roomId 를 부여할 땐 미사용
  fun publishMessageToAll(chatMessage: ChatMessageDTO): ChatMessageDTO {
    log.info("chat  message >> ${chatMessage.message}")
//    chatProducer.sendMessagePublic(chatMessage) // 이렇게 해도 된다. 단 response void 로 변경하고 @SendTo 는 주석한다.
    return chatMessage
  }
}