package com.cinema.catalog.infrastructure.db;

import com.cinema.catalog.domain.FilmCategory;
import com.cinema.catalog.domain.Screening;
import com.cinema.catalog.domain.ScreeningRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
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
    public List<Screening> readAll() {
        return jpaScreeningRepository.findAll();
    }

    @Override
    public List<Screening> readByFilmTitle(String filmTitle) {
        return jpaScreeningRepository.findByFilm_Title(filmTitle);
    }

    @Override
    public List<Screening> readByFilmCategory(FilmCategory category) {
        return jpaScreeningRepository.findByFilm_Category(category);
    }

    @Override
    public List<Screening> readByDateBetween(LocalDateTime from, LocalDateTime to) {
        return jpaScreeningRepository.findByDateBetween(from, to);
    }

    @Override
    public List<Screening> readEndedWithRoom(LocalDateTime currentDate) {
        return jpaScreeningRepository.findEndedWithRoom(currentDate);
    }

    @Override
    public void delete(Screening screening) {
        jpaScreeningRepository.delete(screening);
    }
}

interface JpaScreeningRepository extends JpaRepository<Screening, Long> {
    List<Screening> findByFilm_Title(String filmTitle);

    List<Screening> findByFilm_Category(FilmCategory filmCategory);

    List<Screening> findByDateBetween(LocalDateTime from, LocalDateTime to);

    @Query("select s from Screening s where s.endDate < :currentDate and s.roomId is not null")
    List<Screening> findEndedWithRoom(LocalDateTime currentDate);
}
