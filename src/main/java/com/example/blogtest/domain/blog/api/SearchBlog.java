package com.example.blogtest.domain.blog.api;

public interface SearchBlog {
    String callBlogSearch(String query, String sort, Integer page, Integer size);
}
