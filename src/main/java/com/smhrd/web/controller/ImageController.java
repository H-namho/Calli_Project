package com.smhrd.web.controller;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.smhrd.web.dto.DownloadDto;
import com.smhrd.web.dto.DownloadHistoryDto;
import com.smhrd.web.dto.ImageRequestDto;
import com.smhrd.web.dto.WishResponseDto;
import com.smhrd.web.dto.oauth.UserPrincipalDetails;
import com.smhrd.web.service.ImageAccessService;
import com.smhrd.web.service.ImageService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ImageController {
	
	private final ImageService imageService;
	private final ImageAccessService accessService;
	
	@PostMapping("/generation")  // 이미지 생성
	public ResponseEntity<?> imagegen(@AuthenticationPrincipal UserPrincipalDetails user,
			@Valid @RequestBody ImageRequestDto imagedto) {
		System.out.println(imagedto);
		Integer userid = user.getEntity().getUserId();
		Integer callid =imageService.imagegen(userid,imagedto);
		return ResponseEntity.ok(callid);
	}
	
//	@GetMapping("/{calliId}/download")  // 이미지 다운로드
//	public ResponseEntity<PreviewResponseDto> download(
//			@AuthenticationPrincipal UserPrincipalDetails user, @PathVariable Integer calliId){
//		
//		Integer userId = user.getEntity().getUserId();
//		PreviewResponseDto responseDto = accessService.download(userId, calliId);
//		return ResponseEntity.ok(responseDto);
//	}
	@GetMapping("/{calliId}/download")  // ✅ [수정] POST -> GET (다운로드는 GET이 더 REST스럽고 브라우저도 다루기 쉬움)
	public ResponseEntity<Resource> downloadFile(
	        @AuthenticationPrincipal UserPrincipalDetails user,
	        @PathVariable Integer calliId) throws Exception {

	    Integer userId = user.getEntity().getUserId();

	    // ✅ [수정] 기존 PreviewResponseDto 반환 대신
	    // ✅ [수정] 실제 파일 스트림 + 파일명 같이 반환 받음
	    DownloadDto fileDto = accessService.downloadFile(userId, calliId);

	    // ✅ [추가] 파일명을 Content-Disposition에 넣을 때 한글 깨짐 방지
	    String encodedFileName = URLEncoder.encode(fileDto.getFileName(), StandardCharsets.UTF_8)
	            .replaceAll("\\+", "%20");

	    return ResponseEntity.ok()
	            // ✅ [추가] 브라우저가 "첨부파일 다운로드"로 인식하도록 강제
	            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename*=UTF-8''" + encodedFileName)
	            .contentType(MediaType.APPLICATION_OCTET_STREAM)
	            // ✅ [선택] 길이 알면 내려주면 더 좋음
	            .contentLength(fileDto.getContentLength())
	            .body(fileDto.getResource());
	}
	
	@GetMapping("/showDown")  // 다운로드 내역 보여주기
	public ResponseEntity<?> showDown(@AuthenticationPrincipal UserPrincipalDetails user){
		
		Integer userId = user.getEntity().getUserId();
		
		return  ResponseEntity.ok(accessService.showDown(userId));
	}

	@GetMapping("/wishlist")  // 위시리스트 조회
	public ResponseEntity<?> showwish(@AuthenticationPrincipal UserPrincipalDetails user){
		Integer userid = user.getEntity().getUserId();
		System.out.println("들어옴");
		List<WishResponseDto> list =imageService.showwish(userid);
		System.out.println("나옴");
		System.out.println(list);
		return ResponseEntity.ok(list);	
	}
	
	@PostMapping("/{callid}") // 위시리스트 추가 
	public ResponseEntity<?> addwish(@AuthenticationPrincipal UserPrincipalDetails user,
									@PathVariable Integer callid){
		Integer userid = user.getEntity().getUserId();
		imageService.addwish(userid,callid);
		
		return ResponseEntity.ok(Map.of("msg","위시리스트에 추가되었습니다"));		
	}
	
	@DeleteMapping("/{callid}") // 위시리스트 삭제
	public ResponseEntity<?> removewish(@AuthenticationPrincipal UserPrincipalDetails user,
										@PathVariable Integer callid){
		Integer userid = user.getEntity().getUserId();
		imageService.removewish(userid,callid);
		
		return ResponseEntity.ok(Map.of("msg","삭제가 완료되었습니다"));		
	}
	
	@GetMapping("/{callid}/preview")
	public ResponseEntity<?> preview(@AuthenticationPrincipal UserPrincipalDetails user,
									 @PathVariable Integer callid){
		Integer userId = user.getEntity().getUserId();
		
		return ResponseEntity.ok(accessService.preview(userId, callid));
	}
	
	
}
