package pt.psoft.g1.psoftg1.services;

import pt.psoft.g1.psoftg1.model.Reader;

import java.util.List;
import java.util.Optional;

public interface ReaderService {

    Reader create(CreateReaderRequest request);

    Reader update(String id, UpdateReaderRequest request);

    Optional<Reader> findByUsername(String username);

    Optional<Reader> findByReaderNumber(String readerNumber);

    List<Reader> findByPhoneNumber(String phoneNumber);

    List<Reader> findByReaderName(String name);

    Iterable<Reader> findAll();
}
