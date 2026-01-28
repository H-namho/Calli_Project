package com.smhrd.web.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.smhrd.web.dto.CreditRequestDto;
import com.smhrd.web.dto.CreditResponseDto;
import com.smhrd.web.dto.ShowPageDto;
import com.smhrd.web.dto.oauth.UserPrincipalDetails;
import com.smhrd.web.service.CreditService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CreditController { // 아마 없어질 컨트롤러 
	
	private final CreditService creditService;
	
	@PostMapping("/plus")  // 크레딧 충전하기
	public ResponseEntity<?> pluscredit(@AuthenticationPrincipal UserPrincipalDetails user,
			@RequestBody CreditRequestDto dto) {
		Integer userid = user.getEntity().getUserId(); // 로그인할때의 userid 가져오기
		
		creditService.plus(userid,dto);
	
		return ResponseEntity.ok("충전 완료");
	}
	
	@GetMapping("/showpage")
	public ResponseEntity<?> showpay(@AuthenticationPrincipal UserPrincipalDetails user,
								@RequestParam(required = false, defaultValue = "0")int page,
								@RequestParam(required = false, defaultValue = "10")int size){
		Integer userId=user.getEntity().getUserId();
		
		ShowPageDto<CreditResponseDto> result = creditService.showpage(userId, page, size);
		return ResponseEntity.ok(result);
				
	}
//	@PostMapping("/genminus") // 이미지 생성시 크레딧  차감
//	public ResponseEntity<String> genminus(@AuthenticationPrincipal UserPrincipalDetails user) {
//		Integer userid = user.getEntity().getUserId();
//		
//		String msg=creditService.minus(userid,MinusAction.GENERATE, userid);
//		
//		return ResponseEntity.ok("이미지 생성 성공");
//	}
//	
//	@PostMapping("/downminus") // 이미지 다운로드시 크레딧 차감
//	public ResponseEntity<String> downminus(@AuthenticationPrincipal UserPrincipalDetails user){
//		Integer userid = user.getEntity().getUserId();
//		creditService.minus(userid,MinusAction.DOWNLOAD, userid);
//	
//		return ResponseEntity.ok("이미지 다운로드 완료");
//		
//	}
	
}
