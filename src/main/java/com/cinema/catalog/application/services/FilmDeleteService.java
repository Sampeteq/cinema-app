package com.cinema.catalog.application.services;

import com.cinema.catalog.domain.FilmRepository;
import com.cinema.shared.exceptions.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
class FilmDeleteService {

    private final FilmRepository filmRepository;

    void delete(String title) {
        var film = filmRepository
                .readByTitle(title)
                .orElseThrow(() -> new EntityNotFoundException("Film"));
        filmRepository.delete(film);
    }
}
