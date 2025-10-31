package pt.psoft.g1.psoftg1.infrastructure.booklookup.google;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GoogleBooksLookupServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private GoogleBooksLookupService googleBooksLookupService;

    @BeforeEach
    void setup() {
        // Set a fake API key for testing
        ReflectionTestUtils.setField(googleBooksLookupService, "apiKey", "fake-api-key");
    }

    @Test
    void shouldReturnIsbnWhenValidResponse() {
        // Arrange: simulate a valid API JSON response
        GoogleBooksLookupService.IndustryIdentifier isbn13 = new GoogleBooksLookupService.IndustryIdentifier();
        isbn13.setType("ISBN_13");
        isbn13.setIdentifier("9781234567890");

        GoogleBooksLookupService.VolumeInfo volumeInfo = new GoogleBooksLookupService.VolumeInfo();
        volumeInfo.setIndustryIdentifiers(List.of(isbn13));

        GoogleBooksLookupService.GoogleBookItem item = new GoogleBooksLookupService.GoogleBookItem();
        item.setVolumeInfo(volumeInfo);

        GoogleBooksLookupService.GoogleBooksResponse response = new GoogleBooksLookupService.GoogleBooksResponse();
        response.setItems(List.of(item));

        when(restTemplate.getForObject(any(URI.class), eq(GoogleBooksLookupService.GoogleBooksResponse.class)))
                .thenReturn(response);

        // Act
        Optional<String> result = googleBooksLookupService.fetchIsbnByTitle("Clean Code");

        // Assert
        assertTrue(result.isPresent());
        assertEquals("9781234567890", result.get());
    }

    @Test
    void shouldReturnEmptyWhenApiHasNoResults() {
        // Arrange: simulate an empty response from API
        GoogleBooksLookupService.GoogleBooksResponse response = new GoogleBooksLookupService.GoogleBooksResponse();
        response.setItems(List.of()); // empty list

        when(restTemplate.getForObject(any(URI.class), eq(GoogleBooksLookupService.GoogleBooksResponse.class)))
                .thenReturn(response);

        // Act
        Optional<String> result = googleBooksLookupService.fetchIsbnByTitle("NonExistentTitle");

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void shouldReturnEmptyWhenApiFails() {
        // Arrange: simulate a connection failure
        when(restTemplate.getForObject(any(URI.class), eq(GoogleBooksLookupService.GoogleBooksResponse.class)))
                .thenThrow(new RestClientException("Connection error"));

        // Act
        Optional<String> result = googleBooksLookupService.fetchIsbnByTitle("Clean Code");

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void shouldReturnIsbn10WhenIsbn13DoesNotExist() {
        // Arrange: simulate API response with only ISBN_10
        GoogleBooksLookupService.IndustryIdentifier isbn10 = new GoogleBooksLookupService.IndustryIdentifier();
        isbn10.setType("ISBN_10");
        isbn10.setIdentifier("0123456789");

        GoogleBooksLookupService.VolumeInfo volumeInfo = new GoogleBooksLookupService.VolumeInfo();
        volumeInfo.setIndustryIdentifiers(List.of(isbn10));

        GoogleBooksLookupService.GoogleBookItem item = new GoogleBooksLookupService.GoogleBookItem();
        item.setVolumeInfo(volumeInfo);

        GoogleBooksLookupService.GoogleBooksResponse response = new GoogleBooksLookupService.GoogleBooksResponse();
        response.setItems(List.of(item));

        when(restTemplate.getForObject(any(URI.class), eq(GoogleBooksLookupService.GoogleBooksResponse.class)))
                .thenReturn(response);

        // Act
        Optional<String> result = googleBooksLookupService.fetchIsbnByTitle("Old Book");

        // Assert
        assertTrue(result.isPresent());
        assertEquals("0123456789", result.get());
    }
}
