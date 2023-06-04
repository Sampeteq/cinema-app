package code.films.infrastructure.db;

import code.films.application.queries.FilmReadQuery;
import code.films.domain.Film;
import code.films.domain.FilmRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class SpringDataJpaFilmRepository implements FilmRepository {

    private final JpaFilmRepository jpaFilmRepository;

    @Override
    public Film add(Film film) {
        return jpaFilmRepository.save(film);
    }

    @Override
    public List<Film> addMany(List<Film> films) {
        return jpaFilmRepository.saveAll(films);
    }

    @Override
    public Optional<Film> readById(Long filmId) {
        return jpaFilmRepository.findById(filmId);
    }

    @Override
    public List<Film> readBy(FilmReadQuery query) {
        return jpaFilmRepository.getBy(query);
    }

    @Override
    public List<Film> readAll() {
        return jpaFilmRepository.findAll();
    }
}

interface JpaFilmRepository extends JpaRepository<Film, Long> {
    @Query("SELECT f FROM Film f WHERE :#{#params.category} is null or f.category = :#{#params.category}")
    List<Film> getBy(FilmReadQuery params);
}
