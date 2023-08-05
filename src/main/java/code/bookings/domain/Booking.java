package code.bookings.domain;

import code.bookings.domain.exceptions.BookingAlreadyCancelledException;
import code.bookings.domain.exceptions.BookingAlreadyExists;
import code.bookings.domain.exceptions.BookingCancelTooLateException;
import code.bookings.domain.exceptions.BookingTooLateException;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "BOOKINGS")
@EqualsAndHashCode(of = "id")
@Getter
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private BookingStatus status;

    @OneToOne(cascade = CascadeType.ALL, mappedBy = "booking", fetch = FetchType.LAZY)
    private BookingDetails bookingDetails;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Seat seat;

    private Long userId;

    protected Booking() {}

    private Booking(BookingStatus status, Long userId, Seat seat) {
        this.status = status;
        this.seat = seat;
        this.userId = userId;
    }

    public static Booking make(Seat seat, LocalDateTime currentDate, Long userId) {
        var screening = seat.getScreening();
        if (screening.timeToScreeningInHours(currentDate) < 1) {
            throw new BookingTooLateException();
        }
        if (seat.hasActiveBooking()) {
            throw new BookingAlreadyExists();
        }
        var booking = new Booking(
                BookingStatus.ACTIVE,
                userId,
                seat
        );
        seat.addBooking(booking);
        seat.makeNotFree();
        return booking;
    }

    public void cancel(LocalDateTime currentDate) {
        if (status.equals(BookingStatus.CANCELLED)) {
            throw new BookingAlreadyCancelledException();
        }
        var screening = this.seat.getScreening();
        if (screening.timeToScreeningInHours(currentDate) < 24) {
            throw new BookingCancelTooLateException();
        }
        this.status = BookingStatus.CANCELLED;
        this.seat.makeFree();
    }

    public boolean hasStatus(BookingStatus status) {
        return this.status.equals(status);
    }

    public void setBookingDetails(BookingDetails bookingDetails) {
        this.bookingDetails = bookingDetails;
    }

    @Override
    public String toString() {
        return "Booking{" +
                "id=" + id +
                ", status=" + status +
                ", seat=" + seat.getId() +
                ", userId=" + userId +
                '}';
    }
}