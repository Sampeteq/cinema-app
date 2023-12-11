package com.cinema.screenings.application.queries.handlers;

import com.cinema.screenings.application.queries.GetSeatsByScreeningId;
import com.cinema.screenings.application.queries.dto.ScreeningSeatDto;
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

    public List<ScreeningSeatDto> handle(GetSeatsByScreeningId query) {
        log.info("Query:{}", query);
        return jdbcTemplate.query(
                "select s.id, s.row_number, s.number, s.is_free from screenings_seats s order by s.row_number, s.number",
                (resultSet, rowNumber) -> new ScreeningSeatDto(
                        resultSet.getInt("row_number"),
                        resultSet.getInt("number"),
                        resultSet.getBoolean("is_free")
                ));
    }
}
