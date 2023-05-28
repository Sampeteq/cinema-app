package code.screenings.domain;

import code.bookings.domain.exceptions.SeatNotAvailableException;
import code.bookings.domain.exceptions.TooLateToBookingException;
import code.bookings.domain.exceptions.TooLateToCancelBookingException;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.time.Clock;

@Entity
@Table(name = "SEATS")
@EqualsAndHashCode(of = "id")
@Getter
@ToString(exclude = {"screening"})
public class Seat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    private Long id;

    private int rowNumber;

    private int number;

    @Enumerated(EnumType.STRING)
    private SeatStatus status;

    @ManyToOne
    private Screening screening;

    protected Seat() {
    }

    private Seat(int rowNumber, int number, Screening screening) {
        this.rowNumber = rowNumber;
        this.number = number;
        this.status = SeatStatus.FREE;
        this.screening = screening;
    }

    public static Seat of(int rowNumber, int number, Screening screening) {
        return new Seat(
                rowNumber,
                number,
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