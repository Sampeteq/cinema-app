package com.cinema.halls.application.queries.handlers;

import com.cinema.halls.application.queries.GetAllHallOccupations;
import com.cinema.halls.application.queries.dto.HallOccupationDto;
import com.cinema.halls.application.queries.dto.HallOccupationMapper;
import com.cinema.halls.domain.HallOccupationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class GetAllHallOccupationsHandler {

    private final HallOccupationRepository hallOccupationRepository;
    private final HallOccupationMapper hallOccupationMapper;

    public List<HallOccupationDto> handle(GetAllHallOccupations query) {
      log.info("Query:{}", query);
      return hallOccupationRepository
              .getAll()
              .stream()
              .map(hallOccupationMapper::mapToDto)
              .toList();
    }
}
