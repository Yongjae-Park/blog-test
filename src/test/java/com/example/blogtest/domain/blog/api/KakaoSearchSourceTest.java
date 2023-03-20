package com.example.blogtest.domain.blog.api;

import com.example.blogtest.domain.blog.dto.BlogSearchResponseDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class KakaoSearchSourceTest {

    @Autowired
    private SearchBlog searchBlog;

    private String specificUrl = "https://brunch.co.kr/@tourism";

    @DisplayName("Kakao 블로그 검색 api- sort값을 recency로 했을 때 최신순으로 정렬된다.")
    @Test
    void callBlogSearch_recency_TEST() {
        int size = 10;
        int page = 1;
        BlogSearchResponseDto dto =
                searchBlog.callBlogSearch("집짓기", "recency", page, size);

        List<BlogSearchResponseDto.Document> documents = dto.getDocuments();

        assertTrue(documents.get(0).getDatetime().isAfter(documents.get(1).getDatetime()));
    }

    @DisplayName("Kakao 블로그 검색 api- size값이 1인 경우 1개의 블로그 게시물이 조회된다.")
    @Test
    void callBlogSearch_size_Test() {
        int size = 1;
        int page = 1;
        BlogSearchResponseDto dto =
                searchBlog.callBlogSearch("집짓기", "recency", page, size);

        List<BlogSearchResponseDto.Document> documents = dto.getDocuments();

        assertThat(documents.size()).isEqualTo(size);
    }
}