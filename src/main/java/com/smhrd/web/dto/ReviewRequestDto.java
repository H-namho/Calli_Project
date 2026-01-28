package com.smhrd.web.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ReviewRequestDto {
	
	@NotNull
	private Integer calliId;
	
	@Min(1)
	@Max(5)
	private int rating;
	@NotNull
	private String content;
	
}
