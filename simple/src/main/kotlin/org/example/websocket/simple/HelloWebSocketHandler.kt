package org.example.websocket.simple

import org.springframework.web.socket.CloseStatus
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketSession
import org.springframework.web.socket.handler.TextWebSocketHandler

// 클라이언트에서 받은 메시지를 처리하는 핸들러
class HelloWebSocketHandler : TextWebSocketHandler() {
  override fun afterConnectionEstablished(session: WebSocketSession) {
    println(">>> Client Connected : ${session.id}")
  }

  override fun handleTextMessage(session: WebSocketSession, message: TextMessage) {
    println(">>> Receive Message : $message")
  }

  override fun handleTransportError(session: WebSocketSession, exception: Throwable) {
    println(">>> Transport Error : ${session.id}, ${exception.message}, ${exception.stackTrace}")
  }

  override fun afterConnectionClosed(session: WebSocketSession, closeStatus: CloseStatus) {
    println(">>> Connection Closed : ${session.id}")
  }

  // WebSocket 에서 하나의 메시지가 너무 클 경우, 이를 여러 개의 작은 조각으로 나눠서 처리할 수 있어.
  // 이때 클라이언트가 부분 메시지(Partial Message)를 보낼 수도 있고, 서버가 이를 처리할 수도 있는데, 그 여부를 설정하는 게 supportsPartialMessages()야.
  override fun supportsPartialMessages(): Boolean {
    return false
  }

}
