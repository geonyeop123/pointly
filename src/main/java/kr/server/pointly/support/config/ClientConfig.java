package kr.server.pointly.support.config;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@EnableFeignClients(basePackages = "kr.server.pointly.infrastructure")
@Configuration
public class ClientConfig {
}
