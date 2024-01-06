package com.cinema.screenings.application;

import com.cinema.screenings.application.dto.ScreeningSeatDto;
import com.cinema.screenings.domain.ScreeningSeatRepository;
import com.cinema.screenings.infrastructure.ScreeningSeatMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ScreeningSeatService {

    private final ScreeningSeatRepository screeningSeatRepository;
    private final ScreeningSeatMapper screeningSeatMapper;

    public List<ScreeningSeatDto> getSeatsByScreeningId(Long screeningId) {
        log.info("Screening id:{}", screeningId);
        return screeningSeatRepository
                .getAllByScreeningId(screeningId)
                .stream()
                .map(screeningSeatMapper::mapToDto)
                .toList();
    }
}
