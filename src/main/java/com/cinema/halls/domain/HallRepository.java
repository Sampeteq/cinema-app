package com.cinema.halls.domain;

import java.util.List;
import java.util.Optional;

public interface HallRepository {
    Hall add(Hall hall);
    Optional<Hall> getById(String id);
    List<Hall> getAll();
    Long count();
    boolean existsById(String id);
}
