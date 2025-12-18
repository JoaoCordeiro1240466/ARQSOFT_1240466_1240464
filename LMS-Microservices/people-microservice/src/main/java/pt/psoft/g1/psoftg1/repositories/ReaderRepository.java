package pt.psoft.g1.psoftg1.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pt.psoft.g1.psoftg1.model.Reader;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReaderRepository extends JpaRepository<Reader, Long> {

    Optional<Reader> findByReaderNumber(String readerNumber);

    List<Reader> findByPhoneNumber(String phoneNumber);

    List<Reader> findByFullNameContainingIgnoreCase(String name);

    @Query("SELECT COUNT(r) FROM Reader r WHERE r.readerNumber LIKE :yearPrefix%")
    long countByReaderNumberStartingWith(String yearPrefix);
}
