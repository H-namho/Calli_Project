package com.smhrd.web.service;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smhrd.web.dto.DownloadDto;
import com.smhrd.web.dto.DownloadHistoryDto;
import com.smhrd.web.dto.PreviewResponseDto;
import com.smhrd.web.entity.DownloadEntity;
import com.smhrd.web.entity.ImageGenEntity;
import com.smhrd.web.entity.UserEntity;
import com.smhrd.web.enumm.ImageStatus;
import com.smhrd.web.enumm.MinusAction;
import com.smhrd.web.repository.DownloadRepository;
import com.smhrd.web.repository.ImageRepository;
import com.smhrd.web.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.HeadObjectRequest;
import software.amazon.awssdk.services.s3.model.HeadObjectResponse;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;


@Service
@RequiredArgsConstructor
public class ImageAccessService {
	
	private final ImageRepository imageRepository;
	private final UserRepository userRepository;
	private final DownloadRepository downloadRepository;
	
	private final CreditService creditService;
	private final S3Presigner s3Presigner;
	
    @Value("${ncp.bucket-name}")  //   버켓만들면 추가
    private String bucketName;
    private final S3Client s3Client;
    
    @Transactional   // 생성된 이미지를 히스토리에서 보기
    public PreviewResponseDto preview(Integer userId,Integer calliId) {
		
    	ImageGenEntity img = imageRepository.findByCalliIdAndUser_UserId(calliId,userId)
    						.orElseThrow(()->new IllegalArgumentException("이미지를 찾을 수 없습니다"));
    	if(img.getCalliPath() == null || img.getCalliPath().isBlank()) {
    		 throw new org.springframework.web.server.ResponseStatusException(
    	                org.springframework.http.HttpStatus.CONFLICT,
    	                "이미지가 아직 준비되지 않았습니다."
    	        );
    	}
    	if(img.getStatus() != ImageStatus.SUCCESS) {
    		throw new IllegalStateException("이미지가 아직 준비되지 않았습니다."); 
    	}
    	String key = img.getCalliPath();
    	
    	int expiresSec = 60*60*3;
    	GetObjectRequest getObjectRequest = GetObjectRequest.builder()
    										.bucket(bucketName)
    										.key(key).build();
    	GetObjectPresignRequest presign = GetObjectPresignRequest.builder()
    														.signatureDuration(Duration.ofSeconds(expiresSec))
    														.getObjectRequest(getObjectRequest).build();
    	PresignedGetObjectRequest presigned = s3Presigner.presignGetObject(presign);
    	
    	return new PreviewResponseDto(presigned.url().toString(), expiresSec);
    	
    }
//    @Transactional
//    public PreviewResponseDto download(Integer userId, Integer calliId) {  // 다운로드
//    	ImageGenEntity img = imageRepository.findByCalliIdAndUser_UserId(calliId, userId) // 해당 유저가 생성한 이미지가 있는가?
//    							.orElseThrow(()-> new IllegalArgumentException("이미지를 찾을 수 없습니다."));
//    	
//    	if(img.getStatus() != ImageStatus.SUCCESS || img.getCalliPath()==null) {  // 이미지가 생성되지 않았을때
//    		throw new IllegalStateException("이미지가 아직 준비되지 않았습니다.");
//    	}
//    	
//    	String key= img.getCalliPath(); 
//    	int expiresSec = 600;
//    	GetObjectRequest getObjectRequest = GetObjectRequest.builder()
//    										.bucket(bucketName)
//    										.key(key).build();
//    	GetObjectPresignRequest getObjectPresignRequest = GetObjectPresignRequest.builder()
//    													 .signatureDuration(Duration.ofSeconds(expiresSec))
//    													 .getObjectRequest(getObjectRequest)
//    													 .build();
//    	
//    	PresignedGetObjectRequest presigned = s3Presigner.presignGetObject(getObjectPresignRequest);
//    	creditService.minus(userId, MinusAction.DOWNLOAD, calliId);  // 다운로드 비용 차감 -20 크레딧
//    	
//    	UserEntity userEntity = userRepository.findById(userId)
//    							.orElseThrow(()-> new IllegalArgumentException("회원을 찾을 수 없습니다"));
//      	
//    	DownloadEntity downloadEntity= DownloadEntity.builder()  // 다운로드테이블에 fk 두개 저장
//    									.user(userEntity)
//    					    			.image(img).build();
//    								downloadRepository.save(downloadEntity);
//    	
//    	return new PreviewResponseDto(presigned.url().toString(), expiresSec);
//    	
//    }
    @Transactional(readOnly = true) // 다운로드 내역 조회
	public List<DownloadHistoryDto> showDown(Integer userId) {
		
    	 //  DB가 이미지당 1줄로 요약해서 내려줌 (GROUP BY + COUNT)
        var rows = downloadRepository.findDownloadHistorySummary(userId);

        List<DownloadHistoryDto> result = new ArrayList<>();

        //  row마다 프리사인 URL 1개만 생성 
        int expiresSec = 600;

        for (var r : rows) {

            String key = r.getCalliPath();

            GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .build();

            GetObjectPresignRequest presignReq = GetObjectPresignRequest.builder()
                    .signatureDuration(Duration.ofSeconds(expiresSec))
                    .getObjectRequest(getObjectRequest)
                    .build();

            PresignedGetObjectRequest presigned = s3Presigner.presignGetObject(presignReq);

            result.add(
                DownloadHistoryDto.builder()
                    .calliId(r.getCalliId())
                    .imgUrl(presigned.url().toString())
                    .downloadCount(r.getDownloadCount().intValue())
                    .downloadedAt(r.getLastDownloadedAt())
                    .build()
            );
        }
        return result;
    }

       
    
	public DownloadDto downloadFile(Integer userId, Integer calliId) { // 다운로드하기
		ImageGenEntity img = imageRepository.findByCalliIdAndUser_UserId(calliId, userId) // 해당 유저가 생성한 이미지가 있는가?
				.orElseThrow(()-> new IllegalArgumentException("이미지를 찾을 수 없습니다."));

		if(img.getStatus() != ImageStatus.SUCCESS || img.getCalliPath()==null) {  // 이미지가 생성되지 않았을때
			throw new IllegalStateException("이미지가 아직 준비되지 않았습니다.");
		}
		String path = img.getCalliPath();
		if(path==null || path.isBlank()) {
			throw new IllegalArgumentException("해당 파일을 찾을 수 없습니다");
		}
		int count=0;
		count = downloadRepository.countByUser_UserIdAndImage_CalliId(userId, calliId);
		
		if(count >= 3) {
			throw new IllegalArgumentException("다운로드 횟수를 초과 하였습니다");
		}
		HeadObjectResponse head = s3Client.headObject(HeadObjectRequest.builder()
		            .bucket(bucketName)
		            .key(path)
		            .build());
		long contentLength = head.contentLength();
		  
	    ResponseInputStream<GetObjectResponse> s3Stream = s3Client.getObject(builder -> builder
	            .bucket(bucketName)
	            .key(path));
	    Resource resource = new InputStreamResource(s3Stream);
	    creditService.minus(userId, MinusAction.DOWNLOAD, calliId);
	    UserEntity userEntity = userRepository.findById(userId)
	            .orElseThrow(() -> new IllegalArgumentException("회원을 찾을 수 없습니다"));

	    DownloadEntity downloadEntity = DownloadEntity.builder()
	            .user(userEntity)
	            .image(img)
	            .build();
	    downloadRepository.save(downloadEntity);
	    String fileName = path.contains("/") ? path.substring(path.lastIndexOf("/") + 1) : path;

	    return new DownloadDto(resource, fileName, contentLength);
	}
	
	
    
}
