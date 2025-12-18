package pt.psoft.g1.psoftg1.api.dtos;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pt.psoft.g1.psoftg1.model.Book;
import pt.psoft.g1.psoftg1.model.Description;
import pt.psoft.g1.psoftg1.model.Isbn;
import pt.psoft.g1.psoftg1.model.Title;

import java.util.List;

@Mapper(componentModel = "spring")
public interface BookViewMapper {

    @Mapping(target = "photoURI", ignore = true)
    BookView toBookView(Book book);

    List<BookView> toBookView(List<Book> books);

    default String map(Title value) {
        if (value == null) {
            return null;
        }
        return value.toString();
    }

    default String map(Isbn value) {
        if (value == null) {
            return null;
        }
        return value.toString();
    }

    default String map(Description value) {
        if (value == null) {
            return null;
        }
        return value.toString();
    }
}
