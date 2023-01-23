package code.bookings;

import code.bookings.dto.SeatDto;
import code.bookings.exception.BookingException;
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

    boolean isFree() {
        return this.status.equals(SeatStatus.FREE);
    }

    void book(Clock clock) {
        if (this.screening.timeToScreeningStartInHours(clock) < 24) {
            throw new BookingException("Too late to booking");
        }
        if (this.status.equals(SeatStatus.BUSY)) {
            throw new BookingException("Seat busy");
        }
        this.status = SeatStatus.BUSY;
    }

    void cancelBooking(Clock clock) {
        if (this.screening.timeToScreeningStartInHours(clock) < 24) {
            throw new BookingException("Too late to cancel booking");
        }
        this.status = SeatStatus.FREE;
    }

    SeatDto toDto() {
        return new SeatDto(
                this.id,
                this.rowNumber,
                this.number,
                this.status.name()
        );
    }
}