package pt.psoft.g1.psoftg1.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pt.psoft.g1.psoftg1.model.Reader;
import pt.psoft.g1.psoftg1.model.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    @Query("SELECT r FROM Reader r WHERE r.username = :username")
    Optional<Reader> findReaderByUsername(String username);

    List<User> findByFullNameContainingIgnoreCase(String fullName);
}
