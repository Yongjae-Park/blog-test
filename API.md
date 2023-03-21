# API 명세서

## 블로그 검색 API
- 키워드로 블로그를 검색한다.
  - [Request]
    * path 
      * /blog/search 
    * Method 
      * GET 
    * Parameters:
      + query (required)
        + type: String
      + page
        + type: Integer
      + size
        + type: Integer
      + sort
        + type: String
        + example: "accuracy" or "recency"

  - [Success Response]
    - HTTP 200 OK
  
    ```json
    {
        "content": [
        {
            "blogname": "하루의 일상",
            "contents": "츄르 <b>고양이</b> 간식 코스트코 내 돈 내산 했어요. 코스트코에도 <b>고양이</b> 간식 츄르를 판다는 사실을 아시나요? 몇 년 전부터 코스트코 오프라인에서 꾸준히 구입하다가 이번에는 온라인스토어에서 주문해 봤어요. 양이 많아서 1월 구입했는데도 아직도 먹이고 있답니다. 구입할 당시에 90개입 중에 코스트코가 가장 저렴...",
            "datetime": "2023-03-16T07:00:41.000+09:00",
            "thumbnail": "https://search1.kakaocdn.net/argon/130x130_85_c/KZmSggdPeiu",
            "title": "츄르 <b>고양이</b> 간식 코스트코 내돈내산",
            "url": "http://2haru.tistory.com/113"
        },
          ...
        ],
        "pageable": {
            "sort": {
            "empty": true,
            "sorted": false,
            "unsorted": true
            },
        "offset": 5,
        "pageNumber": 1,
        "pageSize": 5,
        "unpaged": false,
        "paged": true
        },
        "totalPages": 2,
        "totalElements": 10,
        "last": true,
        "size": 5,
        "number": 1,
        "sort": {
        "empty": true,
        "sorted": false,
        "unsorted": true
        },
        "numberOfElements": 5,
        "first": false,
        "empty": false
    }    
     ```
  - [Error Response]
    - query 값이 null인 경우 
      - HTTP 400 BAD REQUEST
        ```json 
              {
                  "timestamp": "2023-03-21T22:46:35.981645",
                  "message": "query값은 null일 수 없습니다."
              }
        ```
    - query값이 empty인 경우
        - HTTP 400 BAD REQUEST
          ```json 
          {
              "timestamp": "2023-03-21T22:48:02.268018",
              "message": "getBlogSearchResult.query: 비어 있을 수 없습니다"
          }
          ```
    - page값 1~50 사이의 값이 아닌 경우
        - HTTP 400 BAD REQUEST
          ```json
            {
                "timestamp": "2023-03-21T22:49:35.609001",
                "message": "page 값은 1 ~ 50 사이 값만 가능합니다."
             }
             ```
    - size값 1~50 사이의 값이 아닌 경우
      - HTTP 400 BAD REQUEST
        ```json
          {
              "timestamp": "2023-03-21T22:50:19.730672",
              "message": "size 값은 1 ~ 50 사이 값만 가능합니다."
           }
            ```
## 인기검색어 조회 db로 조회
- 인기검색어 최상위 10개를 조회한다.
- [Request]
    - path
        - /blog/popular_keyword/from_db
    - Method
        - GET
    - parameters
- [Success Response]
    - HTTP 200 OK
      ```json
      {
          "popularKeywords": [
           {
                "keyword": "카카오",
                "searchCount": 2
           },
      {
           "keyword": "고양이",
           "searchCount": 1
       }
       ],
       "dataSize": 2
       }   
      ```
## 인기검색어 조회 cache로 10건 조회
  - 최상위 인기검색어 10개를 조회한다.
    - [Request]
      - path
          - /blog/popular_keyword/from_cache/top10
      - Method
          - GET
      - parameters
    - [Success Response]
        - HTTP 200 OK
          ```json
          {
              "popularKeywords": [
               {
                    "keyword": "카카오",
                    "searchCount": 2
               },
          {
               "keyword": "고양이",
               "searchCount": 1
           }
           ],
           "dataSize": 2
           }   
          ```
## 인기검색어 조회 cache로 n건 조회
  - 최상위 인기검색어 n개를 조회한다.
    - [Request]
        - path
            - /blog/popular_keyword/from_cache/{limit}
        - Method
            - GET
        - pathParameters
          - limit
            - 조회할 최상위 인기 검색어 갯수
    - [Success Response]
        - HTTP 200 OK
          ```json
          {
              "popularKeywords": [
               {
                    "keyword": "카카오",
                    "searchCount": 2
               },
          {
               "keyword": "고양이",
               "searchCount": 1
           }
           ],
           "dataSize": 2
           }   
          ```