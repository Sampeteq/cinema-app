package com.cinema.screenings.application.dto;

import com.cinema.screenings.domain.Seat;
import org.mapstruct.Mapper;

@Mapper
public interface SeatMapper {
    SeatDto toDto(Seat seat);
}
