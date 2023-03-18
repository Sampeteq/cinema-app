package code.bookings.application.services;

import code.bookings.application.dto.BookingDto;
import code.bookings.application.events.BookingCancelledEvent;
import code.bookings.application.events.SeatBookedEvent;
import code.bookings.domain.Booking;
import code.bookings.domain.BookingRepository;
import code.bookings.domain.BookingScreening;
import code.bookings.domain.BookingSeat;
import code.bookings.infrastructure.exceptions.BookingNotFoundException;
import code.films.application.services.ScreeningSearchService;
import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@AllArgsConstructor
public class BookingService {

    private final ScreeningSearchService screeningSearchService;

    private final BookingRepository bookingRepository;

    private final ApplicationEventPublisher applicationEventPublisher;

    public BookingDto make(UUID seatId, String username) {
        var seatDetails = screeningSearchService.searchScreeningDetails(seatId);
        var bookingScreening = BookingScreening
                .builder()
                .id(seatDetails.screeningId())
                .timeToScreeningInHours(seatDetails.timeToScreeningInHours())
                .build();
        var bookingSeat = BookingSeat
                .builder()
                .id(seatId)
                .isAvailable(seatDetails.isSeatAvailable())
                .screening(bookingScreening)
                .build();
        var booking = Booking.make(bookingSeat, username);
        applicationEventPublisher.publishEvent(
                new SeatBookedEvent(seatId)
        );
        return bookingRepository
                .save(booking)
                .toDto();
    }

    public void cancel(UUID bookingId) {
        var booking = getBookingOrThrow(bookingId);
        booking.cancel();
        applicationEventPublisher.publishEvent(
                new BookingCancelledEvent(booking.getSeat().getId())
        );
    }

    private Booking getBookingOrThrow(UUID bookingId) {
        return bookingRepository
                .findById(bookingId)
                .orElseThrow(BookingNotFoundException::new);
    }
}
