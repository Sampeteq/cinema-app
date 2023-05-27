package code.screenings.infrastructure;

import code.screenings.application.commands.ScreeningCreationCommand;
import code.screenings.application.commands.ScreeningCreationService;
import code.screenings.application.dto.ScreeningDto;
import code.screenings.application.dto.SeatDto;
import code.screenings.application.queries.ScreeningReadService;
import code.screenings.application.queries.ScreeningSeatReadService;
import code.screenings.application.queries.ScreeningSeatQuery;
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

    private final ScreeningCreationService screeningCreationService;

    private final ScreeningReadService screeningReadService;

    private final ScreeningSeatReadService screeningSeatReadService;

    @PostMapping
    public ResponseEntity<ScreeningDto> createScreening(
            @RequestBody
            @Valid
            ScreeningCreationCommand dto
    ) {
        var createdScreening = screeningCreationService.handle(dto);
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

        var params = ScreeningReadQuery
                .builder()
                .filmId(filmId)
                .date(date)
                .build();

        return screeningReadService.read(params);
    }

    @GetMapping("/{screeningId}/seats")
    public List<SeatDto> searchSeats(@PathVariable Long screeningId) {
        return screeningSeatReadService.handle(
                ScreeningSeatQuery
                        .builder()
                        .screeningId(screeningId)
                        .build()
        );
    }
}
