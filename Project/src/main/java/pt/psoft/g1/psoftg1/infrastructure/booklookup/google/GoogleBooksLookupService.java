package pt.psoft.g1.psoftg1.infrastructure.booklookup.google;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
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
@Profile("google-books")
@RequiredArgsConstructor
public class GoogleBooksLookupService implements IsbnLookupService {

    private final RestTemplate restTemplate;
    private static final String API_URL = "https://www.googleapis.com/books/v1/volumes";

    @Value("${google.books.api.key}")
    private String apiKey;

    @Override
    @Cacheable(value = "isbnCacheGoogle", key = "#title")
    public Optional<String> fetchIsbnByTitle(String title) {
        System.out.println("A CHAMAR API EXTERNA (GOOGLE) PARA: " + title);

        URI uri = UriComponentsBuilder.fromHttpUrl(API_URL)
                .queryParam("q", "intitle:" + title)
                .queryParam("maxResults", 1)
                .queryParam("key", this.apiKey)
                .build()
                .encode()
                .toUri();

        try {
            GoogleBooksResponse response = restTemplate.getForObject(uri, GoogleBooksResponse.class);

            return Optional.ofNullable(response)
                    .map(GoogleBooksResponse::getItems)
                    .filter(items -> !items.isEmpty())
                    .map(items -> items.get(0))
                    .map(GoogleBookItem::getVolumeInfo)
                    .map(VolumeInfo::getIndustryIdentifiers)
                    .flatMap(identifiers -> identifiers.stream()
                            .filter(id -> "ISBN_13".equals(id.getType()))
                            .findFirst()
                            .or(() -> identifiers.stream()
                                    .filter(id -> "ISBN_10".equals(id.getType()))
                                    .findFirst())
                            .map(IndustryIdentifier::getIdentifier)
                    );

        } catch (RestClientException e) {
            System.err.println("Erro ao chamar a API do Google Books: " + e.getMessage());
            return Optional.empty();
        }
    }
    @Getter @Setter @JsonIgnoreProperties(ignoreUnknown = true)
    static class GoogleBooksResponse { private List<GoogleBookItem> items; }
    @Getter @Setter @JsonIgnoreProperties(ignoreUnknown = true)
    static class GoogleBookItem { private VolumeInfo volumeInfo; }
    @Getter @Setter @JsonIgnoreProperties(ignoreUnknown = true)
    static class VolumeInfo { private List<IndustryIdentifier> industryIdentifiers; }
    @Getter @Setter @JsonIgnoreProperties(ignoreUnknown = true)
    static class IndustryIdentifier { private String type; private String identifier; }

}