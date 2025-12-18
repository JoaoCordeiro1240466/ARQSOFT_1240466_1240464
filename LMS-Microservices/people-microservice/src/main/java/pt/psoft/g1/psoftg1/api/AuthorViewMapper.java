package pt.psoft.g1.psoftg1.api;

import org.mapstruct.Mapper;
import pt.psoft.g1.psoftg1.api.views.AuthorView;
import pt.psoft.g1.psoftg1.model.Author;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AuthorViewMapper {
    AuthorView toAuthorView(Author author);
    List<AuthorView> toAuthorView(List<Author> authors);
}
