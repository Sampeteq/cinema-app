package com.cinema.halls.infrastructure.db;

import com.cinema.halls.domain.Hall;
import com.cinema.halls.domain.Seat;
import org.springframework.stereotype.Component;

@Component
public class JpaHallMapper {

    public JpaHall toJpa(Hall hall) {
        return new JpaHall(
                hall.getId(),
                hall
                        .getSeats()
                        .stream()
                        .map(it -> new JpaSeat(it.rowNumber(), it.number()))
                        .toList()
        );
    }

    public Hall toDomain(JpaHall jpaHall) {
        return new Hall(
                jpaHall.getId(),
                jpaHall
                        .getSeats()
                        .stream()
                        .map(it -> new Seat(it.rowNumber(), it.number()))
                        .toList()
        );
    }
}
