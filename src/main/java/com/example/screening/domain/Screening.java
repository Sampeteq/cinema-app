package com.example.screening.domain;

import com.example.film.domain.FilmId;
import com.example.screening.domain.dto.ScreeningDTO;
import com.example.screening.domain.exception.NoScreeningFreeSeatsException;
import lombok.*;

import javax.persistence.*;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "SCREENINGS")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@EqualsAndHashCode(of = "id")
@ToString
class Screening {

    @EmbeddedId
    @AttributeOverride(name = "value", column = @Column(name = "id"))
    private final ScreeningId id = ScreeningId.of(UUID.randomUUID());

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "date"))
    private ScreeningDate date;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "freeSeats"))
    private FreeSeats freeSeats;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "minAge"))
    private MinAge minAge;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "filmId"))
    private FilmId filmId;

    boolean isAgeEnough(int age) {
        return age >= this.minAge.getValue();
    }

    void decreaseFreeSeatsByOne() {
        if (this.freeSeats.getValue() == 0) {
            throw new NoScreeningFreeSeatsException(this.id);
        } else {
            this.freeSeats = FreeSeats.of(this.freeSeats.getValue() - 1);
        }
    }

    boolean hasFreeSeats() {
        return this.freeSeats.getValue() > 0;
    }

    boolean canCancelReservation(LocalDateTime currentDate) {
        return Duration.between(currentDate, this.date.getValue()).toHours() > 24;
    }

    ScreeningDTO toDTO() {
        return ScreeningDTO
                .builder()
                .id(this.id)
                .date(this.date.getValue())
                .freeSeats(this.freeSeats.getValue())
                .minAge(this.minAge.getValue())
                .filmId(this.filmId)
                .build();
    }
}
