package com.order_manager.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.cloud.openfeign.EnableFeignClients;

@Configuration
@EnableFeignClients(basePackages = "com.order_manager.client") // Сканируем Feign-клиенты в другом пакете
public class FeignConfig {
}
