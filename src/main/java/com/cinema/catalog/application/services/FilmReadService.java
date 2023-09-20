package com.cinema.catalog.application.services;

import com.cinema.catalog.application.dto.FilmDto;
import com.cinema.catalog.application.dto.FilmMapper;
import com.cinema.catalog.domain.FilmRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
class FilmReadService {

    private final FilmRepository filmRepository;
    private final FilmMapper filmMapper;

    List<FilmDto> readAll() {
        return filmRepository
                .readAll()
                .stream()
                .map(filmMapper::mapToDto)
                .toList();
    }
}
