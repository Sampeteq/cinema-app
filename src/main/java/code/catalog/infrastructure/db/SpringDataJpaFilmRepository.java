package code.catalog.infrastructure.db;

import code.catalog.domain.Film;
import code.catalog.domain.ports.FilmRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
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
    public Optional<Film> readById(Long filmId) {
        return jpaFilmRepository.findById(filmId);
    }

    @Override
    public List<Film> readAll() {
        return jpaFilmRepository.findAll();
    }

    @Override
    public boolean existsByTitle(String title) {
        return jpaFilmRepository.existsByTitle(title);
    }
}

interface JpaFilmRepository extends JpaRepository<Film, Long> {
    boolean existsByTitle(String title);
}