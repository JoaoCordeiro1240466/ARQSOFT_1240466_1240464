package pt.psoft.g1.psoftg1.genremanagement.api;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pt.psoft.g1.psoftg1.genremanagement.model.Genre;
import pt.psoft.g1.psoftg1.genremanagement.services.GenreService;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@Tag(name = "Genres", description = "Endpoints for managing Genres")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/genres")
public class GenreController {
    private final GenreService genreService;
    private final GenreViewMapper genreViewMapper;

    @GetMapping
    public ResponseEntity<List<GenreView>> findAll() {
        List<GenreView> genres = ((List<Genre>) genreService.findAll())
                .stream()
                .map(genreViewMapper::toGenreView)
                .collect(Collectors.toList());
        return new ResponseEntity<>(genres, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<GenreView> save(@Valid @RequestBody GenreRequest genreRequest) {
        Genre genre = new Genre(genreRequest.getGenre());
        Genre savedGenre = genreService.save(genre);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedGenre.getPk())
                .toUri();

        return ResponseEntity.created(location).body(genreViewMapper.toGenreView(savedGenre));
    }
}
