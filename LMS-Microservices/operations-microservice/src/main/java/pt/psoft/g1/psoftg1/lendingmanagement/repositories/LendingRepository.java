package pt.psoft.g1.psoftg1.lendingmanagement.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pt.psoft.g1.psoftg1.lendingmanagement.model.Lending;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface LendingRepository extends JpaRepository<Lending, Long> {
    Optional<Lending> findByLendingNumber(String lendingNumber);
    List<Lending> findByReaderIdAndIsbn(String readerId, String isbn);

    @Query("SELECT l FROM Lending l WHERE l.limitDate < :currentDate AND l.returnedDate IS NULL")
    Page<Lending> getOverdue(@Param("currentDate") LocalDate currentDate, Pageable pageable);

    @Query("SELECT l FROM Lending l WHERE (:isbn IS NULL OR l.isbn = :isbn) AND (:readerId IS NULL OR l.readerId = :readerId)")
    Page<Lending> searchLendings(@Param("isbn") String isbn, @Param("readerId") String readerId, Pageable pageable);
}
