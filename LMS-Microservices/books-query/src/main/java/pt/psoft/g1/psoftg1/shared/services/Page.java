package pt.psoft.g1.psoftg1.shared.services;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Page {
    private int pageNumber;
    private int pageSize;
}
