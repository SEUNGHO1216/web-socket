package org.example.websocket.rabbitmq

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class StompRabbitMqApplication

fun main(args: Array<String>) {
  runApplication<StompRabbitMqApplication>(*args)
}
