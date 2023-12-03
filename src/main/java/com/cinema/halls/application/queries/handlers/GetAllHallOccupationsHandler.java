package com.cinema.halls.application.queries.handlers;

import com.cinema.halls.application.queries.GetAllHallOccupations;
import com.cinema.halls.application.queries.dto.HallOccupationDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class GetAllHallOccupationsHandler {

    private final JdbcTemplate jdbcTemplate;

    public List<HallOccupationDto> handle(GetAllHallOccupations query) {
      log.info("Query:{}", query);
      return jdbcTemplate.query(
              "select h.start_at, h.end_at, h.hall_id, h.screening_id from halls_occupations h",
              (resultSet, rowNumber) -> new HallOccupationDto(
                      resultSet.getObject("start_at", LocalDateTime.class),
                      resultSet.getObject("end_at", LocalDateTime.class),
                      resultSet.getLong("hall_id"),
                      resultSet.getLong("screening_id")
              )
      );
    }
}
