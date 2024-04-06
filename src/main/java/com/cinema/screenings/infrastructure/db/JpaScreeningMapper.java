package com.cinema.screenings.infrastructure.db;

import com.cinema.films.infrastrcture.db.JpaFilmMapper;
import com.cinema.halls.infrastructure.db.JpaHallMapper;
import com.cinema.screenings.domain.Screening;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JpaScreeningMapper {

    private final JpaFilmMapper filmMapper;
    private final JpaHallMapper hallMapper;

    public JpaScreening toJpa(Screening screening) {
        return new JpaScreening(
                screening.getId(),
                screening.getDate(),
                screening.getEndDate(),
                filmMapper.toJpa(screening.getFilm()),
                hallMapper.toJpa(screening.getHall())
        );
    }

    public Screening toDomain(JpaScreening screening) {
        return new Screening(
                screening.getId(),
                screening.getDate(),
                screening.getEndDate(),
                filmMapper.toDomain(screening.getFilm()),
                hallMapper.toDomain(screening.getHall())
        );
    }
}
