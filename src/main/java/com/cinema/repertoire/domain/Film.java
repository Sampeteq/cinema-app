package com.cinema.repertoire.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;

@Entity
@Table(name = "films")
@Getter
public class Film {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Enumerated(EnumType.STRING)
    private FilmCategory category;

    private int year;

    private int durationInMinutes;

    protected Film() {}

    public Film(String title, FilmCategory category, int year, int durationInMinutes) {
        this.title = title;
        this.category = category;
        this.year = year;
        this.durationInMinutes = durationInMinutes;
    }
}
