package code.bookings.application.services.mappers;

import code.bookings.application.dto.ScreeningDto;
import code.bookings.domain.Screening;
import code.bookings.domain.Seat;
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
