package pt.psoft.g1.psoftg1.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import pt.psoft.g1.psoftg1.model.Book;

import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long> {

    @Query("SELECT b FROM Book b WHERE b.isbn.isbn = :isbnStr")
    Optional<Book> findByIsbn(@Param("isbnStr") String isbnStr);

    @Transactional
    @Modifying
    @Query("DELETE FROM Book b WHERE b.isbn.isbn = :isbn")
    void deleteByIsbn(@Param("isbn") String isbn);

}
