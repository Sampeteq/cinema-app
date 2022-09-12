package com.example.film.domain;

import com.example.film.domain.dto.FilmDTO;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "FILMS")
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@EqualsAndHashCode(of = "id")
@ToString
class Film {

    @Id
    private final UUID id = UUID.randomUUID();

    private String title;

    @Enumerated(EnumType.STRING)
    private FilmCategory category;

    private int year;

    protected Film() {
    }

    FilmDTO toDTO() {
        return new FilmDTO(this.id, this.title, this.category, this.year);
    }
}
