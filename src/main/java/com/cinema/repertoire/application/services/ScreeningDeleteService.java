package com.cinema.repertoire.application.services;

import com.cinema.repertoire.domain.ScreeningRepository;
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
