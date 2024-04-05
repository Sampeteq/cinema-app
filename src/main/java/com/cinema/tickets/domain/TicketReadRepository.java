package com.cinema.tickets.domain;

import java.util.List;

public interface TicketReadRepository {

    List<TicketDto> getByScreeningId(Long id);


    List<TicketDto> getByUserId(Long id);
}
