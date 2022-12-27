package code.screenings;

import lombok.AllArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

interface ScreeningRepository {

    Screening add(Screening screening);

    Optional<Screening> getById(UUID id);

    List<Screening> getBy(ScreeningSearchParams params);

    boolean existsByDateAndRoomId(ScreeningDate screeningDate, UUID roomId);
}

interface JpaScreeningRepository extends JpaRepository<Screening, UUID> {

    @Query("SELECT s FROM Screening s " +
            "left join fetch s.room " +
            "where " +
            "(:#{#params?.date?.value} is null or s.date.value = :#{#params?.date?.value}) and " +
            "(:#{#params.filmId} is null or s.filmId = :#{#params.filmId})")
    Set<Screening> findBy(ScreeningSearchParams params);

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
    public List<Screening> getBy(ScreeningSearchParams params) {
        return jpaScreeningRepository
                .findBy(params)
                .stream()
                .toList();
    }

    @Override
    public boolean existsByDateAndRoomId(ScreeningDate screeningDate, UUID roomId) {
        return jpaScreeningRepository.existsByDateAndRoom_id(screeningDate, roomId);
    }
}
