package code.catalog.infrastructure.db;

import code.catalog.domain.Film;
import code.catalog.domain.FilmCategory;
import code.catalog.domain.ports.FilmRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class FilmJpaRepositoryAdapter implements FilmRepository {

    private final FilmJpaRepository filmJpaRepository;
    private final Clock clock;

    @Override
    public Film add(Film film) {
        return filmJpaRepository.save(film);
    }

    @Override
    public Optional<Film> readById(Long filmId) {
        return filmJpaRepository.findById(filmId);
    }

    @Override
    public Optional<Film> readByTitle(String title) {
        var r = filmJpaRepository.readByTitle(title, LocalDateTime.now(clock));
        System.out.println(r);
        return r.stream().findFirst();
    }

    @Override
    public List<Film> readByCategory(FilmCategory category) {
        return filmJpaRepository.readByCategory(category, LocalDateTime.now(clock));
    }

    @Override
    public List<Film> readByDate(LocalDate date) {
        return filmJpaRepository.readByDate(date.atStartOfDay(), date.plusDays(1).atStartOfDay());
    }

    @Override
    public List<Film> readAll() {
        return filmJpaRepository.findAll();
    }
}

interface FilmJpaRepository extends JpaRepository<Film, Long>, JpaSpecificationExecutor<Film> {

    @Query(
            "select f from Film f " +
                    "join fetch f.screenings s " +
                    "where f.title = :title and s.endDate > :currentDate"
    )
    List<Film> readByTitle(String title, LocalDateTime currentDate);

    @Query(
            "select f from Film f " +
                    "join fetch f.screenings s " +
                    "where f.category = :category and s.endDate > :currentDate"
    )
    List<Film> readByCategory(FilmCategory category, LocalDateTime currentDate);

    @Query("select distinct f from Film f join fetch f.screenings s where s.date >= :from and s.date <= :to")
    List<Film> readByDate(LocalDateTime from, LocalDateTime to);
}
