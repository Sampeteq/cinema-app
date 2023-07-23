package code.bookings.domain;

import code.bookings.domain.exceptions.BookingAlreadyCancelledException;
import code.bookings.domain.exceptions.BookingCancelTooLateException;
import code.bookings.domain.exceptions.BookingTooLateException;
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
import javax.persistence.ManyToOne;
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

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Screening screening;

    private Long seatId;

    private Long userId;

    protected Booking() {}

    private Booking(BookingStatus status, Long userId, Screening screening, Long seatId) {
        this.status = status;
        this.screening = screening;
        this.seatId = seatId;
        this.userId = userId;
    }

    public static Booking make(Screening screening, Long seatId, Long userId, LocalDateTime currentDate) {
        if (screening.timeToScreeningInHours(currentDate) < 1) {
            throw new BookingTooLateException();
        }
        return new Booking(
                BookingStatus.ACTIVE,
                userId,
                screening,
                seatId
        );
    }

    public void cancel(LocalDateTime currentDate) {
        if (status.equals(BookingStatus.CANCELLED)) {
            throw new BookingAlreadyCancelledException();
        }
        if (this.screening.timeToScreeningInHours(currentDate) < 24) {
            throw new BookingCancelTooLateException();
        }
        this.status = BookingStatus.CANCELLED;
    }

    public void setBookingDetails(BookingDetails bookingDetails) {
        this.bookingDetails = bookingDetails;
    }
}