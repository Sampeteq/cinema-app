package code.bookings.application.services;

import code.bookings.domain.Booking;
import code.bookings.domain.Film;
import code.bookings.domain.Room;
import code.bookings.domain.Screening;
import code.bookings.domain.Seat;
import code.bookings.domain.events.BookingMadeEvent;
import code.bookings.domain.exceptions.BookingAlreadyExists;
import code.bookings.domain.ports.BookingRepository;
import code.bookings.infrastructure.db.BookingFilmRepository;
import code.bookings.infrastructure.db.BookingRoomRepository;
import code.catalog.application.services.SeatDataService;
import code.user.application.services.UserCurrentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookingMakeService {

    private final BookingRepository bookingRepository;
    private final SeatDataService seatDataService;
    private final BookingFilmRepository bookingFilmRepository;
    private final BookingRoomRepository bookingRoomRepository;
    private final UserCurrentService userCurrentService;
    private final Clock clock;
    private final ApplicationEventPublisher applicationEventPublisher;

    @Transactional
    public void makeBooking(Long seatId) {
        if (bookingRepository.existsBySeatId(seatId)) {
            log.error("Booking already exists");
            throw new BookingAlreadyExists();
        }
        var bookingData = seatDataService.readBookingDataBySeatId(seatId);
        var film = bookingFilmRepository
                .findById(bookingData.getFilmId())
                .orElseGet(
                        () -> Film.create(
                                bookingData.getFilmId(),
                                bookingData.getFilmTitle()
                        )
                );
        var room = bookingRoomRepository
                .findById(bookingData.getRoomId())
                .orElseGet(
                        () -> Room.create(bookingData.getRoomId(), bookingData.getRoomCustomId())
                );
       var screening = Screening.create(
               bookingData.getScreeningId(),
               bookingData.getScreeningDate(),
               film,
               room
       );
       film.addFilm(screening);
       var seat = Seat.create(
                seatId,
                bookingData.getSeatRowNumber(),
                bookingData.getSeatNumber()
        );
        film.addSeatsToScreening(seat, screening.getId());
        var currentUserId = userCurrentService.getCurrentUserId();
        var booking = Booking.make(seat, clock, currentUserId);
        var addedBooking = bookingRepository.add(booking);
        log.info("Added a booking:{}", addedBooking);
        var seatBookedEvent = new BookingMadeEvent(seatId);
        applicationEventPublisher.publishEvent(seatBookedEvent);
    }
}
