package com.example.blogtest.domain.blog.service;

import com.example.blogtest.domain.blog.dto.PopularSearchKeywordResponseDto;
import com.example.blogtest.domain.blog.entity.SearchBlogHistory;
import com.example.blogtest.domain.blog.repository.SearchHistoryJpaRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

@SpringBootTest
@Transactional
public class PopularKeywordPerformanceTest {

    @Autowired
    private SearchBlogService searchBlogService;

    @Autowired
    private SearchHistoryJpaRepository searchHistoryJpaRepository;

    final static private int ONE_HUNDRED_THOUSAND = 1000000;

    @DisplayName("성능테스트 - db로 rank조회(order by desc)와 redis rank 조회 비교")
    @Test
    public void compareDBToRedis_Test() {
        insertHistory();

        // 1) rank 조회 by db
        Instant before = Instant.now();
        List<SearchBlogHistory> histories = searchHistoryJpaRepository.findTop10ByOrderBySearchCountDesc();
        Duration elapsed_db = Duration.between(before, Instant.now());

        System.out.println(String.format("Rank(%d) - 소요시간 %d ms", histories.get(0).getSearchCount(), elapsed_db.getNano() / ONE_HUNDRED_THOUSAND));

        // 2) rank 조회 by redis
        before = Instant.now();
        PopularSearchKeywordResponseDto top10RankersDto = searchBlogService.getTopLimitRank(10);
        Duration elapsed_redis = Duration.between(before, Instant.now());

        System.out.println(String.format("Rank(%d) - 소요시간 %d ms", top10RankersDto.getPopularKeywords().get(0).getSearchCount(), elapsed_redis.getNano() / ONE_HUNDRED_THOUSAND));

    }

    private void insertHistory() {
        for (int i = 1; i <= 3000; i++) {
            Pageable pageRequest = PageRequest.of(1, 1, Sort.by(Sort.Order.by("recency")));
            searchBlogService.search("카카오_" + i % 3, pageRequest);
        }
        System.out.println("---end insert---");
    }
}
