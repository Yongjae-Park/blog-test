package com.example.blogtest.application.common.exception.blog;

public class PageSizeValueOutOfBoundsException extends RuntimeException {
    public PageSizeValueOutOfBoundsException() {
        super("size 값은 1 ~ 50 사이 값만 가능합니다.");
    }
}
