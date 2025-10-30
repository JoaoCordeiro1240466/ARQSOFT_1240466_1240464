package pt.psoft.g1.psoftg1.readermanagement.model;

import jakarta.annotation.Nullable;
import lombok.Getter;
import lombok.Setter;
import pt.psoft.g1.psoftg1.exceptions.ConflictException;
import pt.psoft.g1.psoftg1.genremanagement.model.Genre;
import pt.psoft.g1.psoftg1.readermanagement.services.UpdateReaderRequest;
import pt.psoft.g1.psoftg1.shared.model.EntityWithPhoto;
import pt.psoft.g1.psoftg1.usermanagement.model.Reader;

import java.nio.file.InvalidPathException;
import java.util.List;

public class ReaderDetails extends EntityWithPhoto {

    @Getter
    private Long pk;

    @Getter
    @Setter
    private Reader reader;

    // O @Getter foi removido daqui para evitar conflito
    private ReaderNumber readerNumber;

    @Getter
    private BirthDate birthDate;

    private PhoneNumber phoneNumber;

    @Setter
    @Getter
    private boolean gdprConsent;

    @Setter
    @Getter
    private boolean marketingConsent;

    @Setter
    @Getter
    private boolean thirdPartySharingConsent;

    @Getter
    private Long version;

    @Getter
    @Setter
    private List<Genre> interestList;

    public ReaderDetails(int readerNumber, Reader reader, String birthDate, String phoneNumber, boolean gdpr, boolean marketing, boolean thirdParty, String photoURI, List<Genre> interestList) {
        if(reader == null || phoneNumber == null) {
            throw new IllegalArgumentException("Provided argument resolves to null object");
        }
        if(!gdpr) {
            throw new IllegalArgumentException("Readers must agree with the GDPR rules");
        }

        setReader(reader);
        setReaderNumber(new ReaderNumber(readerNumber));
        setPhoneNumber(new PhoneNumber(phoneNumber));
        setBirthDate(new BirthDate(birthDate));
        setGdprConsent(true);
        setPhoto(photoURI);
        setMarketingConsent(marketing);
        setThirdPartySharingConsent(thirdParty);
        setInterestList(interestList);
    }

    public void setPhoneNumber(PhoneNumber number) {
        if(number != null) {
            this.phoneNumber = number;
        }
    }

    public void setReaderNumber(ReaderNumber readerNumber) {
        if(readerNumber != null) {
            this.readerNumber = readerNumber;
        }
    }

    public void setBirthDate(BirthDate date) {
        if(date != null) {
            this.birthDate = date;
        }
    }

    public void applyPatch(final long currentVersion, final UpdateReaderRequest request, String photoURI, List<Genre> interestList) {
        if(currentVersion != this.version) {
            throw new ConflictException("Provided version does not match latest version of this object");
        }

        String birthDate = request.getBirthDate();
        String phoneNumber = request.getPhoneNumber();
        // ... etc ...

        if(birthDate != null) {
            setBirthDate(new BirthDate(birthDate));
        }
        if(phoneNumber != null) {
            setPhoneNumber(new PhoneNumber(phoneNumber));
        }
        // ... etc ...

        if(photoURI != null) {
            try {
                setPhoto(photoURI);
            } catch(InvalidPathException ignored) {}
        }
        // ...
    }

    public void removePhoto(long desiredVersion) {
        if(desiredVersion != this.version) {
            throw new ConflictException("Provided version does not match latest version of this object");
        }
        setPhoto(null);
    }

    // --- GETTERS CORRIGIDOS ---

    /**
     * Retorna a String (para a API e Controladores).
     */
    public String getReaderNumber(){
        return this.readerNumber != null ? this.readerNumber.toString() : null;
    }

    /**
     * Retorna a String (para a API e Controladores).
     */
    public String getPhoneNumber() {
        return this.phoneNumber != null ? this.phoneNumber.toString() : null;
    }

    /**
     * Retorna o Objeto (para o SqlMapper).
     */
    public ReaderNumber getReaderNumberObject() {
        return this.readerNumber;
    }

    /**
     * Retorna o Objeto (para o SqlMapper).
     */
    public PhoneNumber getPhoneNumberObject() {
        return this.phoneNumber;
    }

    // ----------------------------

    public ReaderDetails() {
        // para o Mapper
    }

    public void setPk(Long pk) {
        this.pk = pk;
    }

    public void setVersion(Long version) {
        this.version = version;
    }
}