package com.cinema.screenings.infrastructure;

import com.cinema.screenings.application.dto.ScreeningDto;
import com.cinema.screenings.domain.Screening;
import org.springframework.stereotype.Component;

@Component
public class ScreeningMapper {

    public ScreeningDto mapScreeningToDto(Screening screening) {
        return new ScreeningDto(
                screening.getId(),
                screening.getDate(),
                screening.getEndDate(),
                screening.getFilm().getTitle(),
                screening.getHall().getId()
        );
    }
}
