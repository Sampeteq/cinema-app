package com.cinema.halls.application.queries.handlers;

import com.cinema.halls.application.queries.GetHall;
import com.cinema.halls.application.queries.dto.HallDto;
import com.cinema.halls.domain.HallRepository;
import com.cinema.halls.domain.exceptions.HallNotFoundException;
import com.cinema.halls.infrastructure.HallMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class GetHallHandler {

    private final HallRepository hallRepository;
    private final HallMapper hallMapper;

    public HallDto handle(GetHall query) {
        log.info("Query:{}", query);
        return hallRepository
                .getById(query.hallId())
                .map(hallMapper::toDto)
                .orElseThrow(HallNotFoundException::new);
    }
}
