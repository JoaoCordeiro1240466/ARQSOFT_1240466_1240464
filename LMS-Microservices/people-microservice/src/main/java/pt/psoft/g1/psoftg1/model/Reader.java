package pt.psoft.g1.psoftg1.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.Data; // Added @Data
import lombok.experimental.SuperBuilder;

@Entity
@DiscriminatorValue("READER")
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@Data // Added @Data
public class Reader extends User {
    private String readerNumber;
    // Lombok will generate constructors based on @SuperBuilder and @NoArgsConstructor
}
