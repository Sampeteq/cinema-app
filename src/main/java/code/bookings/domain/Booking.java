package code.bookings.domain;

import code.bookings.domain.exceptions.BookingAlreadyCancelledException;
import code.bookings.domain.exceptions.BookingTooLateException;
import code.bookings.domain.exceptions.BookingCancelTooLateException;
import code.films.domain.FilmScreeningSeat;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.time.Clock;

@Entity
@Table(name = "BOOKINGS")
@EqualsAndHashCode(of = "id")
@Getter
@ToString
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private BookingStatus status;

    @OneToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "seat_id")
    private FilmScreeningSeat seat;

    private Long userId;

    protected Booking() {}

    private Booking(BookingStatus status, FilmScreeningSeat seat, Long userId) {
        this.status = status;
        this.seat = seat;
        this.userId = userId;
    }

    public static Booking make(FilmScreeningSeat seat, Clock clock, Long userId) {
        if (seat.timeToScreeningInHours(clock) < 24) {
            throw new BookingTooLateException();
        }
        return new Booking(
                BookingStatus.ACTIVE,
                seat,
                userId
        );
    }

    public void cancel(Clock clock) {
        if (status.equals(BookingStatus.CANCELLED)) {
            throw new BookingAlreadyCancelledException();
        }
        if (seat.timeToScreeningInHours(clock) < 24) {
            throw new BookingCancelTooLateException();
        }
        this.seat.makeFree();
        this.status = BookingStatus.CANCELLED;
    }
}
