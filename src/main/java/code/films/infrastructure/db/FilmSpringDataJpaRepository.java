package code.films.infrastructure.db;

import code.films.domain.Film;
import code.films.domain.FilmCategory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
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
    public Optional<Film> readByTitle(String title) {
        var r = jpaFilmRepository.readByTitle(title);
        System.out.println(r);
        return r.stream().findFirst();
    }

    @Override
    public List<Film> readByCategory(FilmCategory category) {
        return jpaFilmRepository.readByCategory(category);
    }

    @Override
    public List<Film> readAll() {
        return jpaFilmRepository.findAll();
    }
}

interface FilmJpaRepository extends JpaRepository<Film, Long>, JpaSpecificationExecutor<Film> {

    @Query("select f from Film f join fetch f.screenings s where f.title = :title and s.isFinished = false")
    List<Film> readByTitle(String title);

    @Query("select f from Film f join fetch f.screenings s where f.category = :category and s.isFinished = false")
    List<Film> readByCategory(FilmCategory category);
}
