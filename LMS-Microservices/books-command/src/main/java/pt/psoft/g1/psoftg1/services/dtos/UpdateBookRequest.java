package pt.psoft.g1.psoftg1.services.dtos;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

@Data
public class UpdateBookRequest {

    private String title;

    private String description;

    private String genreId;

    private List<String> authorIds;

    private MultipartFile photo;

    private String photoURI;
}
