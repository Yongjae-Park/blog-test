package com.example.blogtest.application.controller;

import com.example.blogtest.domain.blog.dto.BlogSearchResponseDto;
import com.example.blogtest.domain.blog.dto.PopularSearchKeywordResponseDto;
import com.example.blogtest.domain.blog.service.SearchBlogService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@RestController
@RequiredArgsConstructor
@RequestMapping("/blog")
@Validated
public class SearchBlogController {

    final private SearchBlogService searchService;

    @GetMapping("/search")
    public Page<BlogSearchResponseDto.Document> getBlogSearchResult(@RequestParam @NotNull @NotEmpty String query,
                                         Pageable pageable) {
        return searchService.search(query, pageable);
    }

    @GetMapping("/popular_keyword/from_db")
    public PopularSearchKeywordResponseDto getPopularKeywordsV1_fromDB() {
        return searchService.getPopularKeywords();
    }

    @GetMapping("/popular_keyword/from_cache/{limit}")
    public PopularSearchKeywordResponseDto getPopularKeywordsV2_fromCache(@PathVariable int limit) {
        return searchService.getTopLimitRank(limit);
    }

    @GetMapping("/popular_keyword/from_cache/top10")
    public PopularSearchKeywordResponseDto getPopularKeywordsV3_fromCache_top10() {
        return searchService.getTopLimitRank(10);
    }

}
