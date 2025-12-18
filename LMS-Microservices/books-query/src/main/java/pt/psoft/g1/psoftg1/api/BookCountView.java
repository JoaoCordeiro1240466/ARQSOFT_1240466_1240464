package pt.psoft.g1.psoftg1.api;

import lombok.Data;

@Data
public class BookCountView {
    private BookView bookView;
    private long count;
}
