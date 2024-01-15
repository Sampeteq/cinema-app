package com.cinema.screenings.application;

import com.cinema.screenings.application.dto.ScreeningSeatDto;
import com.cinema.screenings.domain.ScreeningRepository;
import com.cinema.screenings.domain.exceptions.ScreeningNotFoundException;
import com.cinema.screenings.infrastructure.ScreeningSeatMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ScreeningSeatService {

    private final ScreeningRepository screeningRepository;
    private final ScreeningSeatMapper screeningSeatMapper;

    public List<ScreeningSeatDto> getSeatsByScreeningId(Long screeningId) {
        log.info("Screening id:{}", screeningId);
        return screeningRepository
                .getById(screeningId)
                .orElseThrow(ScreeningNotFoundException::new)
                .getSeats()
                .stream()
                .map(screeningSeatMapper::mapToDto)
                .toList();
    }
}
