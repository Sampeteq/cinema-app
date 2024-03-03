package com.cinema.films;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;

@Entity
@Getter
public class Film {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @NotEmpty
    private String title;

    public enum Category {
        COMEDY,
        DRAMA,
        ACTION,
        THRILLER,
        HORROR,
        FANTASY
    }

    @Enumerated(EnumType.STRING)
    @NotNull
    private Film.Category category;

    @Positive
    private int year;

    @Positive
    private int durationInMinutes;

    protected Film() {}

    public Film(String title, Film.Category category, int year, int durationInMinutes) {
        this.title = title;
        this.category = category;
        this.year = year;
        this.durationInMinutes = durationInMinutes;
    }
}
