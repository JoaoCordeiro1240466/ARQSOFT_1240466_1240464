package pt.psoft.g1.psoftg1.services;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import pt.psoft.g1.psoftg1.model.Reader;
import pt.psoft.g1.psoftg1.repositories.ReaderRepository;
import pt.psoft.g1.psoftg1.repositories.UserRepository;
import pt.psoft.g1.psoftg1.services.CreateReaderRequest;
import pt.psoft.g1.psoftg1.services.UpdateReaderRequest;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

@Service
@RequiredArgsConstructor
public class ReaderServiceImpl implements ReaderService {

    private final ReaderRepository readerRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Reader create(CreateReaderRequest request) {
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Username already exists!");
        }

        Reader reader = new Reader();
        reader.setUsername(request.getUsername());
        reader.setPassword(passwordEncoder.encode(request.getPassword()));
        reader.setFullName(request.getFullName());
        reader.setEmail(request.getEmail());
        reader.setPhoneNumber(request.getPhoneNumber());
        reader.setReaderNumber(generateReaderNumber());
        reader.setEnabled(true);

        return userRepository.save(reader);
    }

    @Override
    public Reader update(String username, UpdateReaderRequest request) {
        Reader reader = userRepository.findReaderByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Reader not found"));

        if (request.getFullName() != null) {
            reader.setFullName(request.getFullName());
        }
        if (request.getPhoneNumber() != null) {
            reader.setPhoneNumber(request.getPhoneNumber());
        }
        if (request.getEmail() != null) {
            reader.setEmail(request.getEmail());
        }

        return userRepository.save(reader);
    }

    @Override
    public Optional<Reader> findByUsername(String username) {
        return userRepository.findReaderByUsername(username);
    }

    @Override
    public Optional<Reader> findByReaderNumber(String readerNumber) {
        return readerRepository.findByReaderNumber(readerNumber);
    }

    @Override
    public List<Reader> findByPhoneNumber(String phoneNumber) {
        return readerRepository.findByPhoneNumber(phoneNumber);
    }

    @Override
    public List<Reader> findByReaderName(String name) {
        return readerRepository.findByFullNameContainingIgnoreCase(name);
    }

    @Override
    public Iterable<Reader> findAll() {
        return readerRepository.findAll();
    }

    private String generateReaderNumber() {
        long timestamp = System.currentTimeMillis();
        int randomNum = ThreadLocalRandom.current().nextInt(1000, 10000);
        return "R" + timestamp + "-" + randomNum;
    }


}
