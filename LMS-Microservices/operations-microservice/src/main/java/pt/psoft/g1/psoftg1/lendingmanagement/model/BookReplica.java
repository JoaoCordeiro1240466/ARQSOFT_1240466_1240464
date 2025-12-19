package pt.psoft.g1.psoftg1.lendingmanagement.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookReplica {

    @Id
    private String isbn;
    private String title;
    private String authorId;
    private String genre;
    private int publicationYear;
}
