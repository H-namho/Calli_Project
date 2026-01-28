package com.smhrd.web.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionController {
	
	@ExceptionHandler(MethodArgumentNotValidException.class)  // valid 검증 예외처리
	public ResponseEntity<?> validexception(MethodArgumentNotValidException error){
		String msg = "요청이 올바르지 않습니다"; // 기본 msg
		FieldError fieldError = error.getBindingResult().getFieldError(); // filed에서 검증된 msg중 첫번째 msg
		if(fieldError !=null) { // field에서 검증된 msg가 있다면 
			msg = fieldError.getDefaultMessage(); // 해당 msg 담기
		}
		
		
		return ResponseEntity.badRequest().body(msg); // 400 에러 + msg
	}
	
	@ExceptionHandler(IllegalArgumentException.class) // IllegalArgumentException 핸들러
	public ResponseEntity<?> illegarecption(IllegalArgumentException e){
		String msg = e.getMessage();
		if(msg==null || msg.isBlank()) {
			msg="요청이 올바르지 않습니다";
		}
		return ResponseEntity.badRequest().body(msg);
		
	}
}
