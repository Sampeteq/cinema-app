package code.bookings.application.services;

import code.bookings.domain.Booking;
import code.bookings.domain.BookingDetails;
import code.bookings.domain.Screening;
import code.bookings.domain.Seat;
import code.bookings.domain.events.BookingMadeEvent;
import code.bookings.domain.exceptions.BookingAlreadyExists;
import code.bookings.domain.ports.BookingRepository;
import code.bookings.infrastructure.db.BookingDetailsRepository;
import code.catalog.application.services.SeatDataService;
import code.shared.TimeProvider;
import code.user.application.services.UserCurrentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookingMakeService {

    private final BookingRepository bookingRepository;
    private final SeatDataService seatDataService;
    private final UserCurrentService userCurrentService;
    private final TimeProvider timeProvider;
    private final BookingDetailsRepository bookingDetailsRepository;
    private final ApplicationEventPublisher applicationEventPublisher;

    @Transactional
    public void makeBooking(Long seatId) {
        if (bookingRepository.existsBySeatId(seatId)) {
            log.error("Booking already exists");
            throw new BookingAlreadyExists();
        }
        var bookingData = seatDataService.readBookingDataBySeatId(seatId);
        var screening = Screening.create(
               bookingData.getScreeningId(),
               bookingData.getScreeningDate()
       );
        var seat = Seat.create(
                seatId,
                bookingData.getSeatRowNumber(),
                bookingData.getSeatNumber(),
                screening
        );
        var currentUserId = userCurrentService.getCurrentUserId();
        var booking = Booking.make(seat, timeProvider.getCurrentDate(), currentUserId);
        var addedBooking = bookingRepository.add(booking);
        var bookingDetails = BookingDetails.create(
                bookingData.getFilmTitle(),
                bookingData.getScreeningDate(),
                bookingData.getRoomCustomId(),
                bookingData.getSeatRowNumber(),
                bookingData.getSeatNumber(),
                booking
        );
        bookingDetailsRepository.save(bookingDetails);
        log.info("Added a booking:{}", addedBooking);
        var seatBookedEvent = new BookingMadeEvent(seatId);
        applicationEventPublisher.publishEvent(seatBookedEvent);
    }
}
