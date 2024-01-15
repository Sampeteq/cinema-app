package com.cinema.screenings.infrastructure;

import com.cinema.screenings.application.dto.ScreeningDto;
import com.cinema.screenings.application.dto.ScreeningSeatDto;
import com.cinema.screenings.domain.Screening;
import com.cinema.screenings.domain.ScreeningSeat;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface ScreeningMapper {
    @Mapping(source = "screening.film.title", target = "filmTitle")
    @Mapping(source = "screening.hall.id", target = "hallId")
    ScreeningDto mapScreeningToDto(Screening screening);

    @Mapping(source = "hallSeat.rowNumber", target = "rowNumber")
    @Mapping(source = "hallSeat.number", target = "number")
    @Mapping(source = "free", target = "isFree")
    ScreeningSeatDto mapScreeningSeatToDto(ScreeningSeat seat);
}
