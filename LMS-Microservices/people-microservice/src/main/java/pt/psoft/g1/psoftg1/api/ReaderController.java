package pt.psoft.g1.psoftg1.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pt.psoft.g1.psoftg1.api.views.ReaderView;
import pt.psoft.g1.psoftg1.model.Reader;
import pt.psoft.g1.psoftg1.model.Role;
import pt.psoft.g1.psoftg1.model.User;
import pt.psoft.g1.psoftg1.services.CreateReaderRequest;
import pt.psoft.g1.psoftg1.services.ReaderService;
import pt.psoft.g1.psoftg1.services.UpdateReaderRequest;
import pt.psoft.g1.psoftg1.services.UserService;

import java.util.ArrayList;
import java.util.List;

@Tag(name = "Readers", description = "Endpoints to manage readers")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/readers")
public class ReaderController {
    private final ReaderService readerService;
    private final UserService userService;
    private final ReaderViewMapper readerViewMapper;

    @Operation(summary = "Gets reader data for the authenticated user, or all readers if authenticated as a Librarian")
    @GetMapping
    public ResponseEntity<?> getData(Authentication authentication) {
        User loggedUser = userService.getAuthenticatedUser(authentication);

        if (loggedUser.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_" + Role.LIBRARIAN))) {
            List<Reader> readers = new ArrayList<>();
            readerService.findAll().forEach(readers::add);
            return ResponseEntity.ok().body(readerViewMapper.toReaderView(readers));
        }

        Reader reader = readerService.findByUsername(loggedUser.getUsername())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Reader not found for username: " + loggedUser.getUsername()));
        return ResponseEntity.ok().body(readerViewMapper.toReaderView(reader));
    }

    @Operation(summary = "Gets a reader by their reader number")
    @GetMapping(value="/{year}/{seq}")
    @RolesAllowed(Role.LIBRARIAN)
    public ResponseEntity<ReaderView> findByReaderNumber(
            @PathVariable("year") @Parameter(description = "The year of the Reader to find") final Integer year,
            @PathVariable("seq") @Parameter(description = "The sequential number of the Reader to find") final Integer seq) {
        String readerNumber = year + "/" + seq;
        final var reader = readerService.findByReaderNumber(readerNumber)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Could not find reader with specified reader number"));
        return ResponseEntity.ok()
                .body(readerViewMapper.toReaderView(reader));
    }

    @Operation(summary = "Gets a list of Readers by phone number")
    @GetMapping(params = "phoneNumber")
    public List<ReaderView> findByPhoneNumber(@RequestParam(name = "phoneNumber") final String phoneNumber) {
        List<Reader> readers = readerService.findByPhoneNumber(phoneNumber);
        if(readers.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No readers found with phone number: " + phoneNumber);
        }
        return readerViewMapper.toReaderView(readers);
    }

    @Operation(summary = "Finds readers by name (Librarian only)")
    @RolesAllowed(Role.LIBRARIAN)
    @GetMapping(params = "name")
    public List<ReaderView> findByReaderName(@RequestParam("name") final String name) {
        List<Reader> readers = this.readerService.findByReaderName(name);
        if(readers.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Could not find reader with name: " + name);
        }
        return readerViewMapper.toReaderView(readers);
    }

    @Operation(summary = "Creates a reader")
    @RolesAllowed({Role.LIBRARIAN, Role.ADMIN})
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<ReaderView> createReader(@Valid @RequestBody CreateReaderRequest readerRequest) {
        Reader reader = readerService.create(readerRequest);
        final var newReaderUri = ServletUriComponentsBuilder.fromCurrentRequestUri()
                .pathSegment(reader.getReaderNumber())
                .build().toUri();
        return ResponseEntity.created(newReaderUri)
                .body(readerViewMapper.toReaderView(reader));
    }

    @Operation(summary = "Updates the authenticated reader's profile")
    @RolesAllowed(Role.READER)
    @PatchMapping
    public ResponseEntity<ReaderView> updateReader(
            @Valid @RequestBody UpdateReaderRequest readerRequest,
            Authentication authentication) {

        User loggedUser = userService.getAuthenticatedUser(authentication);
        Reader reader = readerService.update(loggedUser.getUsername(), readerRequest);

        return ResponseEntity.ok()
                .body(readerViewMapper.toReaderView(reader));
    }
}
