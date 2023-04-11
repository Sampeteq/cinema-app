package code.films.infrastructure.rest.mappers;

import code.films.domain.Film;
import code.films.infrastructure.rest.dto.FilmDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface FilmMapper {

    FilmDto mapToDto(Film film);
}
