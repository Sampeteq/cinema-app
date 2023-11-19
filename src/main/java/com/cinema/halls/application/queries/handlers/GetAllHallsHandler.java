package com.cinema.halls.application.queries.handlers;

import com.cinema.halls.application.queries.dto.HallDto;
import com.cinema.halls.application.queries.dto.HallMapper;
import com.cinema.halls.application.queries.GetAllHalls;
import com.cinema.halls.domain.HallRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class GetAllHallsHandler {

    private final HallRepository hallRepository;
    private final HallMapper hallMapper;

    public List<HallDto> handle(GetAllHalls query) {
        log.info("Query:{}", query);
        return hallRepository
                .getAll()
                .stream()
                .map(hallMapper::toDto)
                .toList();
    }
}
