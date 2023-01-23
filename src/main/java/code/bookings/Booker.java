package code.bookings;

import code.bookings.dto.BookDto;
import code.bookings.dto.BookingDto;
import code.bookings.exception.ScreeningNotFoundException;
import code.bookings.exception.BookingNotFoundException;
import code.bookings.exception.SeatNotFoundException;
import lombok.AllArgsConstructor;

import java.time.Clock;
import java.util.UUID;

@AllArgsConstructor
class Booker {

    private final ScreeningRepository screeningRepository;

    private final BookingRepository bookingRepository;

    BookingDto bookSeat(BookDto dto, String username, Clock clock) {
        var screening = getScreeningOrThrow(dto.screeningId());
        var seat = getSeatOrThrow(screening, dto.seatId());
        seat.book(clock);
        var booking = new Booking(
                UUID.randomUUID(),
                dto.firstName(),
                dto.lastName(),
                BookingStatus.ACTIVE,
                seat,
                username
        );
        return bookingRepository
                .save(booking)
                .toDto();
    }

    void cancelSeat(UUID seatId, Clock clock) {
        var booking = getBookingOrThrow(seatId);
        booking.cancel(clock);
    }

    private Screening getScreeningOrThrow(UUID screeningId) {
        return screeningRepository
                .findById(screeningId)
                .orElseThrow(ScreeningNotFoundException::new);
    }

    private Seat getSeatOrThrow(Screening screening, UUID seatId) {
        return screening
                .getSeat(seatId)
                .orElseThrow(SeatNotFoundException::new);
    }

    private Booking getBookingOrThrow(UUID bookingId) {
        return bookingRepository
                .findById(bookingId)
                .orElseThrow(BookingNotFoundException::new);
    }
}
