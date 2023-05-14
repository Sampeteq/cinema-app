package code.screenings.infrastructure;

import code.screenings.application.commands.CreateScreeningCommand;
import code.screenings.application.commands.CreateScreeningHandler;
import code.screenings.application.dto.ScreeningDto;
import code.screenings.application.dto.SeatDto;
import code.screenings.application.queries.GetScreeningHandler;
import code.screenings.application.queries.GetScreeningSeatsHandler;
import code.screenings.application.queries.GetScreeningSeatsQuery;
import code.screenings.application.queries.GetScreeningsQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

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
            Long filmId,

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
    public List<SeatDto> searchSeats(@PathVariable Long screeningId) {
        return getScreeningSeatsHandler.handle(
                GetScreeningSeatsQuery
                        .builder()
                        .screeningId(screeningId)
                        .build()
        );
    }
}
