package com.smhrd.web.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AnswerDto {

	private Integer qid;
	private String qtitle;
	private String qcontent;
	private String qcategory;
	private String answer;
	private String writer;
	private String status;
	private LocalDateTime qat;
	private LocalDateTime aat; 
}
