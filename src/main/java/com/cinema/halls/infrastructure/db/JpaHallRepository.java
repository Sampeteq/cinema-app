package com.cinema.halls.infrastructure.db;

import com.cinema.halls.domain.Hall;
import com.cinema.halls.domain.HallRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

interface JpaHallRepository extends JpaRepository<JpaHall, Long> {

    @Query("""

            select hall from JpaHall hall left join fetch hall.seats
        """)
    List<JpaHall> getAllWithSeats();
}

@Repository
@RequiredArgsConstructor
class JpaHallRepositoryAdapter implements HallRepository {

    private final JpaHallRepository jpaHallRepository;
    private final JpaHallMapper mapper;

    @Override
    public Hall save(Hall hall) {
        return mapper.toDomain(jpaHallRepository.save(mapper.toJpa(hall)));
    }

    @Override
    public void delete(Hall hall) {
        jpaHallRepository.delete(mapper.toJpa(hall));
    }

    @Override
    public Optional<Hall> getById(Long id) {
        return jpaHallRepository
                .findById(id)
                .map(mapper::toDomain);
    }

    @Override
    public List<Hall> getAllWithSeats() {
        return jpaHallRepository
                .getAllWithSeats()
                .stream()
                .map(mapper::toDomain)
                .toList();
    }
}
