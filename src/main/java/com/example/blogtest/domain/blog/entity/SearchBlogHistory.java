package com.example.blogtest.domain.blog.entity;

import lombok.Builder;
import lombok.Getter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Version;

@Entity
@Getter
public class SearchBlogHistory {
    @Id
    @GeneratedValue
    private Long id;

    private String keyword;

    private Long searchCount;

    @Version
    private Long version;

    public SearchBlogHistory() {}

    @Builder
    public SearchBlogHistory(String keyword, Long searchCount) {
        this.keyword = keyword;
        this.searchCount = searchCount;
    }
    public void incrementSearchCount() {
        searchCount += 1;
    }
}
