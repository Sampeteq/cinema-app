package com.cinema.catalog.application.dto;

import com.cinema.bookings.domain.Seat;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper
public interface SeatMapper {

    @Mapping(target = "isFree", source = "seat", qualifiedByName = "isFreeMap")
    SeatDto toDto(Seat seat);
    List<SeatDto> toDto(List<Seat> seat);

    @Named("isFreeMap")
    static boolean isFreeMap(Seat seat) {
        return seat.isFree();
    }
}
