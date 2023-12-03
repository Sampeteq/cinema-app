package com.cinema.halls.application.queries.handlers;

import com.cinema.halls.application.queries.GetSeatIdByHallIdAndPosition;
import com.cinema.halls.domain.exceptions.SeatNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class GetSeatIdByHallIdAndPositionHandler {

    private final JdbcTemplate jdbcTemplate;

    public Long handle(GetSeatIdByHallIdAndPosition query) {
        log.info("Query:{}", query);
        try {
            return jdbcTemplate.queryForObject(
                    "SELECT s.id FROM seats s WHERE s.hall_id = ? AND s.row_number = ? AND s.number = ?",
                    Long.class,
                    query.hallId(),
                    query.rowNumber(),
                    query.number()
            );
        } catch (EmptyResultDataAccessException exception) {
            throw new SeatNotFoundException();
        }
    }
}
