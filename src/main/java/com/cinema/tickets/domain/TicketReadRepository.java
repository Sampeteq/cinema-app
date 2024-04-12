package com.cinema.tickets.domain;

import java.util.List;
import java.util.UUID;

public interface TicketReadRepository {

    List<TicketDto> getByScreeningId(UUID id);

    List<TicketUserDto> getByUserId(UUID id);
}
