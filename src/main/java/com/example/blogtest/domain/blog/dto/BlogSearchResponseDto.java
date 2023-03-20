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

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Getter
@NoArgsConstructor
@ToString
public class BlogSearchResponseDto implements Serializable {

    private List<Document> documents;
    private PageNationData meta;

    @Getter
    @NoArgsConstructor
    @ToString
    public static class Document implements Serializable{
        private String blogname;
        private String contents;

        @JsonDeserialize(using = LocalDateTimeDeserializer.class)
        @JsonSerialize(using = LocalDateTimeSerializer.class)
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS+09:00")
        private LocalDateTime datetime;
        private String thumbnail;
        private String title;
        private String url;
    }
    @Getter
    @NoArgsConstructor
    @ToString
    public static class PageNationData implements Serializable{
        private Boolean is_end;
        private Long pageable_count;
        private Long total_count;
    }
}
