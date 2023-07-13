package code.bookings.domain;

import code.bookings.domain.exceptions.BookingCancelTooLateException;
import code.bookings.domain.exceptions.BookingTooLateException;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity(name = "booking_seat")
@Table(name = "bookings_seats")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Seat {

    @Id
    private Long id;

    private int rowNumber;

    private int number;

    @ManyToOne(cascade = CascadeType.ALL)
    private Screening screening;

    @OneToOne(mappedBy = "seat", cascade = CascadeType.ALL)
    private Booking booking;

    private Seat(Long id, int rowNumber, int number) {
        this.id = id;
        this.rowNumber = rowNumber;
        this.number = number;
    }

    public static Seat create(Long id, int rowNumber, int number) {
        return new Seat(
                id,
                rowNumber,
                number
        );
    }

    public void assignScreening(Screening screening) {
        this.screening = screening;
    }

    public void bookSeat(Booking booking, LocalDateTime currentDate) {
        if (this.screening.timeToScreeningInHours(currentDate) < 1) {
            throw new BookingTooLateException();
        }
        this.booking = booking;
    }

    public void cancelBooking(LocalDateTime currentDate) {
        if (this.screening.timeToScreeningInHours(currentDate) < 24) {
            throw new BookingCancelTooLateException();
        }
        this.booking = null;
    }
}
