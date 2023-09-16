package com.cinema.bookings.application.services;

import com.cinema.bookings.application.dto.BookingMakeDto;
import com.cinema.bookings.domain.Booking;
import com.cinema.bookings.domain.events.BookingMadeEvent;
import com.cinema.bookings.domain.ports.BookingRepository;
import com.cinema.bookings.domain.ports.ScreeningRepository;
import com.cinema.shared.events.EventPublisher;
import com.cinema.shared.exceptions.EntityNotFoundException;
import com.cinema.shared.time.TimeProvider;
import com.cinema.user.application.services.UserFacade;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
class BookingMakeService {

    private final ScreeningRepository screeningRepository;
    private final UserFacade userFacade;
    private final TimeProvider timeProvider;
    private final BookingRepository bookingRepository;
    private final EventPublisher eventPublisher;

    @Transactional
    public void makeBooking(BookingMakeDto dto) {
        var screening = screeningRepository
                .readByIdWithSeats(dto.screeningId())
                .orElseThrow(() -> new EntityNotFoundException("Screening"));
        var currentUserId = userFacade.readCurrentUserId();
        var booking = Booking.make(
                timeProvider.getCurrentDate(),
                screening,
                dto.rowNumber(),
                dto.seatNumber(),
                currentUserId
        );
        var addedBooking = bookingRepository.add(booking);
        log.info("Added a booking:{}", addedBooking);
        var bookingMadeEvent = new BookingMadeEvent(
                addedBooking.getId(),
                screening.getId(),
                screening.getDate(),
                addedBooking.getSeat().getRowNumber(),
                addedBooking.getSeat().getNumber(),
                currentUserId
        );
        eventPublisher.publish(bookingMadeEvent);
        log.info("Event published: {}", bookingMadeEvent);
    }
}
