package ua.khylko98.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class IdNotFoundException extends RuntimeException {
    public IdNotFoundException(String msg) {
        super(msg);
    }

    public IdNotFoundException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
