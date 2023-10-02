package com.cinema.catalog.infrastructure.db;

import com.cinema.catalog.domain.Screening;
import com.cinema.catalog.domain.ScreeningRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class SpringDataJpaScreeningRepository implements ScreeningRepository {

    private final JpaScreeningRepository jpaScreeningRepository;

    @Override
    public Optional<Screening> readById(Long id) {
        return jpaScreeningRepository.findById(id);
    }

    @Override
    public void delete(Screening screening) {
        jpaScreeningRepository.delete(screening);
    }
}

interface JpaScreeningRepository extends JpaRepository<Screening, Long> {
}
