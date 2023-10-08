package com.cinema.repertoire.application.dto;

import com.cinema.repertoire.domain.Film;
import org.mapstruct.Mapper;

@Mapper
public interface FilmMapper {
    FilmDto mapToDto(Film film);
}
