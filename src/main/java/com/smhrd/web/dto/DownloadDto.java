package com.smhrd.web.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.core.io.Resource;

@Data
@AllArgsConstructor
public class DownloadDto {
	 private final Resource resource;      // ✅ S3에서 가져온 파일 스트림
	 private final String fileName;        // ✅ 다운로드 파일명
	 private final long contentLength;     // ✅ 파일 사이즈
}
