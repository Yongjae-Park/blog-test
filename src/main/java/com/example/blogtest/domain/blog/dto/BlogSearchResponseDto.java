package com.example.blogtest.domain.blog.dto;

import com.example.blogtest.application.common.config.JacksonConfiguration;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Getter
@NoArgsConstructor
@ToString
public class BlogSearchResponseDto {

    private List<Document> documents;
    private PageNationData meta;

    @Getter
    @NoArgsConstructor
    @ToString
    public static class Document {
        private String blogname;
        private String contents;

        @JsonDeserialize(using = LocalDateTimeDeserializer.class)
        @JsonSerialize(using = LocalDateTimeSerializer.class)
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
        private LocalDateTime datetime;
        private String thumbnail;
        private String title;
        private String url;
    }
    @Getter
    @NoArgsConstructor
    @ToString
    public static class PageNationData {
        private Boolean is_end;
        private Long pageable_count;
        private Long total_count;
    }
}
