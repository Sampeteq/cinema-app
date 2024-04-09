package com.cinema.films.infrastrcture.db;

import com.cinema.films.domain.FilmCategory;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "film")
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class JpaFilm {
    @Id
    private UUID id;
    private String title;
    @Enumerated(EnumType.STRING)
    private FilmCategory category;
    private int year;
    private int durationInMinutes;
}
