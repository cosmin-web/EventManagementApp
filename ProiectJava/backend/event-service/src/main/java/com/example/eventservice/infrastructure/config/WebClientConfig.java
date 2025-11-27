package com.example.eventservice.infrastructure.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Bean
    public WebClient clientServiceWebClient(
            @Value("${client.client-service.base-url:http://client-service:8082/api/client-service}") String baseUrl) {

        return WebClient.builder()
                .baseUrl(baseUrl)
                .build();
    }
}

