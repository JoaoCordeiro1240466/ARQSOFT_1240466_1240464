package pt.psoft.g1.psoftg1.genremanagement.api;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(description = "Request to create a new Genre")
public class GenreRequest {
    @NotBlank(message = "Genre name cannot be empty")
    private String genre;
}
