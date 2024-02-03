package com.cinema.halls.application;

import com.cinema.halls.application.dto.CreateHallDto;
import com.cinema.halls.domain.Hall;
import com.cinema.halls.domain.HallRepository;
import com.cinema.halls.domain.Seat;
import com.cinema.halls.domain.exceptions.HallNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class HallService {

    private final HallRepository hallRepository;

    public Hall createHall(CreateHallDto dto) {
        log.info("Dto:{}", dto);
        var seats = dto.seats()
                .stream()
                .map(createSeatDto -> new Seat(createSeatDto.rowNumber(), createSeatDto.number()))
                .toList();
        var hall = new Hall(seats);
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

    public List<Hall> getAllHalls() {
        return hallRepository.findAll();
    }
}
