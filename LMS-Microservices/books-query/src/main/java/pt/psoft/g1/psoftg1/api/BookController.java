package pt.psoft.g1.psoftg1.api;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pt.psoft.g1.psoftg1.model.Book;
import pt.psoft.g1.psoftg1.services.BookService;

import java.util.List;

@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    @GetMapping
    public ResponseEntity<List<Book>> getAllBooks(
            @RequestParam(required = false) String genreId,
            @RequestParam(required = false) String authorId) {

        List<Book> books;
        if (genreId != null) {
            books = bookService.findByGenreId(genreId);
        } else if (authorId != null) {
            books = bookService.findByAuthorId(authorId);
        } else {
            books = bookService.findAll();
        }
        return ResponseEntity.ok(books);
    }

    @GetMapping("/{isbn}")
    public ResponseEntity<Book> getBookByIsbn(@PathVariable String isbn) {
        return bookService.findByIsbn(isbn)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
