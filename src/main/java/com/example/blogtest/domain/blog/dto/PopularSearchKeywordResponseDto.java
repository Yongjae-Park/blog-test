package com.example.blogtest.domain.blog.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class PopularSearchKeywordResponseDto {

    private List<KeywordDto> popularKeywords;
    private int dataSize;

    @Data
    @Builder
    public static class KeywordDto {
        private String keyword;

        private Long searchCount;
    }
}