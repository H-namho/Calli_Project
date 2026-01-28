package com.smhrd.web.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Bean
    public WebClient fastApiWebClient() {
        return WebClient.builder()
//                .baseUrl("https://premythical-ringless-jewel.ngrok-free.dev")
        		.baseUrl("https://mallie-probeable-luis.ngrok-free.dev")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .codecs(configurer ->
                        configurer.defaultCodecs().maxInMemorySize(10 * 1024 * 1024) // ✅ 10MB
                )
                .build();
    }
}
