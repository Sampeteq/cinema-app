package code.screenings;

import code.screenings.dto.SeatBookingDto;
import code.screenings.exception.SeatBookingNotFoundException;
import lombok.AllArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
class SeatBookingSearcher {

    private final SeatBookingRepository seatBookingRepository;

    SeatBookingDto searchSeatBooking(UUID bookingId, String username) {
        return getBookingOrThrow(bookingId, username).toDTO();
    }

    private SeatBooking getBookingOrThrow(UUID ticketId, String username) {
        return seatBookingRepository
                .findByIdAndUsername(ticketId, username)
                .orElseThrow(() -> new SeatBookingNotFoundException(ticketId));
    }
}
