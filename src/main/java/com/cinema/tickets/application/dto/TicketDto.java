package com.cinema.tickets.application.dto;

public record TicketDto(
        int rowNumber,
        int seatNumber,
        boolean free
) {
}
