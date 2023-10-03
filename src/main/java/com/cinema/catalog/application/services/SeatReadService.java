package com.cinema.catalog.application.services;

import com.cinema.catalog.application.dto.SeatDto;
import com.cinema.catalog.application.dto.SeatMapper;
import com.cinema.catalog.domain.ScreeningRepository;
import com.cinema.shared.exceptions.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
class SeatReadService {

    private final ScreeningRepository screeningRepository;
    private final SeatMapper seatMapper;

    @Transactional(readOnly = true)
    public List<SeatDto> readSeatsByScreeningId(Long id) {
        return screeningRepository
                .readById(id)
                .orElseThrow(() -> new EntityNotFoundException("Screening"))
                .getSeats()
                .stream()
                .map(seatMapper::toDto)
                .toList();
    }
}
