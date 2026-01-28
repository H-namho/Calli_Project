package com.smhrd.web.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class QuestionResponseDto {
	
	private Integer qid;
	private String qtitle;
	private String qcontent;
	private String qcategory;
	private String answer;
	private String status;
	private LocalDateTime qat;
	private LocalDateTime aat;
	
	
}
