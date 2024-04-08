package com.cinema.tickets.domain;

import java.util.List;

public interface TicketReadRepository {

    List<TicketDto> getByScreeningId(long id);

    List<TicketDto> getByUserId(long id);
}
