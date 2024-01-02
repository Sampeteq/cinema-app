package com.cinema.screenings.infrastructure;

import com.cinema.screenings.application.dto.ScreeningDto;
import com.cinema.screenings.domain.Screening;
import org.mapstruct.Mapper;

@Mapper
public interface ScreeningMapper {
    ScreeningDto mapToDto(Screening screening, String filmTitle);
}
