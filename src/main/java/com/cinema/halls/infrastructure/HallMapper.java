package com.cinema.halls.infrastructure;

import com.cinema.halls.application.dto.HallDto;
import com.cinema.halls.domain.Hall;
import org.mapstruct.Mapper;

@Mapper
public interface HallMapper {

    HallDto toDto(Hall hall);
}
