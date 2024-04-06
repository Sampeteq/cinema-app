package com.cinema.films.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@ToString
public class Film {
    private Long id;
    private String title;
    private FilmCategory category;
    private int year;
    private int durationInMinutes;
}
