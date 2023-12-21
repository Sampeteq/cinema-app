package com.cinema.halls.infrastructure.config;

import com.cinema.halls.domain.Hall;
import com.cinema.halls.domain.HallRepository;
import com.cinema.halls.domain.HallSeat;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class CreateHallService {

    private final HallRepository hallRepository;

    public void handle(ConfigHallDto command) {
        var seats = command
                .seats()
                .stream()
                .map(configSeatDto -> new HallSeat(configSeatDto.rowNumber(), configSeatDto.number()))
                .toList();
        var hall = new Hall(seats);
        hallRepository.add(hall);
    }
}
