package code.bookings.domain;

import code.bookings.domain.exceptions.BookingAlreadyCancelledException;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.time.LocalDateTime;

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

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Seat seat;

    @OneToOne(cascade = CascadeType.ALL, mappedBy = "booking", fetch = FetchType.LAZY)
    private BookingDetails bookingDetails;

    private Long userId;

    protected Booking() {}

    private Booking(BookingStatus status, Seat seat, Long userId) {
        this.status = status;
        this.seat = seat;
        this.userId = userId;
    }

    public static Booking make(Seat seat, LocalDateTime currentDate, Long userId) {
        var booking = new Booking(
                BookingStatus.ACTIVE,
                seat,
                userId
        );
        seat.bookSeat(booking, currentDate);
        return booking;
    }

    public void setBookingDetails(BookingDetails bookingDetails) {
        this.bookingDetails = bookingDetails;
    }

    public void cancel(LocalDateTime currentDate) {
        if (status.equals(BookingStatus.CANCELLED)) {
            throw new BookingAlreadyCancelledException();
        }
        this.seat.cancelBooking(currentDate);
        this.status = BookingStatus.CANCELLED;
    }
}
