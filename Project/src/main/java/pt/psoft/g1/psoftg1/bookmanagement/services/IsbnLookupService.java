package pt.psoft.g1.psoftg1.bookmanagement.services;

import org.springframework.cache.annotation.Cacheable;
import java.util.Optional;

/**
 * Interface (Contrato/Porto) para um serviço que procura ISBNs
 * em APIs externas.
 * A implementação real (Google, OpenLibrary) será injetada pelo Spring
 * com base no perfil ativo.
 */
public interface IsbnLookupService {

    /**
     * Procura um ISBN por título.
     * O resultado será cacheado no "isbnCache"
     * (que partilha a configuração do Redis).
     *
     * @param title O título do livro a procurar.
     * @return Um Optional contendo o ISBN (se encontrado).
     */
    @Cacheable(value = "isbnCache", key = "#title")
    Optional<String> fetchIsbnByTitle(String title);
}