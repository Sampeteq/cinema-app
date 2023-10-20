package com.cinema.films.application.dto;

import com.cinema.films.domain.Film;
import org.mapstruct.Mapper;

@Mapper
public interface FilmMapper {
    FilmDto mapToDto(Film film);
}
