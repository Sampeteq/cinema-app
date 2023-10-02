package com.cinema.catalog.domain;

import java.util.Optional;

public interface ScreeningRepository {
    Optional<Screening> readById(Long id);
    void delete(Screening screening);
}
