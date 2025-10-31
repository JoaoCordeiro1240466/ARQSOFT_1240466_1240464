package pt.psoft.g1.psoftg1.infrastructure.booklookup.openlibrary;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OpenLibraryLookupServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private OpenLibraryLookupService openLibraryLookupService; // ✅ fixed

    @BeforeEach
    void setup() {
        // No specific setup required, RestTemplate is mocked
    }

    @Test
    void shouldReturnIsbnWhenValidResponse() {
        // Arrange: simulate a valid API response
        OpenLibraryLookupService.OpenLibraryDoc doc = new OpenLibraryLookupService.OpenLibraryDoc();
        doc.setIsbn(List.of("9780451524935", "0451524934")); // Multiple ISBNs, should take the first one

        OpenLibraryLookupService.OpenLibraryResponse response = new OpenLibraryLookupService.OpenLibraryResponse();
        response.setDocs(List.of(doc));

        when(restTemplate.getForObject(any(URI.class), eq(OpenLibraryLookupService.OpenLibraryResponse.class)))
                .thenReturn(response);

        // Act
        Optional<String> result = openLibraryLookupService.fetchIsbnByTitle("1984");

        // Assert
        assertTrue(result.isPresent());
        assertEquals("9780451524935", result.get());
    }

    @Test
    void shouldReturnEmptyWhenApiReturnsNoResults() {
        // Arrange: simulate response with empty docs
        OpenLibraryLookupService.OpenLibraryResponse response = new OpenLibraryLookupService.OpenLibraryResponse();
        response.setDocs(List.of()); // Empty list

        when(restTemplate.getForObject(any(URI.class), eq(OpenLibraryLookupService.OpenLibraryResponse.class)))
                .thenReturn(response);

        // Act
        Optional<String> result = openLibraryLookupService.fetchIsbnByTitle("Nonexistent Book");

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void shouldReturnEmptyWhenDocsHaveNoIsbn() {
        // Arrange: simulate doc without ISBNs
        OpenLibraryLookupService.OpenLibraryDoc doc = new OpenLibraryLookupService.OpenLibraryDoc();
        doc.setIsbn(List.of()); // Empty ISBN list

        OpenLibraryLookupService.OpenLibraryResponse response = new OpenLibraryLookupService.OpenLibraryResponse();
        response.setDocs(List.of(doc));

        when(restTemplate.getForObject(any(URI.class), eq(OpenLibraryLookupService.OpenLibraryResponse.class)))
                .thenReturn(response);

        // Act
        Optional<String> result = openLibraryLookupService.fetchIsbnByTitle("Book Without ISBN");

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void shouldReturnEmptyWhenApiFails() {
        // Arrange: simulate API connection failure
        when(restTemplate.getForObject(any(URI.class), eq(OpenLibraryLookupService.OpenLibraryResponse.class)))
                .thenThrow(new RestClientException("Connection error"));

        // Act
        Optional<String> result = openLibraryLookupService.fetchIsbnByTitle("Faulty Book");

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void shouldHandleNullResponseGracefully() {
        // Arrange: simulate null API response
        when(restTemplate.getForObject(any(URI.class), eq(OpenLibraryLookupService.OpenLibraryResponse.class)))
                .thenReturn(null);

        // Act
        Optional<String> result = openLibraryLookupService.fetchIsbnByTitle("Null Book");

        // Assert
        assertTrue(result.isEmpty());
    }
}
