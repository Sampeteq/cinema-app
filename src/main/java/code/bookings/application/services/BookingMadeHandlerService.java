package code.bookings.application.services;

import code.bookings.domain.BookingStatus;
import code.bookings.domain.BookingView;
import code.bookings.domain.events.BookingMadeEvent;
import code.bookings.domain.ports.BookingViewRepository;
import code.catalog.application.services.ScreeningDetailsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookingMadeHandlerService {

    private final ScreeningDetailsService screeningDetailsService;
    private final BookingViewRepository bookingViewRepository;

    @EventListener
    public void handle(BookingMadeEvent event) {
        var screeningDetails = screeningDetailsService.readScreeningDetails(event.screeningId());
        var bookingView = new BookingView(
                event.bookingId(),
                BookingStatus.ACTIVE,
                screeningDetails.getFilmTitle(),
                event.screeningDate(),
                screeningDetails.getRoomCustomId(),
                event.seatRowNumber(),
                event.seatNumber(),
                event.userId()
        );
        bookingViewRepository.add(bookingView);
        log.info("Booking view added: {}", bookingView);
    }
}
