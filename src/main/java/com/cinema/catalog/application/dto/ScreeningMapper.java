package com.cinema.catalog.application.dto;

import com.cinema.catalog.domain.Screening;
import org.mapstruct.Mapper;

@Mapper
public interface ScreeningMapper {
    ScreeningDto mapToDto(Screening screening);
}
