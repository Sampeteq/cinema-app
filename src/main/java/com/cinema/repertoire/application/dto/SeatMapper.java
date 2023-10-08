package com.cinema.repertoire.application.dto;

import com.cinema.repertoire.domain.Seat;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface SeatMapper {

    @Mapping(target = "isFree", source = "seat.free")
    SeatDto toDto(Seat seat);
}
