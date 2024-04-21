package com.cinema.halls.application.dto;

import java.util.List;
import java.util.UUID;

record HallDto(
        UUID id,
        List<SeatDto> seats
) {
}
