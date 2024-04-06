package com.cinema.screenings.infrastructure.db;

import com.cinema.screenings.domain.Screening;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class JpaScreeningMapper {

    public JpaScreening toJpa(Screening screening) {
        return new JpaScreening(
                screening.getId(),
                screening.getDate(),
                screening.getEndDate(),
                screening.getFilmId(),
                screening.getHallId()
        );
    }

    public Screening toDomain(JpaScreening screening) {
        return new Screening(
                screening.getId(),
                screening.getDate(),
                screening.getEndDate(),
                screening.getFilmId(),
                screening.getHallId()
        );
    }
}
