package code.screenings;

import code.screenings.dto.ScreeningTicketStatusDto;
import code.screenings.dto.ScreeningTicketDto;
import code.screenings.exception.BookingAlreadyCancelledException;
import code.screenings.exception.ScreeningSeatBusyException;
import code.screenings.exception.TooLateToBookingException;
import code.screenings.exception.TooLateToCancelBookingException;
import lombok.*;

import javax.persistence.*;
import java.time.Clock;
import java.util.UUID;

@Entity
@Table(name = "SCREENINGS_TICKETS")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PACKAGE)
@EqualsAndHashCode(of = "id")
@ToString
class ScreeningTicket {

    @Id
    private UUID id;

    private String firstName;

    private String lastName;

    @Enumerated(EnumType.STRING)
    private ScreeningTicketStatus status;

    @ManyToOne
    private Screening screening;

    @OneToOne
    private ScreeningSeat seat;

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

    ScreeningTicketDto toDTO() {
        return ScreeningTicketDto
                .builder()
                .ticketId(this.id)
                .screeningId(this.screening.getId())
                .seatId(this.seat.getId())
                .firstName(this.firstName)
                .lastName(this.lastName)
                .status(ScreeningTicketStatusDto.valueOf(this.status.name()))
                .build();
    }
}
