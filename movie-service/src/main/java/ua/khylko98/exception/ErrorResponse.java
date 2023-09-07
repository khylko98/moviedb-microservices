package ua.khylko98.exception;

import java.time.LocalDateTime;

public record ErrorResponse(
        String path,
        String message,
        int statusCode,
        LocalDateTime dateTime
) {}
