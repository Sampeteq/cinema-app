package code.screenings;

import code.screenings.dto.SeatBookingDto;
import code.screenings.exception.SeatBookingNotFoundException;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.UUID;

@AllArgsConstructor
class SeatBookingSearcher {

    private final SeatBookingRepository seatBookingRepository;

    SeatBookingDto searchSeatBooking(UUID bookingId, String username) {
        return getBookingOrThrow(bookingId, username).toDto();
    }

    private SeatBooking getBookingOrThrow(UUID ticketId, String username) {
        return seatBookingRepository
                .findByIdAndUsername(ticketId, username)
                .orElseThrow(SeatBookingNotFoundException::new);
    }

    public List<SeatBookingDto> searchAllByUsername(String username) {
        return seatBookingRepository
                .findByUsername(username)
                .stream()
                .map(SeatBooking::toDto)
                .toList();
    }
}
