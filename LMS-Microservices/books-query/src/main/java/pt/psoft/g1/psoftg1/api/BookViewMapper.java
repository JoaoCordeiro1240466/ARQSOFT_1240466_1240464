package pt.psoft.g1.psoftg1.api;

import org.mapstruct.Mapper;
import pt.psoft.g1.psoftg1.model.Book;

import java.util.List;

@Mapper(componentModel = "spring")
public interface BookViewMapper {

    BookView toBookView(Book book);

    List<BookView> toBookView(List<Book> books);
}
