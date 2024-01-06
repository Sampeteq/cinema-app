package com.cinema.halls.infrastructure;

import com.cinema.halls.domain.Hall;
import com.cinema.halls.domain.HallRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
class JpaHallRepositoryAdapter implements HallRepository {

    private final JpaHallRepository jpaHallRepository;

    @Override
    public Hall add(Hall hall) {
        return jpaHallRepository.save(hall);
    }

    @Override
    public void delete(Hall hall) {
        jpaHallRepository.delete(hall);
    }

    @Override
    public Optional<Hall> getById(Long id) {
        return jpaHallRepository.findByIdWithSeats(id);
    }

    @Override
    public List<Hall> getAll() {
        return jpaHallRepository.findAllWithSeats();
    }
}

interface JpaHallRepository extends JpaRepository<Hall, Long> {
    @Query("from Hall hall left join fetch hall.seats where hall.id = :id")
    Optional<Hall> findByIdWithSeats(@Param("id") Long id);
    @Query("from Hall hall left join fetch hall.seats")
    List<Hall> findAllWithSeats();
}
