package pt.psoft.g1.psoftg1.api;

import lombok.Data;

@Data
public class BookAverageLendingDurationView {
    private BookView bookView;
    private double averageLendingDuration;
}
