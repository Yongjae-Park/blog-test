package com.example.blogtest.domain.blog.repository;

import com.example.blogtest.domain.blog.entity.SearchBlogHistory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class SearchHistoryJpaRepositoryTest {

    @Autowired
    private SearchHistoryJpaRepository searchHistoryJpaRepository;

    private String KEYWORD = "카카오";

    List<SearchBlogHistory> histories = new ArrayList<>();

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

    @DisplayName("searchCount 값으로 내림차순 정렬된다.")
    @Test
    public void findTop10ByOrderBySearchCountDesc_Order_Test() {
        createHistories(5);

        searchHistoryJpaRepository.saveAll(histories);

        List<SearchBlogHistory> histories = searchHistoryJpaRepository.findTop10ByOrderBySearchCountDesc();

        assertThat(histories.get(0).getSearchCount()).isGreaterThan(histories.get(1).getSearchCount());
    }

    @DisplayName("전체 히스토리가 10개 이상이어도 searchCount 값 상위 10개만 조회한다.")
    @Test
    public void findTop10ByOrderBySearchCountDesc_count_Test() {
        createHistories(30);

        searchHistoryJpaRepository.saveAll(histories);
        
        List<SearchBlogHistory> histories = searchHistoryJpaRepository.findTop10ByOrderBySearchCountDesc();

        assertThat(histories.size()).isEqualTo(10);
    }

    @DisplayName("전체 히스토리가 10개 미만이면 전체 히스토리 조회한다.")
    @Test
    public void findTop10ByOrderBySearchCountDesc_less_than_10_Test() {
        int total_size = 5;
        createHistories(total_size);

        searchHistoryJpaRepository.saveAll(histories);

        List<SearchBlogHistory> histories = searchHistoryJpaRepository.findTop10ByOrderBySearchCountDesc();

        assertThat(histories.size()).isEqualTo(total_size);
    }

    private void createHistories(int tc) {
        for (int i = 0; i < tc; i++) {
            createHistory("kakao" + i, 0L + i);
        }
    }

    private SearchBlogHistory createHistory(String keyword, Long count) {
        SearchBlogHistory history = SearchBlogHistory.builder()
                .keyword(keyword)
                .searchCount(count)
                .build();
        histories.add(history);
        return history;
    }

}