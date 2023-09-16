package com.cinema.bookings.domain;

import com.cinema.bookings.domain.exceptions.BookingAlreadyExists;
import com.cinema.bookings.domain.exceptions.BookingCancelTooLateException;
import com.cinema.bookings.domain.exceptions.BookingTooLateException;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

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

    public boolean placedOn(int rowNumber, int seatNumber) {
        return this.rowNumber == rowNumber && this.number == seatNumber;
    }

    public void addBooking(LocalDateTime currentDate, Booking booking) {
        if (screening.timeToScreeningInHours(currentDate) < 1) {
            throw new BookingTooLateException();
        }
        if (this.booking != null) {
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
}