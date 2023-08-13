package code.bookings.application.services;

import code.bookings.domain.BookingStatus;
import code.bookings.domain.BookingView;
import code.bookings.domain.events.BookingMadeEvent;
import code.bookings.domain.ports.BookingViewRepository;
import code.catalog.application.services.CatalogApiForBookingsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookingMadeHandlerService {

    private final CatalogApiForBookingsService catalogApiForBookingsService;
    private final BookingViewRepository bookingViewRepository;

    @EventListener
    public void handle(BookingMadeEvent event) {
        var screeningDetails = catalogApiForBookingsService.readScreeningDetails(event.screeningId());
        var bookingView = new BookingView(
                event.bookingId(),
                BookingStatus.ACTIVE,
                screeningDetails.filmTitle(),
                event.screeningDate(),
                screeningDetails.roomCustomId(),
                event.seatRowNumber(),
                event.seatNumber(),
                event.userId()
        );
        bookingViewRepository.add(bookingView);
        log.info("Booking view added: {}", bookingView);
    }
}
