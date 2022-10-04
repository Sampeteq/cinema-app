package com.example.film.domain;

import com.example.film.domain.dto.FilmDTO;
import lombok.*;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "FILMS")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@EqualsAndHashCode(of = "uuid")
@ToString
class Film {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private UUID uuid;

    private String title;

    @Enumerated(EnumType.STRING)
    private FilmCategory category;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "year"))
    private FilmYear year;

    FilmDTO toDTO() {
        return new FilmDTO(this.id, this.title, this.category, this.year.getValue());
    }
}
