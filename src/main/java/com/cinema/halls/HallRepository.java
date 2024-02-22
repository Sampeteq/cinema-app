package com.cinema.halls;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

interface HallRepository extends JpaRepository<Hall, Long> {

    @Query("""
            select hall from Hall hall left join fetch hall.seats
            """)
    List<Hall> findAllWithSeats();

    @Query("""
                select hall from Hall hall left join fetch hall.seats
            """)
    Optional<Hall> findByIdWithSeats(Long hallId);
}
