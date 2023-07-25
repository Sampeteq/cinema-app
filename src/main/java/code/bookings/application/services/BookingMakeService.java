package code.bookings.application.services;

import code.bookings.domain.BookingDetails;
import code.bookings.domain.ports.BookingRepository;
import code.bookings.infrastructure.db.SeatRepository;
import code.catalog.application.services.SeatDataService;
import code.shared.EntityNotFoundException;
import code.shared.TimeProvider;
import code.user.application.services.UserCurrentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookingMakeService {

    private final SeatRepository seatRepository;
    private final UserCurrentService userCurrentService;
    private final TimeProvider timeProvider;
    private final SeatDataService seatDataService;
    private final BookingRepository bookingRepository;

    @Transactional
    public void makeBooking(Long seatId) {
        var seat = seatRepository
                .findById(seatId)
                .orElseThrow(() -> new EntityNotFoundException("Seat"));
        var currentUserId = userCurrentService.getCurrentUserId();
        var booking = seat.book(timeProvider.getCurrentDate(), currentUserId);
        var bookingData = seatDataService.readBookingDataBySeatId(seatId);
        var bookingDetails = BookingDetails.create(
                bookingData.getFilmTitle(),
                bookingData.getRoomCustomId(),
                booking
        );
        booking.setBookingDetails(bookingDetails);
        var addedBooking = bookingRepository.add(booking);
        log.info("Added a booking:{}", addedBooking);
    }
}
