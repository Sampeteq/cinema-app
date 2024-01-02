package com.cinema.screenings.infrastructure;

import com.cinema.screenings.application.dto.ScreeningSeatDto;
import com.cinema.screenings.domain.ScreeningSeat;
import org.mapstruct.Mapper;

@Mapper
public interface ScreeningSeatMapper {
    ScreeningSeatDto mapToDto(ScreeningSeat seat);
}
