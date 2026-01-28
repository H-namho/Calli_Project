package com.smhrd.web.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DownloadHistoryDto {
    private Integer calliId;
    private String imgUrl;
    private LocalDateTime downloadedAt;

    private String inputText;

    // ✅ UI용(원하면 빼도 됨)
    private int downloadCount;
    private int maxDownload;
}
