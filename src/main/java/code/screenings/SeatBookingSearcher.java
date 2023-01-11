package code.screenings;

import code.screenings.dto.SeatBookingView;
import code.screenings.exception.SeatBookingNotFoundException;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.UUID;

@AllArgsConstructor
class SeatBookingSearcher {

    private final SeatBookingRepository seatBookingRepository;

    SeatBookingView searchSeatBooking(UUID bookingId, String username) {
        return getBookingOrThrow(bookingId, username).toView();
    }

    private SeatBooking getBookingOrThrow(UUID ticketId, String username) {
        return seatBookingRepository
                .findByIdAndUsername(ticketId, username)
                .orElseThrow(() -> new SeatBookingNotFoundException(ticketId));
    }

    public List<SeatBookingView> searchAllByUsername(String username) {
        return seatBookingRepository
                .findByUsername(username)
                .stream()
                .map(SeatBooking::toView)
                .toList();
    }
}
