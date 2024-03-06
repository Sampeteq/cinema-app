package com.cinema.halls.infrastructure;

import com.cinema.halls.domain.Hall;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

interface JpaHallRepository extends JpaRepository<Hall, Long> {

    @Query("""

            select hall from Hall hall left join fetch hall.seats
        """)
    List<Hall> findAllWithSeats();

    @Query("""
            select hall from Hall hall left join fetch hall.seats
        """)
    Optional<Hall> findByIdWithSeats(Long hallId);
}
