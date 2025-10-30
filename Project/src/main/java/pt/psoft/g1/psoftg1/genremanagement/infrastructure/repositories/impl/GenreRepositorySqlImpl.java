package pt.psoft.g1.psoftg1.genremanagement.infrastructure.repositories.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import pt.psoft.g1.psoftg1.bookmanagement.services.GenreBookCountDTO;
import pt.psoft.g1.psoftg1.genremanagement.model.Genre;
import pt.psoft.g1.psoftg1.genremanagement.repositories.GenreRepository;
import pt.psoft.g1.psoftg1.genremanagement.services.GenreLendingsDTO;
import pt.psoft.g1.psoftg1.genremanagement.services.GenreLendingsPerMonthDTO;
import pt.psoft.g1.psoftg1.infrastructure.persistence.sql.mapper.GenreSqlMapper;
import pt.psoft.g1.psoftg1.infrastructure.persistence.sql.repo.SpringDataJpaGenreRepository;
import pt.psoft.g1.psoftg1.infrastructure.persistence.sql.model.GenreJpaEntity;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.Map; // <-- IMPORT NECESSÁRIO

@Repository
@Profile("sql")
@RequiredArgsConstructor
public class GenreRepositorySqlImpl implements GenreRepository {

    private final SpringDataJpaGenreRepository jpaRepo;
    private final GenreSqlMapper mapper;

    private static final String CACHE_NAME = "genreCache";

    @Override
    public Iterable<Genre> findAll() {
        return jpaRepo.findAll().stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    @Cacheable(value = CACHE_NAME, key = "#genreName")
    public Optional<Genre> findByString(String genreName) {
        return jpaRepo.findByGenre(genreName).map(mapper::toDomain);
    }

    @Override
    @CacheEvict(value = CACHE_NAME, key = "#genre.genre")
    public Genre save(Genre genre) {
        GenreJpaEntity entity = mapper.toEntity(genre);
        GenreJpaEntity savedEntity = jpaRepo.save(entity);
        return mapper.toDomain(savedEntity);
    }

    @Override
    @CacheEvict(value = CACHE_NAME, key = "#genre.genre")
    public void delete(Genre genre) {
        jpaRepo.delete(mapper.toEntity(genre));
    }

    // --- MÉTODO CORRIGIDO (Erro 1) ---
    @Override
    public Page<GenreBookCountDTO> findTop5GenreByBookCount(Pageable pageable) {
        Page<Object[]> rawPage = jpaRepo.findTop5GenreByBookCountRaw(pageable);

        return rawPage.map(row -> {
            GenreJpaEntity genreEntity = (GenreJpaEntity) row[0];
            Long count = (Long) row[1];
            Genre genreDomain = mapper.toDomain(genreEntity);

            // CORREÇÃO: O construtor espera (String, long)
            return new GenreBookCountDTO(genreDomain.getGenre(), count);
        });
    }

    // --- MÉTODO CORRIGIDO (Sem erros, já estava certo) ---
    @Override
    public List<GenreLendingsDTO> getAverageLendingsInMonth(LocalDate month, pt.psoft.g1.psoftg1.shared.services.Page page) {
        Pageable pageable = PageRequest.of(page.getNumber() - 1, page.getLimit());
        List<Object[]> rawList = jpaRepo.getAverageLendingsInMonthRaw(month.getYear(), month.getMonthValue(), pageable);

        return rawList.stream().map(row -> {
            GenreJpaEntity genreEntity = (GenreJpaEntity) row[0];
            Double average = ((Number) row[1]).doubleValue();
            return new GenreLendingsDTO(mapper.toDomain(genreEntity), average);
        }).collect(Collectors.toList());
    }

    // --- MÉTODO REESCRITO (Erro 2) ---
    @Override
    public List<GenreLendingsPerMonthDTO> getLendingsPerMonthLastYearByGenre() {
        LocalDate oneYearAgo = LocalDate.now().minusYears(1);
        List<Object[]> rawList = jpaRepo.getLendingsPerMonthLastYearByGenreRaw(oneYearAgo);

        // O DTO espera dados agrupados por (Ano, Mês).
        // Vamos agrupar os dados planos (Ano, Mês, Género, Contagem)

        // Passo 1: Converter dados brutos num DTO "intermédio" (GenreLendingsDTO)
        // Usamos GenreLendingsDTO(String, Double) - assumindo que existe
        var flatStats = rawList.stream()
                .map(row -> new MonthlyStat(
                        (Integer) row[0], // Ano
                        (Integer) row[1], // Mês
                        new GenreLendingsDTO((String) row[2], (Long) row[3]) // (Género, Contagem)
                )).collect(Collectors.toList());

        // Passo 2: Agrupar por Ano e Mês
        Map<Integer, Map<Integer, List<GenreLendingsDTO>>> groupedStats = flatStats.stream()
                .collect(Collectors.groupingBy(
                        MonthlyStat::year,
                        Collectors.groupingBy(
                                MonthlyStat::month,
                                Collectors.mapping(MonthlyStat::dto, Collectors.toList())
                        )
                ));

        // Passo 3: Converter o Mapa no DTO final
        return groupedStats.entrySet().stream()
                .flatMap(yearEntry -> yearEntry.getValue().entrySet().stream()
                        .map(monthEntry -> new GenreLendingsPerMonthDTO(
                                yearEntry.getKey(),   // Ano (int)
                                monthEntry.getKey(),  // Mês (int)
                                monthEntry.getValue() // Lista<GenreLendingsDTO>
                        ))
                ).collect(Collectors.toList());
    }

    // --- MÉTODO REESCRITO (Erro 2) ---
    @Override
    public List<GenreLendingsPerMonthDTO> getLendingsAverageDurationPerMonth(LocalDate startDate, LocalDate endDate) {
        List<Object[]> rawList = jpaRepo.getLendingsAverageDurationPerMonthRaw(startDate, endDate);

        // Mesmo processo de agrupamento do método anterior

        // Passo 1: Converter dados brutos
        var flatStats = rawList.stream()
                .map(row -> new MonthlyStat(
                        (Integer) row[0], // Ano
                        (Integer) row[1], // Mês
                        new GenreLendingsDTO((String) row[2], ((Number) row[3]).doubleValue()) // (Género, Média)
                )).collect(Collectors.toList());

        // Passo 2: Agrupar por Ano e Mês
        Map<Integer, Map<Integer, List<GenreLendingsDTO>>> groupedStats = flatStats.stream()
                .collect(Collectors.groupingBy(
                        MonthlyStat::year,
                        Collectors.groupingBy(
                                MonthlyStat::month,
                                Collectors.mapping(MonthlyStat::dto, Collectors.toList())
                        )
                ));

        // Passo 3: Converter o Mapa no DTO final
        return groupedStats.entrySet().stream()
                .flatMap(yearEntry -> yearEntry.getValue().entrySet().stream()
                        .map(monthEntry -> new GenreLendingsPerMonthDTO(
                                yearEntry.getKey(),   // Ano (int)
                                monthEntry.getKey(),  // Mês (int)
                                monthEntry.getValue() // Lista<GenreLendingsDTO>
                        ))
                ).collect(Collectors.toList());
    }


    /**
     * Classe 'record' privada para ajudar no agrupamento dos dados.
     */
    private record MonthlyStat(int year, int month, GenreLendingsDTO dto) {}
}