package pt.psoft.g1.psoftg1.lendingmanagement.services;

import pt.psoft.g1.psoftg1.lendingmanagement.model.Lending;
import pt.psoft.g1.psoftg1.shared.services.Page; // This import is for the custom Page class, not Spring's Page

import java.util.List;
import java.util.Optional;

public interface LendingService {
    /**
     * @param lendingNumber
     * @return {@code Optional<Lending>}
     */
    Optional<Lending> findByLendingNumber(String lendingNumber);
    /**
     * @param readerId - Reader ID of the Reader associated with the lending
     * @param isbn         - ISBN of the book associated with the lending
     * @return {@code List<Lending>}
     */
    List<Lending> listByReaderIdAndIsbn(String readerId, String isbn);
    Lending create(CreateLendingRequest resource); //No ID passed, as it is auto generated
    Lending setReturned(String id, SetLendingReturnedRequest resource, long desiredVersion);
    org.springframework.data.domain.Page<Lending> getOverdue(Page page); // Using custom Page
    org.springframework.data.domain.Page<Lending> searchLendings(Page page, SearchLendingQuery request); // Using custom Page
}
