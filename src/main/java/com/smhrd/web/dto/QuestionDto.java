package com.smhrd.web.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuestionDto {
	
	private String qtitle;
	private String qcontent;
	private String qcategory;
	
	
}
