package com.cinema.halls.infrastructure.db;

import com.cinema.halls.domain.HallOccupation;
import com.cinema.halls.domain.HallOccupationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
class JpaHallOccupationRepositoryAdapter implements HallOccupationRepository {

    private final JpaHallOccupationRepository jpaHallOccupationRepository;

    @Override
    public List<HallOccupation> getAll() {
        return jpaHallOccupationRepository.findAll();
    }
}

interface JpaHallOccupationRepository extends JpaRepository<HallOccupation, Long> {
}
