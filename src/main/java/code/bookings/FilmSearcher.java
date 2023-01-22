package code.bookings;

import code.bookings.dto.FilmDto;
import code.bookings.dto.FilmSearchParamsDto;
import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
class FilmSearcher {

    private final FilmRepository filmRepository;

    List<FilmDto> searchFilmsBy(FilmSearchParamsDto paramsDto) {
        var params = FilmSearchParams
                .builder()
                .category(paramsDto.category == null ? null : FilmCategory.fromDTO(paramsDto.category))
                .build();
        return filmRepository
                .findBy(params)
                .stream()
                .map(Film::toDto)
                .toList();
    }
}
