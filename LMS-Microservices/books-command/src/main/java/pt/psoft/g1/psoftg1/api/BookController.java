package pt.psoft.g1.psoftg1.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pt.psoft.g1.psoftg1.model.Book;
import pt.psoft.g1.psoftg1.services.BookService;
import pt.psoft.g1.psoftg1.services.dtos.CreateBookRequest;
import pt.psoft.g1.psoftg1.services.dtos.UpdateBookRequest;
import pt.psoft.g1.psoftg1.exceptions.ConflictException;
import pt.psoft.g1.psoftg1.exceptions.NotFoundException;
import pt.psoft.g1.psoftg1.api.dtos.BookView;
import pt.psoft.g1.psoftg1.api.dtos.BookViewMapper;

@Tag(name = "Books", description = "Endpoints for managing Books")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/books")
public class BookController {
    private final BookService bookService;
    private final BookViewMapper bookViewMapper;

    private static final String IF_MATCH_HEADER = "If-Match";

    @Operation(summary = "Register a new Book")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<BookView> create(@Valid @RequestBody CreateBookRequest resource) {
        try {
            Book book = bookService.create(resource);
            final var newBookUri = ServletUriComponentsBuilder.fromCurrentRequestUri()
                    .path("/{isbn}")
                    .buildAndExpand(book.getIsbn().toString())
                    .toUri();

            return ResponseEntity.created(newBookUri)
                    .eTag(Long.toString(book.getVersion()))
                    .body(bookViewMapper.toBookView(book));
        } catch (ConflictException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Could not create book: " + e.getMessage());
        }
    }

    @Operation(summary = "Updates a specific Book")
    @PatchMapping(value = "/{isbn}")
    public ResponseEntity<BookView> updateBook(@PathVariable final String isbn,
                                               final WebRequest request,
                                               @Valid @RequestBody final UpdateBookRequest resource) {

        final String ifMatchValue = request.getHeader(IF_MATCH_HEADER);
        if (ifMatchValue == null || ifMatchValue.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You must issue a conditional PATCH using 'if-match'");
        }
        long version;
        try {
            version = Long.parseLong(ifMatchValue.replace("\"", ""));
        } catch (NumberFormatException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid version format in 'if-match' header.");
        }


        try {
            Book book = bookService.update(isbn, resource, version);
            return ResponseEntity.ok()
                    .eTag(Long.toString(book.getVersion()))
                    .body(bookViewMapper.toBookView(book));
        } catch (NotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (ConflictException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }
    }
}
