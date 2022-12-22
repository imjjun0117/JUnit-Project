package site.joony.junitproject.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;
import site.joony.junitproject.domain.Book;
import site.joony.junitproject.domain.BookRepository;
import site.joony.junitproject.util.MailSender;
import site.joony.junitproject.web.dto.response.BookListRespDto;
import site.joony.junitproject.web.dto.response.BookRespDto;
import site.joony.junitproject.web.dto.request.BookSaveReqDto;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

//@DataJpaTest
@ActiveProfiles("dev") // 개발모드일때(application_dev.yml) 작동해라
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
        assertThat(bookRespDto.getTitle()).isEqualTo(dto.getTitle());
        assertThat(bookRespDto.getAuthor()).isEqualTo(dto.getAuthor());

    }

    //체크 포인트
    @Test
    public void 책목록보기_테스트(){
        //given

        //stub(가설)
        List<Book> books = new ArrayList<>();
        books.add(new Book(1L, "junit 강의", "메타코딩"));
        books.add(new Book(2L, "spring 강의", "겟인데어"));

        when(bookRepository.findAll()).thenReturn(books);

        //when(실행)
        BookListRespDto bookRespDtoList =  bookService.책목록보기();

        //print
//        bookRespDtoList.stream().forEach((dto)->{
//            System.out.println("============ 테스트");
//            System.out.println(dto.getId());
//            System.out.println(dto.getTitle());
//
//        });

        //then(검증)
        assertThat(bookRespDtoList.getItems().get(0).getTitle()).isEqualTo("junit 강의");
        assertThat(bookRespDtoList.getItems().get(0).getAuthor()).isEqualTo("메타코딩");
        assertThat(bookRespDtoList.getItems().get(1).getTitle()).isEqualTo("spring 강의");
        assertThat(bookRespDtoList.getItems().get(1).getAuthor()).isEqualTo("겟인데어");
    }

    @Test
    public void 책한건보기_테스트(){
        //given
        Long id = 1L;
        Book book = new Book(1L,"junit강의","메타코딩");

        Optional<Book> bookOP = Optional.of(book);

        //stub
        when(bookRepository.findById(id)).thenReturn(bookOP);


        //when
        BookRespDto bookRespDto = bookService.책한건보기(id);

        //then

        assertThat(bookRespDto.getTitle()).isEqualTo(book.getTitle());
        assertThat(bookRespDto.getAuthor()).isEqualTo(book.getAuthor());

    }

    @Test
    public void 책수정하기_테스트(){

        //given
        Long id = 1L;
        BookSaveReqDto dto = new BookSaveReqDto();
        dto.setTitle("spring강의"); // junit강의
        dto.setAuthor("겟인데어"); // 메타코딩

        //stub
        Book book = new Book(1L,"junit강의","메타코딩");
        Optional<Book> bookOP = Optional.of(book);
        when(bookRepository.findById(id)).thenReturn(bookOP);

        //when
        BookRespDto bookRespDto = bookService.책수정하기(id,dto);

        //then
        assertThat(bookRespDto.getTitle()).isEqualTo(dto.getTitle());
        assertThat(bookRespDto.getAuthor()).isEqualTo(dto.getAuthor());
    }

}
