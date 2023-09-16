package com.cinema.bookings.application.services;

import com.cinema.bookings.domain.Screening;
import com.cinema.bookings.domain.ports.ScreeningRepository;
import com.cinema.catalog.application.dto.SeatDto;
import com.cinema.catalog.application.dto.SeatMapper;
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
    public List<SeatDto> readSeatsByScreeningId(Long screeningId) {
        return screeningRepository
                .readByIdWithSeats(screeningId)
                .map(Screening::getSeats)
                .map(seatMapper::toDto)
                .orElseThrow(() -> new EntityNotFoundException("Screening"));
    }
}
