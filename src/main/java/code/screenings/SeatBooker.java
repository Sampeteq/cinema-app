package code.screenings;

import code.screenings.dto.SeatBookingRequest;
import code.screenings.dto.SeatBookingView;
import code.screenings.exception.ScreeningNotFoundException;
import code.screenings.exception.SeatBookingNotFoundException;
import code.screenings.exception.SeatNotFoundException;
import lombok.AllArgsConstructor;

import java.time.Clock;
import java.util.UUID;

@AllArgsConstructor
class SeatBooker {

    private final ScreeningRepository screeningRepository;

    private final SeatBookingRepository seatBookingRepository;

    SeatBookingView book(SeatBookingRequest dto, String username, Clock clock) {
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
                .toView();
    }

    void cancel(UUID seatId, Clock clock) {
        var booking = getBookingOrThrow(seatId);
        booking.cancel(clock);
    }

    private Screening getScreeningOrThrow(UUID screeningId) {
        return screeningRepository
                .findById(screeningId)
                .orElseThrow(() -> new ScreeningNotFoundException(screeningId));
    }

    private Seat getSeatOrThrow(Screening screening, UUID seatId) {
        return screening
                .getSeat(seatId)
                .orElseThrow(() -> new SeatNotFoundException(seatId));
    }

    private SeatBooking getBookingOrThrow(UUID ticketId) {
        return seatBookingRepository
                .findById(ticketId)
                .orElseThrow(() -> new SeatBookingNotFoundException(ticketId));
    }
}
