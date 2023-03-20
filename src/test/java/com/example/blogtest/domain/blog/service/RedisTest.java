package com.example.blogtest.domain.blog.service;

import com.example.blogtest.domain.blog.api.SearchBlog;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StopWatch;

@SpringBootTest
public class RedisTest {

    @Autowired
    private SearchBlog searchBlog;

    private double externalApiCallTakenTime;

    @BeforeEach
    void setUp() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        searchBlog.callBlogSearch("카카오", "recency", 6,10);
        stopWatch.stop();
        externalApiCallTakenTime = stopWatch.getTotalTimeSeconds();

        System.out.println("외부 api 호출 소요시간 = " + externalApiCallTakenTime);
    }

    @Test
    @Transactional
    public void redis_Test() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        searchBlog.callBlogSearch("카카오", "recency", 6,10);
        stopWatch.stop();

        System.out.println("캐시 조회 소요시간 = " + stopWatch.getTotalTimeSeconds());
    }

}
