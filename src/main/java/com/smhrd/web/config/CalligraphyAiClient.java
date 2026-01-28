package com.smhrd.web.config;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.smhrd.web.dto.GenerateRequestDto;
import com.smhrd.web.dto.GenerateResponseDto;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CalligraphyAiClient {

    private final WebClient fastApiWebClient;

    public GenerateResponseDto generate(GenerateRequestDto request) {
    	return fastApiWebClient.post()
    	        .uri("/api/calligraphy/generate")
    	        .bodyValue(request)
    	        .retrieve()
    	        .onStatus(s -> s.isError(), resp ->
    	            resp.bodyToMono(String.class)
    	                .map(body -> new RuntimeException("FastAPI error: " + resp.statusCode() + " body=" + body))
    	        )
    	        .bodyToMono(GenerateResponseDto.class)
    	        .block();
    	}
}