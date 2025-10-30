package pt.psoft.g1.psoftg1.infrastructure.persistence.sql.repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pt.psoft.g1.psoftg1.bookmanagement.services.BookCountDTO; // Import necessário para o DTO (embora não usado no retorno)
import pt.psoft.g1.psoftg1.infrastructure.persistence.sql.model.BookJpaEntity;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface SpringDataJpaBookRepository extends JpaRepository<BookJpaEntity, Long>,
        JpaSpecificationExecutor<BookJpaEntity> {

    // Métodos find-by (corrigidos de acordo com os nomes dos campos)
    List<BookJpaEntity> findByGenre_Genre(String genre);
    List<BookJpaEntity> findByTitle_Title(String title);
    Optional<BookJpaEntity> findByIsbn_Isbn(String isbn);

    // Query para encontrar livros pelo nome do autor (complexo)
    @Query("SELECT b FROM BookJpaEntity b JOIN b.authors a WHERE a.name.name = :authorName")
    List<BookJpaEntity> findByAuthorName(@Param("authorName") String authorName);

    // Query para encontrar livros pelo número do autor
    @Query("SELECT b FROM BookJpaEntity b JOIN b.authors a WHERE a.authorNumber = :authorNumber")
    List<BookJpaEntity> findBooksByAuthorNumber(@Param("authorNumber") Long authorNumber);

    // --- QUERY CORRIGIDA ---
    // 1. Alterado o tipo de retorno para Page<Object[]> (dados brutos)
    // 2. Corrigido 'l.lendingDate' para 'l.startDate'
    // 3. Alterado o nome do método para 'findTop5BooksLentRaw'
    @Query("SELECT l.book, COUNT(l) " + // Retorna a Entidade e a Contagem
            "FROM LendingJpaEntity l " +
            "WHERE l.startDate >= :oneYearAgo " + // Corrigido
            "GROUP BY l.book " +
            "ORDER BY COUNT(l) DESC")
    Page<Object[]> findTop5BooksLentRaw(@Param("oneYearAgo") LocalDate oneYearAgo, Pageable pageable);
}