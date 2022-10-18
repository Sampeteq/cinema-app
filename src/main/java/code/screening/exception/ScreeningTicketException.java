package code.screening.exception;

import java.util.UUID;

public class ScreeningTicketException extends ScreeningException {

    private ScreeningTicketException(String message, ScreeningExceptionType type) {
        super(message, type);
    }

    public static ScreeningTicketException notFound(UUID ticketUuid) {
        return new ScreeningTicketException(
                "Screening ticket not found: " + ticketUuid,
                ScreeningExceptionType.NOT_FOUND
        );
    }

    public static ScreeningTicketException tooLateToBook() {
        return new ScreeningTicketException(
                "Too late to book screening ticket",
                ScreeningExceptionType.DATE
        );
    }

    public static ScreeningTicketException tooLateToCancel() {
        return new ScreeningTicketException(
                "Too late to cancel screening ticket",
                ScreeningExceptionType.DATE
        );
    }

    public static ScreeningTicketException alreadyCancelled(UUID ticketUuid) {
        return new ScreeningTicketException(
                "Screening ticket already cancelled:" + ticketUuid,
                ScreeningExceptionType.STATUS
        );
    }
}
