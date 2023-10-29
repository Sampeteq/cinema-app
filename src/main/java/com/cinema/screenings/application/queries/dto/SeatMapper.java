package com.cinema.screenings.application.queries.dto;

import com.cinema.screenings.domain.Seat;
import org.mapstruct.Mapper;

@Mapper
public interface SeatMapper {
    SeatDto toDto(Seat seat);
}
