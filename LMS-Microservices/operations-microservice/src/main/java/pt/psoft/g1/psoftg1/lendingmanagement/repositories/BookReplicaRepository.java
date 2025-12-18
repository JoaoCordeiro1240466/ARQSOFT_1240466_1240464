package pt.psoft.g1.psoftg1.lendingmanagement.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pt.psoft.g1.psoftg1.lendingmanagement.model.BookReplica;

public interface BookReplicaRepository extends JpaRepository<BookReplica, String> {
}
