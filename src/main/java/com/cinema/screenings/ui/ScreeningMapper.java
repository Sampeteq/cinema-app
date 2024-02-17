package com.cinema.screenings.ui;

import com.cinema.screenings.domain.Screening;
import org.springframework.stereotype.Component;

@Component
class ScreeningMapper {

    ScreeningView mapScreeningToDto(Screening screening) {
        return new ScreeningView(
                screening.getId(),
                screening.getDate(),
                screening.getFilm().getTitle(),
                screening.getHall().getId()
        );
    }
}
