package com.example.blogtest.domain.blog.service;

import com.example.blogtest.domain.blog.api.SearchBlog;
import com.example.blogtest.domain.blog.dto.BlogSearchResponseDto;
import com.example.blogtest.domain.blog.dto.PopularSearchKeywordResponseDto;
import com.example.blogtest.domain.blog.entity.SearchBlogHistory;
import com.example.blogtest.domain.blog.repository.SearchHistoryJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SearchBlogService {

    final private SearchBlog searchSource;

    final private SearchHistoryJpaRepository searchHistoryJpaRepository;

    final private ZSetOperations zSetOperations;

    final static private String POPULAR_KEYWORD_KEY = "popularKeyword";

    @Transactional
    public BlogSearchResponseDto search(String query, String sort, Integer page, Integer size) {
        /*
            1. api 호출하여 검색결과를 받아온다.
            2. 검색 히스토리를 저장한다.
            3. 받아온 검색결과를 반환한다.
         */
        BlogSearchResponseDto blogSearchResponseDto = searchSource.callBlogSearch(query, sort, page, size);

        Optional<SearchBlogHistory> findHistory = searchHistoryJpaRepository.findByKeyword(query);

        if (findHistory.isPresent()) {
            findHistory.get().incrementSearchCount();
            searchHistoryJpaRepository.save(findHistory.get());
        } else {
            SearchBlogHistory history = SearchBlogHistory.builder()
                    .keyword(query)
                    .searchCount(1L)
                    .build();

            searchHistoryJpaRepository.save(history);
        }

        setSearchKeyword(query, getCount(query) + 1);

        return blogSearchResponseDto;
    }

    public PopularSearchKeywordResponseDto getTopLimitRank(int limit) {
        /*
            1. 캐시에서 top limit의 인기검색어를 가져온다.
            2. dto로 변환하여 반환한다.
         */
        Set<String> rangeSet = zSetOperations.reverseRange(POPULAR_KEYWORD_KEY, 0, limit - 1);

        return toPopularSearchKeywordResponseDto(new ArrayList<>(rangeSet));
    }

    private PopularSearchKeywordResponseDto toPopularSearchKeywordResponseDto(ArrayList<String> searchBlogTop10Keywords) {
        List<PopularSearchKeywordResponseDto.KeywordDto> popularKeywords = new ArrayList<>();

        for (String keyword : searchBlogTop10Keywords) {
            popularKeywords.add(
                        PopularSearchKeywordResponseDto.KeywordDto.builder()
                                .keyword(keyword)
                                .searchCount(getCount(keyword))
                                .build());
        }

        return PopularSearchKeywordResponseDto.builder()
                .popularKeywords(popularKeywords)
                .dataSize(popularKeywords.size())
                .build();
    }

    private Long getCount(String keyword) {
        if (zSetOperations.score(POPULAR_KEYWORD_KEY, keyword) == null)
            return 0L;
        return zSetOperations.score(POPULAR_KEYWORD_KEY, keyword).longValue();
    }

    private void setSearchKeyword(String keyword, Long count) {
        zSetOperations.add(POPULAR_KEYWORD_KEY, keyword ,count);
    }

    public PopularSearchKeywordResponseDto getPopularKeywords() {
        List<SearchBlogHistory> top10Histories = searchHistoryJpaRepository.findTop10ByOrderBySearchCountDesc();
        return getResult(top10Histories);
    }

    private PopularSearchKeywordResponseDto getResult(List<SearchBlogHistory> top10Histories) {
        List<PopularSearchKeywordResponseDto.KeywordDto> keywordDtos = top10Histories.stream()
                .map(history -> toDto(history))
                .collect(Collectors.toList());

        return PopularSearchKeywordResponseDto.builder()
                .dataSize(keywordDtos.size())
                .popularKeywords(keywordDtos)
                .build();
    }

    private PopularSearchKeywordResponseDto.KeywordDto toDto(SearchBlogHistory history) {
        return PopularSearchKeywordResponseDto.KeywordDto.builder()
                .keyword(history.getKeyword())
                .searchCount(history.getSearchCount())
                .build();
    }
}
