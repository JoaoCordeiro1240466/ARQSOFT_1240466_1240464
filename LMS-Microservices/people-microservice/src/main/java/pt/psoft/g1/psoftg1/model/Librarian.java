package pt.psoft.g1.psoftg1.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@DiscriminatorValue("LIBRARIAN")
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
public class Librarian extends User {
    // Lombok will generate constructors based on @SuperBuilder and @NoArgsConstructor
}
