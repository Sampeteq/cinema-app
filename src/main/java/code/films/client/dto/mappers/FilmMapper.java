package code.films.client.dto.mappers;

import code.films.domain.Film;
import code.films.client.dto.FilmDto;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper
public interface FilmMapper {

    FilmDto mapToDto(Film film);

    List<FilmDto> mapToDto(List<Film> films);
}
