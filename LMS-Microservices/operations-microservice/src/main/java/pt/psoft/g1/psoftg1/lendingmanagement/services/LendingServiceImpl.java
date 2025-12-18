package pt.psoft.g1.psoftg1.lendingmanagement.services;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import pt.psoft.g1.psoftg1.exceptions.LendingForbiddenException;
import pt.psoft.g1.psoftg1.exceptions.NotFoundException;
import pt.psoft.g1.psoftg1.lendingmanagement.model.Fine;
import pt.psoft.g1.psoftg1.lendingmanagement.model.Lending;
import pt.psoft.g1.psoftg1.lendingmanagement.repositories.BookReplicaRepository;
import pt.psoft.g1.psoftg1.lendingmanagement.repositories.FineRepository;
import pt.psoft.g1.psoftg1.lendingmanagement.repositories.LendingRepository;
import pt.psoft.g1.psoftg1.shared.services.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;


import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;
import java.util.UUID; // Added import for UUID

@Service
@RequiredArgsConstructor
public class LendingServiceImpl implements LendingService{
    private final LendingRepository lendingRepository;
    private final FineRepository fineRepository;
    private final BookReplicaRepository bookReplicaRepository;

    @Value("${lendingDurationInDays}")
    private int lendingDurationInDays;
    @Value("${fineValuePerDayInCents}")
    private int fineValuePerDayInCents;

    @Override
    public Optional<Lending> findByLendingNumber(String lendingNumber){
        return lendingRepository.findByLendingNumber(lendingNumber);
    }

    @Override
    public List<Lending> listByReaderIdAndIsbn(String readerId, String isbn){
        return lendingRepository.findByReaderIdAndIsbn(readerId, isbn);
    }

    @Override
    public Lending create(final CreateLendingRequest resource) {
        // Validate existence in BookReplica
        if (!bookReplicaRepository.existsById(resource.getIsbn())) {
            throw new NotFoundException("Book not found");
        }

        // Generate a simple lendingNumber (e.g., UUID)
        String lendingNumber = UUID.randomUUID().toString();

        // Create Lending instance with generated lendingNumber and provided IDs
        final Lending l = new Lending(resource.getIsbn(),
                                      resource.getReaderNumber(), // Assuming readerNumber from request maps to readerId in Lending
                                      lendingNumber,
                                      lendingDurationInDays,
                                      fineValuePerDayInCents);

        return lendingRepository.save(l);
    }

    @Override
    public Lending setReturned(final String lendingNumber, final SetLendingReturnedRequest resource, final long desiredVersion) {

        var lending = lendingRepository.findByLendingNumber(lendingNumber)
                .orElseThrow(() -> new NotFoundException("Cannot update lending with this lending number"));

        lending.setReturned(desiredVersion, resource.getCommentary());

        if(lending.getDaysDelayed() > 0){
            final var fine = new Fine(lending);
            fineRepository.save(fine);
        }

        return lendingRepository.save(lending);
    }

    @Override
    public org.springframework.data.domain.Page<Lending> getOverdue(Page page) {
        if (page == null) {
            page = new Page(1, 10);
        }
        Pageable pageable = PageRequest.of(page.getPageNumber() - 1, page.getPageSize());
        return lendingRepository.getOverdue(LocalDate.now(), pageable);
    }

    @Override
    public org.springframework.data.domain.Page<Lending> searchLendings(Page page, SearchLendingQuery query){
        if (page == null) {
            page = new Page(1, 10);
        }
        Pageable pageable = PageRequest.of(page.getPageNumber() - 1, page.getPageSize());

        String isbn = (query != null) ? query.getIsbn() : null;
        String readerId = (query != null) ? query.getReaderNumber() : null; // Assuming readerNumber in query maps to readerId in repo

        return lendingRepository.searchLendings(isbn, readerId, pageable);
    }
}
