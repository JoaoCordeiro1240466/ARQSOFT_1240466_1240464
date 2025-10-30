package pt.psoft.g1.psoftg1.lendingmanagement.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter; // <-- ADICIONADO IMPORT
import lombok.Setter; // <-- ADICIONADO IMPORT

import java.io.Serializable;
import java.time.LocalDate;

@Embeddable
@Getter // <-- ADICIONADO
@Setter // <-- ADICIONADO
public class LendingNumber implements Serializable {

    @Column(name = "LENDING_NUMBER", length = 32)
    @NotNull
    @NotBlank
    @Size(min = 6, max = 32)
    private String lendingNumber;

    // ... (Construtores mantidos) ...
    public LendingNumber(int year, int sequential) {
        if(year < 1970 || LocalDate.now().getYear() < year)
            throw new IllegalArgumentException("Invalid year component");
        if(sequential < 0)
            throw new IllegalArgumentException("Sequencial component cannot be negative");
        this.lendingNumber = year + "/" + sequential;
    }

    public LendingNumber(String lendingNumber){
        if(lendingNumber == null)
            throw new IllegalArgumentException("Lending number cannot be null");

        int year, sequential;
        try {
            year        = Integer.parseInt(lendingNumber, 0, 4, 10);
            sequential  = Integer.parseInt(lendingNumber, 5, lendingNumber.length(), 10);
            if(lendingNumber.charAt(4) != '/')
                throw new IllegalArgumentException("Lending number has wrong format. It should be \"{year}/{sequential}\"");
        }catch (NumberFormatException | IndexOutOfBoundsException e){
            throw new IllegalArgumentException("Lending number has wrong format. It should be \"{year}/{sequential}\"");
        }
        this.lendingNumber = year + "/" + sequential;
    }

    public  LendingNumber(int sequential) {
        if(sequential < 0)
            throw new IllegalArgumentException("Sequencial component cannot be negative");
        this.lendingNumber = LocalDate.now().getYear() + "/" + sequential;
    }

    public LendingNumber() {} // O construtor já era público/default

    public String toString() {
        return this.lendingNumber;
    }
}