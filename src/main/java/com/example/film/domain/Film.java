package com.example.film.domain;

import com.example.film.domain.dto.FilmDTO;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "FILMS")
@EqualsAndHashCode(of = "id")
@ToString
class Film {

    @Id
    private UUID id = UUID.randomUUID();

    private String title;

    @Enumerated(EnumType.STRING)
    private FilmCategory category;

    private int year;

    protected Film() {
    }

    Film(String title, FilmCategory category, int year) {
        this.title = title;
        this.category = category;
        this.year = year;
    }

    FilmDTO toDTO() {
        return new FilmDTO(this.id, this.title, this.category, this.year);
    }
}
