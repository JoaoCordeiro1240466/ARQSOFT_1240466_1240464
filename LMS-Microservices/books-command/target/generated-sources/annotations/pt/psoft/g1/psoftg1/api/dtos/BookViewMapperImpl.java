package pt.psoft.g1.psoftg1.api.dtos;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import pt.psoft.g1.psoftg1.model.Book;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-12-19T00:02:24+0000",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.11 (Oracle Corporation)"
)
@Component
public class BookViewMapperImpl implements BookViewMapper {

    @Override
    public BookView toBookView(Book book) {
        if ( book == null ) {
            return null;
        }

        BookView bookView = new BookView();

        bookView.setIsbn( map( book.getIsbn() ) );
        bookView.setTitle( map( book.getTitle() ) );
        bookView.setDescription( map( book.getDescription() ) );
        bookView.setGenreId( book.getGenreId() );
        List<String> list = book.getAuthorIds();
        if ( list != null ) {
            bookView.setAuthorIds( new ArrayList<String>( list ) );
        }

        return bookView;
    }

    @Override
    public List<BookView> toBookView(List<Book> books) {
        if ( books == null ) {
            return null;
        }

        List<BookView> list = new ArrayList<BookView>( books.size() );
        for ( Book book : books ) {
            list.add( toBookView( book ) );
        }

        return list;
    }
}
