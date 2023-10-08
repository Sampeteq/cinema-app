package com.cinema.repertoire.domain;

import com.cinema.repertoire.application.dto.ScreeningQueryDto;

import java.util.List;
import java.util.Optional;

public interface ScreeningRepository {
    Screening add(Screening screening);
    void delete(Screening screening);
    List<Screening> readAllBy(ScreeningQueryDto queryDto);
    List<Screening> readWithRoom();
    Optional<Screening> readById(Long id);
}
