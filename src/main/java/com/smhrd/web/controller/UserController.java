package com.smhrd.web.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.smhrd.web.dto.ChangePwDto;
import com.smhrd.web.dto.QuestionDto;
import com.smhrd.web.dto.ReviewRequestDto;
import com.smhrd.web.dto.ReviewResponseDto;
import com.smhrd.web.dto.UpdateDto;
import com.smhrd.web.dto.UserRequestDto;
import com.smhrd.web.dto.oauth.UserPrincipalDetails;
import com.smhrd.web.entity.UserEntity;
import com.smhrd.web.service.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api")
public class UserController {
	
	private final UserService service;
	public UserController(UserService service) {
		this.service=service;
	}
	@PostMapping("/chkpw")
	public ResponseEntity<?> chkPw(@RequestBody Map<String,String> req,
									@AuthenticationPrincipal UserPrincipalDetails user){
		Integer userId = user.getEntity().getUserId();
		String loginPw = req.get("loginPw");
		System.out.println(loginPw);
		boolean chk =service.chkpw(userId,loginPw);
		if(!chk) { return ResponseEntity.badRequest().body(Map.of("msg","비밀번호가 일치하지 않습니다"));}
		return ResponseEntity.ok("일치확인");
	}
	
	@PostMapping("/checkid")
	public ResponseEntity<?> checkId(@RequestBody Map<String,String> req){
		
		String loginId = req.get("loginId");
		Map<String, String> map = new HashMap<>();
		
		boolean chkid =service.chkid(loginId);
		if(chkid) { // 아이디가 있을경우
			map.put("msg","이미 사용중인 아이디입니다."); // 아이디 없을경우
			return ResponseEntity.badRequest().body(map);	
		}
		map.put("msg", "사용가능한 아이디입니다"); 
		return ResponseEntity.ok(map);
			
	}
	
	@PostMapping("/join")  // 회원가입
	public ResponseEntity<?> join(@Valid @RequestBody UserRequestDto dto) {
		
		int row = service.join(dto);
		if(row==0) {
			return ResponseEntity.status(409).body(Map.of("msg","회원가입 실패"));	
		}
		return ResponseEntity.status(201).body(Map.of("msg","회원가입 성공"));
		
	}
		
	@PostMapping("/findid")  // 아이디찾기 
	public ResponseEntity<?> findid(@RequestBody Map<String,String> map) {
		
		System.out.println(map.get("userEmail"));
		String loginid = service.findid(map.get("userEmail"));
		if(loginid==null) {	
			return ResponseEntity.status(404).body(Map.of(
					"msg","존재하지않는 회원입니다"));
		}
	    return ResponseEntity.ok(loginid);
	}
	
	@PostMapping("/updateme") // 회원정보수정
	public ResponseEntity<?> updateme(@Valid @RequestBody UpdateDto dto, @AuthenticationPrincipal UserPrincipalDetails user){
		
		Integer userId = user.getEntity().getUserId();
		service.updateme(dto,userId);
		return ResponseEntity.ok("업데이트 완료");
	}
	
	@PostMapping("/changepw")
	public ResponseEntity<?> changepw(@Valid @RequestBody ChangePwDto dto){
		System.out.println(dto);
		service.changepw(dto);
		return ResponseEntity.ok("비밀번호 변경 완료");
	}
	
	@GetMapping("/me")
    public ResponseEntity<?> me(@AuthenticationPrincipal UserPrincipalDetails auth) {
        // ✅ 로그인 안 된 경우
        if (auth == null ) {
            return ResponseEntity.status(401).body(Map.of("msg", "UNAUTHORIZED"));
        }
        
        UserEntity user = service.findme(auth.getEntity().getUserId());
        // ✅ 로그인 된 경우 (아이디/이메일 등 네 정책에 맞게 내려주면 됨)
        // ✅ Map.of 대신 null 허용되는 Map 사용
        Map<String, Object> map = new java.util.HashMap<>();
        map.put("loginId", user.getLoginId());
        map.put("userName", user.getUserName());
        map.put("userEmail", user.getUserEmail());   // null이어도 OK
        map.put("userPhone", user.getUserPhone());   // null이어도 OK
        map.put("freeToken", user.getFreeToken());
        map.put("balance", user.getBalance());
        map.put("role", user.getUserRole());
        map.put("msg", "OK");

        return ResponseEntity.ok(map);
            
    }
	
	@GetMapping("/review") // 리뷰 목록 보여주기
	public List<ReviewResponseDto> showreview(){
		List<ReviewResponseDto> list = service.showreview();
		return list;
	}
	
	@PostMapping("/write")  // 리뷰 작성하기
	public ResponseEntity<?> writereview(@AuthenticationPrincipal UserPrincipalDetails user,
										@Valid@RequestBody ReviewRequestDto dto){
		Integer userid = user.getEntity().getUserId();
		service.write(userid,dto);
		
		return ResponseEntity.ok(Map.of("msg","리뷰가 등록되었습니다"));
	}
	
	@PostMapping("/question")  // 질문작성
	public ResponseEntity<?> qeustion(@AuthenticationPrincipal UserPrincipalDetails user,
									@RequestBody QuestionDto dto){
		Integer userId = user.getEntity().getUserId();
		service.question(userId,dto);
		return ResponseEntity.ok("등록이 완료되었습니다.");
	}
	
	@GetMapping("/showqeustion")
	public ResponseEntity<?> showqeustion(){
		
		return ResponseEntity.ok(service.showquestion());
	}
	


}