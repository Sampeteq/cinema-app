package com.cinema.screenings.infrastructure.db;

import com.cinema.films.infrastrcture.db.JpaFilm;
import com.cinema.halls.infrastructure.db.JpaHall;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
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
    @ManyToOne(fetch = FetchType.LAZY)
    private JpaFilm film;
    @ManyToOne(fetch = FetchType.LAZY)
    private JpaHall hall;
}
