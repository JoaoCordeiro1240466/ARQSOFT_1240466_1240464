package pt.psoft.g1.psoftg1.services;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import pt.psoft.g1.psoftg1.model.Reader;
import pt.psoft.g1.psoftg1.repositories.ReaderRepository;
import pt.psoft.g1.psoftg1.repositories.UserRepository;

import java.time.Year;
import java.util.List;
import java.util.Optional;

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

        String year = String.valueOf(Year.now().getValue());
        long count = readerRepository.countByReaderNumberStartingWith(year + "/");
        String readerNumber = year + "/" + (count + 1);

        Reader reader = new Reader();
        reader.setUsername(request.getUsername());
        reader.setFullName(request.getFullName());
        reader.setPhoneNumber(request.getPhoneNumber());
        reader.setEmail(request.getEmail());
        reader.setPassword(passwordEncoder.encode(request.getPassword()));
        reader.setReaderNumber(readerNumber);
        reader.setEnabled(true);
        reader.addAuthority("ROLE_READER");

        return readerRepository.save(reader);
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
}
