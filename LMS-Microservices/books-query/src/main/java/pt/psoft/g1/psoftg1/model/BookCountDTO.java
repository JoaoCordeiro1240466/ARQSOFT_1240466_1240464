package pt.psoft.g1.psoftg1.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookCountDTO {
    private Book book;
    private long count;
}
