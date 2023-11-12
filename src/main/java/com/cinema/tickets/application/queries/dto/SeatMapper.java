package com.cinema.tickets.application.queries.dto;

import com.cinema.tickets.domain.Seat;
import org.mapstruct.Mapper;

@Mapper
public interface SeatMapper {
    SeatDto toDto(Seat seat);
}
