package com.smhrd.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ImageRequestDto {
	
	@NotBlank(message = "프롬프트를 입력해주세요")
    private String textPrompt;
	@NotBlank(message = "프롬프트를 입력해주세요")
    private String stylePrompt;
	@NotBlank(message = "프롬프트를 입력해주세요")
    private String bgPrompt;
//	@NotNull(message = "사이즈를 선택해주세요")
    private Integer size;
//    private String modelName;  // 고정 값 
//    private String modelVersion; // 고정 값
}
