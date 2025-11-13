package org.example.websocket.rabbitmq.config


import org.springframework.amqp.core.Binding
import org.springframework.amqp.core.BindingBuilder
import org.springframework.amqp.core.FanoutExchange
import org.springframework.amqp.core.Queue
import org.springframework.amqp.core.TopicExchange
import org.springframework.amqp.rabbit.annotation.EnableRabbit
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory
import org.springframework.amqp.rabbit.connection.ConnectionFactory
import org.springframework.amqp.rabbit.core.RabbitAdmin
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


@EnableRabbit
@Configuration
class RabbitMQConfig {

  val log = logger()

  @Value("\${spring.rabbitmq.host}")
  lateinit var HOST: String

  @Value("\${spring.rabbitmq.port}")
  lateinit var AMQP_PORT: Number

  @Value("\${spring.rabbitmq.username}")
  lateinit var USERNAME: String

  @Value("\${spring.rabbitmq.password}")
  lateinit var PASSWORD: String

  @Bean
  fun connectionFactory(): ConnectionFactory {
    log.info("rabbit mq config port : ${AMQP_PORT.toInt()}")

    val cachingConnectionFactory = CachingConnectionFactory(HOST, AMQP_PORT.toInt())
    cachingConnectionFactory.username = USERNAME
    cachingConnectionFactory.setPassword(PASSWORD)
    cachingConnectionFactory.virtualHost = "/"

    return cachingConnectionFactory
  }

  // "chat.queue"라는 이름의 Queue 생성
  @Bean
  fun chatQueue(): Queue {
    return Queue("chat.queue", true) // durable을 true로 제공
  }

  // "public" 하게 모두에게 접근 가능한 큐 생성
  @Bean
  fun broadcastQueue(): Queue {
    return Queue("chat.broadcast", true)
  }

  // 4가지 Binding 전략 중 TopicExchange 전략을 사용. "chat.topicExchange"를 이름으로 지정
  @Bean
  fun chatTopicExchange(): TopicExchange {
    return TopicExchange("chat.topicExchange", true, false)
  }

  // 4가지 Binding 전략 중 FanoutExchange 전략을 사용. "chat.fanoutExchange"를 이름으로 지정
  @Bean
  fun chatFanoutExchange(): FanoutExchange {
    return FanoutExchange("chat.fanoutExchange", true, false)
  }

  // Exchange와 Queue를 연결. "chat.queue"에 "chat.exchange" 규칙을 Binding
  @Bean
  fun chatTopicBinding(chatQueue: Queue?, chatTopicExchange: TopicExchange): Binding {
    return BindingBuilder
      .bind(chatQueue)
      .to(chatTopicExchange)
      .with("*.room.*")
  }

  @Bean
  fun chatFanoutBinding(broadcastQueue: Queue, fanoutExchange: FanoutExchange): Binding {
    return BindingBuilder
      .bind(broadcastQueue)
      .to(fanoutExchange)
  }

  // 여기에 꼭 declare 해서 명시적으로 입력해줘야하는지 아닌지 추후 테스트 필요
  @Bean
  fun rabbitAdmin(broadcastQueue: Queue): RabbitAdmin {
    return RabbitAdmin(connectionFactory())
      .apply{
        declareExchange(chatTopicExchange())
        declareQueue(chatQueue())
        declareBinding(chatTopicBinding(chatQueue(), chatTopicExchange()))

        declareExchange(chatFanoutExchange())
        declareQueue(broadcastQueue())
        declareBinding(chatFanoutBinding(broadcastQueue(),chatFanoutExchange()))
        isAutoStartup = true
      }
  }

  @Bean
  fun rabbitTemplate(): RabbitTemplate {
    val rabbitTemplate = RabbitTemplate(connectionFactory())
    rabbitTemplate.messageConverter = Jackson2JsonMessageConverter()
    return rabbitTemplate
  }
}