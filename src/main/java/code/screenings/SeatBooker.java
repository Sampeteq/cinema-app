package code.screenings;

import code.screenings.dto.BookSeatDto;
import code.screenings.dto.SeatBookingDto;
import code.screenings.exception.ScreeningNotFoundException;
import code.screenings.exception.SeatNotFoundException;
import code.screenings.exception.SeatBookingNotFoundException;
import lombok.AllArgsConstructor;

import java.time.Clock;
import java.util.UUID;

@AllArgsConstructor
class SeatBooker {

    private final ScreeningRepository screeningRepository;

    private final SeatBookingRepository seatBookingRepository;

    SeatBookingDto book(BookSeatDto dto, Clock clock) {
        var screening = getScreeningOrThrow(dto.screeningId());
        var seat = screening
                .getSeat(dto.seatId())
                .orElseThrow(() -> new SeatNotFoundException(dto.seatId()));
        seat.book(clock);
        var booking = SeatBooking
                .builder()
                .id(UUID.randomUUID())
                .firstName(dto.firstName())
                .lastName(dto.lastName())
                .seat(seat)
                .build();
        return seatBookingRepository
                .add(booking)
                .toDTO();
    }

    void cancel(UUID seatId, Clock clock) {
        var booking = getBookingOrThrow(seatId);
        booking.cancel(clock);
    }

    private Screening getScreeningOrThrow(UUID screeningId) {
        return screeningRepository
                .getById(screeningId)
                .orElseThrow(() -> new ScreeningNotFoundException(screeningId));
    }

    private SeatBooking getBookingOrThrow(UUID ticketId) {
        return seatBookingRepository
                .getById(ticketId)
                .orElseThrow(() -> new SeatBookingNotFoundException(ticketId));
    }
}
