package com.example.screening.domain;


import com.example.film.domain.FilmId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

interface ScreeningRepository extends JpaRepository<Screening, ScreeningId> {

    List<Screening> findAllByFilmId(FilmId filmId);

    List<Screening> findByDate(ScreeningDate date);
}
