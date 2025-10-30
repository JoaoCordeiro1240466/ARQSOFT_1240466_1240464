package pt.psoft.g1.psoftg1.infrastructure.persistence.sql.repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pt.psoft.g1.psoftg1.infrastructure.persistence.sql.model.LendingJpaEntity;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface SpringDataJpaLendingRepository extends JpaRepository<LendingJpaEntity, Long>,
        JpaSpecificationExecutor<LendingJpaEntity> {

    Optional<LendingJpaEntity> findByLendingNumber_LendingNumber(String lendingNumber);

    @Query("SELECT l FROM LendingJpaEntity l " +
            "WHERE l.readerDetails.readerNumber.readerNumber = :readerNumber " +
            "AND l.book.isbn.isbn = :isbn")
    List<LendingJpaEntity> listByReaderNumberAndIsbn(@Param("readerNumber") String readerNumber,
                                                     @Param("isbn") String isbn);

    @Query("SELECT COUNT(l) FROM LendingJpaEntity l " +
            "WHERE l.lendingNumber.lendingNumber LIKE CONCAT(:year, '/%')")
    int getCountInYear(@Param("year") int year);

    @Query("SELECT l FROM LendingJpaEntity l " +
            "WHERE l.readerDetails.readerNumber.readerNumber = :readerNumber " +
            "AND l.returnedDate IS NULL")
    List<LendingJpaEntity> listOutstandingByReaderNumber(@Param("readerNumber") String readerNumber);


    /**
     * CORRIGIDO:
     * 1. Usa DATEDIFF(fim, inicio) (sintaxe MySQL)
     * 2. Adiciona nativeQuery = true
     * 3. Usa nomes de colunas da BD (ex: T_LENDING)
     */
    @Query(value = "SELECT AVG(DATEDIFF('DAY', l.start_date, l.returned_date)) " +
            "FROM t_lending l WHERE l.returned_date IS NOT NULL",
            nativeQuery = true) // <-- Adiciona nativeQuery
    Double getAverageDuration();

    @Query(value = "SELECT AVG(DATEDIFF('DAY', l.start_date, l.returned_date)) " +
            "FROM t_lending l " +
            "JOIN t_book b ON l.book_pk = b.pk " +
            "WHERE b.isbn = :isbn AND l.returned_date IS NOT NULL",
            nativeQuery = true) // <-- Adiciona nativeQuery
    Double getAvgLendingDurationByIsbn(@Param("isbn") String isbn);

    /**
     * CORRIGIDO: Usa CURRENT_DATE (SQL standard)
     * (Esta query não precisa de ser nativa, mas mantemos consistência)
     */
    @Query("SELECT l FROM LendingJpaEntity l " +
            "WHERE l.returnedDate IS NULL AND l.limitDate < CURRENT_DATE")
    Page<LendingJpaEntity> getOverdue(Pageable pageable);
}
