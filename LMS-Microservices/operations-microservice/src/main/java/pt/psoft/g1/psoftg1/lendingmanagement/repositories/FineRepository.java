package pt.psoft.g1.psoftg1.lendingmanagement.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pt.psoft.g1.psoftg1.lendingmanagement.model.Fine;

import java.util.Optional;

public interface FineRepository extends JpaRepository<Fine, Long> {


    Optional<Fine> findByLendingLendingNumber(String lendingNumber);
}