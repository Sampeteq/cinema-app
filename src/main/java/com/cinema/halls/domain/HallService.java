package com.cinema.halls.domain;

import com.cinema.halls.domain.exceptions.HallNotFoundException;
import com.cinema.halls.infrastructure.ui.HallCreateDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Slf4j
public class HallService {

    private final HallRepository hallRepository;

    public Hall createHall(HallCreateDto hallCreateDto) {
        log.info("HallCreateDto:{}", hallCreateDto);
        var addedHall = hallRepository.save(new Hall(UUID.randomUUID(), hallCreateDto.seats()));
        log.info("Hall added:{}", addedHall);
        return addedHall;
    }

    public void deleteHall(UUID id) {
        log.info("Hall id:{}", id);
        var hall = hallRepository
                .getById(id)
                .orElseThrow(HallNotFoundException::new);
        log.info("Found hall:{}", hall);
        hallRepository.delete(hall);
        log.info("Hall deleted");
    }

    public List<Hall> getAllHallsWithSeats() {
        return hallRepository.getAllWithSeats();
    }

    public Hall getHallById(UUID hallId) {
        return hallRepository
                .getById(hallId)
                .orElseThrow(HallNotFoundException::new);
    }
}
