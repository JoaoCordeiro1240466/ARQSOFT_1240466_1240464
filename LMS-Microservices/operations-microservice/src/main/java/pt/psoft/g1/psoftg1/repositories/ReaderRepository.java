package pt.psoft.g1.psoftg1.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pt.psoft.g1.psoftg1.model.Reader;

import java.util.Optional;

@Repository
public interface ReaderRepository extends JpaRepository<Reader, Long> {
    Optional<Reader> findByUsername(String username);
    Optional<Reader> findByDatabaseId(Long databaseId);
}
