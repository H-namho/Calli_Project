package com.smhrd.web.service;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import com.smhrd.web.dto.ImageRequestDto;
import com.smhrd.web.dto.WishResponseDto;
import com.smhrd.web.entity.ImageGenEntity;
import com.smhrd.web.entity.UserEntity;
import com.smhrd.web.entity.WishlistEntity;
import com.smhrd.web.enumm.ImageStatus;
import com.smhrd.web.enumm.MinusAction;
import com.smhrd.web.repository.ImageRepository;
import com.smhrd.web.repository.UserRepository;
import com.smhrd.web.repository.WishlistRepository;

import lombok.RequiredArgsConstructor;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;

@Service
@RequiredArgsConstructor
public class ImageService {

	@Value("${ncp.bucket-name}")
	private  String bucketName;
	private final S3Presigner s3Presigner;
    private final WishlistRepository wishlistRepository;
	private final CreditService creditService;
	private final ImageRepository imageRepository;
	private final UserRepository userRepository;
	private final ImageMaker imageMaker;

	
	@Transactional  // 이미지 생성
	public Integer imagegen(Integer userid,ImageRequestDto imagedto) { 
		// imagedto => prompt~ , size 
		UserEntity userEntity = userRepository.findById(userid)  // 로그인했는지 체크
								.orElseThrow(()->new IllegalArgumentException("회원을 찾을 수 없습니다"));
		
		String styprm = imagedto.getStylePrompt(); // 각각의 프롬프트
		String bgprm = imagedto.getBgPrompt();
		String textprm = imagedto.getTextPrompt();
		
		ImageGenEntity genEntity = ImageGenEntity.builder() 
									.user(userEntity) // 회원번호
									.textPrompt(textprm) 
									.bgPrompt(bgprm)
									.stylePrompt(styprm)
									.status(ImageStatus.WAIT) // 이미지 생성대기
									.size(imagedto.getSize()) // 사이즈							
									.modelName("켈리").modelVersion("1.0").build();									
		ImageGenEntity imageGenEntity =imageRepository.save(genEntity);
		
		Integer calliId = imageGenEntity.getCalliId();
		System.out.println("111111111111");
		creditService.minus(userid, MinusAction.GENERATE, calliId); // 크레딧 여부 확인
		System.out.println("222222222222");
	    TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCommit() {
            	System.out.println("gen전");
                imageMaker.generateAsync(calliId, userid, imagedto);
            }
        });
		

	    return calliId;	  	
	}
	@Transactional(readOnly = true)  // 위시리스트 조회
	public List<WishResponseDto> showwish(Integer userId){
		List<WishlistEntity> wishlistEntity =wishlistRepository.findByUser_UserId(userId); // 위시리스트 사진 list형태로 받아옴
		System.out.println("통과");
		List<WishResponseDto> list = new ArrayList<>(); // 응답해줄 리스트
		int inspiresSec=600;
		for(WishlistEntity w : wishlistEntity) {
			String key = w.getCalli().getCalliPath();
			if (key == null || key.isBlank()) continue;
			WishResponseDto dto = WishResponseDto.builder()
									.wishlistId(w.getWishlistId())
									.calliId(w.getCalli().getCalliId())
									.imgPath(key)
									.imgUrl(presignUrl(key, inspiresSec))
									.wishedAt(w.getWishedAt())
									.build();
			list.add(dto);
		}
		return list; // 배열을 리턴하여 프론트로 응답
	}

	@Transactional   // 위시리스트 추가
	public void addwish(Integer userid, Integer callid) { 
		
		boolean chkwish = wishlistRepository.existsByUser_UserIdAndCalli_CalliId(userid,callid);  // 위시리스트에 해당유저가 생성한 이미지가 있는지 체크
		if(chkwish) { // 이미지가 있다면 위시리스트 중복
			throw new IllegalArgumentException("이미 위시리스트에 추가된 항목입니다.");
		}
		UserEntity userEntity = userRepository.findById(userid).orElseThrow(()-> new IllegalArgumentException("회원을 찾을 수 없습니다"));
		ImageGenEntity genEntity = imageRepository.findById(callid)
								.orElseThrow(()-> new IllegalArgumentException("해당이미지를 찾을 수 없습니다."));
		
		WishlistEntity wishlistEntity = WishlistEntity.builder()
										.calli(genEntity)
										.user(userEntity)
										.build();
		wishlistRepository.save(wishlistEntity);  // FK 2개 저장
	}
	
	@Transactional // 위시리스트 삭제
	public void removewish(Integer userid, Integer callid) { 
		
		boolean chkwish = wishlistRepository.existsByUser_UserIdAndCalli_CalliId(userid, callid); // 위시리스트에 해당유저의 이미지가 있는가?
		if(!chkwish) { // 이미지가 없다면 항목존재X
			throw new IllegalArgumentException("삭제할 항목이 존재하지 않습니다.");
		}
		wishlistRepository.deleteByUser_UserIdAndCalli_CalliId(userid,callid); // 삭제
	}
	
	public String presignUrl(String key, int expiresSec) {
		GetObjectRequest objectRequest = GetObjectRequest.builder()
										.bucket(bucketName)
										.key(key)
										.build();
		GetObjectPresignRequest objectPresignRequest = GetObjectPresignRequest.builder()
														.signatureDuration(Duration.ofSeconds(expiresSec))
														.getObjectRequest(objectRequest).build();
		
		PresignedGetObjectRequest presigned = s3Presigner.presignGetObject(objectPresignRequest);
		return presigned.url().toString();
	}
	
	
}
