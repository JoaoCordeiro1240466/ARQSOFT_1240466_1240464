package pt.psoft.g1.psoftg1.infrastructure.persistence.sql.mapper;

import org.springframework.stereotype.Component;
import pt.psoft.g1.psoftg1.genremanagement.model.Genre;
import pt.psoft.g1.psoftg1.infrastructure.persistence.sql.model.GenreJpaEntity;

/**
 * Mapper (Tradutor) entre o modelo de domínio Genre e a entidade JPA GenreJpaEntity.
 */
@Component
public class GenreSqlMapper {

    /**
     * Converte a entidade da Base de Dados (JPA) para o Modelo de Domínio.
     */
    public Genre toDomain(GenreJpaEntity entity) {
        if (entity == null) {
            return null;
        }
        // Usamos o construtor do domínio que já tem a validação
        Genre domain = new Genre(entity.getGenre());
        // Definimos o PK que foi gerado pela BD
        domain.setPk(entity.getPk());
        return domain;
    }

    /**
     * Converte o Modelo de Domínio para a Entidade da Base de Dados (JPA).
     */
    public GenreJpaEntity toEntity(Genre domain) {
        if (domain == null) {
            return null;
        }
        GenreJpaEntity entity = new GenreJpaEntity();
        entity.setPk(domain.getPk()); // Passa o PK (se existir)
        entity.setGenre(domain.getGenre()); // Passa o nome
        return entity;
    }
}