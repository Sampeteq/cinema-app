package code.film;

import code.film.dto.AddFilmDTO;
import code.film.dto.FilmDTO;
import code.film.exception.FilmNotFoundException;
import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public class FilmFacade {

    private final FilmRepository filmRepository;

    public FilmDTO add(AddFilmDTO cmd) {
        var film = new Film(
                cmd.title(),
                cmd.filmCategory(),
                FilmYear.of(cmd.year())
        );
        return filmRepository
                .save(film)
                .toDTO();
    }

    public FilmDTO read(Long filmId) {
        return filmRepository
                .findById(filmId)
                .map(Film::toDTO)
                .orElseThrow(() -> new FilmNotFoundException(filmId));
    }

    public List<FilmDTO> readAll() {
        return filmRepository
                .findAll()
                .stream()
                .map(Film::toDTO)
                .toList();
    }

    public List<FilmDTO> readByCategory(FilmCategory category) {
        return filmRepository
                .findByCategory(category)
                .stream()
                .map(Film::toDTO)
                .toList();
    }

    public boolean isPresent(Long filmId) {
        return filmRepository.findById(filmId).isPresent();
    }
}
