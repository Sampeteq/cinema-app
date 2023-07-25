package code.bookings.application.services;

import code.bookings.domain.Screening;
import code.bookings.domain.Seat;
import code.bookings.domain.ports.ScreeningRepository;
import code.catalog.domain.events.ScreeningCreatedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.stream.IntStream.rangeClosed;

@Service
@RequiredArgsConstructor
public class ScreeningCreatedHandlerService {

    private final ScreeningRepository screeningRepository;

    @EventListener(ScreeningCreatedEvent.class)
    public void handle(ScreeningCreatedEvent event) {
        var seats = createSeats(event.rowsQuantity(), event.seatsQuantityInOneRow());
        var screening = Screening.create(event.id(), event.date(), seats);
        screeningRepository.add(screening);
    }

    private List<Seat> createSeats(int rowsQuantity, int seatsQuantityInOneRow) {
        return rangeClosed(1, rowsQuantity)
                .boxed()
                .flatMap(rowNumber -> rangeClosed(1, seatsQuantityInOneRow)
                        .mapToObj(seatNumber -> Seat.create(rowNumber, seatNumber))
                )
                .toList();
    }
}
