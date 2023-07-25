package code.bookings.domain;

import code.bookings.domain.exceptions.BookingAlreadyExists;
import code.bookings.domain.exceptions.BookingCancelTooLateException;
import code.bookings.domain.exceptions.BookingTooLateException;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "SEATS")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode(of = "id")
@Getter
@ToString(exclude = {"screening"})
public class Seat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int rowNumber;

    private int number;

    private boolean isFree;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Screening screening;

    @OneToOne(mappedBy = "seat", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Booking booking;

    private Seat(int rowNumber, int number, boolean isFree) {
        this.rowNumber = rowNumber;
        this.number = number;
        this.isFree = isFree;
    }

    public static Seat create(int rowNumber, int number) {
        final var isFree = true;
        return new Seat(
                rowNumber,
                number,
                isFree
        );
    }

    public void assignScreening(Screening screening) {
        this.screening = screening;
    }


    public Booking book(LocalDateTime currentDate, Long userId) {
        if (this.booking != null && this.booking.hasStatus(BookingStatus.ACTIVE)) {
            throw new BookingAlreadyExists();
        }
        if (this.screening.timeToScreeningInHours(currentDate) < 1) {
            throw new BookingTooLateException();
        }
        this.isFree = false;
        var newBooking = Booking.make(this.screening, this, userId);
        this.booking = newBooking;
        return newBooking;
    }

    public void cancelBooking(LocalDateTime currentDate) {
        if (this.screening.timeToScreeningInHours(currentDate) < 24) {
            throw new BookingCancelTooLateException();
        }
        this.booking = null;
        this.isFree = true;
    }
}