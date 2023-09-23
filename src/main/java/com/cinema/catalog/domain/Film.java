package com.cinema.catalog.domain;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "films")
@Getter
@ToString(exclude = "screenings")
public class Film {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Enumerated(EnumType.STRING)
    private FilmCategory category;

    private int year;

    private int durationInMinutes;

    @OneToMany(mappedBy = "film", cascade = CascadeType.PERSIST)
    private final List<Screening> screenings = new ArrayList<>();

    protected Film() {}

    public Film(String title, FilmCategory category, int year, int durationInMinutes) {
        this.title = title;
        this.category = category;
        this.year = year;
        this.durationInMinutes = durationInMinutes;
    }

    public LocalDateTime calculateScreeningEndDate(LocalDateTime screeningDate) {
        return screeningDate.plusMinutes(this.durationInMinutes);
    }

    public void addScreening(Screening newScreening) {
        this.screenings.add(newScreening);
    }
}
