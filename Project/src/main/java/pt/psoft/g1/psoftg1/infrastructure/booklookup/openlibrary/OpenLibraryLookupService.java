package pt.psoft.g1.psoftg1.infrastructure.booklookup.openlibrary;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import pt.psoft.g1.psoftg1.bookmanagement.services.IsbnLookupService;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@Service
@Profile("open-library")
@RequiredArgsConstructor
public class OpenLibraryLookupService implements IsbnLookupService {

    private final RestTemplate restTemplate;
    private static final String API_URL = "https://openlibrary.org/search.json";


    @Override
    @Cacheable(value = "isbnCacheOpenLibrary", key = "#title")
    public Optional<String> fetchIsbnByTitle(String title) {
        System.out.println("A CHAMAR API EXTERNA (OPEN LIBRARY) PARA: " + title);


        URI uri = UriComponentsBuilder.fromHttpUrl(API_URL)
                .queryParam("title", title)
                .queryParam("limit", 1)
                .queryParam("fields", "key,title,isbn")
                .build()
                .toUri();

        try {

            OpenLibraryResponse response = restTemplate.getForObject(uri, OpenLibraryResponse.class);

            return Optional.ofNullable(response)
                    .map(OpenLibraryResponse::getDocs)
                    .filter(docs -> !docs.isEmpty())
                    .map(docs -> docs.get(0))
                    .map(OpenLibraryDoc::getIsbn)
                    .filter(isbns -> isbns != null && !isbns.isEmpty())
                    .map(isbns -> isbns.get(0));

        } catch (RestClientException e) {
            System.err.println("Erro ao chamar a API do Open Library: " + e.getMessage());

            return Optional.empty();
        }
    }

    @Getter @Setter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class OpenLibraryResponse {
        private List<OpenLibraryDoc> docs;
    }

    @Getter @Setter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class OpenLibraryDoc {
        private List<String> isbn;
    }
}