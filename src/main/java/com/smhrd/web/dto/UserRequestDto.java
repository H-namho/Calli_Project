package com.smhrd.web.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class UserRequestDto {

	@NotBlank(message = "아이디를 입력해주세요")
	@Pattern(regexp = "^[A-Za-z]+$", message = "아이디는 영문만 가능합니다.")
	private String loginId;
	@NotBlank(message = "비밀번호를 입력해주세요")
	private String loginPw;
	@NotBlank(message = "이름을 입력해주세요")
	private String userName;
	@NotBlank(message = "이메일을 입력해주세요")
	@Email(message = "지원하지 않는 이메일 형식입니다")
	private String userEmail;
	@NotBlank(message = "휴대전화번호를 입력해주세요")
	private String userPhone;

}
	