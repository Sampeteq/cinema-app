package com.cinema.halls.application.queries.handlers;

import com.cinema.halls.application.queries.dto.HallDto;
import com.cinema.halls.application.queries.dto.HallMapper;
import com.cinema.halls.application.queries.GetFirstAvailableHall;
import com.cinema.halls.domain.HallRepository;
import com.cinema.halls.domain.exceptions.HallsNoAvailableException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Slf4j
public class GetFirstAvailableHallHandler {

    private final HallRepository hallRepository;
    private final HallMapper hallMapper;

    @Transactional(readOnly = true)
    public HallDto handle(GetFirstAvailableHall query) {
        return hallRepository
                .getAll()
                .stream()
                .filter(hall -> hall.hasNoOccupationOn(query.start(), query.end()))
                .findFirst()
                .map(hallMapper::toDto)
                .orElseThrow(HallsNoAvailableException::new);
    }
}
