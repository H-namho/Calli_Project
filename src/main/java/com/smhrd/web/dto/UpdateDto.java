package com.smhrd.web.dto;

import jakarta.validation.constraints.Email;
import lombok.Data;

@Data
public class UpdateDto {
	
	private String loginPw;
	private String userName;
	@Email
	private String userEmail;
	private String userPhone;	
}
