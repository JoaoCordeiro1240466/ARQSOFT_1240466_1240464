package pt.psoft.g1.psoftg1.lendingmanagement.services;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "A DTO for setting a Lending as returned")
public class SetLendingReturnedRequest {
    @Size(max = 1024)
    @Schema(description = "A commentary about the lending return.", example = "The book was returned in good condition.")
    private String commentary;

    @Min(0)
    @Max(10)
    @Schema(description = "The grade given to the lending, from 0 to 10.", example = "8")
    private Integer grade;
}
