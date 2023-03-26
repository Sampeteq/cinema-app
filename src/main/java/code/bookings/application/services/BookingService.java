package code.bookings.application.services;

import code.bookings.application.dto.BookingDto;
import code.bookings.domain.Booking;
import code.bookings.domain.BookingRepository;
import code.bookings.infrastructure.exceptions.BookingNotFoundException;
import code.bookings.domain.Seat;
import code.bookings.domain.SeatRepository;
import code.bookings.infrastructure.exceptions.SeatNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Clock;
import java.util.UUID;

@Component
@AllArgsConstructor
public class BookingService {

    private final SeatRepository seatRepository;

    private final BookingRepository bookingRepository;

    private final Clock clock;

    public BookingDto make(UUID seatId, String username) {
        var seat = getSeatOrThrow(seatId);
        var booking = Booking.make(seat, username, clock);
        return bookingRepository
                .save(booking)
                .toDto();
    }

    public void cancel(UUID bookingId) {
        var booking = getBookingOrThrow(bookingId);
        booking.cancel(clock);
    }

    private Seat getSeatOrThrow(UUID seatId) {
        return seatRepository
                .findById(seatId)
                .orElseThrow(SeatNotFoundException::new);
    }

    private Booking getBookingOrThrow(UUID bookingId) {
        return bookingRepository
                .findById(bookingId)
                .orElseThrow(BookingNotFoundException::new);
    }
}
