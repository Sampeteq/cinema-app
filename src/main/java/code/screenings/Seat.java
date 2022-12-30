package code.screenings;

import code.screenings.dto.SeatDto;
import code.screenings.exception.BookingAlreadyCancelledException;
import code.screenings.exception.SeatBusyException;
import code.screenings.exception.TooLateToSeatBookingException;
import code.screenings.exception.TooLateToCancelSeatBookingException;
import lombok.*;

import javax.persistence.*;
import java.time.Clock;
import java.util.UUID;

@Entity
@Table(name = "SEATS")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@EqualsAndHashCode(of = "id")
@ToString
class Seat {

    @Id
    @Getter
    private UUID id;

    private int rowNumber;

    private int number;

    @Enumerated(EnumType.STRING)
    private SeatStatus status;

    @ManyToOne
    private Screening screening;

    boolean hasNoAssignedScreening() {
        return this.screening == null;
    }

    void assignScreening(Screening screening) {
        this.screening = screening;
    }

    boolean isFree() {
        return this.status.equals(SeatStatus.FREE);
    }

    void book(Clock clock) {
        if (this.screening.timeToScreeningStartInHours(clock) < 24) {
            throw new TooLateToSeatBookingException();
        }
        if (this.status.equals(SeatStatus.BUSY)) {
            throw new SeatBusyException(this.screening.getId());
        }
        this.status = SeatStatus.BUSY;
    }

    void cancelBooking(Clock clock) {
        if (this.status.equals(SeatStatus.FREE)) {
            throw new BookingAlreadyCancelledException(this.id);
        }
        if (this.screening.timeToScreeningStartInHours(clock) < 24) {
            throw new TooLateToCancelSeatBookingException(this.id);
        }
        this.status = SeatStatus.FREE;
    }

    SeatDto toDTO() {
        return new SeatDto(
                this.id,
                this.rowNumber,
                this.number,
                this.status.name(),
                this.screening.getId()
        );
    }

    public Seat copyWithNewScreening(Screening newScreening) {
        return new Seat(
                UUID.randomUUID(),
                this.rowNumber,
                this.number,
                this.status,
                newScreening
        );
    }
}