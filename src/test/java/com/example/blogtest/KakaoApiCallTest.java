package com.example.blogtest;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.HttpClientBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
public class KakaoApiCallTest {

    private String url;

    private String query;

    private String sort;

    private String authorizationKey;
    @Value("${external.api.api-key}")
    private String restAPIKey;

    private String authorizationValue;

    @BeforeEach
    void setUp() {
        url = "https://dapi.kakao.com/v2/search/blog";
        query = "카카오";
        authorizationKey = "Authorization";
        authorizationValue = "KakaoAK " + restAPIKey;
        sort = "recency";
    }
    @DisplayName("HttpClient 사용하여 블로그검색 api 호출 테스트")
    @Test
    public void kakao_api_call_TEST() throws IOException, URISyntaxException {
        HttpClient client = HttpClientBuilder.create().build();
        URI uri = createURI();

        HttpGet getRequest = createHttpGet(uri); //KEY 입력
        HttpResponse response = client.execute(getRequest);

        assertThat(response.getStatusLine().getStatusCode()).isEqualTo(HttpStatus.SC_OK);

        printResponse(response);
    }

    private HttpGet createHttpGet(URI uri) {
        HttpGet get = new HttpGet();
        get.setURI(uri);
        get.addHeader(authorizationKey, authorizationValue);
        return get;
    }

    private static void printResponse(HttpResponse response) throws IOException {
        if (response.getStatusLine().getStatusCode() == 200) {
            ResponseHandler<String> handler = new BasicResponseHandler();
            String body = handler.handleResponse(response);
            System.out.println(body);
        } else {
            System.out.println("response is error : " + response.getStatusLine().getStatusCode());
        }
    }

    private URI createURI() throws URISyntaxException {
        return new URIBuilder(url)
                .addParameter("query", query)
                .addParameter("sort", sort)
                .build();
    }
}
