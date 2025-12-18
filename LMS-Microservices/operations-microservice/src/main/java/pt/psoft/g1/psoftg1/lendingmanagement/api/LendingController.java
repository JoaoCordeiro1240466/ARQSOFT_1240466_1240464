package pt.psoft.g1.psoftg1.lendingmanagement.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pt.psoft.g1.psoftg1.exceptions.NotFoundException;
import pt.psoft.g1.psoftg1.lendingmanagement.model.Lending;
import pt.psoft.g1.psoftg1.lendingmanagement.services.CreateLendingRequest;
import pt.psoft.g1.psoftg1.lendingmanagement.services.LendingService;
import pt.psoft.g1.psoftg1.lendingmanagement.services.SearchLendingQuery;
import pt.psoft.g1.psoftg1.lendingmanagement.services.SetLendingReturnedRequest;
import pt.psoft.g1.psoftg1.shared.api.ListResponse;
import pt.psoft.g1.psoftg1.shared.services.ConcurrencyService;
import pt.psoft.g1.psoftg1.shared.services.Page;
import pt.psoft.g1.psoftg1.shared.services.SearchRequest;
import java.util.List;
import java.util.Objects;

@Tag(name = "Lendings", description = "Endpoints for managing Lendings")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/lendings")
public class LendingController {
    private final LendingService lendingService;
    private final ConcurrencyService concurrencyService;

    private final LendingViewMapper lendingViewMapper;

    @Operation(summary = "Creates a new Lending")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<LendingView> create(@Valid @RequestBody final CreateLendingRequest resource) {

        final var lending = lendingService.create(resource);

        final var newlendingUri = ServletUriComponentsBuilder.fromCurrentRequestUri()
                .pathSegment(lending.getLendingNumber())
                .build().toUri();

        return ResponseEntity.created(newlendingUri)
                .contentType(MediaType.parseMediaType("application/hal+json"))
                .eTag(Long.toString(lending.getVersion()))
                .body(lendingViewMapper.toLendingView(lending));
    }

    @Operation(summary = "Gets a specific Lending")
    @GetMapping(value = "/{year}/{seq}")
    public ResponseEntity<LendingView> findByLendingNumber(
            Authentication authentication,
            @PathVariable("year")
            @Parameter(description = "The year of the Lending to find")
            final Integer year,
            @PathVariable("seq")
            @Parameter(description = "The sequencial of the Lending to find")
            final Integer seq) {

        String ln = year + "/" + seq;
        final var lending = lendingService.findByLendingNumber(ln)
                .orElseThrow(() -> new NotFoundException(Lending.class, ln));

        boolean isLibrarian = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_LIBRARIAN"));

        //if Librarian is logged in, skip ahead
        if (!isLibrarian) {
            // Assuming authentication.getName() is the reader's unique identifier (readerId)
            if (!Objects.equals(authentication.getName(), lending.getReaderId())) {
                throw new AccessDeniedException("Reader does not have permission to view this lending");
            }
        }
        final var lendingUri = ServletUriComponentsBuilder.fromCurrentRequestUri()
                .build().toUri();

        return ResponseEntity.ok().location(lendingUri)
                .contentType(MediaType.parseMediaType("application/hal+json"))
                .eTag(Long.toString(lending.getVersion()))
                .body(lendingViewMapper.toLendingView(lending));
    }

    @Operation(summary = "Sets a lending as returned")
    @PatchMapping(value = "/{year}/{seq}")
    public ResponseEntity<LendingView> setLendingReturned(
            final WebRequest request,
            final Authentication authentication,
            @PathVariable("year")
            @Parameter(description = "The year component of the Lending to find")
            final Integer year,
            @PathVariable("seq")
            @Parameter(description = "The sequential component of the Lending to find")
            final Integer seq,
            @Valid @RequestBody final SetLendingReturnedRequest resource) {
        final String ifMatchValue = request.getHeader(ConcurrencyService.IF_MATCH);
        if (ifMatchValue == null || ifMatchValue.isEmpty() || ifMatchValue.equals("null")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "You must issue a conditional PATCH using 'if-match'");
        }
        String ln = year + "/" + seq;
        final var maybeLending = lendingService.findByLendingNumber(ln)
                .orElseThrow(() -> new NotFoundException(Lending.class, ln));

        // Assuming authentication.getName() is the reader's unique identifier (readerId)
        if (!Objects.equals(authentication.getName(), maybeLending.getReaderId())) {
            throw new AccessDeniedException("Reader does not have permission to edit this lending");
        }

        final var lending = lendingService.setReturned(ln, resource, concurrencyService.getVersionFromIfMatchHeader(ifMatchValue));

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType("application/hal+json"))
                .eTag(Long.toString(lending.getVersion()))
                .body(lendingViewMapper.toLendingView(lending));
    }

    @Operation(summary = "Get list of overdue lendings")
    @GetMapping(value = "/overdue")
    public ListResponse<LendingView> getOverdueLendings(@Valid @RequestBody Page page) {
        final org.springframework.data.domain.Page<Lending> overdueLendingsPage = lendingService.getOverdue(page);
        final List<Lending> overdueLendings = overdueLendingsPage.getContent();
        if(overdueLendings.isEmpty())
            throw new NotFoundException("No lendings to show");
        return new ListResponse<>(lendingViewMapper.toLendingView(overdueLendings));
    }

    @PostMapping("/search")
    public ListResponse<LendingView> searchReaders(
            @RequestBody final SearchRequest<SearchLendingQuery> request) {
        final org.springframework.data.domain.Page<Lending> readerListPage = lendingService.searchLendings(request.getPage(), request.getQuery());
        final List<Lending> readerList = readerListPage.getContent();
        return new ListResponse<>(lendingViewMapper.toLendingView(readerList));
    }
}
