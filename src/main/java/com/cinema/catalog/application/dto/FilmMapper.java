package com.cinema.catalog.application.dto;

import com.cinema.catalog.domain.Film;
import org.mapstruct.Mapper;

@Mapper
public interface FilmMapper {
    FilmDto mapToDto(Film film);
}
