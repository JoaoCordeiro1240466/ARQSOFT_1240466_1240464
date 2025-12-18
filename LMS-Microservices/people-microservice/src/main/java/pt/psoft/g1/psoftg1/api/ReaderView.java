package pt.psoft.g1.psoftg1.api.views;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "A detailed view of a Reader")
public class ReaderView {

    private String readerNumber;
    private String email;
    private String fullName;
    private String phoneNumber;

}
