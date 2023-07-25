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

    @OneToOne(cascade = CascadeType.ALL, mappedBy = "booking", fetch = FetchType.LAZY)
    private BookingDetails bookingDetails;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Seat seat;

    private Long userId;

    protected Booking() {}

    private Booking(BookingStatus status, Long userId, Seat seat) {
        this.status = status;
        this.seat = seat;
        this.userId = userId;
    }

    public static Booking make(Seat seat, Long userId) {
        return new Booking(
                BookingStatus.ACTIVE,
                userId,
                seat
        );
    }

    public void cancel(LocalDateTime currentDate) {
        if (status.equals(BookingStatus.CANCELLED)) {
            throw new BookingAlreadyCancelledException();
        }
        this.seat.cancelBooking(currentDate);
        this.status = BookingStatus.CANCELLED;
    }

    public boolean hasStatus(BookingStatus status) {
        return this.status.equals(status);
    }

    public void setBookingDetails(BookingDetails bookingDetails) {
        this.bookingDetails = bookingDetails;
    }
}