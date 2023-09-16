package com.cinema.bookings.domain;

import com.cinema.bookings.domain.exceptions.BookingAlreadyCancelledException;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;

@Entity
@Table(name = "bookings")
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
        var foundSeat = screening.findSeat(rowNumber, seatNumber);
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