package com.cinema.catalog.application.services;

import com.cinema.catalog.application.dto.ScreeningDto;
import com.cinema.catalog.application.dto.ScreeningMapper;
import com.cinema.catalog.domain.FilmCategory;
import com.cinema.catalog.domain.Screening;
import com.cinema.catalog.domain.ScreeningReadOnlyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import static java.util.Comparator.comparing;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
class ScreeningReadService {

    private final ScreeningReadOnlyRepository screeningReadOnlyRepository;
    private final ScreeningMapper screeningMapper;

    public List<ScreeningDto> readAllScreenings() {
        return screeningReadOnlyRepository
                .readAll()
                .stream()
                .sorted(comparing(Screening::getDate))
                .map(screeningMapper::mapToDto)
                .toList();
    }

    public List<ScreeningDto> readScreeningsByFilmTitle(String filmTitle) {
        return screeningReadOnlyRepository
                .readByFilmTitle(filmTitle)
                .stream()
                .sorted(comparing(Screening::getDate))
                .map(screeningMapper::mapToDto)
                .toList();
    }

    public List<ScreeningDto> readScreeningsByFilmCategory(FilmCategory category) {
        return screeningReadOnlyRepository
                .readByFilmCategory(category)
                .stream()
                .sorted(comparing(Screening::getDate))
                .map(screeningMapper::mapToDto)
                .toList();
    }

    public List<ScreeningDto> readScreeningsByDate(LocalDate date) {
        return screeningReadOnlyRepository
                .readByDateBetween(date.atStartOfDay(), date.plusDays(1).atStartOfDay())
                .stream()
                .sorted(comparing(Screening::getDate))
                .map(screeningMapper::mapToDto)
                .toList();
    }
}
