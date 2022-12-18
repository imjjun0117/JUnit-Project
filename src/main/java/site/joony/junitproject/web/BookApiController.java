package site.joony.junitproject.web;

import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import site.joony.junitproject.service.BookService;
import site.joony.junitproject.web.dto.response.BookListRespDto;
import site.joony.junitproject.web.dto.response.BookRespDto;
import site.joony.junitproject.web.dto.request.BookSaveReqDto;
import site.joony.junitproject.web.dto.response.CMRespDto;

import javax.validation.Valid;
import java.nio.Buffer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RequiredArgsConstructor
@RestController
public class BookApiController { //컴포지션 = has 관계

    private final BookService bookService;


    // 1. 책등록
    // key=value&key=value
    // { "key" : value, "key" : value }
    @PostMapping("/api/v1/book")
    public ResponseEntity<?> saveBook(@RequestBody @Valid BookSaveReqDto bookSaveReqDto, BindingResult bindingResult) {

        if(bindingResult.hasErrors()){
            Map<String, String> errorMap = new HashMap<>();
            for(FieldError fe : bindingResult.getFieldErrors()){
                errorMap.put(fe.getField(),fe.getDefaultMessage());
            }//end for
            System.out.println("========================");
            System.out.println(errorMap.toString());
            System.out.println("========================");

            throw new RuntimeException(errorMap.toString());
        }//end if

        BookRespDto bookRespDto = bookService.책등록하기(bookSaveReqDto);

        CMRespDto<?> cmRespDto = CMRespDto.builder()
                .code(1).msg("글 저장 성공")
                .body(bookRespDto).build();

        return new ResponseEntity<>(cmRespDto, HttpStatus.CREATED); // 201 = insert
    }

    @PostMapping("/api/v2/book")
    public ResponseEntity<?> saveBook2(@RequestBody BookSaveReqDto bookSaveReqDto, BindingResult bindingResult) {

        BookRespDto bookRespDto = bookService.책등록하기(bookSaveReqDto);

        CMRespDto<?> cmRespDto = CMRespDto.builder()
                .code(1).msg("글 저장 성공")
                .body(bookRespDto).build();

        return new ResponseEntity<>(cmRespDto, HttpStatus.CREATED); // 201 = insert
    }

    // 2. 책목록보기
    @GetMapping("api/v1/book")
    public ResponseEntity<?> getBookList(){
        BookListRespDto bookRespDtos = bookService.책목록보기();
        //List 반환 시 오브젝트 형태로 통일하는게 좋음(컬렉션, 오브젝트 통합되지 않으면 응답받을 때 혼동)
        return new ResponseEntity<>(CMRespDto.builder().code(1)
                                    .body(bookRespDtos).msg("글 목록보기 성공").build(),
                                    HttpStatus.OK);
    }//getBookList

    // 3. 책한건보기
    @GetMapping("api/v1/book/{id}")
    public ResponseEntity<?> getBookOne(@PathVariable Long id){
        BookRespDto bookRespDto = bookService.책한건보기(id);

        return new ResponseEntity<>(CMRespDto.builder().code(1)
                .body(bookRespDto).msg("글 목록보기 성공").build(),
                HttpStatus.OK);
    }//getBookOne

    // 4. 책삭제하기
    @DeleteMapping("api/v1/book/{id}")
    public ResponseEntity<?> deleteBook(@PathVariable Long id){
        bookService.책삭제하기(id);

        return new ResponseEntity<>(CMRespDto.builder().code(1).msg("책삭제 성공")
                                    .body(null).build(), HttpStatus.OK);
    }//deleteBook

    // 5. 책수정하기
    @PutMapping("api/v1/book/{id}")
    public ResponseEntity<?> updateBook(@RequestBody @Valid BookSaveReqDto bookSaveReqDto, @PathVariable Long id, BindingResult bindingResult) {

        BookRespDto bookRespDto = bookService.책수정하기(id, bookSaveReqDto);

        return new ResponseEntity<>(CMRespDto.builder().code(1).msg("책 수정 성공").body(bookRespDto).build(), HttpStatus.OK);
    }


}
