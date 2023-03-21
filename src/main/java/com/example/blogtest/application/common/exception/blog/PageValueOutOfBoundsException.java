package com.example.blogtest.application.common.exception.blog;

public class PageValueOutOfBoundsException extends RuntimeException{
    public PageValueOutOfBoundsException() {
        super("page 값은 1 ~ 50 사이 값만 가능합니다.");
    }
}
