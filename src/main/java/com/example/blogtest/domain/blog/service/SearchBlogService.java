package com.example.blogtest.domain.blog.service;

import com.example.blogtest.domain.blog.api.SearchBlog;
import com.example.blogtest.domain.blog.dto.BlogSearchResponseDto;
import com.example.blogtest.domain.blog.dto.PopularSearchKeywordResponseDto;
import com.example.blogtest.domain.blog.entity.SearchBlogHistory;
import com.example.blogtest.domain.blog.repository.SearchHistoryJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SearchBlogService {

    final private SearchBlog searchSource;
    final private SearchHistoryJpaRepository searchHistoryJpaRepository;

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
                    .searchCount(0L)
                    .build();

            searchHistoryJpaRepository.save(history);
        }

        return blogSearchResponseDto;
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
