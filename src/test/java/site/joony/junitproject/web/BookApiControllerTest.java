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
import site.joony.junitproject.service.BookService;
import site.joony.junitproject.web.dto.request.BookSaveReqDto;

import static org.assertj.core.api.Assertions.assertThat;

//통합테스트(C, S, R)
//모든 레이어 통합
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BookApiControllerTest {

    @Autowired // final로 DI 주입 불가능 Autowired 사용
    private BookService bookService;

    @Autowired
    private TestRestTemplate rt;

    private static ObjectMapper om;
    private static HttpHeaders headers;
    @BeforeAll
    public static void init(){
        om = new ObjectMapper();
        headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
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

        ResponseEntity<?> response = rt.exchange("/api/v1/book", HttpMethod.POST, request, String.class);
        System.out.println(response.getBody());

        //then
        DocumentContext dc = JsonPath.parse(response.getBody());
        Object title = dc.read("$.body.title"); // 제네릭으로 인해 Object 타입이 반환된다.
        Object author = dc.read("$.body.author");

        assertThat(title).isEqualTo("스프링1강");
        assertThat(author).isEqualTo("겟인데어");

    }


}//BookApiControllerTest
