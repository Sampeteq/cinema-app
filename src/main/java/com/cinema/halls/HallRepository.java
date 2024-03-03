package com.cinema.halls;

import java.util.List;
import java.util.Optional;

interface HallRepository {

    Hall save(Hall hall);

    void delete(Hall hall);

    boolean existsById(Long id);

    Optional<Hall> getById(Long id);

    Optional<Hall> getByIdWithSeats(Long id);

    List<Hall> getAllWithSeats();
}
