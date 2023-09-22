package com.cinema.catalog.application.dto;

import com.cinema.catalog.domain.Seat;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface SeatMapper {

    @Mapping(target = "isFree", source = "seat.free")
    SeatDto toDto(Seat seat);
}
