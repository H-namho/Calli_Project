package com.smhrd.web.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WishResponseDto {

	private Integer wishlistId;
	private Integer calliId;
	private String imgPath;
	private String imgUrl;
	private LocalDateTime wishedAt;
}
