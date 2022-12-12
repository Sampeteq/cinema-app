package code.films;

import lombok.AllArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

interface FilmRepository  {

    Film add(Film film);

    Optional<Film> getById(UUID id);

    List<Film> getBy(FilmSearchParams params);
}

interface JpaFilmRepository extends JpaRepository<Film, UUID> {

    @Query(
            "SELECT f FROM Film f WHERE " +
                    ":#{#params?.category} is null or f.category = :#{#params?.category}"
    )
    Set<Film> searchBy(FilmSearchParams params);
}

@AllArgsConstructor
class JpaFilmRepositoryAdapter implements FilmRepository {

    private final JpaFilmRepository jpaFilmRepository;

    @Override
    public Film add(Film film) {
        return jpaFilmRepository.save(film);
    }

    @Override
    public Optional<Film> getById(UUID id) {
        return jpaFilmRepository.findById(id);
    }

    @Override
    public List<Film> getBy(FilmSearchParams params) {
        return jpaFilmRepository
                .searchBy(params)
                .stream()
                .toList();
    }
}


