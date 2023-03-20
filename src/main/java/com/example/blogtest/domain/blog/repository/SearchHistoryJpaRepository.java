package com.example.blogtest.domain.blog.repository;

import com.example.blogtest.domain.blog.entity.SearchBlogHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import javax.persistence.LockModeType;
import java.util.Optional;

public interface SearchHistoryJpaRepository extends JpaRepository<SearchBlogHistory, Long> {

    @Lock(LockModeType.PESSIMISTIC_FORCE_INCREMENT)
    Optional<SearchBlogHistory> findByKeyword(String keyword);
}
