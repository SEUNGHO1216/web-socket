package org.example.websocket.rabbitmq.config

import org.slf4j.LoggerFactory

inline fun <reified T> T.logger() = LoggerFactory.getLogger(T::class.java)!!

class LoggerUtil {

}