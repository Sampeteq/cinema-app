package com.cinema.bookings_views.application.services;

import com.cinema.bookings.domain.BookingStatus;
import com.cinema.bookings.domain.events.BookingCancelledEvent;
import com.cinema.bookings_views.domain.ports.BookingViewRepository;
import com.cinema.shared.exceptions.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Service
@RequiredArgsConstructor
class BookingCancelledHandlerService {

    private final BookingViewRepository bookingViewRepository;

    @Transactional
    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void handle(BookingCancelledEvent event) {
        bookingViewRepository
                .readById(event.bookingId())
                .orElseThrow(() -> new EntityNotFoundException("Booking view"))
                .changeStatus(BookingStatus.CANCELLED);
    }
}
