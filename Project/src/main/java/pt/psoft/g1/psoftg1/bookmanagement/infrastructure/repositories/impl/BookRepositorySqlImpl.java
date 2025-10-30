package pt.psoft.g1.psoftg1.bookmanagement.infrastructure.repositories.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.repository.query.Param;

import pt.psoft.g1.psoftg1.bookmanagement.model.Book;
import pt.psoft.g1.psoftg1.bookmanagement.repositories.BookRepository;
import pt.psoft.g1.psoftg1.bookmanagement.services.BookCountDTO;
import pt.psoft.g1.psoftg1.bookmanagement.services.SearchBooksQuery;
import pt.psoft.g1.psoftg1.infrastructure.persistence.sql.mapper.BookSqlMapper;
import pt.psoft.g1.psoftg1.infrastructure.persistence.sql.model.BookJpaEntity;
import pt.psoft.g1.psoftg1.infrastructure.persistence.sql.repo.SpringDataJpaBookRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@Profile("sql")
@RequiredArgsConstructor
public class BookRepositorySqlImpl implements BookRepository {

    private final SpringDataJpaBookRepository jpaRepo;
    private final BookSqlMapper mapper;

    private static final String CACHE_NAME = "bookCache";

    @Override
    public List<Book> findByGenre(String genre) {
        return jpaRepo.findByGenre_Genre(genre).stream()
                .map(mapper::toDomain).collect(Collectors.toList());
    }

    @Override
    public List<Book> findByTitle(String title) {
        return jpaRepo.findByTitle_Title(title).stream()
                .map(mapper::toDomain).collect(Collectors.toList());
    }

    @Override
    public List<Book> findByAuthorName(String authorName) {
        return jpaRepo.findByAuthorName(authorName).stream()
                .map(mapper::toDomain).collect(Collectors.toList());
    }

    @Override
    @Cacheable(value = CACHE_NAME, key = "#isbn")
    public Optional<Book> findByIsbn(String isbn) {
        return jpaRepo.findByIsbn_Isbn(isbn).map(mapper::toDomain);
    }

    @Override
    public List<Book> findBooksByAuthorNumber(Long authorNumber) {
        return jpaRepo.findBooksByAuthorNumber(authorNumber).stream()
                .map(mapper::toDomain).collect(Collectors.toList());
    }

    @Override
    @CacheEvict(value = CACHE_NAME, key = "#book.isbn")
    public Book save(Book book) {
        BookJpaEntity entity = mapper.toEntity(book);
        BookJpaEntity savedEntity = jpaRepo.save(entity);
        return mapper.toDomain(savedEntity);
    }

    @Override
    @CacheEvict(value = CACHE_NAME, key = "#book.isbn")
    public void delete(Book book) {
        jpaRepo.delete(mapper.toEntity(book));
    }

    @Override
    public List<Book> searchBooks(pt.psoft.g1.psoftg1.shared.services.Page page, SearchBooksQuery query) {
        Specification<BookJpaEntity> spec = (root, q, cb) -> cb.conjunction();
        Pageable pageable = org.springframework.data.domain.PageRequest.of(page.getNumber() - 1, page.getLimit());

        return jpaRepo.findAll(spec, pageable).getContent().stream()
                .map(mapper::toDomain).collect(Collectors.toList());
    }


    @Override
    public Page<BookCountDTO> findTop5BooksLent(LocalDate oneYearAgo, Pageable pageable) {

        // 1. Chama a nova query "Raw"
        Page<Object[]> rawPage = jpaRepo.findTop5BooksLentRaw(oneYearAgo, pageable);

        // 2. Mapeia manualmente os resultados (Object[]) para o DTO (BookCountDTO)
        return rawPage.map(row -> {
            // row[0] é a BookJpaEntity
            // row[1] é o COUNT (Long)
            BookJpaEntity bookEntity = (BookJpaEntity) row[0];
            Long count = (Long) row[1];

            // Converte a Entidade da BD para o Modelo de Domínio
            Book bookDomain = mapper.toDomain(bookEntity);

            // Cria o DTO com os objetos de Domínio
            return new BookCountDTO(bookDomain, count);
        });
    }
}