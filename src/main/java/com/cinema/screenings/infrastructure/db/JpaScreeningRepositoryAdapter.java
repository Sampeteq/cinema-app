package com.cinema.screenings.infrastructure.db;

import com.cinema.screenings.application.queries.GetScreenings;
import com.cinema.screenings.domain.Screening;
import com.cinema.screenings.domain.ScreeningRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class JpaScreeningRepositoryAdapter implements ScreeningRepository {

    private final JpaScreeningRepository jpaScreeningRepository;

    @Override
    public Screening add(Screening screening) {
        return jpaScreeningRepository.save(screening);
    }

    @Override
    public void delete(Screening screening) {
        jpaScreeningRepository.delete(screening);
    }

    @Override
    public Optional<Screening> getById(Long id) {
        return jpaScreeningRepository.findById(id);
    }

    @Override
    public List<Screening> getWithRoom() {
        return jpaScreeningRepository.findWithRoom();
    }

    @Override
    public List<Screening> getAll(GetScreenings query) {
        return jpaScreeningRepository.findAll(
                dateSpec(query)
        );
    }

    private static Specification<Screening> dateSpec(GetScreenings query) {
        return (root, jpaQuery, criteriaBuilder) -> query.date() == null ?
                        criteriaBuilder.conjunction() :
                        criteriaBuilder.between(
                                root.get("date"),
                                query.date(),
                                query.date().plusDays(1)
                        );
    }
}

interface JpaScreeningRepository extends JpaRepository<Screening, Long>, JpaSpecificationExecutor<Screening> {

    @Query("select s from Screening s where s.roomId is not null")
    List<Screening> findWithRoom();
}
