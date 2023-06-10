package code.films.application.dto;

import code.films.domain.FilmScreening;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper
public interface FilmScreeningMapper {

    FilmScreeningDto mapToDto(FilmScreening screening);

    List<FilmScreeningDto> mapToDto(List<FilmScreening> screening);
}
