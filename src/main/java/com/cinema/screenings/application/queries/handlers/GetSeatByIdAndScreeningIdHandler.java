package com.cinema.screenings.application.queries.handlers;

import com.cinema.screenings.application.queries.GetSeatByIdAndScreeningId;
import com.cinema.screenings.application.queries.dto.ScreeningSeatDto;
import com.cinema.screenings.domain.exceptions.ScreeningSeatNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class GetSeatByIdAndScreeningIdHandler {

    private final JdbcTemplate jdbcTemplate;

    public ScreeningSeatDto handle(GetSeatByIdAndScreeningId query) {
        log.info("Query:{}", query);
        try {
            return jdbcTemplate.queryForObject(
                    "select " +
                            "s.id, " +
                            "s.row_number, " +
                            "s.number, " +
                            "s.is_free from screenings_seats s where s.id = ? and s.screening_id = ?",
                    (resultSet, rowNum) -> new ScreeningSeatDto(
                            resultSet.getLong("id"),
                            resultSet.getInt("row_number"),
                            resultSet.getInt("number"),
                            resultSet.getBoolean("is_free")
                    ),
                    query.seatId(),
                    query.screeningId()
            );
        } catch (EmptyResultDataAccessException exception) {
            throw new ScreeningSeatNotFoundException();
        }
    }
}
