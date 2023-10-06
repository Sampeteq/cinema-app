package com.cinema.catalog.application.services;

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

    public Screening readById(Long id) {
        return screeningRepository
                .readById(id)
                .orElseThrow(() -> new EntityNotFoundException("Screening"));
    }

    public List<ScreeningDto> readAllScreenings(ScreeningQueryDto queryDto) {
        return screeningRepository
                .readAllBy(queryDto)
                .stream()
                .sorted(comparing(Screening::getDate))
                .map(screeningMapper::mapToDto)
                .toList();
    }
}
