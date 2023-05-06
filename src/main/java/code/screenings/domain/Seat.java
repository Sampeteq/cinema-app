package code.screenings.domain;

import code.bookings.domain.exceptions.SeatNotAvailableException;
import code.bookings.domain.exceptions.TooLateToBookingException;
import code.bookings.domain.exceptions.TooLateToCancelBookingException;
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

    public static Seat of(int rowNumber, int number, Screening screening) {
        return new Seat(
                UUID.randomUUID(),
                rowNumber,
                number,
                SeatStatus.FREE,
                screening
        );
    }

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