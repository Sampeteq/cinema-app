package com.cinema.halls.domain;

import java.util.List;
import java.util.Optional;

public interface HallRepository {

    Hall save(Hall hall);

    void delete(Hall hall);

    Optional<Hall> getById(long id);

    List<Hall> getAllWithSeats();
}
