package pt.psoft.g1.psoftg1.infrastructure.persistence.sql.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pt.psoft.g1.psoftg1.infrastructure.persistence.sql.model.FineJpaEntity;

import java.util.Optional;

@Repository
public interface SpringDataJpaFineRepository extends JpaRepository<FineJpaEntity, Long> {

    /**
     * O Spring Data implementa isto.
     * Procura Fine -> pelo seu 'lending' -> pelo 'lendingNumber' (objeto) -> pelo 'lendingNumber' (String)
     */
    Optional<FineJpaEntity> findByLending_LendingNumber_LendingNumber(String lendingNumber);

}