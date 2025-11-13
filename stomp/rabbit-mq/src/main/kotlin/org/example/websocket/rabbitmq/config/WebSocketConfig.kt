package org.example.websocket.rabbitmq.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration
import org.springframework.messaging.simp.config.MessageBrokerRegistry
import org.springframework.util.AntPathMatcher
import org.springframework.web.socket.config.annotation.EnableWebSocket
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker
import org.springframework.web.socket.config.annotation.StompEndpointRegistry
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer


@Configuration
@EnableWebSocket
@EnableWebSocketMessageBroker
class WebSocketConfig: WebSocketMessageBrokerConfigurer {

  val log = logger()

  @Value("\${spring.rabbitmq.host}")
  lateinit var RELAY_HOST: String

  @Value("\${spring.rabbitmq.stomp-port}")
  lateinit var RELAY_PORT: Number

  @Value("\${spring.rabbitmq.username}")
  lateinit var RELAY_USERNAME: String

  @Value("\${spring.rabbitmq.password}")
  lateinit var RELAY_PASSWORD: String

  override fun registerStompEndpoints(registry: StompEndpointRegistry) {
    registry.addEndpoint("/ws/stomp")
      .setAllowedOriginPatterns("*") // 실제 환경에선 API 서버 도메인만 허용
      .withSockJS() // JS 라이브러리. for old browser, 모바일 환경을 위해서는 지워야한다 ?
  }

  override fun configureMessageBroker(registry: MessageBrokerRegistry) {
    log.info("web socket config relay port : ${RELAY_PORT.toInt()}")
    // 외부의 RabbitMQ 브로커를 사용하기 위한 설정
    registry.enableStompBrokerRelay("/queue", "/topic", "/exchange", "/amq/queue")
      .setRelayHost(RELAY_HOST)
      .setRelayPort(RELAY_PORT.toInt())
      .setVirtualHost("/") // 필요시 /chat , /mainboard, /game 과 같은 식으로 도메인 나누듯이 호스트랑 분배해서 독립적 컨트롤 가능하다
      .setClientLogin(RELAY_USERNAME) // spring 서버가 클라이언트 같은 역할을 수행 (pub/sub 거의 채팅에서 사용)
      .setClientPasscode(RELAY_PASSWORD);
    // 클라이언트가 메시지를 보낼 때 사용할 prefix 설정 (/pub -> @MessageMapping 에 자동으로 매핑됨)
    registry.setApplicationDestinationPrefixes("/pub");
    registry.setPathMatcher(AntPathMatcher(".")); // URL을 / -> .으로

  }
}