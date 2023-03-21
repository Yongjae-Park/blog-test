package com.example.blogtest.application.controller;

import com.example.blogtest.application.common.exception.blog.PageSizeValueOutOfBoundsException;
import com.example.blogtest.application.common.exception.blog.PageValueOutOfBoundsException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.MissingServletRequestParameterException;

import javax.validation.ConstraintViolationException;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc

public class SearchBlogControllerTest {

    @Autowired
    private MockMvc mockMvc;

    final private String PATH = "/blog";

    private ObjectMapper mapper = new ObjectMapper();


    @DisplayName("블로그 검색 실패 - query값이 null이면 파라미터 검증에 실패한다.")
    @Test
    public void getBlogSearchResult_queryValidationNotNull_failure_Test() throws Exception {

        mockMvc.perform(get(PATH + "/search")
                        .param("page", "1")
                        .param("size", "1")
                        .param("sort", "recency")
                ).andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect((result ->
                        assertTrue(result.getResolvedException().getClass().isAssignableFrom(MissingServletRequestParameterException.class))));
    }

    @DisplayName("블로그 검색 실패 - query값이 공백이면 파라미터 검증에 실패한다.")
    @Test
    public void getBlogSearchResult_queryValueNotEmpty_failure_Test() throws Exception {

        mockMvc.perform(get(PATH + "/search")
                        .param("query","")
                        .param("page", "1")
                        .param("size", "1")
                        .param("sort", "recency")
                ).andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect((result ->
                        assertTrue(result.getResolvedException().getClass().isAssignableFrom(ConstraintViolationException.class))));
    }

    @DisplayName("블로그 검색 실패 - page값이 1보다 작아 파라미터 검증에 실패한다.")
    @Test
    public void getBlogSearchResult_pageValueOutOfBounds_failure_Test() throws Exception {

        mockMvc.perform(get(PATH + "/search")
                        .param("query","카카오")
                        .param("page", "0")
                        .param("size", "1")
                        .param("sort", "recency")
                ).andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect((result ->
                        assertTrue(result.getResolvedException().getClass().isAssignableFrom(PageValueOutOfBoundsException.class))));
    }

    @DisplayName("블로그 검색 실패 - page값이 50을 초과해 파라미터 검증에 실패한다.")
    @Test
    public void getBlogSearchResult_pageValueOutOfBounds2_failure_Test() throws Exception {

        mockMvc.perform(get(PATH + "/search")
                        .param("query","카카오")
                        .param("page", "51")
                        .param("size", "1")
                        .param("sort", "recency")
                ).andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect((result ->
                        assertTrue(result.getResolvedException().getClass().isAssignableFrom(PageValueOutOfBoundsException.class))));
    }

    @DisplayName("블로그 검색 실패 - size값이 1보다 작아 파라미터 검증에 실패한다.")
    @Test
    public void getBlogSearchResult_pageSizeValueOutOfBounds_failure_Test() throws Exception {

        mockMvc.perform(get(PATH + "/search")
                        .param("query","카카오")
                        .param("page", "1")
                        .param("size", "0")
                        .param("sort", "recency")
                ).andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect((result ->
                        assertTrue(result.getResolvedException().getClass().isAssignableFrom(PageSizeValueOutOfBoundsException.class))));
    }

    @DisplayName("블로그 검색 실패 - size 50을 초과해 파라미터 검증에 실패한다.")
    @Test
    public void getBlogSearchResult_pageSizeValueOutOfBounds2_failure_Test() throws Exception {

        mockMvc.perform(get(PATH + "/search")
                        .param("query","카카오")
                        .param("page", "1")
                        .param("size", "51")
                        .param("sort", "recency")
                ).andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect((result ->
                        assertTrue(result.getResolvedException().getClass().isAssignableFrom(PageSizeValueOutOfBoundsException.class))));
    }

    @DisplayName("블로그 검색 성공 - page 파라미터가 없어도 디폴트 1페이지로 설정되어 블로그 검색에 성공한다.")
    @Test
    public void getBlogSearchResult_pageValueValidation_success_Test() throws Exception {
        int defaultPageValue = 1;

        mockMvc.perform(get(PATH + "/search")
                        .param("query","카카오")
                        .param("size", "10")
                        .param("sort", "recency")
                ).andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.pageable.pageNumber").value(defaultPageValue));
    }

    @DisplayName("블로그 검색 성공 - size 파라미터가 없어도 디폴트 1페이지로 설정되어 블로그 검색에 성공한다.")
    @Test
    public void getBlogSearchResult_pageSizeValueValidation_success_Test() throws Exception {
        int defaultSize = 10;

        mockMvc.perform(get(PATH + "/search")
                        .param("query","카카오")
                        .param("page", "1")
                        .param("sort", "recency")
                ).andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size").value(defaultSize));
    }

}