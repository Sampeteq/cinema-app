package code.screenings.infrastructure;

import code.screenings.client.dto.SeatDto;
import code.screenings.client.queries.GetScreeningSeatsQuery;
import code.screenings.client.queries.GetScreeningsQuery;
import code.screenings.client.queries.GetScreeningHandler;
import code.screenings.client.commands.CreateScreeningCommand;
import code.screenings.client.commands.handlers.CreateScreeningHandler;
import code.screenings.client.dto.ScreeningDto;
import code.screenings.client.queries.handlers.GetScreeningSeatsHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/screenings")
@RequiredArgsConstructor
public class ScreeningController {

    private final CreateScreeningHandler createScreeningHandler;

    private final GetScreeningHandler getScreeningHandler;

    private final GetScreeningSeatsHandler getScreeningSeatsHandler;

    @PostMapping
    public ResponseEntity<ScreeningDto> createScreening(
            @RequestBody
            @Valid
            CreateScreeningCommand dto
    ) {
        var createdScreening = createScreeningHandler.createScreening(dto);
        return new ResponseEntity<>(createdScreening, HttpStatus.CREATED);
    }

    @GetMapping
    public List<ScreeningDto> searchScreeningsBy(
            @RequestParam(required = false)
            UUID filmId,

            @RequestParam(required = false)
            @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
            LocalDateTime date
    ) {

        var params = GetScreeningsQuery
                .builder()
                .filmId(filmId)
                .date(date)
                .build();

        return getScreeningHandler.handle(params);
    }

    @GetMapping("/{screeningId}/seats")
    public List<SeatDto> searchSeats(@PathVariable UUID screeningId) {
        return getScreeningSeatsHandler.handle(
                GetScreeningSeatsQuery
                        .builder()
                        .screeningId(screeningId)
                        .build()
        );
    }
}
