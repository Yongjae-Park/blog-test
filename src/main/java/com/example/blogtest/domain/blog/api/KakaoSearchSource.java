package com.example.blogtest.domain.blog.api;

import com.example.blogtest.domain.blog.dto.BlogSearchResponseDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class KakaoSearchSource implements SearchBlog {

    @Value("${external.apis.kakao.blog-search-api.url}")
    private String url;
    @Value("${external.apis.kakao.blog-search-api.api-key}")
    private String restAPIKey;
    final private String AUTHORIZATION_KEY = "Authorization";

    @Override
    @Cacheable(cacheNames = "kakaoBlogSearchApiCache", key = "#query + #page + #size")
    public BlogSearchResponseDto callBlogSearch(String query, String sort, Integer page, Integer size) {
        System.out.println("not cache");
        BlogSearchResponseDto responseBody = null;
        try {
            HttpClient client = HttpClientBuilder.create().build();
            URI uri = createURI(query, sort, page, size);
            HttpGet getRequest = createHttpGet(uri);
            HttpResponse response = client.execute(getRequest);
            responseBody = toDto(response);
        }catch (URISyntaxException | IOException e) {
            e.printStackTrace();
        }
        return responseBody;
    }

    private HttpGet createHttpGet(URI uri) {

        HttpGet get = new HttpGet();
        get.setURI(uri);
        get.addHeader(AUTHORIZATION_KEY, restAPIKey);

        return get;
    }

    private BlogSearchResponseDto toDto(HttpResponse response) throws IOException {
        if (response.getStatusLine().getStatusCode() == 200) {
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());

            return mapper.readValue(
                    response.getEntity().getContent(), BlogSearchResponseDto.class);
        }
        throw new RuntimeException("Http response status is not OK!!");
    }

    private URI createURI(String query, String sort, Integer page, Integer size) throws URISyntaxException {
        return new URIBuilder(url)
                .addParameter(ParameterKeyType.QUERY.getKeyName(), query)
                .addParameter(ParameterKeyType.SORT.getKeyName(), sort)
                .addParameter(ParameterKeyType.PAGE.getKeyName(), String.valueOf(page))
                .addParameter(ParameterKeyType.SIZE.getKeyName(), String.valueOf(size))
                .build();
    }

    public enum ParameterKeyType {
        QUERY("query"),
        SORT("sort"),
        PAGE("page"),
        SIZE("size");

        final private String keyName;

        ParameterKeyType(String keyName) {
            this.keyName = keyName;
        }

        public String getKeyName() {
            return keyName;
        }
    }
}
