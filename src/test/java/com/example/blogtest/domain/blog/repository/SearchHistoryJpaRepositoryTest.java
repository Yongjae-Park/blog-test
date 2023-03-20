package com.example.blogtest.domain.blog.repository;

import com.example.blogtest.domain.blog.entity.SearchBlogHistory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class SearchHistoryJpaRepositoryTest {

    @Autowired
    private SearchHistoryJpaRepository searchHistoryJpaRepository;

    private String KEYWORD = "카카오";

    @DisplayName("블로그 검색 히스토리를 저장한다.")
    @Test
    public void save_Test() {
        SearchBlogHistory history = SearchBlogHistory.builder()
                .keyword(KEYWORD)
                .searchCount(1L)
                .build();

        SearchBlogHistory savedHistory = searchHistoryJpaRepository.save(history);

        assertThat(savedHistory.getKeyword()).isEqualTo(KEYWORD);
    }

    @DisplayName("키워드(검색어)로 history를 조회한다.")
    @Test
    @Transactional
    public void findByKeyword_Test() {
        SearchBlogHistory history = SearchBlogHistory.builder()
                .keyword(KEYWORD)
                .searchCount(1L)
                .build();

        searchHistoryJpaRepository.save(history);

        SearchBlogHistory findHistory = searchHistoryJpaRepository.findByKeyword(KEYWORD).orElseThrow();

        assertThat(findHistory.getKeyword()).isEqualTo(KEYWORD);
    }

    @DisplayName("history 업데이트가 있을 경우 version이 증가한다.")
    @Test
    @Transactional
    public void version_increment_Test() {
        SearchBlogHistory history = SearchBlogHistory.builder()
                .keyword(KEYWORD)
                .searchCount(1L)
                .build();

        SearchBlogHistory savedHistory = searchHistoryJpaRepository.save(history);
        Long preVersion = savedHistory.getVersion();

        savedHistory.incrementSearchCount();
        searchHistoryJpaRepository.save(history);

        SearchBlogHistory findHistory = searchHistoryJpaRepository.findByKeyword(KEYWORD).orElseThrow();

        assertThat(findHistory.getVersion() - 1).isEqualTo(preVersion);
    }

}