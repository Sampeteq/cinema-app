package com.cinema.halls.domain;

import com.cinema.halls.domain.Hall;

import java.util.List;
import java.util.Optional;

public interface HallRepository {

    Hall save(Hall hall);

    void delete(Hall hall);

    boolean existsById(Long id);

    Optional<Hall> getById(Long id);

    Optional<Hall> getByIdWithSeats(Long id);

    List<Hall> getAllWithSeats();
}
