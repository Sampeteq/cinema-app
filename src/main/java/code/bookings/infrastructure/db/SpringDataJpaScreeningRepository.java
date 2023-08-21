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
    public Optional<Screening> readByIdWithSeat(Long id, int rowNumber, int seatNumber) {
        return screeningJpaRepository.readByIdWithSeat(id, rowNumber, seatNumber);
    }
}

interface ScreeningJpaRepository extends JpaRepository<Screening, Long> {

    @Query("select s from booking_screening s left join fetch s.seats where s.id = :id")
    Optional<Screening> readByIdWithSeats(@Param("id") Long id);

    @Query(
            "select s from booking_screening s " +
            "left join fetch s.seats se " +
            "where s.id = :id and se.rowNumber = :rowNumber and se.number = :seatNumber"
    )
    Optional<Screening> readByIdWithSeat(
            @Param("id")
            Long id,

            @Param("rowNumber")
            int rowNumber,

            @Param("seatNumber")
            int seatNumber
    );
}
