package code.catalog.domain.ports;

import code.catalog.domain.FilmCategory;
import code.catalog.domain.Screening;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface ScreeningReadOnlyRepository {
    List<Screening> readAll();

    List<Screening> readByFilmTitle(String filmTitle);

    List<Screening> readByFilmCategory(FilmCategory filmCategory);

    List<Screening> readByDateBetween(LocalDateTime from, LocalDateTime to);

    @Query("select s from Screening s where s.endDate < CURRENT_DATE")
    List<Screening> readEnded();
}
