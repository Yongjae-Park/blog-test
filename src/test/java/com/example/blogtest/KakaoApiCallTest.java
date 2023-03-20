package com.example.blogtest;

import com.example.blogtest.domain.blog.dto.BlogSearchResponseDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.HttpClientBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class KakaoApiCallTest {

    @Value("${external.apis.kakao.blog-search-api.url}")
    private String url;

    @Value("${external.apis.kakao.blog-search-api.api-key}")
    private String restAPIKey;

    final private String AUTHORIZATION_KEY = "Authorization";
    final private String PARAM_KEY_QUERY = "query";
    final private String PARAM_KEY_SORT = "sort";
    final private String PARAM_KEY_PAGE = "page";
    final private String PARAM_KEY_SIZE = "size";

    private String queryValue;

    private String sortValue;

    @Autowired
    ObjectMapper objectMapper;

    private static String PATTERN_DATE = "yyyy-MM-dd";
    private static String PATTERN_TIME = "HH:mm:ss";
    private static String PATTERN_DATETIME = String.format("%s %s", PATTERN_DATE, PATTERN_TIME);

    @BeforeEach
    void setUp() {
        queryValue = "카카오";
        sortValue = "recency";
    }

    @DisplayName("Kakao 블로그 검색 api 호출하여 정상 응답을 받아온다.")
    @Test
    public void kakao_api_call_TEST() throws IOException, URISyntaxException {
        HttpClient client = HttpClientBuilder.create().build();
        URI uri = createURI();

        HttpGet getRequest = createHttpGet(uri);
        HttpResponse response = client.execute(getRequest);

        assertThat(response.getStatusLine().getStatusCode()).isEqualTo(HttpStatus.SC_OK);

        printResponse(response);
    }

    private HttpGet createHttpGet(URI uri) {
        HttpGet get = new HttpGet();
        get.setURI(uri);
        get.addHeader(AUTHORIZATION_KEY, restAPIKey);
        return get;
    }

    private void printResponse(HttpResponse response) throws IOException {
        if (response.getStatusLine().getStatusCode() == 200) {
            BlogSearchResponseDto res = toDto(response);
            System.out.println(res);
            System.out.println(res.toString());
        } else {
            System.out.println("response is error : " + response.getStatusLine().getStatusCode());
        }
    }

    private BlogSearchResponseDto toDto(HttpResponse response) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());

        BlogSearchResponseDto dto = mapper.readValue(response.getEntity().getContent(), BlogSearchResponseDto.class);
        return dto;
    }

    private URI createURI() throws URISyntaxException {
        return new URIBuilder(url)
                .addParameter(PARAM_KEY_QUERY, queryValue)
                .addParameter(PARAM_KEY_SORT, sortValue)
                .addParameter(PARAM_KEY_PAGE, "50")
                .addParameter(PARAM_KEY_SIZE, "50")
                .build();
    }

}
