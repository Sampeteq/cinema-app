package code.screening;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

interface ScreeningRepository extends JpaRepository<Screening, Long> {

    List<Screening> findAllByFilmId(Long filmId);

    List<Screening> findByDate(ScreeningDate date);
}
