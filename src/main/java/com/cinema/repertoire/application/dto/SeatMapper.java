package com.cinema.repertoire.application.dto;

import com.cinema.repertoire.domain.Seat;
import org.mapstruct.Mapper;

@Mapper
public interface SeatMapper {
    SeatDto toDto(Seat seat);
}
