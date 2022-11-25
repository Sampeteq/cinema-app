package code.screenings;

import lombok.AllArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

interface ScreeningRoomRepository {

    ScreeningRoom add(ScreeningRoom room);

    Optional<ScreeningRoom> getById(UUID id);

    List<ScreeningRoom> getAll();

    boolean existsByNumber(int number);
}

interface JpaScreeningRoomRepository extends JpaRepository<ScreeningRoom, UUID> {

    boolean existsByNumber(int number);
}

@AllArgsConstructor
class JpaScreeningRoomRepositoryAdapter implements ScreeningRoomRepository {

    private final JpaScreeningRoomRepository jpaScreeningRoomRepository;

    @Override
    public ScreeningRoom add(ScreeningRoom room) {
        return jpaScreeningRoomRepository.save(room);
    }

    @Override
    public Optional<ScreeningRoom> getById(UUID id) {
        return jpaScreeningRoomRepository.findById(id);
    }

    @Override
    public List<ScreeningRoom> getAll() {
        return jpaScreeningRoomRepository.findAll();
    }

    @Override
    public boolean existsByNumber(int number) {
        return jpaScreeningRoomRepository.existsByNumber(number);
    }
}
