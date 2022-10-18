package code.screening.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ScreeningException extends RuntimeException {

    private final String message;

    private final ScreeningExceptionType type;

    public boolean isType(ScreeningExceptionType type) {
        return this.type.equals(type);
    }

    public static ScreeningException screeningNotFound(Long screeningId) {
        return new ScreeningException("Screening not found: " + screeningId, ScreeningExceptionType.NOT_FOUND);
    }
}
