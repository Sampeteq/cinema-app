package code.screenings;

import code.screenings.dto.TicketDTO;
import code.screenings.exception.BookingAlreadyCancelledException;
import code.screenings.exception.TooLateToBookingException;
import code.screenings.exception.TooLateToCancelBookingException;
import code.screenings.exception.ScreeningNoFreeSeatsException;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.Clock;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "SCREENINGS_TICKETS")
@EqualsAndHashCode(of = "id")
@ToString
class ScreeningTicket {

    private static final BigDecimal TICKET_BASIC_PRIZE = new BigDecimal("10.0");

    @Id
    private UUID id = UUID.randomUUID();

    private String firstName;

    private String lastName;

    private BigDecimal prize = TICKET_BASIC_PRIZE;

    private ScreeningTicketStatus status;

    @Getter
    private UUID screeningId;

    protected ScreeningTicket() {
    }

    ScreeningTicket(String firstName, String lastName, UUID screeningId) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.screeningId = screeningId;
    }

    void book(LocalDateTime screeningDate, int screeningFreeSeats, Clock clock) {
        var differenceBetweenCurrentDateAndScreeningOneInHours = Duration
                .between(LocalDateTime.now(clock), screeningDate)
                .abs()
                .toHours();
        if (differenceBetweenCurrentDateAndScreeningOneInHours < 24) {
            throw new TooLateToBookingException();
        }
        if (screeningFreeSeats == 0) {
            throw new ScreeningNoFreeSeatsException(this.screeningId);
        }
        this.status = ScreeningTicketStatus.BOOKED;
    }

    void cancel(LocalDateTime screeningDate, Clock clock) {
        if (this.status.equals(ScreeningTicketStatus.CANCELLED)) {
            throw new BookingAlreadyCancelledException(this.id);
        }
        var differenceBetweenCurrentDateAndScreeningOneInHours = Duration
                .between(LocalDateTime.now(clock), screeningDate)
                .abs()
                .toHours();
        if (differenceBetweenCurrentDateAndScreeningOneInHours < 24) {
            throw new TooLateToCancelBookingException(this.id);
        }
        this.status = ScreeningTicketStatus.CANCELLED;
    }

    TicketDTO toDTO() {
        return TicketDTO
                .builder()
                .ticketId(this.id)
                .firstName(this.firstName)
                .lastName(this.lastName)
                .prize(this.prize)
                .status(this.status)
                .screeningId(this.screeningId)
                .build();
    }
}
