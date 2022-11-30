package code.films;

import code.films.dto.AddFilmDTO;
import code.films.dto.FilmCategoryDTO;
import code.films.dto.FilmDTO;
import code.films.dto.FilmSearchParamDTO;
import code.films.exception.FilmNotFoundException;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@AllArgsConstructor
public class FilmFacade {

    private final FilmRepository filmRepository;

    public FilmDTO add(AddFilmDTO dto) {
        var film = new Film(
                dto.title(),
                FilmCategory.fromDTO(dto.filmCategory()),
                FilmYear.of(dto.year())
        );
        return filmRepository
                .add(film)
                .toDTO();
    }

    public FilmDTO read(UUID filmId) {
        return filmRepository
                .getById(filmId)
                .map(Film::toDTO)
                .orElseThrow(() -> new FilmNotFoundException(filmId));
    }

    public List<FilmDTO> search(Map<FilmSearchParamDTO, Object> parameters) {
        var categoryDTO = (FilmCategoryDTO) parameters.get(FilmSearchParamDTO.CATEGORY);
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
