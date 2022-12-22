package site.joony.junitproject.web;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.stereotype.Repository;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import site.joony.junitproject.domain.Book;
import site.joony.junitproject.domain.BookRepository;
import site.joony.junitproject.service.BookService;
import site.joony.junitproject.web.dto.request.BookSaveReqDto;
import site.joony.junitproject.web.dto.response.BookRespDto;

import java.nio.DoubleBuffer;

import static org.assertj.core.api.Assertions.assertThat;

//통합테스트(C, S, R)
//모든 레이어 통합
@ActiveProfiles("dev") // 개발모드일때(application_dev.yml) 작동해라
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BookApiControllerTest {

//    @Autowired // final로 DI 주입 불가능 Autowired 사용
//    private BookService bookService;

    @Autowired
    private TestRestTemplate rt;

    @Autowired
    private BookRepository bookRepository;

    private static ObjectMapper om;
    private static HttpHeaders headers;

    @BeforeAll //BeforeAll로 선언된 메서드는 static!
    public static void init(){
        om = new ObjectMapper();
        headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
    }

    @BeforeEach
    public void 데이터준비(){
        String title = "junit";
        String author = "겟인데어";
        Book book = Book.builder()
                .title(title)
                .author(author)
                .build();
        bookRepository.save(book);
    }

    @Sql("classpath:db/tableInit.sql")
    @Test
    public void udpateBook_test() throws JsonProcessingException {

        //given
        Integer id = 1;
        BookSaveReqDto bookSaveReqDto = new BookSaveReqDto();
        bookSaveReqDto.setTitle("spring");
        bookSaveReqDto.setAuthor("joony");

        String body = om.writeValueAsString(bookSaveReqDto);


        //when
        HttpEntity<String> request = new HttpEntity<>(body, headers);
        ResponseEntity<String> response = rt.exchange("/api/v1/book/" + id, HttpMethod.PUT, request, String.class);

        DocumentContext dc = JsonPath.parse(response.getBody());

        String title = dc.read("$.body.title");
        String author = dc.read("$.body.author");

        //then

        assertThat(title).isEqualTo("spring");
        assertThat(author).isEqualTo("joony");



    }


    @Sql("classpath:db/tableInit.sql")
    @Test
    public void deleteBook_test(){
        //given
        Integer id = 1;

        //when
        HttpEntity<String> request = new HttpEntity<>(null, headers);
        ResponseEntity<String> response = rt.exchange("/api/v1/book/"+id, HttpMethod.DELETE, request, String.class);

//        System.out.println(response.getBody());

        DocumentContext dc = JsonPath.parse(response.getBody());
        Integer code = dc.read("$.code");

        //then
        //상태코드
        System.out.println("deleteBook_test : " + response.getStatusCode());
        //상태코드번호
        System.out.println("deleteBook_test : " + response.getStatusCodeValue());

        assertThat(code).isEqualTo(1);

    }

    @Sql("classpath:db/tableInit.sql")
    @Test
    public void getBookOne_test(){ // 1. getBookOne_test 시작전에 BeforeEach를 시작하는데 !! 이 모든 것 전에 테이블을 한번 초기화함
        //given
        Integer id = 1;

        //when
        HttpEntity<String> request = new HttpEntity<>(null, headers);
        ResponseEntity<String> response = rt.exchange("/api/v1/book/"+id, HttpMethod.GET, request, String.class);

//        System.out.println(response.getBody());

        DocumentContext dc = JsonPath.parse(response.getBody());
        String title = dc.read("$.body.title");
        Integer code = dc.read("$.code");

        //then
        assertThat(title).isEqualTo("junit");
        assertThat(code).isEqualTo(1);
    }

    @Sql("classpath:db/tableInit.sql")
    @Test
    public void getBookList_test(){
        //given

        //when
        HttpEntity<String> request = new HttpEntity<>(null, headers); //get 요청은 body가 없음
        ResponseEntity<String> response = rt.exchange("/api/v1/book", HttpMethod.GET, request, String.class);

        System.out.println(response.getBody());
        //then
        DocumentContext dc = JsonPath.parse(response.getBody());
        Integer code = dc.read("$.code");
        String title = dc.read("$.body.items[0].title");

        assertThat(code).isEqualTo(1);
        assertThat(title).isEqualTo("junit");

    }

    @Test
    public void saveBook_test() throws Exception {
        //given
        BookSaveReqDto bookSaveReqDto = new BookSaveReqDto();
        bookSaveReqDto.setTitle("스프링1강");
        bookSaveReqDto.setAuthor("겟인데어");

        String body = om.writeValueAsString(bookSaveReqDto);
        System.out.println("=========================");
        System.out.println(body);
        System.out.println("=========================");

        //when
        HttpEntity<String> request = new HttpEntity<>(body, headers);

        //요청주소, 요청방식, 요청데이터, 응답방식
        // 제네릭이 (?) extends Object 이면 Object 타입이 반환된다.
        ResponseEntity<String> response = rt.exchange("/api/v1/book", HttpMethod.POST, request, String.class);

        System.out.println(response.getBody());

        //then
        DocumentContext dc = JsonPath.parse(response.getBody());

        //ReponseEntity<String> 제네릭 타입과 맞춘다
        String title = dc.read( "$.body.title");
        String author = dc.read("$.body.author");

        assertThat(title).isEqualTo("스프링1강");
        assertThat(author).isEqualTo("겟인데어");

    }


}//BookApiControllerTest
