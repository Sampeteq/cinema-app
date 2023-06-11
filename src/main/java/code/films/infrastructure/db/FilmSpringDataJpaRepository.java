package code.films.infrastructure.db;

import code.films.domain.Film;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class FilmSpringDataJpaRepository implements FilmRepository {

    private final FilmJpaRepository jpaFilmRepository;

    @Override
    public Film add(Film film) {
        return jpaFilmRepository.save(film);
    }

    @Override
    public Optional<Film> readById(Long filmId) {
        return jpaFilmRepository.findById(filmId);
    }

    @Override
    public List<Film> readAll() {
        return jpaFilmRepository.findAll();
    }
}

interface FilmJpaRepository extends JpaRepository<Film, Long> {
}
