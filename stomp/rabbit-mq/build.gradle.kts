tasks.getByName("bootJar") {
  enabled = true
}
tasks.getByName("jar") {
  enabled = false
}

dependencies {
  //RabbitMQ
  implementation("org.springframework.boot:spring-boot-starter-amqp")
  implementation("org.springframework.boot:spring-boot-starter-reactor-netty")

  runtimeOnly("io.netty:netty-resolver-dns-native-macos:4.1.104.Final:osx-aarch_64") // MacOS Silicon 라이브러리 누락 문제
  testImplementation("org.springframework.amqp:spring-rabbit-test")
}

