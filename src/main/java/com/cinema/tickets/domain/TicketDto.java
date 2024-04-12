package com.cinema.tickets.domain;

public record TicketDto(
        int rowNumber,
        int seatNumber,
        boolean free
) {
}
