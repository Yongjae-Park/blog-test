package com.example.blogtest.domain.blog.api;

import com.example.blogtest.domain.blog.dto.BlogSearchResponseDto;

public interface SearchBlog {
    BlogSearchResponseDto callBlogSearch(String query, String sort, Integer page, Integer size);
}
