package pt.psoft.g1.psoftg1.infrastructure.persistence.sql.repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pt.psoft.g1.psoftg1.bookmanagement.services.BookCountDTO;
import pt.psoft.g1.psoftg1.infrastructure.persistence.sql.model.BookJpaEntity;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface SpringDataJpaBookRepository extends JpaRepository<BookJpaEntity, Long>,
        JpaSpecificationExecutor<BookJpaEntity> {

    List<BookJpaEntity> findByGenre_Genre(String genre);
    List<BookJpaEntity> findByTitle_Title(String title);
    Optional<BookJpaEntity> findByIsbn_Isbn(String isbn);

    @Query("SELECT b FROM BookJpaEntity b JOIN b.authors a WHERE a.name.name = :authorName")
    List<BookJpaEntity> findByAuthorName(@Param("authorName") String authorName);

    @Query("SELECT b FROM BookJpaEntity b JOIN b.authors a WHERE a.authorNumber = :authorNumber")
    List<BookJpaEntity> findBooksByAuthorNumber(@Param("authorNumber") Long authorNumber);

    @Query("SELECT l.book, COUNT(l) " +
            "FROM LendingJpaEntity l " +
            "WHERE l.startDate >= :oneYearAgo " +
            "GROUP BY l.book " +
            "ORDER BY COUNT(l) DESC")
    Page<Object[]> findTop5BooksLentRaw(@Param("oneYearAgo") LocalDate oneYearAgo, Pageable pageable);
}