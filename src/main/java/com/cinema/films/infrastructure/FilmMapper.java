package com.cinema.films.infrastructure;

import com.cinema.films.application.dto.FilmDto;
import com.cinema.films.domain.Film;
import org.mapstruct.Mapper;

@Mapper
public interface FilmMapper {
    FilmDto mapToDto(Film film);
}
