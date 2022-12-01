package code.screenings;

import code.screenings.dto.ScreeningTicketStatusDTO;
import code.screenings.dto.ScreeningTicketDTO;
import code.screenings.exception.BookingAlreadyCancelledException;
import code.screenings.exception.ScreeningSeatBusyException;
import code.screenings.exception.TooLateToBookingException;
import code.screenings.exception.TooLateToCancelBookingException;
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

    @Enumerated(EnumType.STRING)
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
        if (this.screening.timeToScreeningStartInHours(clock) < 24) {
            throw new TooLateToBookingException();
        }
        if (!this.seat.isFree()) {
            throw new ScreeningSeatBusyException(this.seat.getId());
        }
        this.status = ScreeningTicketStatus.BOOKED;
        this.seat.busy();
    }

    void cancel(Clock clock) {
        if (this.status.equals(ScreeningTicketStatus.CANCELLED)) {
            throw new BookingAlreadyCancelledException(this.id);
        }
        if (this.screening.timeToScreeningStartInHours(clock) < 24) {
            throw new TooLateToCancelBookingException(this.id);
        }
        this.status = ScreeningTicketStatus.CANCELLED;
        this.seat.free();
    }

    ScreeningTicketDTO toDTO() {
        return ScreeningTicketDTO
                .builder()
                .ticketId(this.id)
                .screeningId(this.screening.getId())
                .seatId(this.seat.getId())
                .firstName(this.firstName)
                .lastName(this.lastName)
                .status(ScreeningTicketStatusDTO.valueOf(this.status.name()))
                .build();
    }
}
