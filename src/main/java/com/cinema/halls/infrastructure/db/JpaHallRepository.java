package com.cinema.halls.infrastructure.db;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface JpaHallRepository extends JpaRepository<JpaHall, UUID> {

    @Query("""

            select hall from JpaHall hall left join fetch hall.seats
        """)
    List<JpaHall> getAllWithSeats();
}

