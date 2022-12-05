package code.films;

import code.films.dto.AddFilmDto;
import code.films.dto.FilmCategoryDto;
import code.films.dto.FilmDto;
import code.films.dto.FilmSearchParamDto;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@AllArgsConstructor
public class FilmFacade {

    private final FilmRepository filmRepository;

    public FilmDto add(AddFilmDto dto) {
        var film = new Film(
                dto.title(),
                FilmCategory.fromDTO(dto.filmCategory()),
                FilmYear.of(dto.year())
        );
        return filmRepository
                .add(film)
                .toDTO();
    }

    public List<FilmDto> search(Map<FilmSearchParamDto, Object> parameters) {
        var categoryDTO = (FilmCategoryDto) parameters.get(FilmSearchParamDto.CATEGORY);
        var category = categoryDTO != null ? FilmCategory.fromDTO(categoryDTO) : null;
        var example = Film
                        .builder()
                        .category(category)
                        .build();
        return filmRepository
                .getByExample(example)
                .stream()
                .map(Film::toDTO)
                .toList();
    }

    public boolean isPresent(UUID filmId) {
        return filmRepository.getById(filmId).isPresent();
    }
}
