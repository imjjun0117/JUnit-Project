package site.joony.junitproject.domain;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest // DB와 관련된 컴포넌트만 메모리에 로딩 (단위 테스트)
public class BookRepositoryTest {

    @Autowired // DI
    private BookRepository bookRepository;

    //1. 책 등록

    @Test
    public void 책등록_test(){
        System.out.println("책등록_test 실행");
    }

    //2. 책 목록보기

    //3. 책 한권보기

    //4. 책 수정

    //5. 책 삭제

}
