package pt.psoft.g1.psoftg1.lendingmanagement.services;

import org.hibernate.StaleObjectStateException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pt.psoft.g1.psoftg1.exceptions.ConflictException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import pt.psoft.g1.psoftg1.authormanagement.model.Author;
import pt.psoft.g1.psoftg1.authormanagement.repositories.AuthorRepository;
import pt.psoft.g1.psoftg1.bookmanagement.model.Book;
import pt.psoft.g1.psoftg1.bookmanagement.repositories.BookRepository;
import pt.psoft.g1.psoftg1.exceptions.LendingForbiddenException;
import pt.psoft.g1.psoftg1.genremanagement.model.Genre;
import pt.psoft.g1.psoftg1.genremanagement.repositories.GenreRepository;
import pt.psoft.g1.psoftg1.lendingmanagement.model.Lending;
import pt.psoft.g1.psoftg1.lendingmanagement.repositories.LendingRepository;
import pt.psoft.g1.psoftg1.readermanagement.model.ReaderDetails;
import pt.psoft.g1.psoftg1.readermanagement.repositories.ReaderRepository;
import pt.psoft.g1.psoftg1.usermanagement.model.Reader;
import pt.psoft.g1.psoftg1.usermanagement.repositories.UserRepository;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;


@Transactional
@SpringBootTest
@ActiveProfiles({"base65", "sql"})
class LendingServiceImplTest {
    @Autowired
    private LendingService lendingService;
    @Autowired
    private LendingRepository lendingRepository;
    @Autowired
    private ReaderRepository readerRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private GenreRepository genreRepository;
    @Autowired
    private AuthorRepository authorRepository;

    private Lending lending;
    private ReaderDetails readerDetails;
    private Reader reader;
    private Book book;
    private Author author;
    private Genre genre;

    @BeforeEach
    void setUp() {
        // 1. Criar e GUARDAR o Author (Pai)
        author = new Author("Manuel Antonio Pina",
                "Manuel António Pina foi um jornalista e escritor português, premiado em 2011 com o Prémio Camões",
                null);
        // CORREÇÃO: Reatribui a 'author' a instância gerida retornada pelo save()
        author = authorRepository.save(author);

        // 2. Criar e GUARDAR o Genre (Pai)
        genre = new Genre("Género");
        // CORREÇÃO: Reatribui a 'genre' a instância gerida
        genre = genreRepository.save(genre);

        // 3. Criar e GUARDAR o Reader/User (Pai)
        reader = Reader.newReader("manuel@gmail.com", "Manuelino123!", "Manuel Sarapinto das Coives");
        reader.setId("reader-teste-123");
        // CORREÇÃO: Reatribui a 'reader' a instância gerida
        reader = userRepository.save(reader);

        // 4. Criar e GUARDAR o Book (Filho de Author e Genre)
        List<Author> authors = List.of(author);
        book = new Book("9782826012092",
                "O Inspetor Max",
                "conhecido pastor-alemão que trabalha para a Judiciária...",
                genre,    // <-- Agora 'genre' é um objeto gerido
                authors,  // <-- Agora 'author' é um objeto gerido
                null);
        // CORREÇÃO: Esta era a linha 75 que falhava. Agora funciona.
        book = bookRepository.save(book);

        // 5. Criar e GUARDAR o ReaderDetails (Filho de Reader)
        // (Nota: Corrigido 'null' para uma lista vazia para segurança)
        readerDetails = new ReaderDetails(1,
                reader, // <-- 'reader' já está guardado
                "2000-01-01",
                "919191919",
                true,
                true,
                true,
                null,
                new java.util.ArrayList<>()); // <-- Corrigido de null
        // CORREÇÃO: Reatribui a 'readerDetails' a instância gerida
        readerDetails = readerRepository.save(readerDetails);

        // 6. Criar e GUARDAR o Lending (Filho de Book e ReaderDetails)
        lending = Lending.newBootstrappingLending(book,
                readerDetails, // <-- 'readerDetails' já está guardado
                LocalDate.now().getYear(),
                999,
                LocalDate.of(LocalDate.now().getYear(), 1,1),
                LocalDate.of(LocalDate.now().getYear(), 1,11),
                15,
                300);
        // CORREÇÃO: Reatribui a 'lending' a instância gerida
        lending = lendingRepository.save(lending);
    }

    @AfterEach
    void tearDown() {
        lendingRepository.delete(lending);
        readerRepository.delete(readerDetails);
        userRepository.delete(reader);
        bookRepository.delete(book);
        genreRepository.delete(genre);
        authorRepository.delete(author);
    }

    @Test
    void testFindByLendingNumber() {
        assertThat(lendingService.findByLendingNumber(LocalDate.now().getYear() + "/999")).isPresent();
        assertThat(lendingService.findByLendingNumber(LocalDate.now().getYear() + "/1")).isEmpty();
    }
/*
    @Test
    void testListByReaderNumberAndIsbn() {

    }
 */
    @Test
    void testCreate() {
        var request = new CreateLendingRequest("9782826012092",
                LocalDate.now().getYear() + "/1");
        var lending1 = lendingService.create(request);
        assertThat(lending1).isNotNull();
        var lending2 = lendingService.create(request);
        assertThat(lending2).isNotNull();
        var lending3 = lendingService.create(request);
        assertThat(lending3).isNotNull();

        // 4th lending
        assertThrows(LendingForbiddenException.class, () -> lendingService.create(request));

        lendingRepository.delete(lending3);
        lendingRepository.save(Lending.newBootstrappingLending(book,
                readerDetails,
                2024,
                997,
                LocalDate.of(2024, 3,1),
                null,
                15,
                300));

        // Having an overdue lending
        assertThrows(LendingForbiddenException.class, () -> lendingService.create(request));

    }

    @Test
    void testSetReturned() {
        int year = 2024, seq = 888;
        var notReturnedLending = lendingRepository.save(Lending.newBootstrappingLending(book,
                readerDetails,
                year,
                seq,
                LocalDate.of(2024, 3,1),
                null,
                15,
                300));
        var request = new SetLendingReturnedRequest(null);
        assertThrows(ConflictException.class,
                () -> lendingService.setReturned(year + "/" + seq, request, (notReturnedLending.getVersion()-1)));

        assertDoesNotThrow(
                () -> lendingService.setReturned(year + "/" + seq, request, notReturnedLending.getVersion()));
    }
/*
    @Test
    void testGetAverageDuration() {
    }

    @Test
    void testGetOverdue() {
    }

 */
}
