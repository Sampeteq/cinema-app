package code.catalog.infrastructure.db;

import code.catalog.application.dto.ScreeningDetails;
import code.catalog.domain.FilmCategory;
import code.catalog.domain.Screening;
import code.catalog.domain.ports.ScreeningReadOnlyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@org.springframework.stereotype.Repository
@RequiredArgsConstructor
public class ScreeningJpaReadOnlyRepositoryAdapter implements ScreeningReadOnlyRepository {

    private final ScreeningReadOnlyJpaRepository screeningReadOnlyJpaRepository;

    @Override
    public List<Screening> readAll() {
        return screeningReadOnlyJpaRepository.findAll();
    }

    @Override
    public List<Screening> readByFilmTitle(String filmTitle) {
        return screeningReadOnlyJpaRepository.findByFilm_Title(filmTitle);
    }

    @Override
    public List<Screening> readByFilmCategory(FilmCategory filmCategory) {
        return screeningReadOnlyJpaRepository.findByFilm_Category(filmCategory);
    }

    @Override
    public List<Screening> readByDateBetween(LocalDateTime from, LocalDateTime to) {
        return screeningReadOnlyJpaRepository.findByDateBetween(from, to);
    }

    @Override
    public List<Screening> readEnded() {
        return screeningReadOnlyJpaRepository.findEnded();
    }

    @Override
    public Optional<ScreeningDetails> readDetailsBySeatId(Long seatId) {
        return screeningReadOnlyJpaRepository
                .readDetailsBySeatId(seatId, PageRequest.of(0, 1))
                .stream()
                .findFirst();
    }
}

interface ScreeningReadOnlyJpaRepository extends Repository<Screening, Long> {
    List<Screening> findAll();

    List<Screening> findByFilm_Title(String filmTitle);

    List<Screening> findByFilm_Category(FilmCategory filmCategory);

    List<Screening> findByDateBetween(LocalDateTime from, LocalDateTime to);

    @Query("select s from Screening s where s.endDate < CURRENT_DATE")
    List<Screening> findEnded();

    @Query(
            "select new code.catalog.application.dto.ScreeningDetails(" +
                    "sc.id, " +
                    "sc.date, " +
                    "r.id, " +
                    "r.customId, " +
                    "film.id, " +
                    "film.title" +
                    ") from Screening sc, Room r, Seat se, Film film where se.id = :seatId"
    )
    Page<ScreeningDetails> readDetailsBySeatId(Long seatId, Pageable pageable);
}
