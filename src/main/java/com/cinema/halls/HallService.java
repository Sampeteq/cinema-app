package com.cinema.halls;

import com.cinema.halls.exceptions.HallNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
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
                .findById(id)
                .orElseThrow(HallNotFoundException::new);
        log.info("Found hall:{}", hall);
        hallRepository.delete(hall);
        log.info("Hall deleted");
    }

    public List<Hall> getAllHallsWithSeats() {
        return hallRepository.findAllWithSeats();
    }

    public Hall getHallWithSeatsById(Long hallId) {
        return hallRepository
                .findByIdWithSeats(hallId)
                .orElseThrow(HallNotFoundException::new);
    }

    public boolean hallExistsById(Long hallId) {
        return hallRepository.existsById(hallId);
    }
}