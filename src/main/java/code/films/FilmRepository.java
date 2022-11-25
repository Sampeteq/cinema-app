package code.films;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

interface FilmRepository  {

    Film add(Film film);

    Optional<Film> getById(UUID id);

    List<Film> getAll(Example<Film> example);
}

interface JpaFilmRepository extends JpaRepository<Film, UUID> {

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
    public List<Film> getAll(Example<Film> example) {
        return jpaFilmRepository.findAll(example);
    }
}


