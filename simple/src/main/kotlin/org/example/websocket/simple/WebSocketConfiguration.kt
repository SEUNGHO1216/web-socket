package org.example.websocket.simple

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.socket.WebSocketHandler
import org.springframework.web.socket.config.annotation.EnableWebSocket
import org.springframework.web.socket.config.annotation.WebSocketConfigurer
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry

@EnableWebSocket
@Configuration
class WebSocketConfiguration: WebSocketConfigurer {

  // 웹소켓 클라이언트와의 엔드포인트 지점 설정 및 핸들러 설정
  override fun registerWebSocketHandlers(registry: WebSocketHandlerRegistry) {
    registry.addHandler(simpleWebSocketHandler(), "/hello")
      .setAllowedOrigins("*")
  }

  // 웹소켓 로직 핸들링하는 핸들러 등록
  @Bean
  fun simpleWebSocketHandler(): WebSocketHandler {
    return HelloWebSocketHandler();
  }
}