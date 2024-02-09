package com.cinema.screenings.infrastructure;

import com.cinema.screenings.application.dto.ScreeningView;
import com.cinema.screenings.domain.Screening;
import org.springframework.stereotype.Component;

@Component
public class ScreeningMapper {

    public ScreeningView mapScreeningToDto(Screening screening) {
        return new ScreeningView(
                screening.getId(),
                screening.getDate(),
                screening.getEndDate(),
                screening.getFilm().getTitle(),
                screening.getHall().getId()
        );
    }
}
