package code.catalog.application.dto;

import code.catalog.domain.Film;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper
public interface FilmMapper {

    FilmDto mapToDto(Film film);

    List<FilmDto> mapToDto(List<Film> films);
}
