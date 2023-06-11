package code.films.infrastructure.db;

import code.films.domain.FilmScreeningSeat;
import org.springframework.data.repository.Repository;

import java.util.Optional;

public interface FilmScreeningSeatReadOnlyRepository extends Repository<FilmScreeningSeat, Long> {
    Optional<FilmScreeningSeat> getById(Long seatId);
}
