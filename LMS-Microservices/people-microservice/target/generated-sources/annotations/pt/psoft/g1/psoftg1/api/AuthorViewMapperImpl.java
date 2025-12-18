package pt.psoft.g1.psoftg1.api;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import pt.psoft.g1.psoftg1.api.views.AuthorView;
import pt.psoft.g1.psoftg1.model.Author;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-12-18T20:09:58+0000",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.11 (Oracle Corporation)"
)
@Component
public class AuthorViewMapperImpl implements AuthorViewMapper {

    @Override
    public AuthorView toAuthorView(Author author) {
        if ( author == null ) {
            return null;
        }

        AuthorView authorView = new AuthorView();

        authorView.setAuthorNumber( author.getAuthorNumber() );
        authorView.setBio( author.getBio() );

        return authorView;
    }

    @Override
    public List<AuthorView> toAuthorView(List<Author> authors) {
        if ( authors == null ) {
            return null;
        }

        List<AuthorView> list = new ArrayList<AuthorView>( authors.size() );
        for ( Author author : authors ) {
            list.add( toAuthorView( author ) );
        }

        return list;
    }
}
