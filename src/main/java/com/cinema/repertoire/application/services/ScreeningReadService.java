package com.cinema.repertoire.application.services;

import com.cinema.repertoire.application.dto.ScreeningDetailsDto;
import com.cinema.repertoire.application.dto.ScreeningDto;
import com.cinema.repertoire.application.dto.ScreeningMapper;
import com.cinema.repertoire.application.dto.ScreeningQueryDto;
import com.cinema.repertoire.domain.Screening;
import com.cinema.repertoire.domain.ScreeningRepository;
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

    public List<ScreeningDto> readAllScreeningsBy(ScreeningQueryDto queryDto) {
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
