package pt.psoft.g1.psoftg1.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pt.psoft.g1.psoftg1.api.views.AuthorView;
import pt.psoft.g1.psoftg1.model.Author;
import pt.psoft.g1.psoftg1.services.AuthorService;
import pt.psoft.g1.psoftg1.services.CreateAuthorRequest;
import pt.psoft.g1.psoftg1.services.UpdateAuthorRequest;


import java.util.List;

@Tag(name = "Author", description = "Endpoints for managing Authors")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/authors")
public class AuthorController {
    private final AuthorService authorService;
    private final AuthorViewMapper authorViewMapper;

    @Operation(summary = "Creates a new Author")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<AuthorView> create(@Valid @RequestBody CreateAuthorRequest resource) {
        final var author = authorService.create(resource);
        final var newAuthorUri = ServletUriComponentsBuilder.fromCurrentRequestUri()
                .path("/{id}")
                .buildAndExpand(author.getAuthorNumber())
                .toUri();
        return ResponseEntity.created(newAuthorUri)
                .body(authorViewMapper.toAuthorView(author));
    }

    @Operation(summary = "Updates a specific author")
    @PatchMapping(value = "/{authorNumber}")
    public ResponseEntity<AuthorView> partialUpdate(
            @PathVariable("authorNumber") @Parameter(description = "The number of the Author to find") final Long authorNumber,
            @Valid @RequestBody UpdateAuthorRequest resource) {

        Author author = authorService.partialUpdate(authorNumber, resource);

        return ResponseEntity.ok()
                .body(authorViewMapper.toAuthorView(author));
    }

    @Operation(summary = "Get an author by their author number")
    @GetMapping(value = "/{authorNumber}")
    public ResponseEntity<AuthorView> findByAuthorNumber(@PathVariable("authorNumber") final Long authorNumber) {
        final var author = authorService.findByAuthorNumber(authorNumber)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Author with number " + authorNumber + " not found"));
        return ResponseEntity.ok()
                .body(authorViewMapper.toAuthorView(author));
    }

    @Operation(summary = "Search authors by name")
    @GetMapping
    public List<AuthorView> findByName(@RequestParam("name") final String name) {
        final var authors = authorService.findByName(name);
        return authorViewMapper.toAuthorView(authors);
    }
}
