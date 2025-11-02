package pt.psoft.g1.psoftg1.bookmanagement.infrastructure.repositories.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import pt.psoft.g1.psoftg1.bookmanagement.model.Book;
import pt.psoft.g1.psoftg1.bookmanagement.services.BookCountDTO;
import pt.psoft.g1.psoftg1.bookmanagement.services.SearchBooksQuery;
import pt.psoft.g1.psoftg1.infrastructure.persistence.sql.mapper.BookSqlMapper;
import pt.psoft.g1.psoftg1.infrastructure.persistence.sql.model.BookJpaEntity;
import pt.psoft.g1.psoftg1.infrastructure.persistence.sql.repo.SpringDataJpaBookRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class BookRepositorySqlImplTest {

    @Mock
    private SpringDataJpaBookRepository jpaRepo;

    @Mock
    private BookSqlMapper mapper;

    @InjectMocks
    private BookRepositorySqlImpl bookRepository;


    @Test
    void shouldReturnBookWhenFoundByIsbn() {
        // Arrange
        String isbn = "9783161484100";
        BookJpaEntity foundEntity = new BookJpaEntity();
        Book expectedBook = new Book();
        expectedBook.setIsbn(isbn);

        when(jpaRepo.findByIsbn_Isbn(isbn)).thenReturn(Optional.of(foundEntity));
        when(mapper.toDomain(foundEntity)).thenReturn(expectedBook);

        // Act
        Optional<Book> result = bookRepository.findByIsbn(isbn);

        // Assert
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(expectedBook);
    }

    @Test
    void shouldReturnEmptyWhenNotFoundByIsbn() {
        // Arrange
        String isbn = "000-0-00-000000-0";
        when(jpaRepo.findByIsbn_Isbn(isbn)).thenReturn(Optional.empty());

        // Act
        Optional<Book> result = bookRepository.findByIsbn(isbn);

        // Assert
        assertThat(result).isEmpty();
        verify(jpaRepo).findByIsbn_Isbn(isbn);
        // Garante que o mapper nunca foi chamado se nada foi encontrado
        verify(mapper, never()).toDomain(any());
    }

    @Test
    void shouldSaveAndReturnMappedBook() {
        // Arrange
        Book bookToSave = new Book(); // Domínio (entrada)
        bookToSave.setTitle("O Guia do Mochileiro das Galáxias");

        BookJpaEntity entityToSave = new BookJpaEntity(); // Entidade (intermédio)
        BookJpaEntity savedEntity = new BookJpaEntity(); // Entidade (retorno do JPA)
        Book expectedSavedBook = new Book(); // Domínio (saída)
        expectedSavedBook.setTitle("O Guia do Mochileiro das Galáxias");

        // Configurar Mocks (sequência de mapeamento)
        when(mapper.toEntity(bookToSave)).thenReturn(entityToSave);
        when(jpaRepo.save(entityToSave)).thenReturn(savedEntity);
        when(mapper.toDomain(savedEntity)).thenReturn(expectedSavedBook);

        // Act
        Book result = bookRepository.save(bookToSave);

        // Assert
        assertThat(result).isEqualTo(expectedSavedBook);
        verify(mapper).toEntity(bookToSave);
        verify(jpaRepo).save(entityToSave);
        verify(mapper).toDomain(savedEntity);
    }

    @Test
    void shouldSearchBooksWithCorrectPageNumber() {
        // Arrange
        // 1. Input: Pedimos página 3, com 5 itens
        pt.psoft.g1.psoftg1.shared.services.Page page = new pt.psoft.g1.psoftg1.shared.services.Page(3, 5);
        SearchBooksQuery query = new SearchBooksQuery();

        // 2. Expectativa: Spring Data usa paginação 0-indexada, logo (3 - 1 = 2)
        Pageable expectedPageable = PageRequest.of(2, 5);

        // 3. Mocks: Simular o retorno do JPA
        BookJpaEntity entity = new BookJpaEntity();
        Book domainBook = new Book();
        Page<BookJpaEntity> mockPage = new PageImpl<>(List.of(entity), expectedPageable, 1);

        // O 'any(Specification.class)' funciona porque a especificação é simples
        // O 'eq(expectedPageable)' garante que o cálculo da página está correto
        when(jpaRepo.findAll(any(Specification.class), eq(expectedPageable))).thenReturn(mockPage);
        when(mapper.toDomain(entity)).thenReturn(domainBook);

        // Act
        List<Book> results = bookRepository.searchBooks(page, query);

        // Assert
        assertThat(results).hasSize(1);
        assertThat(results).contains(domainBook);

        // Verifica se o JpaRepo foi chamado com os argumentos corretos
        verify(jpaRepo).findAll(any(Specification.class), eq(expectedPageable));
    }

    @Test
    void shouldCorrectlyMapTopBooksLentFromRawPage() {
        // Arrange
        LocalDate date = LocalDate.now().minusYears(1);
        Pageable pageable = PageRequest.of(0, 5);

        // 1. Simular os dados "crus" (raw) vindos do JPA
        BookJpaEntity entity1 = new BookJpaEntity();
        Object[] rawRow1 = new Object[]{entity1, 50L}; // Livro 1 com 50 empréstimos
        BookJpaEntity entity2 = new BookJpaEntity();
        Object[] rawRow2 = new Object[]{entity2, 45L}; // Livro 2 com 45 empréstimos

        List<Object[]> rawList = List.of(rawRow1, rawRow2);
        Page<Object[]> mockPage = new PageImpl<>(rawList, pageable, 2);

        // 2. Simular os objetos de domínio que o mapper vai retornar
        Book domainBook1 = new Book();
        domainBook1.setTitle("Livro 1");
        Book domainBook2 = new Book();
        domainBook2.setTitle("Livro 2");

        // 3. Configurar Mocks
        when(jpaRepo.findTop5BooksLentRaw(date, pageable)).thenReturn(mockPage);
        when(mapper.toDomain(entity1)).thenReturn(domainBook1);
        when(mapper.toDomain(entity2)).thenReturn(domainBook2);

        // Act
        Page<BookCountDTO> resultPage = bookRepository.findTop5BooksLent(date, pageable);

        // Assert
        assertThat(resultPage).isNotNull();
        assertThat(resultPage.getTotalElements()).isEqualTo(2);
        assertThat(resultPage.getContent()).hasSize(2);

        // Verifica se o mapeamento para o DTO está correto
        BookCountDTO dto1 = resultPage.getContent().get(0);
        assertThat(dto1.getBook()).isEqualTo(domainBook1); // Verifica o objeto Book
        assertThat(dto1.getLendingCount()).isEqualTo(50L);  // Verifica a contagem

        BookCountDTO dto2 = resultPage.getContent().get(1);
        assertThat(dto2.getBook()).isEqualTo(domainBook2);
        assertThat(dto2.getLendingCount()).isEqualTo(45L);

        // Verifica se os mocks foram chamados
        verify(jpaRepo).findTop5BooksLentRaw(date, pageable);
        verify(mapper).toDomain(entity1);
        verify(mapper).toDomain(entity2);
    }
}