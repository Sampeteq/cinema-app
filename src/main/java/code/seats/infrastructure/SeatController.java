package code.seats.infrastructure;

import code.seats.client.queries.handlers.GetScreeningSeatsHandler;
import code.seats.client.queries.GetScreeningSeatsQuery;
import code.seats.client.dto.SeatDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class SeatController {

    private final GetScreeningSeatsHandler getScreeningSeatsHandler;

    @GetMapping("/films/screenings/{screeningId}/seats")
    public List<SeatDto> searchSeats(@PathVariable UUID screeningId) {
        return getScreeningSeatsHandler.handle(
                GetScreeningSeatsQuery
                        .builder()
                        .screeningId(screeningId)
                        .build()
        );
    }
}
