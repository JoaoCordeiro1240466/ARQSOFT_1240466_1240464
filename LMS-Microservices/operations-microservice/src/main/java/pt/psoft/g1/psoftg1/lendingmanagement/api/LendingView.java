package pt.psoft.g1.psoftg1.lendingmanagement.api;

import lombok.Data;
import java.time.LocalDate;

@Data
public class LendingView {

    private String lendingNumber;

    // 👇 Antes devia ser objeto, agora são Strings
    private String bookIsbn;
    private String readerCode;

    private LocalDate startDate;
    private LocalDate limitDate;
    private LocalDate returnedDate;
    private Double fineValuePerDayInCents;
    private String commentary;
    private String code;
}