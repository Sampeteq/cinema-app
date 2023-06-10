package code.films.application.dto;

import code.films.domain.FilmScreeningSeat;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper
public interface FilmScreeningSeatMapper {

    @Mapping(target = "isFree", source = "seat", qualifiedByName = "isFreeMap")
    FilmScreeningSeatDto toDto(FilmScreeningSeat seat);
    List<FilmScreeningSeatDto> toDto(List<FilmScreeningSeat> seat);

    @Named("isFreeMap")
    static boolean isFreeMap(FilmScreeningSeat seat) {
        return seat.isFree();
    }
}
