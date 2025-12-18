package pt.psoft.g1.psoftg1.services;

import java.util.Optional;

public interface IsbnLookupService {
    Optional<String> fetchIsbnByTitle(String title);
}
