package code.catalog.infrastructure.db;

import code.catalog.domain.Film;
import code.catalog.domain.FilmCategory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
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
    public List<Film> readByDate(LocalDate date) {
        return jpaFilmRepository.readByDate(date.atStartOfDay(), date.plusDays(1).atStartOfDay());
    }

    @Override
    public List<Film> readAll() {
        return jpaFilmRepository.findAll();
    }
}

interface FilmJpaRepository extends JpaRepository<Film, Long>, JpaSpecificationExecutor<Film> {

    @Query(
            "select f from Film f " +
                    "join fetch f.screenings s " +
                    "where f.title = :title and s.endDate > CURRENT_DATE"
    )
    List<Film> readByTitle(String title);

    @Query(
            "select f from Film f " +
                    "join fetch f.screenings s " +
                    "where f.category = :category and s.endDate > CURRENT_DATE"
    )
    List<Film> readByCategory(FilmCategory category);

    @Query("select distinct f from Film f join fetch f.screenings s where s.date >= :from and s.date <= :to")
    List<Film> readByDate(LocalDateTime from, LocalDateTime to);
}
