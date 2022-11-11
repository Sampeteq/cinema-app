package code.screenings;

import code.screenings.dto.TicketDTO;
import code.screenings.exception.BookingAlreadyCancelledException;
import code.screenings.exception.ScreeningNoFreeSeatsException;
import code.screenings.exception.TooLateToBookingException;
import code.screenings.exception.TooLateToCancelBookingException;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.time.Clock;
import java.util.UUID;

@Entity
@Table(name = "SCREENINGS_TICKETS")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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

    @ManyToOne
    private Screening screening;

    ScreeningTicket(String firstName, String lastName, Screening screening) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.screening = screening;
    }

    void book(Clock clock) {
        if (this.screening.differenceBetweenCurrentDateAndScreeningOneInHours(clock) < 24) {
            throw new TooLateToBookingException();
        }
        if (!this.screening.hasFreeSeats()) {
            throw new ScreeningNoFreeSeatsException(this.screening.getId());
        }
        this.status = ScreeningTicketStatus.BOOKED;
        this.screening.decreaseFreeSeatsByOne();
    }

    void cancel(Clock clock) {
        if (this.status.equals(ScreeningTicketStatus.CANCELLED)) {
            throw new BookingAlreadyCancelledException(this.id);
        }
        if (this.screening.differenceBetweenCurrentDateAndScreeningOneInHours(clock) < 24) {
            throw new TooLateToCancelBookingException(this.id);
        }
        this.status = ScreeningTicketStatus.CANCELLED;
        this.screening.increaseFreeSeatsByOne();
    }

    TicketDTO toDTO() {
        return TicketDTO
                .builder()
                .ticketId(this.id)
                .firstName(this.firstName)
                .lastName(this.lastName)
                .prize(this.prize)
                .status(this.status)
                .screeningId(this.screening.getId())
                .build();
    }
}
