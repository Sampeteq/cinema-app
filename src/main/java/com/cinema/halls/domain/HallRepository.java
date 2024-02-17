package com.cinema.halls.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface HallRepository extends JpaRepository<Hall, Long> {

    @Query("""
            select hall from Hall hall left join fetch hall.seats
            """)
    List<Hall> findAllWithSeats();
}
