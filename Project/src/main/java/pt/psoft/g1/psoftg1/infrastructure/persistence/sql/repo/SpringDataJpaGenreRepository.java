package pt.psoft.g1.psoftg1.infrastructure.persistence.sql.repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pt.psoft.g1.psoftg1.infrastructure.persistence.sql.model.GenreJpaEntity;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface SpringDataJpaGenreRepository extends JpaRepository<GenreJpaEntity, Long> {

    Optional<GenreJpaEntity> findByGenre(String genreName);

    @Query("SELECT g, COUNT(b) " +
            "FROM GenreJpaEntity g LEFT JOIN BookJpaEntity b ON b.genre = g " +
            "GROUP BY g " +
            "ORDER BY COUNT(b) DESC")
    Page<Object[]> findTop5GenreByBookCountRaw(Pageable pageable);

    /**
     * CORRIGIDO:
     * 1. Usa DATEDIFF(fim, inicio) (sintaxe MySQL)
     * 2. Adiciona nativeQuery = true
     * 3. Usa nomes de colunas e tabelas da BD
     * 4. Usa YEAR() e MONTH() (funções MySQL)
     */
    @Query(value = "SELECT g.*, AVG(DATEDIFF(l.returned_date, l.start_date)) " +
            "FROM t_lending l " +
            "JOIN t_book b ON l.book_pk = b.pk " +
            "JOIN t_genre g ON b.genre_pk = g.pk " +
            "WHERE l.returned_date IS NOT NULL AND YEAR(l.start_date) = :year AND MONTH(l.start_date) = :month " +
            "GROUP BY g.pk, g.genre", // Agrupa por todos os campos não agregados
            nativeQuery = true)
    List<Object[]> getAverageLendingsInMonthRaw(@Param("year") int year, @Param("month") int month, Pageable pageable);

    /**
     * CORRIGIDO:
     * 1. Usa nativeQuery = true
     * 2. Usa nomes de colunas e tabelas da BD
     */
    @Query(value = "SELECT YEAR(l.start_date), MONTH(l.start_date), g.genre, COUNT(l) " +
            "FROM t_lending l " +
            "JOIN t_book b ON l.book_pk = b.pk " +
            "JOIN t_genre g ON b.genre_pk = g.pk " +
            "WHERE l.start_date >= :oneYearAgo " +
            "GROUP BY g.genre, YEAR(l.start_date), MONTH(l.start_date)",
            nativeQuery = true)
    List<Object[]> getLendingsPerMonthLastYearByGenreRaw(@Param("oneYearAgo") LocalDate oneYearAgo);

    /**
     * CORRIGIDO:
     * 1. Usa DATEDIFF(fim, inicio) (sintaxe MySQL)
     * 2. Adiciona nativeQuery = true
     * 3. Usa nomes de colunas e tabelas da BD
     */
    @Query(value = "SELECT YEAR(l.start_date), MONTH(l.start_date), g.genre, AVG(DATEDIFF(l.returned_date, l.start_date)) " +
            "FROM t_lending l " +
            "JOIN t_book b ON l.book_pk = b.pk " +
            "JOIN t_genre g ON b.genre_pk = g.pk " +
            "WHERE l.returned_date IS NOT NULL AND l.start_date BETWEEN :startDate AND :endDate " +
            "GROUP BY g.genre, YEAR(l.start_date), MONTH(l.start_date)",
            nativeQuery = true)
    List<Object[]> getLendingsAverageDurationPerMonthRaw(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
}
