package com.cinema.tickets.ui;

import com.cinema.tickets.application.queries.dto.TicketDto;

import java.util.List;

public record TicketsResponse(List<TicketDto> tickets) {
}
