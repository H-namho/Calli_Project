package com.smhrd.web.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReviewResponseDto {
	private Integer reviewId;
	private Integer calliId; // 어떤 이미지 리뷰인지
	private String maskedUserName; // 김*수
	private int rating; // 1~5
	private String content; // 리스트용 미리보기
	private LocalDateTime reviewAt; // 작성시간
}
