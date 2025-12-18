package pt.psoft.g1.psoftg1.services.dtos;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;

@Data
public class CreateBookRequest {

    @NotBlank
    private String isbn;

    @NotBlank
    private String title;

    private String description;

    @NotBlank
    private String genreId;

    @NotEmpty
    private List<String> authorIds;

    private MultipartFile photo;

    private String photoURI;
}
