package com.smhrd.web.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ShowPageDto<T> {

    /**
     * ✅ 현재 페이지의 실제 데이터 목록
     */
    private List<T> content;

    /**
     * ✅ 현재 페이지 번호 (0부터 시작)
     */
    private int page;

    /**
     * ✅ 요청한 페이지 사이즈
     */
    private int size;

    /**
     * ✅ 전체 데이터 개수 (DB 전체 row 수)
     */
    private long totalElements;

    /**
     * ✅ 전체 페이지 수 (size 기준으로 계산된 페이지 수)
     */
    private int totalPages;

    /**
     * ✅ 첫 페이지 여부
     */
    private boolean first;

    /**
     * ✅ 마지막 페이지 여부
     */
    private boolean last;
}
