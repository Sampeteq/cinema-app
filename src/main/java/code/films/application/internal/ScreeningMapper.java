package code.films.application.internal;

import code.films.application.dto.ScreeningDto;
import code.films.domain.Screening;
import code.films.domain.Seat;
import org.springframework.stereotype.Component;

@Component
public class ScreeningMapper {
    public ScreeningDto mapToDto(Screening screening) {
        return new ScreeningDto(
                screening.getId(),
                screening.getDate(),
                screening.getMinAge(),
                screening.getFilm().getId(),
                screening.getRoom().getId(),
                (int) screening.getSeats().stream().filter(Seat::isFree).count()
        );
    }
}
