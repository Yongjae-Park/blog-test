package com.example.blogtest.application.controller;

import com.example.blogtest.domain.blog.dto.BlogSearchResponseDto;
import com.example.blogtest.domain.blog.dto.PopularSearchKeywordResponseDto;
import com.example.blogtest.domain.blog.service.SearchBlogService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.IOException;
import java.net.URISyntaxException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/blog")
public class SearchBlogController {

    final private SearchBlogService searchService;

    @GetMapping("/search")
    public Page<BlogSearchResponseDto.Document> getSearch(@RequestParam @NotNull String query,
                                         Pageable pageRequest) {
        Page<BlogSearchResponseDto.Document> result = searchService.search(query, pageRequest);
        return result;
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
