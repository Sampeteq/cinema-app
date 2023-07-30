package code.bookings.infrastructure.db;

import code.bookings.domain.Screening;
import code.bookings.domain.ports.ScreeningRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class SpringDataJpaScreeningRepository implements ScreeningRepository {

    private final ScreeningJpaRepository screeningJpaRepository;

    @Override
    public Screening add(Screening screening) {
        return screeningJpaRepository.save(screening);
    }

    @Override
    public Optional<Screening> readById(Long id) {
        return screeningJpaRepository.findById(id);
    }
}

interface ScreeningJpaRepository extends JpaRepository<Screening, Long> {
}
