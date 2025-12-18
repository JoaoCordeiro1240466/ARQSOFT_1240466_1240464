package pt.psoft.g1.psoftg1.api.views;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(description = "A detailed view of an Author")
public class AuthorView {

    @NotNull
    private Long authorNumber;

    @NotNull
    private String fullName;

    @NotNull
    private String bio;

}
