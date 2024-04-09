package com.cinema.films.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.util.UUID;

@AllArgsConstructor
@Getter
@ToString
public class Film {
    private UUID id;
    private String title;
    private FilmCategory category;
    private int year;
    private int durationInMinutes;
}
