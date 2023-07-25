package code.bookings.application.services;

import code.bookings.domain.BookingDetails;
import code.bookings.domain.ports.BookingRepository;
import code.bookings.domain.ports.SeatRepository;
import code.catalog.application.services.ScreeningDetailsService;
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
    private final ScreeningDetailsService screeningDetailsService;
    private final BookingRepository bookingRepository;

    @Transactional
    public void makeBooking(Long seatId) {
        var seat = seatRepository
                .readById(seatId)
                .orElseThrow(() -> new EntityNotFoundException("Seat"));
        var currentUserId = userCurrentService.getCurrentUserId();
        var booking = seat.book(timeProvider.getCurrentDate(), currentUserId);
        var bookingData = screeningDetailsService.readBookingDataBySeatId(seatId);
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
