package az.code.etaskifyapi.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
public class BearerStringException extends RuntimeException {
    public BearerStringException() {
        super("Couldn't find bearer string, header will be ignored");
    }
}
