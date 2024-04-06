package com.cinema.screenings.infrastructure.ui;

import com.cinema.screenings.domain.Screening;

public class ScreeningMapper {

    public ScreeningDto mapToDto(Screening screening) {
        return new ScreeningDto(
                screening.getId(),
                screening.getDate(),
                screening.getEndDate(),
                screening.getFilm().getId(),
                screening.getHall().getId()
        );
    }
}
