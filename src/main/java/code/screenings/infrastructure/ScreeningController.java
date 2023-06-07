package code.screenings.infrastructure;

import code.screenings.application.commands.ScreeningCreateCommand;
import code.screenings.application.handlers.ScreeningCreateHandler;
import code.screenings.application.dto.ScreeningDto;
import code.screenings.application.dto.SeatDto;
import code.screenings.application.handlers.ScreeningReadHandler;
import code.screenings.application.handlers.ScreeningSeatReadHandler;
import code.screenings.application.queries.ScreeningReadQuery;
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

    private final ScreeningCreateHandler screeningCreateHandler;

    private final ScreeningReadHandler screeningReadHandler;

    private final ScreeningSeatReadHandler screeningSeatReadHandler;

    @PostMapping
    public ResponseEntity<ScreeningDto> createScreening(
            @RequestBody
            @Valid
            ScreeningCreateCommand cmd
    ) {
        var createdScreening = screeningCreateHandler.handle(cmd);
        return new ResponseEntity<>(createdScreening, HttpStatus.CREATED);
    }

    @GetMapping
    public List<ScreeningDto> readScreeningsBy(
            @RequestParam(required = false)
            String filmTitle,

            @RequestParam(required = false)
            @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
            LocalDateTime date
    ) {
        var params = ScreeningReadQuery
                .builder()
                .filmTitle(filmTitle)
                .date(date)
                .build();
        return screeningReadHandler.handle(params);
    }

    @GetMapping("/{screeningId}/seats")
    public List<SeatDto> readSeats(@PathVariable Long screeningId) {
        return screeningSeatReadHandler.handle(screeningId);
    }
}
