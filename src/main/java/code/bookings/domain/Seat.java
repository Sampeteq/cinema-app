package code.bookings.domain;

import code.bookings.domain.exceptions.BookingAlreadyExists;
import code.bookings.domain.exceptions.BookingCancelTooLateException;
import code.bookings.domain.exceptions.BookingTooLateException;
import lombok.AccessLevel;
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
@Table(name = "seats")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = "id")
@Getter
@ToString(exclude = {"screening", "booking"})
public class Seat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int rowNumber;

    private int number;

    private boolean isFree;

    @ManyToOne(fetch = FetchType.LAZY)
    private Screening screening;

    @OneToOne(mappedBy = "seat", cascade = CascadeType.ALL)
    private Booking booking;

    private Seat(int rowNumber, int number, boolean isFree) {
        this.rowNumber = rowNumber;
        this.number = number;
        this.isFree = isFree;
    }

    private Seat(Long id, int rowNumber, int number, boolean isFree) {
        this.id = id;
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
    public static Seat create(Long id, int rowNumber, int number) {
        final var isFree = true;
        return new Seat(
                id,
                rowNumber,
                number,
                isFree
        );
    }

    public void assignScreening(Screening screening) {
        this.screening = screening;
    }

    public boolean placedOn(int rowNumber, int seatNumber) {
        return this.rowNumber == rowNumber && this.number == seatNumber;
    }

    public void addBooking(LocalDateTime currentDate, Booking booking) {
        if (screening.timeToScreeningInHours(currentDate) < 1) {
            throw new BookingTooLateException();
        }
        if (this.hasActiveBooking()) {
            throw new BookingAlreadyExists();
        }
        this.booking = booking;
        this.isFree = false;
    }

    public void removeBooking(LocalDateTime currentDate) {
        if (this.screening.timeToScreeningInHours(currentDate) < 24) {
            throw new BookingCancelTooLateException();
        }
        this.booking = null;
        this.isFree = true;
    }

    private boolean hasActiveBooking() {
        return
                this.booking != null &&
                        this.booking.hasSeat(this) &&
                        this.booking.hasStatus(BookingStatus.ACTIVE);
    }
}