package code.films.infrastructure.db;

import code.films.client.queries.GetFilmsQuery;
import code.films.domain.Film;
import code.films.domain.FilmRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

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
    public Optional<Film> readById(UUID filmId) {
        return jpaFilmRepository.findById(filmId);
    }

    @Override
    public List<Film> readBy(GetFilmsQuery query) {
        return jpaFilmRepository.getBy(query);
    }
}

interface JpaFilmRepository extends JpaRepository<Film, UUID> {
    @Query("SELECT f FROM Film f WHERE :#{#params.category} is null or f.category = :#{#params.category}")
    List<Film> getBy(GetFilmsQuery params);
}
