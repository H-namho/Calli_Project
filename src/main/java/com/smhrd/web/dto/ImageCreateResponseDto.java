package com.smhrd.web.dto;

import com.smhrd.web.enumm.ImageStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class ImageCreateResponseDto {
    private Integer calliId;
    private ImageStatus status; // WAIT
    private String Textprompt;
    private String Styleprompt;
}
