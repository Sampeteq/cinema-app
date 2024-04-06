package com.cinema.screenings.infrastructure.db;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Entity
@Table(name = "screening")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
public class JpaScreening {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter
    private Long id;
    private LocalDateTime date;
    private LocalDateTime endDate;
    private Long filmId;
    private Long hallId;
}
