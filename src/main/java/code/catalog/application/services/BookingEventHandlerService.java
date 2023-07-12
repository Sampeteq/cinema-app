package code.catalog.application.services;

import code.bookings.domain.events.BookingCancelledEvent;
import code.bookings.domain.events.BookingMadeEvent;
import code.catalog.infrastructure.db.SeatRepository;
import code.shared.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookingEventHandlerService {

    private final SeatRepository seatRepository;

    @EventListener
    public void handle(BookingMadeEvent event) {
        var seat = seatRepository
                .findById(event.seatId())
                .orElseThrow(() -> new EntityNotFoundException("Seat"));
        seat.makeNotFree();
        seatRepository.save(seat);
    }

    @EventListener
    public void handle(BookingCancelledEvent event) {
        var seat = seatRepository
                .findById(event.seatId())
                .orElseThrow(() -> new EntityNotFoundException("Seat"));
        seat.makeFree();
        seatRepository.save(seat);
    }
}
