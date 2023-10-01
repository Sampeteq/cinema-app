package com.cinema.catalog.infrastructure.db;

import com.cinema.catalog.domain.FilmCategory;
import com.cinema.catalog.domain.Screening;
import com.cinema.catalog.domain.ScreeningReadOnlyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@org.springframework.stereotype.Repository
@RequiredArgsConstructor
class SpringDataJpaScreeningReadOnlyRepository implements ScreeningReadOnlyRepository {

    private final JpaScreeningReadOnlyRepository jpaScreeningReadOnlyRepository;

    @Override
    public Optional<Screening> readById(Long id) {
        return jpaScreeningReadOnlyRepository.findById(id);
    }

    @Override
    public List<Screening> readAll() {
        return jpaScreeningReadOnlyRepository.findAll();
    }

    @Override
    public List<Screening> readByFilmTitle(String filmTitle) {
        return jpaScreeningReadOnlyRepository.findByFilm_Title(filmTitle);
    }

    @Override
    public List<Screening> readByFilmCategory(FilmCategory filmCategory) {
        return jpaScreeningReadOnlyRepository.findByFilm_Category(filmCategory);
    }

    @Override
    public List<Screening> readByDateBetween(LocalDateTime from, LocalDateTime to) {
        return jpaScreeningReadOnlyRepository.findByDateBetween(from, to);
    }

    @Override
    public List<Screening> readEndedWithRoom(LocalDateTime currentDate) {
        return jpaScreeningReadOnlyRepository.findEndedWithRoom(currentDate);
    }
}

interface JpaScreeningReadOnlyRepository extends Repository<Screening, Long> {
    Optional<Screening> findById(Long id);

    List<Screening> findAll();

    List<Screening> findByFilm_Title(String filmTitle);

    List<Screening> findByFilm_Category(FilmCategory filmCategory);

    List<Screening> findByDateBetween(LocalDateTime from, LocalDateTime to);

    @Query("select s from Screening s where s.endDate < :currentDate and s.roomCustomId is not null")
    List<Screening> findEndedWithRoom(LocalDateTime currentDate);
}
