package com.smhrd.web.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.smhrd.web.service.AdminService;

import lombok.RequiredArgsConstructor;

@RequestMapping("/api")
@RestController
@RequiredArgsConstructor
public class AdminController {

	private final AdminService adminService;
	@GetMapping("/Admin/show")
	public ResponseEntity<?> showq(){
			
		return ResponseEntity.ok(adminService.showq());
	}
	
	@PostMapping("/{qId}/Admin/answer")
	public ResponseEntity<?> answer(@RequestBody Map<String,String> map,@PathVariable Integer qId){
		String answer = map.get("answer");
		
		adminService.answer(qId,answer);
		return ResponseEntity.ok("completed");
	}
	
	@GetMapping("/Admin/manage")
	public ResponseEntity<?> manage(){
		
		return ResponseEntity.ok(adminService.manage());
	}
//	@PostMapping
}
