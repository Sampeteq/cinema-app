package com.cinema.bookings.application.services;

import com.cinema.bookings.domain.events.BookingCancelledEvent;
import com.cinema.bookings.domain.ports.BookingRepository;
import com.cinema.shared.events.EventPublisher;
import com.cinema.shared.exceptions.EntityNotFoundException;
import com.cinema.shared.time.TimeProvider;
import com.cinema.user.application.services.UserFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
class BookingCancelService {

    private final UserFacade userFacade;
    private final BookingRepository bookingRepository;
    private final TimeProvider timeProvider;
    private final EventPublisher eventPublisher;

    @Transactional
    public void cancelBooking(Long bookingId) {
        var currentUserId = userFacade.readCurrentUserId();
        var booking = bookingRepository
                .readByIdAndUserId(bookingId, currentUserId)
                .orElseThrow(() -> new EntityNotFoundException("Booking"));
        booking.cancel(timeProvider.getCurrentDate());
        var bookingCancelledEvent = new BookingCancelledEvent(bookingId);
        eventPublisher.publish(bookingCancelledEvent);
    }
}
