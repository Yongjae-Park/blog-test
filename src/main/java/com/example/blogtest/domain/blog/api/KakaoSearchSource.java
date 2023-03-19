package com.example.blogtest.domain.blog.api;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;

public class KakaoSearchSource implements SearchBlog {

    @Value("${external.apis.kakao.blog-search-api.url}")
    private String url;
    @Value("${external.apis.kakao.blog-search-api.api-key}")
    private String restAPIKey;

    private String authorizationValue;
    private String queryValue;
    private String sortValue;

    @Override
    public String callBlogSearch(String query, String sort, Integer page, Integer size) {
        return null;
    }

    @Getter
    public enum ParameterKeyType {
        QUERY("query"),
        SORT("sort"),
        PAGE("page"),
        SIZE("size");

        final private String keyName;

        ParameterKeyType(String keyName) {
            this.keyName = keyName;
        }

        public String getKeyName() {
            return keyName;
        }
    }
}
