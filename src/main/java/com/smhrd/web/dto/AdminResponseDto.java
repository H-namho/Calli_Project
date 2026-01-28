package com.smhrd.web.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class AdminResponseDto {

	private Integer userId;
	private String userName;
	private String userEmail;
	private String status;
	private Long humanAt;
	private LocalDateTime lastAt;
	
}
