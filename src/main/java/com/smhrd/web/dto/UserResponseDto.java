package com.smhrd.web.dto;

import lombok.Data;

@Data
public class UserResponseDto {

	private String loginId; // 회원id
	private String userName; // 이름
	private String userEmail; // 이메일
	private String userPhone; // 폰넘버
	private String userRole; // role
	private int balance; // 잔액
	private int freeToken; // 공짜토큰
}
