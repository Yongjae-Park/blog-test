package com.example.blogtest.domain.blog.service;

import com.example.blogtest.domain.blog.dto.PopularSearchKeywordResponseDto;
import com.example.blogtest.domain.blog.entity.SearchBlogHistory;
import com.example.blogtest.domain.blog.repository.SearchHistoryJpaRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class SearchBlogServiceTest {
    private static final ExecutorService service =
            Executors.newFixedThreadPool(100);

    @Autowired
    private SearchBlogService searchBlogService;

    @Autowired
    private SearchHistoryJpaRepository searchHistoryJpaRepository;

    private long historyId;

    private List<SearchBlogHistory> histories = new ArrayList<>();

    @DisplayName("동시성 테스트 - from order by desc : 100개의 스레드가 동시에 검색 서비스 호출시 검색 히스토리의 searchCount가 호출 수 만큼 증가한다.")
    @Test
    public void SimultaneousBlogSearch_fromOrderByDESC_Test() throws InterruptedException {
        createHistory();

        CountDownLatch latch = new CountDownLatch(100);

        for (int i = 0; i < 100; i++) {
            service.execute(() -> {
                searchBlogService.search("카카오", "recency", 1, 1);
                latch.countDown();
            });
        }

        latch.await();
        SearchBlogHistory history = searchHistoryJpaRepository.findById(historyId).orElseThrow();
        System.out.println("history.getSearchCount() = " + history.getSearchCount());
        assertThat(history.getSearchCount()).isEqualTo(101);
    }

    @DisplayName("동시성 테스트 - from redis cache : 100개의 스레드가 동시에 검색 서비스 호출시 검색 히스토리의 검색카운트가 호출 수 만큼 증가한다.")
    @Test
    public void SimultaneousBlogSearch_fromRedisCache_Test() throws InterruptedException {
        createHistory();

        CountDownLatch latch = new CountDownLatch(100);

        for (int i = 0; i < 100; i++) {
            service.execute(() -> {
                searchBlogService.search("카카오", "recency", 1, 1);
                latch.countDown();
            });
        }

        latch.await();
        PopularSearchKeywordResponseDto dto = searchBlogService.getPopularKeywords();
        assertThat(dto.getPopularKeywords().get(0).getSearchCount()).isEqualTo(101);
    }

    private void createHistory() {
        SearchBlogHistory history = SearchBlogHistory.builder()
                .keyword("카카오")
                .searchCount(1L)
                .build();

        SearchBlogHistory savedHistory = searchHistoryJpaRepository.save(history);
        this.historyId = savedHistory.getId();
    }

    @DisplayName("인기 검색어 조회후 PopularSearchKeywordResponseDto로 랩핑하여 사이즈 값을 확인한다.")
    @Test
    public void getPopularKeyword_Test() {
        int initSize = searchHistoryJpaRepository.findAll().size();

        int size = 8;
        createHistories(8);
        PopularSearchKeywordResponseDto res = searchBlogService.getPopularKeywords();

        assertThat(res.getDataSize()).isEqualTo(size + initSize);
    }

    private void createHistories(int tc) {
        for (int i = 0; i < tc; i++) {
            createHistory("kakao" + i, 0L + i);
        }
        searchHistoryJpaRepository.saveAll(histories);
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