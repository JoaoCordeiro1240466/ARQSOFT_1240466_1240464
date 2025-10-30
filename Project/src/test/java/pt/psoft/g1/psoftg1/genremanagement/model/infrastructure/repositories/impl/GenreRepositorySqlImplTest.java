package pt.psoft.g1.psoftg1.genremanagement.infrastructure.repositories.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

// Imports do seu projeto
import pt.psoft.g1.psoftg1.bookmanagement.services.GenreBookCountDTO;
import pt.psoft.g1.psoftg1.genremanagement.model.Genre;
import pt.psoft.g1.psoftg1.genremanagement.services.GenreLendingsDTO;
import pt.psoft.g1.psoftg1.genremanagement.services.GenreLendingsPerMonthDTO;
import pt.psoft.g1.psoftg1.infrastructure.persistence.sql.mapper.GenreSqlMapper;
import pt.psoft.g1.psoftg1.infrastructure.persistence.sql.model.GenreJpaEntity;
import pt.psoft.g1.psoftg1.infrastructure.persistence.sql.repo.SpringDataJpaGenreRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

// Imports estáticos
import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class GenreRepositorySqlImplTest {

    // Mocks: As dependências que queremos simular
    @Mock
    private SpringDataJpaGenreRepository jpaRepo;

    @Mock
    private GenreSqlMapper mapper;

    // A classe que estamos a testar
    @InjectMocks
    private GenreRepositorySqlImpl genreRepository;

    // --- Teste 1: Leitura Simples (Caminho Feliz) ---

    @Test
    void shouldReturnGenreWhenFoundByString() {
        // Arrange
        String genreName = "Ficção";
        GenreJpaEntity entity = new GenreJpaEntity();
        Genre expectedGenre = new Genre(genreName); // Assumindo um construtor

        when(jpaRepo.findByGenre(genreName)).thenReturn(Optional.of(entity));
        when(mapper.toDomain(entity)).thenReturn(expectedGenre);

        // Act
        Optional<Genre> result = genreRepository.findByString(genreName);

        // Assert
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(expectedGenre);
        verify(jpaRepo).findByGenre(genreName);
        verify(mapper).toDomain(entity);
    }

    // --- Teste 2: Leitura Simples (Caminho Triste) ---

    @Test
    void shouldReturnEmptyWhenNotFoundByString() {
        // Arrange
        String genreName = "Não Existe";
        when(jpaRepo.findByGenre(genreName)).thenReturn(Optional.empty());

        // Act
        Optional<Genre> result = genreRepository.findByString(genreName);

        // Assert
        assertThat(result).isEmpty();
        verify(jpaRepo).findByGenre(genreName);
        verify(mapper, never()).toDomain(any());
    }

    // --- Teste 3: Mapeamento de Página (findTop5GenreByBookCount) ---

    @Test
    void shouldCorrectlyMapTopGenreByBookCount() {
        // Arrange
        Pageable pageable = PageRequest.of(0, 5);

        // 1. Dados "crus" (raw) do JPA
        GenreJpaEntity entity1 = new GenreJpaEntity();
        GenreJpaEntity entity2 = new GenreJpaEntity();
        Object[] rawRow1 = new Object[]{entity1, 100L}; // Entidade 1, 100 livros
        Object[] rawRow2 = new Object[]{entity2, 50L};  // Entidade 2, 50 livros
        Page<Object[]> mockPage = new PageImpl<>(List.of(rawRow1, rawRow2), pageable, 2);

        // 2. Dados de domínio que o mapper vai retornar
        Genre domain1 = new Genre("Ficção");
        Genre domain2 = new Genre("História");

        // 3. Configurar Mocks
        when(jpaRepo.findTop5GenreByBookCountRaw(pageable)).thenReturn(mockPage);
        when(mapper.toDomain(entity1)).thenReturn(domain1);
        when(mapper.toDomain(entity2)).thenReturn(domain2);

        // Act
        Page<GenreBookCountDTO> resultPage = genreRepository.findTop5GenreByBookCount(pageable);

        // Assert
        assertThat(resultPage).isNotNull();
        assertThat(resultPage.getTotalElements()).isEqualTo(2);

        // Verifica o mapeamento (a sua lógica customizada)
        GenreBookCountDTO dto1 = resultPage.getContent().get(0);
        assertThat(dto1.getGenre()).isEqualTo("Ficção");
        // --- CORRIGIDO ---
        assertThat(dto1.getBookCount()).isEqualTo(100L); // Era getCount()

        GenreBookCountDTO dto2 = resultPage.getContent().get(1);
        assertThat(dto2.getGenre()).isEqualTo("História");
        // --- CORRIGIDO ---
        assertThat(dto2.getBookCount()).isEqualTo(50L); // Era getCount()
    }

    // --- Teste 4: Lógica de Agrupamento (getLendingsPerMonthLastYearByGenre) ---
    // Este é o teste mais importante

    @Test
    void shouldCorrectlyGroupLendingsPerMonth() {
        // Arrange
        // 1. Simular os dados "planos" (flat) que vêm do jpaRepo
        // (Ano, Mês, Género, Contagem)
        Object[] rawRow1 = new Object[]{2024, 10, "Ficção", 50L};  // Outubro
        Object[] rawRow2 = new Object[]{2024, 10, "História", 25L}; // Outubro
        Object[] rawRow3 = new Object[]{2024, 9, "Ficção", 30L};   // Setembro
        List<Object[]> rawList = List.of(rawRow1, rawRow2, rawRow3);

        // 2. Configurar o mock (usamos 'any' para a data, pois o LocalDate.now() é difícil de mockar)
        when(jpaRepo.getLendingsPerMonthLastYearByGenreRaw(any(LocalDate.class))).thenReturn(rawList);

        // Act
        List<GenreLendingsPerMonthDTO> resultList = genreRepository.getLendingsPerMonthLastYearByGenre();

        // Assert
        assertThat(resultList).isNotNull();
        // Esperamos 2 DTOs: um para Outubro/2024, um para Setembro/2024
        assertThat(resultList).hasSize(2);

        // 3. Verificar o DTO de Outubro (que deve ter 2 estatísticas de género)
        GenreLendingsPerMonthDTO octDto = resultList.stream()
                .filter(dto -> dto.getYear() == 2024 && dto.getMonth() == 10)
                .findFirst().orElse(null);

        assertThat(octDto).isNotNull();
        // --- CORRIGIDO ---
        assertThat(octDto.getValues()).hasSize(2); // Era getStats()
        // Assumindo que GenreLendingsDTO tem construtor (String, Long) e um bom .equals()
        // --- CORRIGIDO ---
        assertThat(octDto.getValues()).containsExactlyInAnyOrder( // Era getStats()
                new GenreLendingsDTO("Ficção", 50L),
                new GenreLendingsDTO("História", 25L)
        );

        // 4. Verificar o DTO de Setembro (que deve ter 1 estatística de género)
        GenreLendingsPerMonthDTO sepDto = resultList.stream()
                .filter(dto -> dto.getYear() == 2024 && dto.getMonth() == 9)
                .findFirst().orElse(null);

        assertThat(sepDto).isNotNull();
        // --- CORRIGIDO ---
        assertThat(sepDto.getValues()).hasSize(1); // Era getStats()
        // --- CORRIGIDO ---
        assertThat(sepDto.getValues()).containsExactly( // Era getStats()
                new GenreLendingsDTO("Ficção", 30L)
        );

        // O mapper não é usado neste método, o que é correto
        verify(mapper, never()).toDomain(any());
    }

    // --- Teste 5: Lógica de Agrupamento (getLendingsAverageDurationPerMonth) ---

    @Test
    void shouldCorrectlyGroupLendingsAverageDurationPerMonth() {
        // Arrange
        LocalDate startDate = LocalDate.of(2024, 1, 1);
        LocalDate endDate = LocalDate.of(2024, 12, 31);

        // 1. Simular dados "planos": (Ano, Mês, Género, Média Duração)
        Object[] rawRow1 = new Object[]{2024, 10, "Ficção", 14.5};  // Outubro
        Object[] rawRow2 = new Object[]{2024, 10, "História", 20.0}; // Outubro
        Object[] rawRow3 = new Object[]{2024, 9, "Ficção", 12.0};   // Setembro
        List<Object[]> rawList = List.of(rawRow1, rawRow2, rawRow3);

        // 2. Configurar Mock
        when(jpaRepo.getLendingsAverageDurationPerMonthRaw(startDate, endDate)).thenReturn(rawList);

        // Act
        List<GenreLendingsPerMonthDTO> resultList = genreRepository.getLendingsAverageDurationPerMonth(startDate, endDate);

        // Assert
        assertThat(resultList).isNotNull();
        assertThat(resultList).hasSize(2); // Um para Outubro, um para Setembro

        // 3. Verificar o DTO de Outubro
        GenreLendingsPerMonthDTO octDto = resultList.stream()
                .filter(dto -> dto.getYear() == 2024 && dto.getMonth() == 10)
                .findFirst().orElse(null);

        assertThat(octDto).isNotNull();
        // --- CORRIGIDO ---
        assertThat(octDto.getValues()).hasSize(2); // Era getStats()
        // Assumindo que GenreLendingsDTO tem construtor (String, Double) e um bom .equals()
        // --- CORRIGIDO ---
        assertThat(octDto.getValues()).containsExactlyInAnyOrder( // Era getStats()
                new GenreLendingsDTO("Ficção", 14.5),
                new GenreLendingsDTO("História", 20.0)
        );

        // 4. Verificar o DTO de Setembro
        GenreLendingsPerMonthDTO sepDto = resultList.stream()
                .filter(dto -> dto.getYear() == 2024 && dto.getMonth() == 9)
                .findFirst().orElse(null);

        assertThat(sepDto).isNotNull();
        // --- CORRIGIDO ---
        assertThat(sepDto.getValues()).hasSize(1); // Era getStats()
        // --- CORRIGIDO ---
        assertThat(sepDto.getValues()).containsExactly( // Era getStats()
                new GenreLendingsDTO("Ficção", 12.0)
        );

        verify(jpaRepo).getLendingsAverageDurationPerMonthRaw(startDate, endDate);
        verify(mapper, never()).toDomain(any()); // Mapper também não é usado aqui
    }
}