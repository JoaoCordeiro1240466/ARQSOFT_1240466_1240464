package pt.psoft.g1.psoftg1.bookmanagement.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pt.psoft.g1.psoftg1.bookmanagement.model.Book;
import pt.psoft.g1.psoftg1.bookmanagement.model.BookCountDTO;
import pt.psoft.g1.psoftg1.bookmanagement.repositories.BookRepository;
import lombok.RequiredArgsConstructor;
import pt.psoft.g1.psoftg1.exceptions.ConflictException;
import pt.psoft.g1.psoftg1.exceptions.NotFoundException;
import pt.psoft.g1.psoftg1.shared.repositories.PhotoRepository;
import pt.psoft.g1.psoftg1.shared.services.Page;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@PropertySource({"classpath:config/library.properties"})
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final PhotoRepository photoRepository;

    @Value("${suggestionsLimitPerGenre}")
    private long suggestionsLimitPerGenre;

    @Override
    public Book create(CreateBookRequest request, String isbn) {
        if (bookRepository.findByIsbn(isbn).isPresent()) {
            throw new ConflictException("Book with ISBN " + isbn + " already exists");
        }

        // The logic for fetching authors and genres has been removed.
        // We now trust the IDs provided in the request.
        // Validation would happen in the respective microservices or via API calls.
        List<String> authorIds = request.getAuthorIds();
        String genreId = request.getGenreId();

        MultipartFile photo = request.getPhoto();
        String photoURI = request.getPhotoURI();
        if (photo == null && photoURI != null || photo != null && photoURI == null) {
            request.setPhoto(null);
            request.setPhotoURI(null);
        }

        Book newBook = new Book(isbn, request.getTitle(), request.getDescription(), genreId, authorIds, photoURI);

        return bookRepository.save(newBook);
    }

    @Override
    public Book update(UpdateBookRequest request, Long currentVersion) {
        var book = findByIsbn(request.getIsbn());

        // The logic for fetching and setting Author and Genre objects has been removed.
        // The applyPatch method now works directly with the DTO containing the IDs.
        MultipartFile photo = request.getPhoto();
        String photoURI = request.getPhotoURI();
        if (photo == null && photoURI != null || photo != null && photoURI == null) {
            request.setPhoto(null);
            request.setPhotoURI(null);
        }

        book.applyPatch(currentVersion, request);

        return bookRepository.save(book);
    }

    @Override
    public Book save(Book book) {
        return this.bookRepository.save(book);
    }

    @Override
    public List<BookCountDTO> findTop5BooksLent() {
        LocalDate oneYearAgo = LocalDate.now().minusYears(1);
        Pageable pageableRules = PageRequest.of(0, 5);
        return this.bookRepository.findTop5BooksLent(oneYearAgo, pageableRules).getContent();
    }

    @Override
    public Book removeBookPhoto(String isbn, long desiredVersion) {
        Book book = this.findByIsbn(isbn);
        String photoFile;
        try {
            photoFile = book.getPhoto().getPhotoFile();
        } catch (NullPointerException e) {
            throw new NotFoundException("Book did not have a photo assigned to it.");
        }

        book.removePhoto(desiredVersion);
        var updatedBook = bookRepository.save(book);
        photoRepository.deleteByPhotoFile(photoFile);
        return updatedBook;
    }

    @Override
    public List<Book> findByGenre(String genreId) {
        // Assuming the repository method is updated to search by genreId
        return this.bookRepository.findByGenreId(genreId);
    }

    @Override
    public List<Book> findByTitle(String title) {
        return bookRepository.findByTitle(title);
    }

    @Override
    public List<Book> findByAuthorId(String authorId) {
        // Assuming the repository method is updated to search if the authorId is in the list of authorIds
        return bookRepository.findByAuthorIdsContaining(authorId);
    }

    @Override
    public Book findByIsbn(String isbn) {
        return this.bookRepository.findByIsbn(isbn)
                .orElseThrow(() -> new NotFoundException(Book.class, isbn));
    }

    @Override
    public List<Book> getBooksSuggestionsForReader(String readerNumber) {
        // This logic is broken after migrating to microservices as it depends on the ReaderRepository.
        // It needs to be re-implemented using API calls to the people-query service to get reader interests.
        // For now, it returns an empty list.
        return new ArrayList<>();
    }



    @Override
    public List<Book> searchBooks(Page page, SearchBooksQuery query) {
        if (page == null) {
            page = new Page(1, 10);
        }
        if (query == null) {
            query = new SearchBooksQuery("", "", "");
        }
        // Assuming the repository method is updated to handle genreId and authorId
        return bookRepository.searchBooks(page, query);
    }
}
