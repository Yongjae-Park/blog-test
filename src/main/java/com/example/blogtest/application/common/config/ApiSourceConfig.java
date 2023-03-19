package com.example.blogtest.application.common.config;

import com.example.blogtest.domain.blog.api.KakaoSearchSource;
import com.example.blogtest.domain.blog.api.SearchBlog;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApiSourceConfig {

    @Bean
    public SearchBlog searchBlogSource() {
        return new KakaoSearchSource();
    }
}
