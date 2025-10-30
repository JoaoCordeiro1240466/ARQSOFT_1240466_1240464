package pt.psoft.g1.psoftg1.infrastructure.persistence.sql.mapper;

import org.springframework.stereotype.Component;
import pt.psoft.g1.psoftg1.bookmanagement.model.Book;
import pt.psoft.g1.psoftg1.infrastructure.persistence.sql.model.BookJpaEntity;
import pt.psoft.g1.psoftg1.infrastructure.persistence.sql.mapper.GenreSqlMapper;
import pt.psoft.g1.psoftg1.infrastructure.persistence.sql.model.GenreJpaEntity;
import lombok.RequiredArgsConstructor;

import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class BookSqlMapper {

    // Mappers das entidades relacionadas
    // O Spring vai injetá-los (assumindo que já existem)
    private final AuthorSqlMapper authorMapper;
    private final GenreSqlMapper genreMapper; // <-- Vais precisar de criar este

    public Book toDomain(BookJpaEntity entity) {
        if (entity == null) return null;

        Book domain = new Book();
        domain.setPk(entity.getPk());
        domain.setVersion(entity.getVersion());
        domain.setIsbn(entity.getIsbn().toString());
        domain.setTitle(entity.getTitle().toString());
        domain.setDescription(entity.getDescription() != null ? entity.getDescription().toString() : null);

        // Converter relações
        if (entity.getGenre() != null) {
            domain.setGenre(genreMapper.toDomain(entity.getGenre()));
        }

        if (entity.getAuthors() != null) {
            domain.setAuthors(
                    entity.getAuthors().stream()
                            .map(authorMapper::toDomain)
                            .collect(Collectors.toList())
            );
        }

        if (entity.getPhoto() != null) {
            domain.setPhoto(entity.getPhoto().getPhotoFile());
        }

        return domain;
    }

    public BookJpaEntity toEntity(Book domain) {
        if (domain == null) return null;

        BookJpaEntity entity = new BookJpaEntity();
        entity.setPk(domain.getPk());
        entity.setVersion(domain.getVersion());
        entity.setIsbn(new pt.psoft.g1.psoftg1.bookmanagement.model.Isbn(domain.getIsbn()));
        entity.setTitle(new pt.psoft.g1.psoftg1.bookmanagement.model.Title(domain.getTitle().toString()));

        if (domain.getDescription() != null) {
            entity.setDescription(new pt.psoft.g1.psoftg1.bookmanagement.model.Description(domain.getDescription()));
        }

        // Converter relações
        if (domain.getGenre() != null) {
            entity.setGenre(genreMapper.toEntity(domain.getGenre()));
        }

        if (domain.getAuthors() != null) {
            entity.setAuthors(
                    domain.getAuthors().stream()
                            .map(authorMapper::toEntity)
                            .collect(Collectors.toList())
            );
        }

        entity.setPhoto(domain.getPhoto());

        return entity;
    }
}
