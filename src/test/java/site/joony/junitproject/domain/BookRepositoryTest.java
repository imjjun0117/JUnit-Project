package site.joony.junitproject.domain;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

@DataJpaTest // DB와 관련된 컴포넌트만 메모리에 로딩 (단위 테스트)
public class BookRepositoryTest {
    //통합 테스트 실행할 경우 메서드들은 순차적으로 실행되지 않는다. --> 순서 보장을 하고 싶다면 Order어
    @Autowired // DI
    private BookRepository bookRepository;

// primary key auto_increment 값이 초기화가 안됨 --> error 발생 아래와 같은 방법으로 해결되지만 속도가 느림
//    @Autowired
//    private EntityManager em;
//
//    @AfterEach
//    public void db_init(){
//        bookRepository.deleteAll();
//        em.createNativeQuery("ALTER TABLEbook ALTER COLUMN id RESTART WITH 1")
//                .executeUpdate();
//    }


    //    @BeforeAll // 테스트 시작 전에 한번만 실행
    @BeforeEach // 각 테스트 시작 전에 한번씩 실행
    public void 데이터준비(){

        String title = "junit";
        String author = "hwang";
        Book book = Book.builder().title(title).author(author).build();

        Book bookPS = bookRepository.save(book);
    }//트랜잭션 종료가 안된다
    //그렇다면 트랜잭션이 언제까지 유지될까?
    //
    // 가정 1. [ 데이터준비() + 1 책등록 ](Transaction1) , [ 데이터준비() + 2 책목록보기 ](Transaction2) (검증 완료)
    // 가정 2. [ 데이터준비() + 1 책등록 + 데이터준비() + 2 책등록보기 ](Transaction1) (검증 실패)

    //1. 책 등록
    @Test
    public void 책등록_test(){
        // 1. given ( 데이터 준비 )
        String title = "junit5";
        String author = "joony";
        Book book = Book.builder().title(title).author(author).build();

        // 2. when ( 테스트 실행 )
        Book bookPS = bookRepository.save(book);

        //3. then ( 검증 )
        Assertions.assertEquals(title, bookPS.getTitle());
        Assertions.assertEquals(author, bookPS.getAuthor());

    }//트랜잭션 종료(저장된 데이터 초기화)

    //2. 책 목록보기
    @Test
    public void 책목록보기_test(){
        //given
        String title = "junit";
        String author = "hwang";

        //when
        List<Book> booksPS = bookRepository.findAll();

        System.out.println("트랜잭션 검증 : =======================> " +booksPS.size());

        //then
        Assertions.assertEquals(title, booksPS.get(0).getTitle());
        Assertions.assertEquals(author, booksPS.get(0).getAuthor());

    }//트랜잭션 종료(저장된 데이터 초기화)


    //3. 책 한권보기
    @Sql("classpath:db/tableInit.sql") // book 엔터티를 초기화하고 테스트 실행 (id를 찾는 모든 테스트에 붙여주는게 좋음)
    @Test
    public void 책한권보기_test(){
        //given
        String title = "junit";
        String author = "hwang";

        //when
        Book bookPS = bookRepository.findById(1L).get();

        //then
        Assertions.assertEquals(title, bookPS.getTitle());
        Assertions.assertEquals(author, bookPS.getAuthor());

    }//트랜잭션 종료(저장된 데이터 초기화)

    //4. 책 삭제
    @Sql("classpath:db/tableInit.sql")
    @Test
    public void 책삭제_test(){
        //given
        Long id = 1L;

        //when
        bookRepository.deleteById(id);

        //then
        Assertions.assertFalse(bookRepository.findById(id).isPresent()); // assertFlase -> false일 경우 성공
    }


    // 1, junit, hwang
    //5. 책 수정
    @Sql("classpath:db/tableInit.sql") // 시퀀스 번호를 가지고 있는 경우 초기화는 무조건!!
    @Test
    public void 책수정_test(){

        //given
        Long id = 1L;
        String title = "junit5";
        String author = "joony";

        Book book = new Book(id, title, author);

        //when

//        bookRepository.findAll().stream().forEach((b) -> {
//            // 1, junit, hwang 출력 BeforeEach로 삽입된 값
//            // 하지만 @Sql로 테이블을 삭제했는데 값이 남아있음 why?

    //        @BeforeEach(트랜잭션 시작) -> @Sql 테이블 드랍 -> update (트랜잭션 종료)
    //        트랜잭션이 실행되기 전까지는 데이터가 메모리에 적재된다. 하지만 @Sql 어노테이션으로 테이블을 드랍하는 것은 HDD에 있는 데이터를 날리는 것이기 때문에
    //        메모리에 있는 데이터들은 그대로 보존된다.
    //
    //        Commit : 메모리 데이터 -> HDD 데이터
    //        RollBack : 메모리 데이터 삭제

//            System.out.println(b.getId());
//            System.out.println(b.getTitle());
//            System.out.println(b.getAuthor());
//            System.out.println("=======================");
//        });

        Book bookPS = bookRepository.save( book );

//        bookRepository.findAll().stream().forEach((b) -> {
//            System.out.println(b.getId());
//            System.out.println(b.getTitle());
//            System.out.println(b.getAuthor());
//            System.out.println("=======================");
//        });


        //then
        Assertions.assertEquals( id, bookPS.getId() );
        Assertions.assertEquals( title, bookPS.getTitle() );
        Assertions.assertEquals( author, bookPS.getAuthor() );

    }



}
