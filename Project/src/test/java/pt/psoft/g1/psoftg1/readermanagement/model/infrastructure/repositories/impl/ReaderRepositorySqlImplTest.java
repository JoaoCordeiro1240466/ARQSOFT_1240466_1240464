package pt.psoft.g1.psoftg1.readermanagement.infraestructure.repositories.impl;

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

// Imports do seu projeto
import pt.psoft.g1.psoftg1.infrastructure.persistence.sql.mapper.ReaderDetailsSqlMapper;
import pt.psoft.g1.psoftg1.infrastructure.persistence.sql.model.ReaderDetailsJpaEntity;
import pt.psoft.g1.psoftg1.infrastructure.persistence.sql.repo.SpringDataJpaReaderRepository;
import pt.psoft.g1.psoftg1.readermanagement.model.ReaderDetails;
import pt.psoft.g1.psoftg1.readermanagement.services.ReaderBookCountDTO;
import pt.psoft.g1.psoftg1.readermanagement.services.SearchReadersQuery;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

// Imports estáticos
import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Testes de Unidade para ReaderRepositorySqlImpl.
 * Foca-se em verificar a lógica de conversão, paginação e
 * chamadas aos colaboradores (jpaRepo, mapper).
 */
@ExtendWith(MockitoExtension.class)
class ReaderRepositorySqlImplTest {

    // Mocks
    @Mock
    private SpringDataJpaReaderRepository jpaRepo;

    @Mock
    private ReaderDetailsSqlMapper mapper;

    // Classe em teste
    @InjectMocks
    private ReaderRepositorySqlImpl readerRepository;

    // --- Teste 1: Leitura Simples (findByReaderNumber) ---

    @Test
    void shouldReturnReaderWhenFoundByReaderNumber() {
        // Arrange
        String readerNumber = "R-123";
        ReaderDetailsJpaEntity entity = new ReaderDetailsJpaEntity();
        ReaderDetails expectedReader = new ReaderDetails();

        when(jpaRepo.findByReaderNumber_ReaderNumber(readerNumber)).thenReturn(Optional.of(entity));
        when(mapper.toDomain(entity)).thenReturn(expectedReader);

        // Act
        Optional<ReaderDetails> result = readerRepository.findByReaderNumber(readerNumber);

        // Assert
        assertThat(result).isPresent();
        assertThat(result.get()).isSameAs(expectedReader);
        verify(jpaRepo).findByReaderNumber_ReaderNumber(readerNumber);
        verify(mapper).toDomain(entity);
    }

    // --- Teste 2: Lógica de Conversão (findByUserId) ---
    // Este teste é importante para verificar o Long -> String

    @Test
    void shouldReturnReaderWhenFoundByUserId() {
        // Arrange
        Long userId = 12345L;
        String expectedStringId = "12345"; // O que esperamos que seja passado ao JpaRepo
        ReaderDetailsJpaEntity entity = new ReaderDetailsJpaEntity();
        ReaderDetails expectedReader = new ReaderDetails();

        // Mock: Esperamos uma chamada com a String "12345"
        when(jpaRepo.findByReader_Id(expectedStringId)).thenReturn(Optional.of(entity));
        when(mapper.toDomain(entity)).thenReturn(expectedReader);

        // Act
        Optional<ReaderDetails> result = readerRepository.findByUserId(userId);

        // Assert
        assertThat(result).isPresent();
        assertThat(result.get()).isSameAs(expectedReader);

        // Verifica se a conversão Long -> String foi feita corretamente
        verify(jpaRepo).findByReader_Id(expectedStringId);
    }

    // --- Teste 3: Lógica de Data (getCountFromCurrentYear) ---

    @Test
    void shouldGetCountFromCurrentYear() {
        // Arrange
        int currentYear = LocalDate.now().getYear();
        int expectedCount = 42;

        when(jpaRepo.getCountInYear(currentYear)).thenReturn(expectedCount);

        // Act
        int result = readerRepository.getCountFromCurrentYear();

        // Assert
        assertThat(result).isEqualTo(expectedCount);
        // Verifica se o ano correto foi passado
        verify(jpaRepo).getCountInYear(currentYear);
    }

    // --- Teste 4: Escrita (Save) ---

    @Test
    void shouldSaveAndReturnMappedReader() {
        // Arrange
        ReaderDetails readerToSave = new ReaderDetails();
        ReaderDetailsJpaEntity entityToSave = new ReaderDetailsJpaEntity();
        ReaderDetailsJpaEntity savedEntity = new ReaderDetailsJpaEntity();
        ReaderDetails expectedSavedReader = new ReaderDetails();

        when(mapper.toEntity(readerToSave)).thenReturn(entityToSave);
        when(jpaRepo.save(entityToSave)).thenReturn(savedEntity);
        when(mapper.toDomain(savedEntity)).thenReturn(expectedSavedReader);

        // Act
        ReaderDetails result = readerRepository.save(readerToSave);

        // Assert
        assertThat(result).isSameAs(expectedSavedReader);
        verify(mapper).toEntity(readerToSave);
        verify(jpaRepo).save(entityToSave);
        verify(mapper).toDomain(savedEntity);
    }

    // --- Teste 5: Lógica de Paginação (searchReaderDetails) ---

    @Test
    void shouldSearchReadersWithCorrectPagination() {
        // Arrange
        // 1. Input: Pedimos página 3, com 10 itens
        pt.psoft.g1.psoftg1.shared.services.Page page = new pt.psoft.g1.psoftg1.shared.services.Page(3, 10);
        SearchReadersQuery query = new SearchReadersQuery();

        // 2. Expectativa: Spring Data usa 0-indexado (3 - 1 = 2)
        Pageable expectedPageable = PageRequest.of(2, 10);

        // 3. Mocks: Simular o retorno (uma página vazia é suficiente)
        Page<ReaderDetailsJpaEntity> mockPage = new PageImpl<>(Collections.emptyList(), expectedPageable, 0);

        // Esperamos QUALQUER Specification (já que está em TODO), mas o Pageable EXATO
        when(jpaRepo.findAll(any(Specification.class), eq(expectedPageable))).thenReturn(mockPage);

        // Act
        List<ReaderDetails> results = readerRepository.searchReaderDetails(page, query);

        // Assert
        assertThat(results).isEmpty();

        // Verifica se o JpaRepo foi chamado com o Pageable *correto*
        verify(jpaRepo).findAll(any(Specification.class), eq(expectedPageable));
    }

    // --- Teste 6: Método Desligado (findTopByGenre) ---

    @Test
    void shouldReturnEmptyPageForFindTopByGenre() {
        // Arrange
        Pageable pageable = PageRequest.of(0, 5);
        String genre = "Ficção";
        LocalDate startDate = LocalDate.now().minusYears(1);
        LocalDate endDate = LocalDate.now();

        // Act
        Page<ReaderBookCountDTO> result = readerRepository.findTopByGenre(pageable, genre, startDate, endDate);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.isEmpty()).isTrue(); // Verifica se a página está vazia
        assertThat(result.getPageable()).isSameAs(pageable); // Verifica se retorna o pageable

        // Verifica se o jpaRepo NUNCA foi chamado (pois o método está desligado)
        // Esta é a forma correta de verificar que NADA aconteceu no mock:
        verifyNoInteractions(jpaRepo);
    }

} // <-- A chaveta de fecho da classe está aqui