package com.cinema.halls.application.queries.handlers;

import com.cinema.halls.application.queries.GetSeatBySeatId;
import com.cinema.halls.application.queries.dto.SeatDto;
import com.cinema.halls.domain.exceptions.SeatNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class GetSeatByIdHandler {

    private final JdbcTemplate jdbcTemplate;

    public SeatDto handle(GetSeatBySeatId query) {
        log.info("Query:{}", query);
        try {
            return jdbcTemplate.queryForObject(
                    "select s.id, s.row_number, s.number from seats s where s.id = ?",
                    (resultSet, rowNum) -> new SeatDto(
                            resultSet.getLong("id"),
                            resultSet.getInt("row_number"),
                            resultSet.getInt("number")
                    ),
                    query.seatId()
            );
        } catch (EmptyResultDataAccessException exception) {
            throw new SeatNotFoundException();
        }
    }
}
