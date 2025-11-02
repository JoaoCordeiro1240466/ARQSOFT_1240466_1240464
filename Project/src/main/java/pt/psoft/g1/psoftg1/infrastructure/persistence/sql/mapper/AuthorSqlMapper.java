package pt.psoft.g1.psoftg1.infrastructure.persistence.sql.mapper;

import org.springframework.stereotype.Component;
import pt.psoft.g1.psoftg1.authormanagement.model.Author;
import pt.psoft.g1.psoftg1.authormanagement.model.Bio;
import pt.psoft.g1.psoftg1.infrastructure.persistence.sql.model.AuthorJpaEntity;
import pt.psoft.g1.psoftg1.shared.model.Name;

@Component
public class AuthorSqlMapper {

    public Author toDomain(AuthorJpaEntity entity) {
        if (entity == null) {
            return null;
        }

        Author domain = new Author();

        domain.setAuthorNumber(entity.getAuthorNumber());
        domain.setVersion(entity.getVersion());

        if (entity.getName() != null) {
            domain.setName(entity.getName().toString());
        }

        if (entity.getBio() != null) {
            domain.setBio(entity.getBio().toString());
        }

        if (entity.getPhoto() != null) {
            domain.setPhoto(entity.getPhoto().getPhotoFile());
        } else {
            domain.setPhoto(null);
        }

        return domain;
    }

    public AuthorJpaEntity toEntity(Author domain) {
        if (domain == null) {
            return null;
        }

        AuthorJpaEntity entity = new AuthorJpaEntity();

        entity.setAuthorNumber(domain.getAuthorNumber());
        entity.setVersion(domain.getVersion());

        entity.setPhoto(domain.getPhoto());

        if (domain.getName() != null) {
            entity.setName(new Name(domain.getName()));
        }

        if (domain.getBio() != null) {
            entity.setBio(new Bio(domain.getBio()));
        }

        return entity;
    }
}