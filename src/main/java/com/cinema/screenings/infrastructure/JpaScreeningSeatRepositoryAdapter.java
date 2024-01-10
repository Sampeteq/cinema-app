package com.cinema.screenings.infrastructure;

import com.cinema.screenings.domain.ScreeningSeat;
import com.cinema.screenings.domain.ScreeningSeatRepository;
import jakarta.persistence.LockModeType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
class JpaScreeningSeatRepositoryAdapter implements ScreeningSeatRepository {

    private final JpaScreeningSeatRepository jpaScreeningSeatRepository;

    @Override
    public ScreeningSeat add(ScreeningSeat seat) {
        return jpaScreeningSeatRepository.save(seat);
    }

    @Override
    public Optional<ScreeningSeat> getById(Long id) {
        return jpaScreeningSeatRepository.findById(id);
    }

    @Override
    public Optional<ScreeningSeat> getByIdAndScreeningId(Long id, Long screeningId) {
        return jpaScreeningSeatRepository.findByIdAndScreeningId(id, screeningId);
    }

    @Override
    public List<ScreeningSeat> getAllByScreeningId(Long screeningId) {
        return jpaScreeningSeatRepository.findAllByScreeningId(screeningId);
    }
}

interface JpaScreeningSeatRepository extends JpaRepository<ScreeningSeat, Long> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<ScreeningSeat> findByIdAndScreeningId(Long id, Long screeningId);

    List<ScreeningSeat> findAllByScreeningId(Long screeningId);
}
