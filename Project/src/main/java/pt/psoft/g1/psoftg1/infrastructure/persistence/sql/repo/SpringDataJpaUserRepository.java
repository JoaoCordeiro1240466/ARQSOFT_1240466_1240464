package pt.psoft.g1.psoftg1.infrastructure.persistence.sql.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import pt.psoft.g1.psoftg1.infrastructure.persistence.sql.model.UserJpaEntity;

import java.util.List;
import java.util.Optional;

@Repository
public interface SpringDataJpaUserRepository extends JpaRepository<UserJpaEntity, String>,
        JpaSpecificationExecutor<UserJpaEntity> {

    Optional<UserJpaEntity> findByUsername(String username);

    List<UserJpaEntity> findByName_Name(String name);

    List<UserJpaEntity> findByName_NameContains(String name);

}