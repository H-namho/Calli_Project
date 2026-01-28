package com.smhrd.web.config;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.HeadBucketRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;

@Service
@RequiredArgsConstructor
public class S3Uploader {

    private final S3Client s3Client; // ImageConfig에서 만든 Bean DI

    @Value("${ncp.bucket-name}")
    private String bucket;

    @Value("${ncp.region}")
    private String region;

    @Value("${ncp.end-Point}")
    private String endPoint;

    /**
     * byte[] 이미지를 Object Storage에 업로드
     * @param bytes 업로드할 이미지 바이트
     * @param key 저장할 경로/파일명 (ex: "calligraphy/1.png")
     * @return 업로드 후 접근 가능한 URL
     */
//    public String upload(byte[] bytes, String key) throws IOException {
//        // 임시 파일 생성
//        Path tempFile = Files.createTempFile("upload", ".png");
//        Files.write(tempFile, bytes);
//        HeadBucketRequest req = HeadBucketRequest.builder()
//                .bucket(bucket)
//                .build();
//
//        // ✅ 여기서 403이면 "업로드 이전 단계(서명/권한/설정)"가 문제라는 뜻
//        s3Client.headBucket(req);
//
//        // headBucket이 예외 없이 지나가면 최소한 "버킷 접근 권한/서명"은 통과한 것
//        System.out.println("[S3] headBucket OK : " + bucket);
//    
//        System.out.println("[S3] endpoint=" + endPoint);
//        System.out.println("[S3] region=" + region);
//        System.out.println("[S3] bucket=" + bucket);
//
////        System.out.println("[S3] accessKey(head)=" + accessKey.substring(0, 6));
////        System.out.println("[S3] secretKey(head)=" + secretKey.substring(0, 6));
//        // S3 PutObject
//        s3Client.putObject(
//                PutObjectRequest.builder()
//                        .bucket(bucket)
//                        .key(key)
//                        .build(),
//                tempFile
//        );
//
//        // 접근 가능한 URL 반환
//        // NCP Object Storage는 endpoint + bucket + key 형식
//        String url = endPoint + "/" + key;
//
//        // 임시 파일 삭제
//        Files.deleteIfExists(tempFile);
//
//        return url;
//    }
    public String upload(byte[] bytes, String key) throws IOException {

        Path tempFile = Files.createTempFile("upload", ".png");

        try {
            Files.write(tempFile, bytes);
            try {
                System.out.println("[S3] listBuckets try...");
                s3Client.listBuckets();
                System.out.println("[S3] listBuckets OK");
            } catch (Exception e) {
                System.out.println("[S3] listBuckets FAIL : " + e.getMessage());
            }
            // ✅ 1) headBucket: "버킷 접근 가능?" 확인
            // - 여기서 403이면: 키/권한/엔드포인트/리전/서명 문제 가능성이 큼
            try {
                HeadBucketRequest req = HeadBucketRequest.builder()
                        .bucket(bucket)
                        .build();

                s3Client.headBucket(req);
                System.out.println("[S3] headBucket OK: " + bucket);

            } catch (S3Exception e) {
                // ✅ 디버깅용 핵심 로그
                System.out.println("[S3] headBucket FAIL");
                System.out.println("  statusCode = " + e.statusCode());
                System.out.println("  awsErrorCode = " + (e.awsErrorDetails() != null ? e.awsErrorDetails().errorCode() : "null"));
                System.out.println("  requestId = " + (e.requestId() != null ? e.requestId() : "null"));
                System.out.println("  message = " + e.getMessage());

                // 여기서 throw 하면 "버킷 접근 단계에서 막힘"이 확정됨
                throw e;
            }
            
            // ✅ 2) putObject: 실제 업로드
            try {
                PutObjectRequest putReq = PutObjectRequest.builder()
                        .bucket(bucket)
                        .key(key)
                        .contentType("image/png")
                        .build();

                s3Client.putObject(putReq, tempFile);
                System.out.println("[S3] putObject OK: " + key);

            } catch (S3Exception e) {
                // ✅ putObject에서만 터지면: "쓰기 권한" 문제 가능성이 큼
                System.out.println("[S3] putObject FAIL");
                System.out.println("  statusCode = " + e.statusCode());
                System.out.println("  awsErrorCode = " + (e.awsErrorDetails() != null ? e.awsErrorDetails().errorCode() : "null"));
                System.out.println("  requestId = " + (e.requestId() != null ? e.requestId() : "null"));
                System.out.println("  message = " + e.getMessage());
                throw e;
            }

            // ✅ URL 만들기 (NCP는 보통 endpoint/bucket/key)
            return endPoint + "/" + bucket + "/" + key;

        } finally {
            // ✅ 예외가 나도 임시파일은 정리
            Files.deleteIfExists(tempFile);
        }
    }
}
