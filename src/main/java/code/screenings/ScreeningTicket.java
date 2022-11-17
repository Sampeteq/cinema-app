package code.screenings;

import code.screenings.dto.TicketDTO;
import code.screenings.exception.*;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.time.Clock;
import java.util.UUID;

@Entity
@Table(name = "SCREENINGS_TICKETS")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = "id")
@ToString
class ScreeningTicket {

    @Id
    private UUID id = UUID.randomUUID();

    private String firstName;

    private String lastName;

    private ScreeningTicketStatus status;

    @ManyToOne
    private Screening screening;

    @OneToOne
    private ScreeningSeat seat;

    ScreeningTicket(String firstName, String lastName, Screening screening, ScreeningSeat seat) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.screening = screening;
        this.seat = seat;
    }

    void book(Clock clock) {
        if (this.screening.differenceBetweenCurrentDateAndScreeningOneInHours(clock) < 24) {
            throw new TooLateToBookingException();
        }
        if (!this.screening.hasFreeSeats()) {
            throw new ScreeningNoFreeSeatsException(this.screening.getId());
        }
        if (!this.seat.isFree()) {
            throw new ScreeningSeatBusyException(this.seat.getId());
        }
        this.status = ScreeningTicketStatus.BOOKED;
        this.seat.busy();
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
        this.seat.free();
        this.screening.increaseFreeSeatsByOne();
    }

    TicketDTO toDTO() {
        return TicketDTO
                .builder()
                .ticketId(this.id)
                .screeningId(this.screening.getId())
                .seatId(this.seat.getId())
                .firstName(this.firstName)
                .lastName(this.lastName)
                .status(this.status)
                .build();
    }
}
