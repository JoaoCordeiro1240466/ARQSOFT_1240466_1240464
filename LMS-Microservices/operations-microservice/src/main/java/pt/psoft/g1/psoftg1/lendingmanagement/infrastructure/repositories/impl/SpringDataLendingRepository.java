package pt.psoft.g1.psoftg1.lendingmanagement.infrastructure.repositories.impl;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pt.psoft.g1.psoftg1.lendingmanagement.model.Lending;

import java.util.Optional;

public interface SpringDataLendingRepository extends JpaRepository<Lending, Long> {

    @Query("SELECT l FROM Lending l WHERE l.lendingNumber = :lendingNumber")
    Optional<Lending> findByLendingNumber(@Param("lendingNumber") String lendingNumber);

}