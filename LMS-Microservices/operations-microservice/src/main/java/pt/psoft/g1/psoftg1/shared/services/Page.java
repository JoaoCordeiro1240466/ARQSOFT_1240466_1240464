package pt.psoft.g1.psoftg1.shared.services;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor // Resolve o erro do construtor (int, int)
public class Page {

    private int number = 1;
    private int limit = 10;

    // 👇 Métodos de compatibilidade para o LendingServiceImpl
    public int getPageNumber() {
        return this.number;
    }

    public int getPageSize() {
        return this.limit;
    }
}