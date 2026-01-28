package com.smhrd.web.dto;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GenerateResponseDto {
    @JsonProperty("image_base64")
    private String imageBase64;

    private Map<String, Object> meta;

    public String getImageBase64() { return imageBase64; }
}
