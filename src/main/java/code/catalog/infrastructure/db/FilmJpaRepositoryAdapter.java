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
public class FilmJpaRepositoryAdapter implements FilmRepository {

    private final FilmJpaRepository filmJpaRepository;

    @Override
    public Film add(Film film) {
        return filmJpaRepository.save(film);
    }

    @Override
    public Optional<Film> readById(Long filmId) {
        return filmJpaRepository.findById(filmId);
    }

    @Override
    public List<Film> readAll() {
        return filmJpaRepository.findAll();
    }

    @Override
    public boolean existsByTitle(String title) {
        return filmJpaRepository.existsByTitle(title);
    }
}

interface FilmJpaRepository extends JpaRepository<Film, Long> {
    boolean existsByTitle(String title);
}
