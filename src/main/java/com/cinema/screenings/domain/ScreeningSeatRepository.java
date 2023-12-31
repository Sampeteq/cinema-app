package com.cinema.screenings.domain;

import java.util.List;
import java.util.Optional;

public interface ScreeningSeatRepository {
    ScreeningSeat add(ScreeningSeat seat);
    Optional<ScreeningSeat> getById(Long id);
    Optional<ScreeningSeat> getByIdAndScreeningId(Long id, Long screeningId);
    List<ScreeningSeat> getAllByScreeningId(Long screeningId);
}
