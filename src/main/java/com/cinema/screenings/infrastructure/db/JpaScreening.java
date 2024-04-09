package com.cinema.screenings.infrastructure.db;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "screening")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
public class JpaScreening {
    @Id
    private UUID id;
    private LocalDateTime date;
    private LocalDateTime endDate;
    private UUID filmId;
    private UUID hallId;
}
