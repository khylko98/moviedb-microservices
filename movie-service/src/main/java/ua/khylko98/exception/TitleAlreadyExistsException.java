package ua.khylko98.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class TitleAlreadyExistsException extends RuntimeException {
    public TitleAlreadyExistsException(String message) {
        super(message);
    }

    public TitleAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }
}
