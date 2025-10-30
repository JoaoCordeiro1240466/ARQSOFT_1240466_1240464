package pt.psoft.g1.psoftg1.infrastructure.persistence.sql.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import pt.psoft.g1.psoftg1.infrastructure.persistence.sql.model.UserJpaEntity;

import java.util.List;
import java.util.Optional;

/**
 * Interface de Repositório INTERNA do Spring Data.
 * Esta interface é um detalhe de implementação da infraestrutura SQL.
 * Ela trabalha APENAS com a UserJpaEntity.
 *
 * O Spring Boot vai criar uma implementação disto em tempo de execução.
 *
 * Estendemos JpaRepository<UserJpaEntity, String> para os métodos CRUD.
 * - UserJpaEntity: A entidade que este repo gere.
 * - String: O tipo do @Id (que é 'private String id' na UserJpaEntity).
 *
 * Estendemos JpaSpecificationExecutor<UserJpaEntity> para permitir
 * as pesquisas dinâmicas (necessário para o teu método searchUsers).
 */
@Repository // Diz ao Spring para detetar esta interface
public interface SpringDataJpaUserRepository extends JpaRepository<UserJpaEntity, String>,
        JpaSpecificationExecutor<UserJpaEntity> {

    /**
     * O Spring Data implementa isto automaticamente com base no nome do método.
     */
    Optional<UserJpaEntity> findByUsername(String username);

    /**
     * O Spring Data implementa isto.
     * O "_" (underscore) é a convenção para aceder a uma propriedade
     * (Name) dentro de um objeto @Embedded (name).
     */
    List<UserJpaEntity> findByName_Name(String name);

    /**
     * O Spring Data também implementa o 'Contains'.
     */
    List<UserJpaEntity> findByName_NameContains(String name);

}