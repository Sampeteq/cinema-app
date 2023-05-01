package code.screenings.domain;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.Repository;

import java.util.Optional;
import java.util.UUID;

public interface ScreeningReadOnlyRepository extends Repository<Screening, UUID>, JpaSpecificationExecutor<Screening> {
    Optional<Screening> getById(UUID id);
}
