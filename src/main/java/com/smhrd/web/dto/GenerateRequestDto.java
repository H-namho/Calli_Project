package com.smhrd.web.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter @Builder
@NoArgsConstructor
@AllArgsConstructor
public class GenerateRequestDto {
    private String prompt;
    private String stylePrompt;
    private String bgPrompt;
    private Integer width;
    private Integer height;
    private Integer seed;
}