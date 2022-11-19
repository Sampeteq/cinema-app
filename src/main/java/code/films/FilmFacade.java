package code.films;

import code.films.dto.AddFilmDTO;
import code.films.dto.FilmCategoryDTO;
import code.films.dto.FilmDTO;
import code.films.exception.FilmNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Example;

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
                .save(film)
                .toDTO();
    }

    public FilmDTO read(UUID filmId) {
        return filmRepository
                .findById(filmId)
                .map(Film::toDTO)
                .orElseThrow(() -> new FilmNotFoundException(filmId));
    }

    public List<FilmDTO> search(Map<String, Object> parameters) {
        var categoryDTO = (FilmCategoryDTO) parameters.get("category");
        var category = categoryDTO != null ? FilmCategory.fromDTO(categoryDTO) : null;
        var example = Example.of(
                Film
                        .builder()
                        .category(category)
                        .build()
        );
        return filmRepository
                .findAll(example)
                .stream()
                .map(Film::toDTO)
                .toList();
    }

    public boolean isPresent(UUID filmId) {
        return filmRepository.findById(filmId).isPresent();
    }
}
