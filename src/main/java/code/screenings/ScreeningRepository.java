package code.screenings;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

interface ScreeningRepository {

    Screening add(Screening screening);

    Optional<Screening> getById(UUID id);

    List<Screening> getByExample(Screening example);

    boolean existsByDateAndRoomId(ScreeningDate screeningDate, UUID roomId);
}

interface JpaScreeningRepository extends JpaRepository<Screening, UUID> {

    boolean existsByDateAndRoom_id(ScreeningDate screeningDate, UUID roomId);
}

@AllArgsConstructor
class JpaScreeningRepositoryAdapter implements ScreeningRepository {

    private final JpaScreeningRepository jpaScreeningRepository;

    @Override
    public Screening add(Screening screening) {
        return jpaScreeningRepository.save(screening);
    }

    @Override
    public Optional<Screening> getById(UUID id) {
        return jpaScreeningRepository.findById(id);
    }

    @Override
    public List<Screening> getByExample(Screening example) {
        return jpaScreeningRepository.findAll(
                Example.of(example)
        );
    }

    @Override
    public boolean existsByDateAndRoomId(ScreeningDate screeningDate, UUID roomId) {
        return jpaScreeningRepository.existsByDateAndRoom_id(screeningDate, roomId);
    }
}
