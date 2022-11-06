package code.bookings;

import code.bookings.dto.TicketDTO;
import code.bookings.exception.BookingAlreadyCancelledException;
import code.bookings.exception.TooLateToBookingException;
import code.bookings.exception.TooLateToCancelBookingException;
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
@EqualsAndHashCode(of = "uuid")
@ToString
class ScreeningTicket {

    private static final BigDecimal TICKET_BASIC_PRIZE = new BigDecimal("10.0");

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private UUID uuid = UUID.randomUUID();

    private String firstName;

    private String lastName;

    private BigDecimal prize = TICKET_BASIC_PRIZE;

    private ScreeningTicketStatus status;

    @Getter
    private Long screeningId;

    protected ScreeningTicket() {
    }

    ScreeningTicket(String firstName, String lastName, Long screeningId) {
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
        this.status = ScreeningTicketStatus.RESERVED;
    }

    void cancel(LocalDateTime screeningDate, Clock clock) {
        if (this.status.equals(ScreeningTicketStatus.CANCELLED)) {
            throw new BookingAlreadyCancelledException(this.uuid);
        }
        var differenceBetweenCurrentDateAndScreeningOneInHours = Duration
                .between(LocalDateTime.now(clock), screeningDate)
                .abs()
                .toHours();
        if (differenceBetweenCurrentDateAndScreeningOneInHours < 24) {
            throw new TooLateToCancelBookingException(this.uuid);
        }
        this.status = ScreeningTicketStatus.CANCELLED;
    }

    TicketDTO toDTO() {
        return TicketDTO
                .builder()
                .ticketUuid(this.uuid)
                .firstName(this.firstName)
                .lastName(this.lastName)
                .prize(this.prize)
                .status(this.status)
                .screeningId(this.screeningId)
                .build();
    }
}
