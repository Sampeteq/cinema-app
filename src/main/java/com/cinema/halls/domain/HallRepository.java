package com.cinema.halls.domain;

import java.util.List;
import java.util.Optional;

public interface HallRepository {
    Hall add(Hall hall);
    Optional<Hall> getById(Long id);
    List<Hall> getAll();
    Long count();
}
