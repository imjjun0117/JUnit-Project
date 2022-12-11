package site.joony.junitproject.service;

import org.hibernate.query.criteria.internal.expression.SimpleCaseExpression;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.internal.matchers.Any;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import site.joony.junitproject.domain.BookRepository;
import site.joony.junitproject.util.MailSender;
import site.joony.junitproject.util.MailSenderStub;
import site.joony.junitproject.web.dto.BookRespDto;
import site.joony.junitproject.web.dto.BookSaveReqDto;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

//@DataJpaTest
@ExtendWith(MockitoExtension.class)
public class BookServiceTest {

//    @Autowired // DI @DataJpaTest가 없으면 메모리에 안뜬다.
//    private BookRepository bookRepository;


    //////가짜 환경 세팅 완료/////
    //@Autowired mockito 작업 시에는 사용 불가
    @InjectMocks // @Mock으로 선언된 가짜 객체를 외존성 주입
    private BookService bookService;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private MailSender mailSender;
    //////가짜 환경 세팅 완료/////

    //문제점 -> 서비스만 테스트하고 싶은데, 레포지토리에이러가 함께 테스트 된다는 점!
    // -> 실제 bookRepository 사용보다는 가짜를 만들어서 사용하기 (Mockito)
    @Test
    public void 책등록하기_테스트(){
        //given
        BookSaveReqDto dto = new BookSaveReqDto();
        dto.setTitle("junit 강의");
        dto.setAuthor("joony");

        //stub(행동정의)
//        MailSenderStub mailSenderStub = new MailSenderStub();

        when(bookRepository.save(any())).thenReturn(dto.toEntity());
        when(mailSender.send()).thenReturn(true);



        //when
//        BookService bookService = new BookService(bookRepository, mailSenderStub);
        //stub 정의를 하지 않은 객체를 바로 넣을 경우 NullPointerException 발생
        BookRespDto bookRespDto = bookService.책등록하기(dto);

        //then
        //Assertions.assertEquals(dto.getTitle(), bookRespDto.getTitle());
        //Assertions.assertEquals(dto.getAuthor(), bookRespDto.getAuthor());
        assertThat(dto.getTitle()).isEqualTo(bookRespDto.getTitle());
        assertThat(dto.getAuthor()).isEqualTo(bookRespDto.getAuthor());

    }

}
