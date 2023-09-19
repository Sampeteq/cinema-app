package com.cinema.catalog.application.services;

import com.cinema.catalog.application.dto.SeatDto;
import com.cinema.catalog.application.dto.SeatMapper;
import com.cinema.catalog.domain.Screening;
import com.cinema.catalog.domain.ports.ScreeningReadOnlyRepository;
import com.cinema.shared.exceptions.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
class SeatReadService {

    private final ScreeningReadOnlyRepository screeningReadOnlyRepository;
    private final SeatMapper seatMapper;

    @Transactional(readOnly = true)
    public List<SeatDto> readSeatsByScreeningId(Long id) {
        return screeningReadOnlyRepository
                .readByIdWithSeats(id)
                .map(Screening::getSeats)
                .map(seatMapper::toDto)
                .orElseThrow(() -> new EntityNotFoundException("Screening"));
    }
}
