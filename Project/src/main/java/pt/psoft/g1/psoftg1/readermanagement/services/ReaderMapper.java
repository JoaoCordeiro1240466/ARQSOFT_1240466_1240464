package pt.psoft.g1.psoftg1.readermanagement.services;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.web.multipart.MultipartFile;
import pt.psoft.g1.psoftg1.genremanagement.model.Genre;
import pt.psoft.g1.psoftg1.readermanagement.model.ReaderDetails;
import pt.psoft.g1.psoftg1.shared.model.Photo;
import pt.psoft.g1.psoftg1.usermanagement.model.Reader;
import pt.psoft.g1.psoftg1.usermanagement.services.UserService;

// 👇 ADICIONA OS IMPORTS PARA OS TEUS VALUE OBJECTS
import pt.psoft.g1.psoftg1.readermanagement.model.BirthDate;
import pt.psoft.g1.psoftg1.readermanagement.model.PhoneNumber;
import pt.psoft.g1.psoftg1.readermanagement.model.ReaderNumber;

import java.nio.file.Paths;
import java.util.List;

/**
 * Brief guide:
 * <a href="https://www.baeldung.com/mapstruct">https://www.baeldung.com/mapstruct</a>
 * */
@Mapper(componentModel = "spring", uses = {ReaderService.class, UserService.class})
public abstract class ReaderMapper {

    @Mapping(target = "username", source = "username")
    @Mapping(target = "password", source = "password")
    @Mapping(target = "name", source = "fullName")
    public abstract Reader createReader(CreateReaderRequest request);

    @Mapping(target = "photo", source = "photoURI")
    @Mapping(target = "interestList", source = "interestList")

    public abstract ReaderDetails createReaderDetails(int readerNumber, Reader reader, CreateReaderRequest request, String photoURI, List<Genre> interestList);

    /**
     * Ensina o MapStruct a converter um String num objeto PhoneNumber
     * chamando o seu construtor.
     */
    protected PhoneNumber stringToPhoneNumber(String phoneNumber) {
        if (phoneNumber == null) {
            return null;
        }
        // Isto assume que a tua classe PhoneNumber tem um
        // construtor público que aceita um String
        return new PhoneNumber(phoneNumber);
    }

    /**
     * Ensina o MapStruct a converter um String num objeto BirthDate
     * chamando o seu construtor.
     */
    protected BirthDate stringToBirthDate(String birthDate) {
        if (birthDate == null) {
            return null;
        }
        // Isto assume que a tua classe BirthDate tem um
        // construtor público que aceita um String
        return new BirthDate(birthDate);
    }

    /**
     * Ensina o MapStruct a converter um int num objeto ReaderNumber
     * chamando o seu construtor.
     */
    protected ReaderNumber intToReaderNumber(int readerNumber) {
        // Isto assume que a tua classe ReaderNumber tem um
        // construtor público que aceita um int (ex: 'new ReaderNumber(seq)')
        return new ReaderNumber(readerNumber);
    }
}