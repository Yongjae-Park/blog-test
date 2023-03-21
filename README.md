# 블로그 검색 서비스

## 기능 설명
1. 블로그 검색
   * 키워드로 블로그 검색
     * 검색소스는 카카오 API의 '키워드로 블로그 검색'(https://developers.kakao.com/docs/latest/ko/daum-search/dev-guide#search-blog)을 활용
   * 검색 결과에서 sorting(정확도순, 최신순) 지원
     * 호출시 파라미터에 "accuracy"(정확도순) 또는 "recency"(최신순)입력, default: "accuracy"
   * 검색 결과는 pagenation 형태로 제공
   * 추후 카카오 API 이외에 새로운 검색 소스가 추가될 수 있음을 고려
     * 인터페이스에 의존, 구현체 외부주입 방식으로 변경에 용이 하도록 구현
2. 인기 검색어 목록
   * 사용자들이 많이 검색한 순서대로, 최대 10개의 검색 키워드를 제공
     * db/redis에서 조회 두가지 방식 제공
   * 검색어 별로 검색된 횟수도 함께 표기

## api 명세서
   * ./API.md 파일 참고
## 추가 라이브러리
1. Redis 추가
   * Redis 서버 설정
     * host: localhost
     * port: 6379
   * 백업 및 warming은 고려하지 않음
2. openapi 추가
   * swagger test위한 디펜던시 추가
## h2 DB datasource 정보
    url: jdbc:h2:tcp://localhost/~/blog-test
    username: sa
    password:
    driver-class-name: org.h2.Driver

## jar파일 다운로드
   * 다운로드 링크
     * github : https://github.com/Yongjae-Park/blog-test/raw/dev/buildjar/blog-test-0.0.1.jar
     * google drice: https://drive.google.com/file/d/18gDkAsEofk3pAAEbSqF97WopdB9gTXVA/view?usp=sharing
