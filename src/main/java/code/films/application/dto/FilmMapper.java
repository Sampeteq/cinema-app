package code.films.application.dto;

import code.films.domain.Film;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper
public interface FilmMapper {

    FilmDto mapToDto(Film film);

    List<FilmDto> mapToDto(List<Film> films);
}
