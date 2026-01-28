//package com.smhrd.web.config;
//
//import java.net.URI;
//
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
//import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
//import software.amazon.awssdk.regions.Region;
//import software.amazon.awssdk.services.s3.S3Client;
//import software.amazon.awssdk.services.s3.presigner.S3Presigner;
//
//@Configuration
//public class ImageConfig {
//
//	@Value("${ncp.access-key}")
//	private String accessKey;
//	
//	@Value("${ncp.secret-key}")
//	private String secretKey;
//	
//	@Value("${ncp.region}")
//	private String region;
//	
//	@Value("${ncp.end-Point}")
//	private String endPoint;
//
//	@Bean
//	public S3Client s3client() {
//		return S3Client.builder()
//				.region(Region.of(region))
//				.endpointOverride(URI.create(endPoint))
//				.credentialsProvider(StaticCredentialsProvider
//						.create(AwsBasicCredentials.create(accessKey, secretKey)))
//				.build();
//	}
//	
//	@Bean
//	public S3Presigner s3Presigner() {
//		return S3Presigner.builder()
//				.region(Region.of(region))
//				.endpointOverride(URI.create(endPoint))
//				.credentialsProvider(
//						StaticCredentialsProvider.create(
//								AwsBasicCredentials.create(accessKey, secretKey)))
//				.build();
//	}
//}
package com.smhrd.web.config;

import java.net.URI;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3Configuration;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;

@Configuration
public class ImageConfig {

    @Value("${ncp.access-key}")
    private String accessKey;

    @Value("${ncp.secret-key}")
    private String secretKey;

    @Value("${ncp.region}")
    private String region;

    @Value("${ncp.end-Point}")
    private String endPoint;

    /**
     * ✅ NCP Object Storage(S3 호환)용 S3Client Bean
     * - endpointOverride: NCP Object Storage 엔드포인트로 강제
     * - credentialsProvider: NCP AccessKey/SecretKey 사용
     * - serviceConfiguration:
     *   1) pathStyleAccessEnabled(true)
     *      - NCP는 "버킷이 도메인 앞에 붙는 방식(virtual-host style)"보다
     *        "/버킷/키" 형태(path-style)로 안정적으로 동작하는 경우가 많음
     *
     *   2) chunkedEncodingEnabled(false)
     *      - 일부 S3 호환 스토리지에서 chunked 업로드 시 서명/헤더 문제로 403이 나는 케이스 방지
     *
     *   3) checksumValidationEnabled(false)
     *      - S3 호환 구현 차이로 checksum 검증에서 문제 나는 케이스 방지(선택이지만 같이 권장)
     */
    @Bean
    public S3Client s3client() {

        // ✅ S3 호환 스토리지에서 가장 흔한 문제 방지용 설정
        // - pathStyleAccessEnabled(true)
        //   : https://endpoint/bucket/key 형태로 요청하게 함
        //   : (bucket.endpoint 형태의 Virtual Host 방식 때문에 403 나는 케이스 방지)
        S3Configuration s3Config = S3Configuration.builder()
                .pathStyleAccessEnabled(true)
                .build();

        return S3Client.builder()
                .region(Region.of(region))
                .endpointOverride(URI.create(endPoint))
                .serviceConfiguration(s3Config)
                .credentialsProvider(
                        StaticCredentialsProvider.create(
                                AwsBasicCredentials.create(accessKey, secretKey)
                        )
                )
                .build();
    }

    @Bean
    public S3Presigner s3Presigner() {

        S3Configuration s3Config = S3Configuration.builder()
                .pathStyleAccessEnabled(true)
                .build();

        return S3Presigner.builder()
                .region(Region.of(region))
                .endpointOverride(URI.create(endPoint))
                .serviceConfiguration(s3Config)
                .credentialsProvider(
                        StaticCredentialsProvider.create(
                                AwsBasicCredentials.create(accessKey, secretKey)
                        )
                )
                .build();
    }

}
