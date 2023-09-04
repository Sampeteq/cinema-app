package code.bookings.domain;

import code.bookings.domain.exceptions.BookingAlreadyCancelledException;
import code.bookings.domain.exceptions.BookingAlreadyExists;
import code.bookings.domain.exceptions.BookingCancelTooLateException;
import code.bookings.domain.exceptions.BookingTooLateException;
import code.shared.exceptions.EntityNotFoundException;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "bookings")
@EqualsAndHashCode(of = "id")
@Getter
@ToString
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private BookingStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    private Screening screening;

    @ManyToOne(fetch = FetchType.LAZY)
    private Seat seat;

    private Long userId;

    protected Booking() {}

    private Booking(BookingStatus status, Long userId, Screening screening, Seat seat) {
        this.status = status;
        this.screening = screening;
        this.seat = seat;
        this.userId = userId;
    }

    public static Booking make(
            LocalDateTime currentDate,
            Screening screening,
            int rowNumber,
            int seatNumber,
            Long userId
    ) {
        if (screening.timeToScreeningInHours(currentDate) < 1) {
            throw new BookingTooLateException();
        }
        var foundSeat = screening
                .getSeats()
                .stream()
                .filter(s -> s.placedOn(rowNumber, seatNumber))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("Seat"));
        if (screening.hasActiveBooking(foundSeat)) {
            throw new BookingAlreadyExists();
        }
        var booking = new Booking(
                BookingStatus.ACTIVE,
                userId,
                screening,
                foundSeat
        );
        screening.addBooking(booking);
        foundSeat.makeNotFree();
        return booking;
    }

    public void cancel(LocalDateTime currentDate) {
        if (status.equals(BookingStatus.CANCELLED)) {
            throw new BookingAlreadyCancelledException();
        }
        if (screening.timeToScreeningInHours(currentDate) < 24) {
            throw new BookingCancelTooLateException();
        }
        this.status = BookingStatus.CANCELLED;
        this.seat.makeFree();
    }

    public boolean hasSeat(Seat seat) {
        return this.seat.equals(seat);
    }

    public boolean hasStatus(BookingStatus status) {
        return this.status.equals(status);
    }
}