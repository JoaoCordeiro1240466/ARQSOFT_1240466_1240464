package pt.psoft.g1.psoftg1.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class NotFoundException extends RuntimeException {

    public NotFoundException(Class<?> clazz, String identifier) {
        super(String.format("Entity %s with identifier %s not found", clazz.getSimpleName(), identifier));
    }
}
