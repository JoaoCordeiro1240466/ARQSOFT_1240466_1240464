package pt.psoft.g1.psoftg1.genremanagement.api;

import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import pt.psoft.g1.psoftg1.genremanagement.model.Genre;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-12-04T02:46:04+0000",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.11 (Oracle Corporation)"
)
@Component
public class GenreViewMapperImpl extends GenreViewMapper {

    @Override
    public GenreView toGenreView(Genre genre) {
        if ( genre == null ) {
            return null;
        }

        GenreView genreView = new GenreView();

        genreView.setGenre( map( genre.getGenre() ) );

        return genreView;
    }

    @Override
    public GenreView mapStringToGenreView(String genre) {
        if ( genre == null ) {
            return null;
        }

        GenreView genreView = new GenreView();

        genreView.setGenre( map( genre ) );

        return genreView;
    }
}
