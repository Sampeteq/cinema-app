package com.cinema.halls.application.queries.dto;

import com.cinema.halls.domain.Hall;
import org.mapstruct.Mapper;

@Mapper
public interface HallMapper {

    HallDto toDto(Hall hall);
}
