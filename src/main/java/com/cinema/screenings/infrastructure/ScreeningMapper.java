package com.cinema.screenings.infrastructure;

import com.cinema.screenings.domain.Screening;

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
