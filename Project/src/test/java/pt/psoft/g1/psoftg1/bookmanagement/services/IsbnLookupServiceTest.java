package pt.psoft.g1.psoftg1.bookmanagement.services;

import org.junit.jupiter.api.Test;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;

class IsbnLookupServiceTest {

    private final IsbnLookupService service = title -> Optional.empty();

    @Test
    void fetchIsbnByTitleShouldReturnEmptyOptional() {
        Optional<String> result = service.fetchIsbnByTitle("Any Book");
        assertTrue(result.isEmpty(), "Default lambda should return empty Optional");
    }

    @Test
    void fetchIsbnByTitleShouldNotThrowException() {
        assertDoesNotThrow(() -> service.fetchIsbnByTitle("Book Title"));
    }
}
