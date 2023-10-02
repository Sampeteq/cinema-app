package com.cinema.catalog.application.services;

import com.cinema.catalog.domain.ScreeningRepository;
import com.cinema.shared.exceptions.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
class ScreeningDeleteService {

    private final ScreeningRepository screeningRepository;

    void delete(Long id) {
        var screening = screeningRepository
                .readById(id)
                .orElseThrow(() -> new EntityNotFoundException("Screening"));
        screeningRepository.delete(screening);
    }
}
