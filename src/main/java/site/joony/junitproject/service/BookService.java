package site.joony.junitproject.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.joony.junitproject.domain.Book;
import site.joony.junitproject.domain.BookRepository;
import site.joony.junitproject.web.dto.BookRespDto;
import site.joony.junitproject.web.dto.BookSaveReqDto;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class BookService {

    private final BookRepository bookRepository; // final이 붙는 순간 객체에 값이 들어있어야함

    //1. 책등록
    @Transactional( rollbackFor = RuntimeException.class ) // 런타임예외가 떨어지면 롤백을 하겠다
    public BookRespDto 책등록하기(BookSaveReqDto dto){
        Book bookPS = bookRepository.save(dto.toEntity());
        // BookPS를 return 시키면 지연 로딩이 발생하여 많은 변수가 생긴다.
        // 영속화된 객체는 서비스단을 통과하지 못하게 막아야한다!
        return BookRespDto.toDto(bookPS);
    }
    //2. 책 목록 보기
    public List<BookRespDto> 책목록보기(){
        return bookRepository.findAll().stream().map(BookRespDto::toDto).collect(Collectors.toList()); // 복제된 bookPS들을 Stream에 담아 List로 변환하여 반환

        //toDto가 static 메소드가 아닌 경우
//        return bookRepository.findAll().stream().map(new BookRespDto()::toDto).collect(Collectors.toList()); // 복제된 bookPS들을 Stream에 담아 List로 변환하여 반환
    }


    //3. 책 한건 보기

    //3. 책 삭제

    //5. 책 수정



}
