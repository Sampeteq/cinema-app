package code.bookings.domain;

import code.bookings.domain.exceptions.BookingAlreadyCancelledException;
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
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "bookings")
@EqualsAndHashCode(of = "id")
@Getter
@ToString(exclude = "seat")
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private BookingStatus status;

    @OneToOne(fetch = FetchType.LAZY)
    private Seat seat;

    private Long userId;

    protected Booking() {}

    private Booking(BookingStatus status, Long userId, Seat seat) {
        this.status = status;
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
        var foundSeat = screening
                .getSeats()
                .stream()
                .filter(s -> s.placedOn(rowNumber, seatNumber))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("Seat"));
        var booking = new Booking(
                BookingStatus.ACTIVE,
                userId,
                foundSeat
        );
        foundSeat.addBooking(currentDate, booking);
        return booking;
    }

    public void cancel(LocalDateTime currentDate) {
        if (status.equals(BookingStatus.CANCELLED)) {
            throw new BookingAlreadyCancelledException();
        }
        this.seat.removeBooking(currentDate);
        this.status = BookingStatus.CANCELLED;
    }
}