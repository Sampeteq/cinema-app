package com.cinema.catalog.application.services;

import com.cinema.catalog.application.dto.ScreeningDetailsDto;
import com.cinema.catalog.application.dto.ScreeningDto;
import com.cinema.catalog.application.dto.ScreeningMapper;
import com.cinema.catalog.application.dto.ScreeningQueryDto;
import com.cinema.catalog.domain.Screening;
import com.cinema.catalog.domain.ScreeningRepository;
import com.cinema.shared.exceptions.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static java.util.Comparator.comparing;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
class ScreeningReadService {

    private final ScreeningRepository screeningRepository;
    private final ScreeningMapper screeningMapper;

    public List<ScreeningDto> readAllScreenings(ScreeningQueryDto queryDto) {
        return screeningRepository
                .readAllBy(queryDto)
                .stream()
                .sorted(comparing(Screening::getDate))
                .map(screeningMapper::mapToDto)
                .toList();
    }

    public ScreeningDetailsDto readScreeningDetails(Long id, int rowNumber, int seatNumber) {
        var screening = screeningRepository
                .readById(id)
                .orElseThrow(() -> new EntityNotFoundException("Screening"));
        var seat = screening.findSeat(rowNumber, seatNumber);
        var seatExists = seat.isPresent();
        return new ScreeningDetailsDto(
                screening.getFilm().getTitle(),
                screening.getDate(),
                screening.getRoomId(),
                seatExists
        );
    }
}
