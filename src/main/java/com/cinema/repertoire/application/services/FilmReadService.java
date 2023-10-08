package com.cinema.repertoire.application.services;

import com.cinema.repertoire.application.dto.FilmDto;
import com.cinema.repertoire.application.dto.FilmMapper;
import com.cinema.repertoire.domain.FilmRepository;
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
