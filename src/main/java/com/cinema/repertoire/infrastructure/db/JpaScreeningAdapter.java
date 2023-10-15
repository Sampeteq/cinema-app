package com.cinema.repertoire.infrastructure.db;

import com.cinema.repertoire.application.dto.ScreeningQueryDto;
import com.cinema.repertoire.domain.Screening;
import com.cinema.repertoire.domain.ScreeningRepository;
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
public class JpaScreeningAdapter implements ScreeningRepository {

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
    public Optional<Screening> readById(Long id) {
        return jpaScreeningRepository.findById(id);
    }

    @Override
    public List<Screening> readWithRoom() {
        return jpaScreeningRepository.findEndedWithRoom();
    }

    @Override
    public List<Screening> readAllBy(ScreeningQueryDto queryDto) {
        Specification<Screening> dateSpec =
                (root, query, criteriaBuilder) -> queryDto.date() == null ?
                        criteriaBuilder.conjunction() :
                        criteriaBuilder.between(root.get("date"), queryDto.date(), queryDto.date().plusDays(1));
        Specification<Screening> categorySpec =
                (root, query, criteriaBuilder) -> queryDto.filmCategory() == null ?
                        criteriaBuilder.conjunction() :
                        criteriaBuilder.equal(root.get("film").get("category"), queryDto.filmCategory());
        Specification<Screening> titleSpec =
                (root, query, criteriaBuilder) -> queryDto.filmTitle() == null ?
                        criteriaBuilder.conjunction() :
                        criteriaBuilder.equal(root.get("film").get("title"), queryDto.filmTitle());
        return jpaScreeningRepository.findAll(
                categorySpec
                        .and(dateSpec)
                        .and(categorySpec)
                        .and(titleSpec)
        );
    }
}

interface JpaScreeningRepository extends JpaRepository<Screening, Long>, JpaSpecificationExecutor<Screening> {

    @Query("select s from Screening s where s.roomId is not null")
    List<Screening> findEndedWithRoom();
}
