package com.example.screening.domain;

import com.example.screening.domain.dto.ScreeningDTO;
import com.example.screening.domain.exception.NoScreeningFreeSeatsException;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "SCREENINGS")
@EqualsAndHashCode(of = "id")
@ToString
@AllArgsConstructor
class Screening {

    @Id
    private final UUID id = UUID.randomUUID();

    @Getter
    private LocalDateTime date;

    private int freeSeats;

    private int minAge;

    private UUID filmId;

    protected Screening() {
    }

    boolean isAgeEnough(int age) {
        return age >= this.minAge;
    }

    void decreaseFreeSeatsByOne() {
        if (this.freeSeats == 0) {
            throw new NoScreeningFreeSeatsException(this.id);
        } else {
            this.freeSeats = this.freeSeats - 1;
        }
    }

    boolean hasFreeSeats() {
        return this.freeSeats > 0;
    }

    boolean canCancelReservation(LocalDateTime currentDate) {
        return Duration.between(currentDate, this.date).toHours() > 24;
    }

    ScreeningDTO toDTO() {
        return ScreeningDTO
                .builder()
                .id(this.id)
                .date(this.date)
                .freeSeats(this.freeSeats)
                .minAge(this.minAge)
                .filmId(this.filmId)
                .build();
    }
}
