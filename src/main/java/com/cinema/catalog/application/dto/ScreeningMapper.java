package com.cinema.catalog.application.dto;

import com.cinema.catalog.domain.Screening;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface ScreeningMapper {

    @Mapping(target = "filmTitle", source = "screening.film.title")
    @Mapping(target = "filmCategory", source = "screening.film.category")
    ScreeningDto mapToDto(Screening screening);
}
