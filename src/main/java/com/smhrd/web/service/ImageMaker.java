package com.smhrd.web.service;

import java.util.Base64;
import java.net.InetAddress;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smhrd.web.config.CalligraphyAiClient;
import com.smhrd.web.config.S3Uploader;
import com.smhrd.web.dto.ImageRequestDto;
import com.smhrd.web.dto.GenerateRequestDto;
import com.smhrd.web.dto.GenerateResponseDto;
import com.smhrd.web.entity.ImageGenEntity;
import com.smhrd.web.enumm.ImageStatus;
import com.smhrd.web.repository.ImageRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ImageMaker {
	
	private final CreditService creditService;
    private final ImageRepository imageRepository;
    private final CalligraphyAiClient aiClient;
    private final S3Uploader s3Uploader; 

    @Async
    public void generateAsync(Integer calliid, Integer userid, ImageRequestDto imagedto) {
        try {
            //  FastAPI 요청 DTO 생성
        	
            GenerateRequestDto aiRequest = GenerateRequestDto.builder()
                    .prompt(imagedto.getTextPrompt())
                    .stylePrompt(imagedto.getStylePrompt())
                    .bgPrompt(imagedto.getBgPrompt())
                    .width(imagedto.getSize())
                    .height(imagedto.getSize())
                    .seed(null)
                    .build();

            //  FastAPI 호출
            GenerateResponseDto response = aiClient.generate(aiRequest);
          
            // Fast API가 준 Base64 -> byte로 디코딩
            byte[] imageBytes = Base64.getDecoder().decode(response.getImageBase64());
            String path = "calligraphy/" + calliid + ".png";
            //  오브젝트스토리지 업로드
            s3Uploader.upload(imageBytes, path); 
            
            // 성공 했을 경우
            sucimage(calliid, path);

        } catch (Exception e) {
            // 실패 했을 경우
            e.printStackTrace();
            failimage(calliid, e.getMessage());
        }
    }

    @Transactional  // 성공시
    public void sucimage(Integer callid, String path) {
        ImageGenEntity genEntity = imageRepository.findById(callid)
                .orElseThrow(() -> new IllegalArgumentException("해당 이미지를 찾을 수 없습니다"));

        genEntity.makesuc(path);
        imageRepository.save(genEntity);
    
    }


    @Transactional // 실패시
    public void failimage(Integer callid, String reason) {
        ImageGenEntity genEntity = imageRepository.findById(callid)
                .orElseThrow(() -> new IllegalArgumentException("해당 이미지를 찾을 수 없습니다"));

        genEntity.makefail(reason);
        imageRepository.save(genEntity);
        Integer userId =genEntity.getUser().getUserId();
        creditService.refund(userId, callid);
    }
}
