package com.cinema.tickets.ui;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public record TicketView(
        Long id,
        String filmTitle,
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm")
        LocalDateTime screeningDate,
        Long hallId,
        Integer rowNumber,
        Integer seatNumber,
        Long userId
) {
}
