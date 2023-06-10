package code.films.domain;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.Repository;

import java.util.Optional;

public interface FilmScreeningReadOnlyRepository extends
        Repository<FilmScreening, Long>,
        JpaSpecificationExecutor<FilmScreening> {
    Optional<FilmScreening> findById(Long id);
}
