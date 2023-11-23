package com.cinema.halls.application.queries.dto;

import com.cinema.halls.domain.HallOccupation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface HallOccupationMapper {

    @Mapping(target = "hallId", source = "occupation.hall.id")
    HallOccupationDto mapToDto(HallOccupation occupation);
}
