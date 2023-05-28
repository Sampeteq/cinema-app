package code.screenings.domain;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.Repository;

import java.util.Optional;

public interface ScreeningReadOnlyRepository extends
        Repository<Screening, Long>,
        JpaSpecificationExecutor<Screening> {
    Optional<Screening> getById(Long id);
}
