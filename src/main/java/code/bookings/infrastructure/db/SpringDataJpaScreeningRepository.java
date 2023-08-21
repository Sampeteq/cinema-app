package code.bookings.infrastructure.db;

import code.bookings.domain.Screening;
import code.bookings.domain.ports.ScreeningRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
class SpringDataJpaScreeningRepository implements ScreeningRepository {

    private final ScreeningJpaRepository screeningJpaRepository;

    @Override
    public Screening add(Screening screening) {
        return screeningJpaRepository.save(screening);
    }

    @Override
    public Optional<Screening> readByIdWithSeats(Long id) {
        return screeningJpaRepository.readByIdWithSeats(id);
    }

    @Override
    public Optional<Screening> readByIdWithSeat(Long id, Long seatId) {
        return screeningJpaRepository.readByIdWithSeat(id, seatId);
    }
}

interface ScreeningJpaRepository extends JpaRepository<Screening, Long> {

    @Query("select s from booking_screening s left join fetch s.seats where s.id = :id")
    Optional<Screening> readByIdWithSeats(@Param("id") Long id);

    @Query(
            "select s from booking_screening s " +
            "left join fetch s.seats se " +
            "where s.id = :id and se.id = :seatId"
    )
    Optional<Screening> readByIdWithSeat(
            @Param("id")
            Long id,
            @Param("seatId")
            Long seatId
    );
}
