package com.cinema.halls.domain;

import com.cinema.halls.domain.exceptions.HallNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@RequiredArgsConstructor
@Slf4j
public class HallService {

    private final HallRepository hallRepository;

    public Hall createHall(Hall hall) {
        log.info("Hall:{}", hall);
        var addedHall = hallRepository.save(hall);
        log.info("Hall added:{}", addedHall);
        return addedHall;
    }

    public void deleteHall(Long id) {
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

    public Hall getHallById(Long hallId) {
        return hallRepository
                .getById(hallId)
                .orElseThrow(HallNotFoundException::new);
    }
}
