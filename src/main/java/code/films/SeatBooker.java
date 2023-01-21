package code.films;

import code.films.dto.BookSeatDto;
import code.films.dto.SeatBookingDto;
import code.films.exception.ScreeningNotFoundException;
import code.films.exception.SeatBookingNotFoundException;
import code.films.exception.SeatNotFoundException;
import lombok.AllArgsConstructor;

import java.time.Clock;
import java.util.UUID;

@AllArgsConstructor
class SeatBooker {

    private final ScreeningRepository screeningRepository;

    private final SeatBookingRepository seatBookingRepository;

    SeatBookingDto book(BookSeatDto dto, String username, Clock clock) {
        var screening = getScreeningOrThrow(dto.screeningId());
        var seat = getSeatOrThrow(screening, dto.seatId());
        seat.book(clock);
        var booking = new SeatBooking(
                UUID.randomUUID(),
                dto.firstName(),
                dto.lastName(),
                seat,
                username
        );
        return seatBookingRepository
                .save(booking)
                .toDto();
    }

    void cancel(UUID seatId, Clock clock) {
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

    private SeatBooking getBookingOrThrow(UUID bookingId) {
        return seatBookingRepository
                .findById(bookingId)
                .orElseThrow(SeatBookingNotFoundException::new);
    }
}
