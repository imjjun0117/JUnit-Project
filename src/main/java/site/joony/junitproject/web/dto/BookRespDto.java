package site.joony.junitproject.web.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import site.joony.junitproject.domain.Book;

@NoArgsConstructor
@Getter
public class BookRespDto {

    private Long id;
    private String title;
    private String author;

    // 둘 중 한가지 방법을 사용
//    public BookRespDto toDto(Book bookPS){
//        this.id = bookPS.getId();
//        this.title = bookPS.getTitle();
//        this.author = bookPS.getAuthor();
//
//        return this;
//    }
  public static BookRespDto toDto(Book bookPS){
        BookRespDto dto = new BookRespDto();

      dto.id = bookPS.getId();
      dto.title = bookPS.getTitle();
      dto.author = bookPS.getAuthor();

      return dto;
    }

}
