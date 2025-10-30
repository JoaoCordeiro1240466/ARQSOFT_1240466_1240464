package pt.psoft.g1.psoftg1.infrastructure.persistence.sql.repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pt.psoft.g1.psoftg1.authormanagement.api.AuthorLendingView;
import pt.psoft.g1.psoftg1.infrastructure.persistence.sql.model.AuthorJpaEntity;

import java.util.List;

@Repository
public interface SpringDataJpaAuthorRepository extends JpaRepository<AuthorJpaEntity, Long> {

    /**
     * O Spring Data implementa isto automaticamente.
     */
    List<AuthorJpaEntity> findByName_NameStartsWith(String name);

    /**
     * O mesmo que acima, mas para uma correspondência exata.
     */
    List<AuthorJpaEntity> findByName_Name(String name);


    // --- QUERY CORRIGIDA ---
    // 1. Alterado o tipo de retorno para Page<Object[]> (dados brutos)
    // 2. Corrigida a lógica do JOIN (Author -> Book -> Lending)
    // 3. Alterado o nome do método para 'findTopAuthorByLendingsRaw'
    @Query("SELECT a, COUNT(l) " +
            "FROM AuthorJpaEntity a " +
            "LEFT JOIN a.books b " + // Junta Author com os seus Livros
            "LEFT JOIN LendingJpaEntity l ON l.book = b " + // Junta Livros com Lendings
            "GROUP BY a " +
            "ORDER BY COUNT(l) DESC")
    Page<Object[]> findTopAuthorByLendingsRaw(Pageable pageableRules);

    /**
     * Implementação da query complexa para 'findCoAuthorsByAuthorNumber'.
     * Esta query agora funciona porque AuthorJpaEntity tem a lista 'books'
     * e BookJpaEntity tem a lista 'authors'.
     */
    @Query("SELECT DISTINCT a2 FROM AuthorJpaEntity a1 " +
            "JOIN a1.books b " +
            "JOIN b.authors a2 " +
            "WHERE a1.authorNumber = :authorNumber AND a2.authorNumber != :authorNumber")
    List<AuthorJpaEntity> findCoAuthorsByAuthorNumber(@Param("authorNumber") Long authorNumber);
}