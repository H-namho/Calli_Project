package com.smhrd.web.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PreviewResponseDto {
	private String url;
	private int expirerSec;
}
