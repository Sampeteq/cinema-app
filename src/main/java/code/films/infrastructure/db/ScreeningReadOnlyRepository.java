package code.films.infrastructure.db;

import code.films.domain.Screening;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.Repository;

import java.util.Optional;

public interface ScreeningReadOnlyRepository extends
        Repository<Screening, Long>,
        JpaSpecificationExecutor<Screening> {
    Optional<Screening> findById(Long id);
}
