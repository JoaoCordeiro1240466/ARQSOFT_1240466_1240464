package pt.psoft.g1.psoftg1.infrastructure.persistence.sql.repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pt.psoft.g1.psoftg1.readermanagement.model.ReaderDetails;
import pt.psoft.g1.psoftg1.readermanagement.services.ReaderBookCountDTO;
import pt.psoft.g1.psoftg1.infrastructure.persistence.sql.model.ReaderDetailsJpaEntity;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface SpringDataJpaReaderRepository extends JpaRepository<ReaderDetailsJpaEntity, Long>,
        JpaSpecificationExecutor<ReaderDetailsJpaEntity> {

    Optional<ReaderDetailsJpaEntity> findByReaderNumber_ReaderNumber(String readerNumber);
    List<ReaderDetailsJpaEntity> findByPhoneNumber_PhoneNumber(String phoneNumber);
    Optional<ReaderDetailsJpaEntity> findByReader_Username(String username);

    Optional<ReaderDetailsJpaEntity> findByReader_Id(String userId);

    @Query("SELECT COUNT(r) FROM ReaderDetailsJpaEntity r " +
            "WHERE r.readerNumber.readerNumber LIKE CONCAT(:currentYear, '/%')")
    int getCountInYear(@Param("currentYear") int currentYear);
}