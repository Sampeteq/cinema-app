package code.screenings.infrastructure;

import code.screenings.application.ScreeningFacade;
import code.screenings.domain.dto.CreateScreeningDto;
import code.screenings.domain.dto.ScreeningDto;
import code.screenings.domain.dto.ScreeningSearchParamsDto;
import code.screenings.domain.dto.SeatDto;
import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@AllArgsConstructor
public class ScreeningCrudController {

    private final ScreeningFacade screeningFacade;

    @PostMapping("/screenings")
    public ScreeningDto createScreening(
            @RequestBody
            @Valid
            CreateScreeningDto dto
    ) {
        return screeningFacade.createScreening(dto);
    }

    @GetMapping("/screenings")
    public List<ScreeningDto> searchScreeningsBy(
            @RequestParam(required = false)
            UUID filmId,

            @RequestParam(required = false)
            @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
            LocalDateTime date
    ) {

        var paramsDto = ScreeningSearchParamsDto
                .builder()
                .filmId(filmId)
                .screeningDate(date)
                .build();

        return screeningFacade.searchScreeningsBy(paramsDto);
    }

    @GetMapping("/screenings/{screeningId}/seats")
    public List<SeatDto> searchSeats(@PathVariable UUID screeningId) {
        return screeningFacade.searchSeats(screeningId);
    }
}

