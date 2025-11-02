package pt.psoft.g1.psoftg1.infrastructure.persistence.sql.mapper;

import org.springframework.stereotype.Component;
import pt.psoft.g1.psoftg1.genremanagement.model.Genre;
import pt.psoft.g1.psoftg1.infrastructure.persistence.sql.model.GenreJpaEntity;

@Component
public class GenreSqlMapper {

    public Genre toDomain(GenreJpaEntity entity) {
        if (entity == null) {
            return null;
        }
        Genre domain = new Genre(entity.getGenre());
        domain.setPk(entity.getPk());
        return domain;
    }

    public GenreJpaEntity toEntity(Genre domain) {
        if (domain == null) {
            return null;
        }
        GenreJpaEntity entity = new GenreJpaEntity();
        entity.setPk(domain.getPk());
        entity.setGenre(domain.getGenre());
        return entity;
    }
}