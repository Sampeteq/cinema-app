package code.seats.domain;

import code.bookings.domain.exceptions.SeatNotAvailableException;
import code.bookings.domain.exceptions.TooLateToBookingException;
import code.bookings.domain.exceptions.TooLateToCancelBookingException;
import code.screenings.domain.Screening;
import lombok.*;

import javax.persistence.*;
import java.time.Clock;
import java.util.UUID;

@Entity
@Table(name = "SEATS")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@EqualsAndHashCode(of = "id")
@Getter
@ToString
public class Seat {

    @Id
    @Getter
    private UUID id;

    private int rowNumber;

    private int number;

    @Enumerated(EnumType.STRING)
    private SeatStatus status;

    @ManyToOne
    private Screening screening;

    public void book(Clock clock) {
        if (!this.status.equals(SeatStatus.FREE)) {
            throw new SeatNotAvailableException();
        }
        if (screening.timeToScreeningStartInHours(clock) < 24) {
            throw new TooLateToBookingException();
        }
        this.status = SeatStatus.BUSY;
    }

    public void cancelBooking(Clock clock) {
        if (screening.timeToScreeningStartInHours(clock) < 24) {
            throw new TooLateToCancelBookingException();
        }
    }

    public boolean isFree() {
        return status.equals(SeatStatus.FREE);
    }
}