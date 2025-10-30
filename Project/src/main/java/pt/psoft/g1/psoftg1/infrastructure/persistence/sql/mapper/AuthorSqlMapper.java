package pt.psoft.g1.psoftg1.infrastructure.persistence.sql.mapper;

import org.springframework.stereotype.Component;
import pt.psoft.g1.psoftg1.authormanagement.model.Author;
import pt.psoft.g1.psoftg1.authormanagement.model.Bio;
import pt.psoft.g1.psoftg1.infrastructure.persistence.sql.model.AuthorJpaEntity;
import pt.psoft.g1.psoftg1.shared.model.Name;

/**
 * Mapper (Tradutor) entre o modelo de domínio Author e a entidade JPA AuthorJpaEntity.
 * A anotação @Component permite que o Spring o detete e o injete
 * no nosso futuro AuthorRepositorySqlImpl.
 */
@Component
public class AuthorSqlMapper {

    /**
     * Converte a entidade da Base de Dados (JPA) para o Modelo de Domínio.
     * (Usado quando LÊS da base de dados).
     */
    public Author toDomain(AuthorJpaEntity entity) {
        if (entity == null) {
            return null;
        }

        // Usamos o construtor vazio (protected) do domínio
        Author domain = new Author();

        // Preenchemos os campos usando os setters que criámos
        domain.setAuthorNumber(entity.getAuthorNumber());
        domain.setVersion(entity.getVersion());

        // O setName(String) do domínio vai recriar o objeto Name
        if (entity.getName() != null) {
            domain.setName(entity.getName().toString());
        }

        // O setBio(String) do domínio vai recriar o objeto Bio
        if (entity.getBio() != null) {
            domain.setBio(entity.getBio().toString());
        }

        // O setPhoto(String) do domínio vai recriar o objeto Photo
        if (entity.getPhoto() != null) {
            domain.setPhoto(entity.getPhoto().getPhotoFile());
        } else {
            domain.setPhoto(null);
        }

        return domain;
    }

    /**
     * Converte o Modelo de Domínio para a Entidade da Base de Dados (JPA).
     * (Usado quando QUERES GUARDAR na base de dados).
     */
    public AuthorJpaEntity toEntity(Author domain) {
        if (domain == null) {
            return null;
        }

        // Usamos o construtor vazio (public) da entidade
        AuthorJpaEntity entity = new AuthorJpaEntity();

        // Mapeamos os campos do domínio para a entidade
        entity.setAuthorNumber(domain.getAuthorNumber());
        entity.setVersion(domain.getVersion());

        // O getPhoto() do domínio (herdado) retorna o objeto Photo
        entity.setPhoto(domain.getPhoto());

        // Recriamos os Value Objects (Name e Bio) a partir do seu
        // valor de string, que é a única coisa que o domínio expõe.
        if (domain.getName() != null) {
            entity.setName(new Name(domain.getName()));
        }

        if (domain.getBio() != null) {
            entity.setBio(new Bio(domain.getBio()));
        }

        return entity;
    }
}