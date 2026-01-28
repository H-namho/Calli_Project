package com.smhrd.web.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChangePwDto {

	@NotBlank(message = "아이디를 입력해주세요")
	private String loginId;
	@NotBlank(message = "이름을 입력해주세요")
	private String userName;
	@NotBlank(message = "이메일을 입력해주세요")
	private String userEmail;
	@NotBlank(message = "비밀번호를 입력해주세요")
	private String newPw;

}
