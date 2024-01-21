package com.cinema.screenings.infrastructure;

import com.cinema.screenings.application.dto.ScreeningDto;
import com.cinema.screenings.domain.Screening;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface ScreeningMapper {
    @Mapping(source = "screening.film.title", target = "filmTitle")
    @Mapping(source = "screening.hall.id", target = "hallId")
    ScreeningDto mapScreeningToDto(Screening screening);
}
