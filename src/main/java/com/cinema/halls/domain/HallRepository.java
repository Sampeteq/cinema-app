package com.cinema.halls.domain;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface HallRepository {

    Hall save(Hall hall);

    void delete(Hall hall);

    Optional<Hall> getById(UUID id);

    List<Hall> getAllWithSeats();
}
