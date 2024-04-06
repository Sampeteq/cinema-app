package com.cinema.screenings.infrastructure.ui;

import com.cinema.screenings.domain.Screening;
import org.springframework.stereotype.Component;

@Component
class ScreeningMapper {

    ScreeningDto mapToDto(Screening screening) {
        return new ScreeningDto(
                screening.getId(),
                screening.getDate(),
                screening.getEndDate(),
                screening.getFilm().getId(),
                screening.getHall().getId()
        );
    }
}