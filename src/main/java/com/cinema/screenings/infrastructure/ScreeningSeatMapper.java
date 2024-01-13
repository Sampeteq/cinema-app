package com.cinema.screenings.infrastructure;

import com.cinema.screenings.application.dto.ScreeningSeatDto;
import com.cinema.screenings.domain.ScreeningSeat;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface ScreeningSeatMapper {
    @Mapping(source = "hallSeat.rowNumber", target = "rowNumber")
    @Mapping(source = "hallSeat.number", target = "number")
    @Mapping(source = "free", target = "isFree")
    ScreeningSeatDto mapToDto(ScreeningSeat seat);
}
