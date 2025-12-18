package pt.psoft.g1.psoftg1.lendingmanagement.infrastructure.repositories.impl;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pt.psoft.g1.psoftg1.lendingmanagement.model.Fine;

import java.util.Optional;

public interface SpringDataFineRepository extends JpaRepository<Fine, Long> {

    // 👇 A SOLUÇÃO: Escrevemos a query explicitamente para o Hibernate não se confundir
    @Query("SELECT f FROM Fine f WHERE f.lending.lendingNumber = :lendingNumber")
    Optional<Fine> findByLendingLendingNumber(@Param("lendingNumber") String lendingNumber);
}
