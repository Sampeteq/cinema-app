package com.cinema.screenings.application.queries.handlers;

import com.cinema.screenings.application.queries.GetSeatsByScreeningId;
import com.cinema.screenings.application.queries.dto.SeatWithStatusDto;
import com.cinema.tickets.application.queries.GetTicketsSeatIdsByScreeningId;
import com.cinema.tickets.application.queries.handlers.GetTicketsSeatIdsByScreeningHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class GetSeatsByScreeningIdHandler {

    private final JdbcTemplate jdbcTemplate;
    private final GetTicketsSeatIdsByScreeningHandler getTicketsSeatIdsByScreeningHandler;

    public List<SeatWithStatusDto> handle(GetSeatsByScreeningId query) {
        log.info("Query:{}", query);
        var hallId = jdbcTemplate.queryForObject(
                "select s.hall_id from screenings s where s.id = ?",
                Long.class,
                query.screeningId()
        );
        var ticketsSeatsId = getTicketsSeatIdsByScreeningHandler.handle(
                new GetTicketsSeatIdsByScreeningId(query.screeningId())
        );
        return jdbcTemplate
                .query(
                        "select s.id, s.row_number, s.number from seats s where s.hall_id = ?",
                        (rs, rowNum) -> new SeatWithStatusDto(
                                rs.getInt("row_number"),
                                rs.getInt("number"),
                                !ticketsSeatsId.contains(rs.getLong("id"))
                        ),
                        hallId
                );
    }
}
